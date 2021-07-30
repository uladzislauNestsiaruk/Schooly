package com.egormoroz.schooly.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.DemoMessagesActivity;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.fixtures.MessagesFixtures;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.IOException;
import java.security.SecureRandom;


public class MessageActivity extends DemoMessagesActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener {
    String TAG = "############";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MessagesList messagesList;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private int[]  grantResult[];
    private static String fileName = null;
    private static final String LOG_TAG = "AudioRecordTest";

    private MediaRecorder recorder = null;

    private MediaPlayer   player = null;

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

    private void startRecording() {
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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        this.messagesList = findViewById(R.id.messagesList);
        initAdapter();
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest" + id + ".3gp";
        MessageInput input = findViewById(R.id.input);
        input.setInputListener(this);
        input.setTypingListener(this);
        ImageView voiceinput = findViewById(R.id.voiceinput);
        voiceinput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        view.setPressed(true);
                        startRecording();
                        Log.d(TAG, "Recording started");
                        break;

                    case MotionEvent.ACTION_UP:
                        view.setPressed(false);
                        stopRecording();
                        id += 1;
                        fileName = getExternalCacheDir().getAbsolutePath();
                        fileName += "/audiorecordtest" + id + ".3gp";
                        Log.d(TAG, "Recording stop");
                        messagesAdapter.addToStart(getVoiceMessage(), true);
                        break;
                }
                return true;
            }

        });
    }

    public static Message getVoiceMessage() {
        Message message = new Message(MessagesFixtures.getRandomId(), MessagesFixtures.getUser(), null);
        message.setVoice(new Message.Voice(fileName, rnd.nextInt(200) + 30));
        return message;
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

    @SuppressLint("ClickableViewAccessibility")
    public void onMessageViewClick(View view, Message message) {

    }
    private void initAdapter() {
        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        super.messagesAdapter.setOnMessageViewClickListener(new MessagesListAdapter.OnMessageViewClickListener<Message>() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public void onMessageViewClick(View view, Message message) {
                ImageView voiceinput = findViewById(R.id.voiceinput);
                voiceinput.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {

                            case MotionEvent.ACTION_DOWN:
                                view.setPressed(true);
                                startRecording();
                                Log.d(TAG, "Recording started");
                                break;

                            case MotionEvent.ACTION_UP:
                                view.setPressed(false);
                                stopRecording();
                                id += 1;
                                fileName = getExternalCacheDir().getAbsolutePath();
                                fileName += "/audiorecordtest" + id + ".3gp";
                                Log.d(TAG, "Recording stop");
                                messagesAdapter.addToStart(getVoiceMessage(), true);
                                break;
                        }
                        return true;
                    }

                });
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
}