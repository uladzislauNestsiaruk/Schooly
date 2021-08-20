package com.egormoroz.schooly.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.User;
import com.egormoroz.schooly.ui.chat.holders.ImageHolder;
import com.egormoroz.schooly.ui.chat.holders.IncomingVoiceMessageViewHolder;
import com.egormoroz.schooly.ui.chat.holders.OutcomingVoiceMessageViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class MessageActivity extends FragmentActivity
        implements MessageInput.InputListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageHolders.ContentChecker<Message>,
        MessageInput.TypingListener{

    private ProgressDialog loadingBar;
    private static final int TOTAL_MESSAGES_COUNT = 100;
    private final String TAG = "##########";
    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private String dialogId;
    private Date lastLoadedDate;
    private DatabaseReference ref;
    private static final byte CONTENT_TYPE_VOICE = 1;
    private static final byte CONTENT_TYPE_IMAGE = 2;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MessagesList messagesList;
    private boolean permissionToRecordAccepted = false;
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO};
    public static String fileName = null;
    private static final String LOG_TAG = "AudioRecordTest";
    private MediaRecorder recorder = null;
    private FirebaseAuth AuthenticationDatabase;
    private FirebaseDatabase database;
    private int duration;
    private String checker = "", myURL = "";
    private StorageTask uoloadTask;
    private Uri fileUri;
    FirebaseStorage storage;
    StorageReference storageReference;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        messagesAdapter.addToStart(getTextMessage(), true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() {
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest" + id + 1 + ".3gp";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        if (recorder != null) {
        recorder.reset();
        recorder.release();
        recorder = null;
        try {
        duration = getDuration(fileName);}
        catch (Exception e){
            e.printStackTrace();
        }
        if (duration <= 9) Log.d(TAG, "Voice too small");
        else {
               duration = duration / 10;
               messagesAdapter.addToStart(getVoiceMessage(fileName, duration), true);
             }
        }
    }
    int id = 0;
    static SecureRandom rnd = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        this.messagesList = findViewById(R.id.messagesList);
        initAdapter();
        imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);
        ImageView back = findViewById(R.id.backtoalldialogs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        loadingBar = new ProgressDialog(this);
        TextView nickname = findViewById(R.id.mnick);
        nickname.setText("Nickname");
        MessageInput input = findViewById(R.id.input);
        input.setInputListener(this);
        input.setTypingListener(this);
        getCurrentChatId();
        initFirebase();
        RecAudio();
        Share();
    }

    public int getDuration(String mUri) {
        int duration;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mUri);
        String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = Integer.parseInt(time)/100;
        mmr.release();
        return duration;
    }


    @SuppressLint("ClickableViewAccessibility")
    public void RecAudio(){
        ImageView voiceinput = findViewById(R.id.voiceinput);
        voiceinput.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        view.setPressed(true);
                        id += 1;
                        startRecording();
                        Log.d(TAG, "Recording started");
                        break;

                    case MotionEvent.ACTION_UP:
                        view.setPressed(false);
                        stopRecording();
                        Log.d(TAG, "Recording stop" + duration);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onSubmit(CharSequence input) {
        messagesAdapter.addToStart(
               getTextMessage(input.toString()), true);
        return true;
    }

    public void Share() {
       ImageView image = findViewById(R.id.imameinput);
       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(Intent.createChooser(intent, "Select Image"), 438);
           }

       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            loadingBar.setTitle("Send image");
            loadingBar.setMessage("Updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            fileUri = data.getData();
                Log.d(TAG,"Loading");
                imageLoader = (imageView, url, payload) -> Picasso.get().load(fileUri).into(imageView);
                fileName = fileUri.toString();
                uploadImage();
                messagesAdapter.addToStart(getImageMessage(), true);
                loadingBar.dismiss();
        }
    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        if (type == CONTENT_TYPE_VOICE) {
            return message.getVoice() != null
                    && message.getVoice().getUrl() != null
                    && !message.getVoice().getUrl().isEmpty();
        }
        return false;
    }

    private void initAdapter() {
        MessageHolders holders = new MessageHolders()
                .registerContentType(
                        CONTENT_TYPE_VOICE,
                        IncomingVoiceMessageViewHolder.class,
                        R.layout.incoming_voice,
                        OutcomingVoiceMessageViewHolder.class,
                        R.layout.outcoming_voice,
                         this)
          .registerContentType(
                CONTENT_TYPE_IMAGE,
                ImageHolder.class,
                R.layout.activity_image_viewer,
                ImageHolder.class,
                R.layout.activity_image_viewer,
                this);

        messagesAdapter = new MessagesListAdapter<>(senderId, holders, imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        this.messagesList.setAdapter(messagesAdapter);

    }

    @Override
    public void onStartTyping() {
        Log.v("Typing listener", getString(R.string.start_typing_status));
    }

    @Override
    public void onStopTyping() {
        Log.v("Typing listener", getString(R.string.stop_typing_status));
    }

    @Override
    public void onSelectionChanged(int count) {

    }

    public void getCurrentChatId(){
        Intent dialogIntent = getIntent();
        dialogId = dialogIntent.getStringExtra("dialogId");
    }


    public void sendId(DatabaseReference ref){
//        database.getReference(ref);
    }


    private void initFirebase(){
        AuthenticationDatabase = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        ref = database.getReference();
        String userId = AuthenticationDatabase.getCurrentUser().getUid();
        ref = ref.child("users").child(userId).child("chats");
        ref = getParentReference(dialogId, ref);
        Log.d(TAG, dialogId +  " -> reference: " + ref.toString());
        sendId(ref) ;
    }


    private DatabaseReference getParentReference(String id, DatabaseReference ref){
        Query query = ref.orderByChild("id").equalTo(id);
        return query.getRef().child(id);
    }




    static ArrayList<String> avatars = new ArrayList<String>() {
        {
            add("http://i.imgur.com/pv1tBmT.png");
            add("http://i.imgur.com/R3Jm1CL.png");
            add("http://i.imgur.com/ROz4Jgh.png");
            add("http://i.imgur.com/Qn9UesZ.png");
        }
    };

    static final ArrayList<String> groupChatImages = new ArrayList<String>() {
        {
            add("http://i.imgur.com/hRShCT3.png");
            add("http://i.imgur.com/zgTUcL3.png");
            add("http://i.imgur.com/mRqh5w1.png");
        }
    };

    static final ArrayList<String> groupChatTitles = new ArrayList<String>() {
        {
            add("Samuel, Michelle");
            add("Jordan, Jordan, Zoe");
            add("Julia, Angel, Kyle, Jordan");
        }
    };

    static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("Samuel Reynolds");
            add("Kyle Hardman");
            add("Zoe Milton");
            add("Angel Ogden");
            add("Zoe Milton");
            add("Angelina Mackenzie");
            add("Kyle Oswald");
            add("Abigail Stevenson");
            add("Julia Goldman");
            add("Jordan Gill");
            add("Michelle Macey");
        }
    };

    static final ArrayList<String> messages = new ArrayList<String>() {
        {
            add("Hello!");
            add("This is my phone number - +1 (234) 567-89-01");
            add("Here is my e-mail - myemail@example.com");
            add("Hey! Check out this awesome link! www.github.com");
            add("Hello! No problem. I can today at 2 pm. And after we can go to the office.");
            add("At first, for some time, I was not able to answer him one word");
            add("At length one of them called out in a clear, polite, smooth dialect, not unlike in sound to the Italian");
            add("By the bye, Bob, said Hopkins");
            add("He made his passenger captain of one, with four of the men; and himself, his mate, and five more, went in the other; and they contrived their business very well, for they came up to the ship about midnight.");
            add("So saying he unbuckled his baldric with the bugle");
            add("Just then her head struck against the roof of the hall: in fact she was now more than nine feet high, and she at once took up the little golden key and hurried off to the garden door.");
        }
    };

    static final ArrayList<String> images = new ArrayList<String>() {
        {
            add("https://habrastorage.org/getpro/habr/post_images/e4b/067/b17/e4b067b17a3e414083f7420351db272b.jpg");
            add("https://cdn.pixabay.com/photo/2017/12/25/17/48/waters-3038803_1280.jpg");
        }
    };

    public static String getRandomId() {
        return Long.toString(UUID.randomUUID().getLeastSignificantBits());
    }

    static String getRandomAvatar() {
        return avatars.get(rnd.nextInt(avatars.size()));
    }

    static String getRandomGroupChatImage() {
        return groupChatImages.get(rnd.nextInt(groupChatImages.size()));
    }

    static String getRandomGroupChatTitle() {
        return groupChatTitles.get(rnd.nextInt(groupChatTitles.size()));
    }

    static String getRandomName() {
        return names.get(rnd.nextInt(names.size()));
    }

    static String getRandomMessage() {
        return messages.get(rnd.nextInt(messages.size()));
    }

    static String getRandomImage() {
        return images.get(rnd.nextInt(images.size()));
    }

    static boolean getRandomBoolean() {
        return rnd.nextBoolean();
    }



    private DatabaseReference messagesRef;



    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }


    protected void loadMessages() {
        //imitation of internet connection
        new Handler().postDelayed(() -> {
            ArrayList<Message> messages = MessageActivity.getMessages(lastLoadedDate, ref);
            lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
            messagesAdapter.addToEnd(messages, false);
        }, 1000);
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return message -> {
            String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                    .format(message.getCreatedAt());

            String text = message.getText();
            if (text == null) text = "[attachment]";

            return String.format(Locale.getDefault(), "%s: %s (%s)",
                    message.getUser().getName(), text, createdAt);
        };
    }

