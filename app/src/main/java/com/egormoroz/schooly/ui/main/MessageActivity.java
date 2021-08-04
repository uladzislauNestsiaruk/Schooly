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
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.DemoMessagesActivity;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.fixtures.MessagesFixtures;

import com.egormoroz.schooly.ui.chat.holders.InVoiceHolder;
import com.egormoroz.schooly.ui.chat.holders.OutVoiceHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;


public class MessageActivity extends DemoMessagesActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener{



    private static final Object CONTENT_TYPE_VOICE = 0;
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
    private long time_start = 1;
    private long time_stop = 1;
    private long time = 0;
    private Duration duration;


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
        recorder.stop();
        recorder.release();
        recorder = null;
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
        duration = Integer.parseInt(time)/1000;
        mmr.release();
        return duration;
    }

    public Message getVoiceMessage() {
        Message message = new Message(MessagesFixtures.getRandomId(), MessagesFixtures.getUser(), null);
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
                        time = getDuration(fileName);
                        Log.d(TAG, "Recording stop" + time);
                        messagesAdapter.addToStart(getVoiceMessage(), true);
                        break;
                }
                return true;
            }

        });
    }



    @Override
    public boolean onSubmit(CharSequence input) {
        super.messagesAdapter.addToStart(
                MessagesFixtures.getTextMessage(input.toString()), true);
        return true;
    }

    @Override
    public void onAddAttachments() {
        super.messagesAdapter.addToStart(
                MessagesFixtures.getImageMessage(), true);
    }


    private void initAdapter() {
        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, super.imageLoader);
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
}