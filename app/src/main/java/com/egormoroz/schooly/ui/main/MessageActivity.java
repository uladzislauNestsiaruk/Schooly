package com.egormoroz.schooly.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.DemoMessagesActivity;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.User;
import com.egormoroz.schooly.ui.chat.holders.IncomingVoiceMessageViewHolder;
import com.egormoroz.schooly.ui.chat.holders.OutcomingVoiceMessageViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class MessageActivity extends DemoMessagesActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        MessageInput.TypingListener{

    @Override
    protected void onStart() {
        super.onStart();
        messagesAdapter.addToStart(getTextMessage(), true);
    }

    private static final byte CONTENT_TYPE_VOICE = 1;
    String TAG = "############";
    private String userId;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MessagesList messagesList;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static String fileName = null;
    private static final String LOG_TAG = "AudioRecordTest";
    private Intent dialogIntent;
    private String dialogId;
    private MediaRecorder recorder = null;
    private MediaPlayer   player = null;
    private FirebaseAuth AuthenticationDatabase;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private long time = 0;
    private int duration;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            fileName = getExternalCacheDir().getAbsolutePath();
            fileName += "/audiorecordtest" + id + 1 + ".3gp";
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
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
              messagesAdapter.addToStart(getVoiceMessage(fileName, duration), true);
              duration = duration / 10;
              ImageView play = findViewById(R.id.pb_play);
              TextView dura = findViewById(R.id.duration);
              dura.setText(String.valueOf(duration));
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
        MessageInput input = findViewById(R.id.input);
        input.setInputListener(this);
        input.setTypingListener(this);
        getCurrentChatId();
        initFirebase();
        RecAudio();
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

    public Message getVoiceMessage() {
        Message message = new Message(getRandomId(), getUser(), null);
        message.setVoice(new Message.Voice(fileName, rnd.nextInt(200) + 30));
        return message;
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
                        Log.d(TAG, "Recording stop" + time);

                        break;
                }
                return true;
            }
        });
    }



    @Override
    public boolean onSubmit(CharSequence input) {
        super.messagesAdapter.addToStart(
               getTextMessage(input.toString()), true);
        return true;
    }

    @Override
    public void onAddAttachments() {
        super.messagesAdapter.addToStart(
              getImageMessage(), true);
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
                         this);
        super.messagesAdapter = new MessagesListAdapter<>(super.senderId,holders, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        super.messagesAdapter.setOnMessageClickListener(new MessagesListAdapter.OnMessageClickListener<Message>() {
            @Override
            public void onMessageClick(Message message) {
                onPlay(true);
            }
        });

        this.messagesList.setAdapter(super.messagesAdapter);

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
        dialogIntent = getIntent();
        dialogId = dialogIntent.getStringExtra("dialogId");
    }


    public void sendId(DatabaseReference ref){
        super.getReference(ref);
    }


    private void initFirebase(){
        AuthenticationDatabase = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        ref = database.getReference();
        userId = AuthenticationDatabase.getCurrentUser().getUid();
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
        message.setImage(new Message.Image(getRandomImage()));
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
}