package com.egormoroz.schooly.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.User;
import com.egormoroz.schooly.ui.chat.holders.InImageHolder;
import com.egormoroz.schooly.ui.chat.holders.InVidHold;
import com.egormoroz.schooly.ui.chat.holders.IncomingVoiceMessageViewHolder;
import com.egormoroz.schooly.ui.chat.holders.OutImageHolder;
import com.egormoroz.schooly.ui.chat.holders.OutVidHold;
import com.egormoroz.schooly.ui.chat.holders.OutcomingVoiceMessageViewHolder;
import com.egormoroz.schooly.ui.profile.OnDataPass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
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


public class MessageActivity extends Activity
        implements MessageInput.InputListener,
        MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageHolders.ContentChecker<Message>,
        MessageInput.TypingListener,
        OnDataPass {

    public static Uri fileUri;
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
    private static final byte CONTENT_TYPE_VIDEO = 3;
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
    private StorageTask uoloadTask;
    private int selectionCount;
    private String nick ;
    FirebaseModel firebaseModel = new FirebaseModel();
    ImageView delete;
    ImageView copy;
    ImageView voiceinput;
    MessageInput input;
    ImageView image;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_default_messages);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        this.messagesList = findViewById(R.id.messagesList);
        initAdapter();
        ImageView avatar = findViewById(R.id.avatar);
        fileName = getRandomAvatar();
        fileUri = Uri.parse(fileName);
        Picasso.get().load(fileUri).into(avatar);
        avatar.setImageURI(fileUri);
        ImageView back = findViewById(R.id.backtoalldialogs);
        ImageView pole = findViewById(R.id.pole);
        pole.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        pole.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (check)
//                {
//                    voiceinput.setImageResource(R.drawable.ic_image);
//                    Share();
//                    check = false;
//                }
//                else
//                {
//                    voiceinput.setImageResource(R.drawable.ic_voicemessage);
//                    RecAudio();
//                    check = true;
//                }
//
//            }
//        });
        InputMethodManager mInputMethodManager  = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        loadingBar = new ProgressDialog(this);
        TextView nickname = findViewById(R.id.mnick);
    //    nickname.setText(getRandomName());
    //    if (ProfileFragment.nickother != null)
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
           nick = arguments.getString("nick");
           nickname.setText(nick);
        }
        nickname.setVisibility(View.VISIBLE);
        image = findViewById(R.id.imageinput);
        voiceinput = findViewById(R.id.voiceinput);
        input = findViewById(R.id.input);
        copy = findViewById(R.id.action_copy);
        delete = findViewById(R.id.action_delete);
        copy.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        input.setInputListener(this);
        input.setTypingListener(this);
        getCurrentChatId();
        RecAudio();
        Share();
    }



    public void onDataPass(String data) {
        Log.d("LOG","привет, я строка из фрагмента: " + data);
        TextView nickname = findViewById(R.id.mnick);
        nickname.setText(data);
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

            voiceinput.setOnTouchListener(new View.OnTouchListener() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override

                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            id += 1;
                            startRecording();
                            Log.d(TAG, "Recording started");
                            break;

                        case MotionEvent.ACTION_UP:
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
       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               pickIntent.setType("image/* video/*");
               startActivityForResult(pickIntent, 438);
           }

       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( data != null) {
            Uri selectedUri = data.getData();
            String[] columns = { MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.MIME_TYPE };

            Cursor cursor = getContentResolver().query(selectedUri, columns, null, null, null);
            cursor.moveToFirst();

            int pathColumnIndex     = cursor.getColumnIndex( columns[0] );
            int mimeTypeColumnIndex = cursor.getColumnIndex( columns[1] );

            String contentPath = cursor.getString(pathColumnIndex);
            String mimeType    = cursor.getString(mimeTypeColumnIndex);
            cursor.close();

            if(mimeType.startsWith("image")) {
                fileUri = data.getData();
                Log.d(TAG,"Loading");
                imageLoader = (imageView, url, payload) -> Picasso.get().load(fileUri).into(imageView);
                fileName = fileUri.toString();
            messagesAdapter.addToStart(getImageMessage(), true);
            uploadImage();
            }
            else if(mimeType.startsWith("video")) {
                fileUri = data.getData();
                Log.d(TAG,"Loading");
                imageLoader = (videoView, url, payload) -> Picasso.get().load(fileUri).into(videoView);
                fileName = fileUri.toString();
                messagesAdapter.addToStart(getVideoMessage(), true);
            }
        }
    }