//    private MessagesFixtures(DatabaseReference ref) {
//        messagesRef = ref.child("messages");
//        throw new AssertionError();
//    }
    private static void uploadMessage(DatabaseReference ref, Message message){
        ref.push().setValue(message);
    }
    private static void uploadMessages(DatabaseReference ref, ArrayList<Message> messages){
        for(Message message : messages)
            uploadMessage(ref, message);
    }
    public static Message getImageMessage() {
        Message message = new Message(getRandomId(), getUser(), null);
        message.setImage(new Message.Image(fileName));
        return message;}

    //useful//

    public static Message getVoiceMessage(String FileName, int Duration) {
        Message message = new Message(getRandomId(), getUser(), null);
        message.setVoice(new Message.Voice(FileName, Duration));
        return message;
    }
    //////////
    public static Message getTextMessage() {
        return getTextMessage(getRandomName());
    }

    public static Message getTextMessage(String text) {
        return new Message(getRandomId(), getUser(), text);
    }

    public static ArrayList<Message> getMessages(Date startDate, DatabaseReference ref) {
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10/*days count*/; i++) {
            int countPerDay = rnd.nextInt(5) + 1;

            for (int j = 0; j < countPerDay; j++) {
                Message message;
                if (i % 2 == 0 && j % 3 == 0) {
                    message = getImageMessage();
                } else {
                    message = getTextMessage();
                }

                Calendar calendar = Calendar.getInstance();
                if (startDate != null) calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, -(i * i + 1));

                message.setCreatedAt(calendar.getTime());
                messages.add(message);
            }
        }
        uploadMessages(ref, messages);
        return messages;
    }

    public static User getUser() {
        boolean even = rnd.nextBoolean();
        return new User(
                even ? "0" : "1",
                even ? names.get(0) : names.get(1),
                even ? avatars.get(0) : avatars.get(1),
                true);
    }

    private void uploadImage()
    {
        if (fileUri != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference.child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(fileUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(MessageActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(MessageActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

}