//TODO: Video duration = 2 min
    @Override
    public boolean hasContentFor(Message message, byte type) {
        if (type == CONTENT_TYPE_VOICE) {
            return message.getVoice() != null
                    && message.getVoice().getUrl() != null
                    && !message.getVoice().getUrl().isEmpty();
        }
        if (type == CONTENT_TYPE_IMAGE) {
            return message.getImageUrl() != null
                    && !message.getImageUrl().isEmpty();
        }
        if (type == CONTENT_TYPE_VIDEO) {
            return message.getImageUrl() != null
                    && !message.getImageUrl().isEmpty();
        }
        return false;
    }

    private void initAdapter() {
        imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);
        MessageHolders holders = new MessageHolders()
                .registerContentType(
                        CONTENT_TYPE_VIDEO,
                        InVidHold.class,
                        R.layout.in_vid,
                        OutVidHold.class,
                        R.layout.out_vid,
                        this)
                .registerContentType(
                        CONTENT_TYPE_VOICE,
                        IncomingVoiceMessageViewHolder.class,
                        R.layout.incoming_voice,
                        OutcomingVoiceMessageViewHolder.class,
                        R.layout.outcoming_voice,
                         this)
                .registerContentType(
                        CONTENT_TYPE_IMAGE,
                        InImageHolder.class,
                        R.layout.in_image,
                        OutImageHolder.class,
                        R.layout.out_image,
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


    public void getCurrentChatId(){
        Intent dialogIntent = getIntent();
        dialogId = dialogIntent.getStringExtra("dialogId");
    }


    static ArrayList<String> avatars = new ArrayList<String>() {
        {
            add("http://i.imgur.com/pv1tBmT.png");
            add("http://i.imgur.com/R3Jm1CL.png");
            add("http://i.imgur.com/ROz4Jgh.png");
            add("http://i.imgur.com/Qn9UesZ.png");
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



    public static String getRandomId() {
        return Long.toString(UUID.randomUUID().getLeastSignificantBits());
    }

    static String getRandomAvatar() {
        return avatars.get(rnd.nextInt(avatars.size()));
    }


    static String getRandomName() {
        return names.get(rnd.nextInt(names.size()));
    }





    private DatabaseReference messagesRef;



    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            //loadMessages();
        }
    }


//    protected void loadMessages() {
//        //imitation of internet connection
//        new Handler().postDelayed(() -> {
//            ArrayList<Message> messages = MessageActivity.getMessages(lastLoadedDate, ref);
//            lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
//            messagesAdapter.addToEnd(messages, false);
//        }, 1000);
//    }

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

//    private static void uploadMessage(DatabaseReference ref, Message message){
//        ref.push().setValue(message);
//    }
//    private static void uploadMessages(DatabaseReference ref, ArrayList<Message> messages){
//        for(Message message : messages)
//            uploadMessage(ref, message);
//    }
    public static  Message getVideoMessage(){
        Message message = new Message(getRandomId(), getUser(), null);
        message.setVideo(new Message.Video(fileName));
        return message;
    }
    public static Message getImageMessage() {
        Message message = new Message(getRandomId(), getUser(), null);
        message.setImage(new Message.Image(fileName));
        return message;}


    public static Message getVoiceMessage(String FileName, int Duration) {
        Message message = new Message(getRandomId(), getUser(), null);
        message.setVoice(new Message.Voice(FileName, Duration));
        return message;
    }

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
//        uploadMessages(ref, messages);
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
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

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
                                  //  progressDialog.dismiss();
                                    Toast
                                            .makeText(MessageActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });


        }
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                messagesAdapter.deleteSelectedMessages();
                break;
            case R.id.action_copy:
                messagesAdapter.copySelectedMessagesText(this, getMessageStringFormatter(), true);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {

        delete.setVisibility(View.VISIBLE);
        copy.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
        if (count > 0){
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesAdapter.deleteSelectedMessages();
            }
        });
        copy.setVisibility(View.VISIBLE);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.d(TAG, "TAp");
            }
        });}
        else
        {
            delete.setVisibility(View.GONE);
            copy.setVisibility(View.GONE);
        }
    }

}
