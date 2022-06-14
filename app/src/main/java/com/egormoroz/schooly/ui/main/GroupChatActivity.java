package com.egormoroz.schooly.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.MessageAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.ar.sceneform.SceneView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class GroupChatActivity extends Activity {
    private String messageReceiverName, messageReceiverImage, messageSenderName;

    private static Activity instance;
    private TextView userName, userLastSeen;
    private ImageView userImage;
    FirebaseModel firebaseModel = new FirebaseModel();

    ImageView back;
    private DatabaseReference RootRef;

    private MediaRecorder recorder = null;
    private int duration;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private ImageButton SendMessageButton, SendFilesButton;
    private EditText MessageInputText;

    private final List<Message> messagesList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;

    private String checker = "", myUrl = "";
    private Uri fileUri;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        firebaseModel.initAll();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RootRef = firebaseModel.getUsersReference();

        instance = this;
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        if (data != null) {
            messageReceiverName = data.getString("groupName");
            messageSenderName = data.getString("curUser");
        }


        //     messageReceiverImage = getIntent().getExtras().get("visit_image").toString();
        DatabaseReference ref = firebaseModel.getUsersReference().child(messageSenderName).child("Chats").child(messageReceiverName).child("Unread");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getRef().setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        IntializeVoice();
        IntializeControllers();

        userMessagesList.scrollToPosition(userMessagesList.getAdapter().getItemCount());
        userName.setText(messageReceiverName);
        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.corners14).into(userImage);

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
                MessageInputText.getText().clear();
            }
        });
        SendFilesButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View view) {

                checker = "image";
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 443);
            }


        });
    }
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    private void IntializeControllers() {

        back = findViewById(R.id.backtoalldialogs);

        userName = findViewById(R.id.custom_profile_name);
        userImage = findViewById(R.id.custom_profile_image);
        userLastSeen = findViewById(R.id.custom_user_last_seen);

        SendMessageButton = findViewById(R.id.send_message_btn);
        SendFilesButton = findViewById(R.id.send_files_btn);
        MessageInputText = findViewById(R.id.input_message);

        messageAdapter = new MessageAdapter(messagesList, messageSenderName);
        userMessagesList = findViewById(R.id.private_messages_list_of_users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 443 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            if (checker.equals("image")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images");
                final String messageSenderRef = "/Groups/" + messageReceiverName + "/Messages/";

                DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child("Groups").child(messageReceiverName).child("Messages").push();
                String messagePushID = userMessageKeyRef.getKey();

                final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");

                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        Map<String, String> messageTextBody = new HashMap<>();
                        messageTextBody.put("message", myUrl);
                        messageTextBody.put("name", fileUri.getLastPathSegment());
                        messageTextBody.put("type", checker);
                        messageTextBody.put("from", messageSenderName);
                        messageTextBody.put("to", messageReceiverName);
                        messageTextBody.put("time", RecentMethods.getCurrentTime());
                        messageTextBody.put("messageID", messagePushID);


                        Map<String, Object> messageBodyDetails = new HashMap<>();
                        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                MessageInputText.setText("");
                            }
                        });
                    }
                });

            }
        }
    }


    @Override
    protected void onStart() {

        super.onStart();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                firebaseModel.getUsersReference().child("Groups").child(messageReceiverName).child("Messages")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Message messages = dataSnapshot.getValue(Message.class);
                                messagesList.add(messages);
                                messageAdapter.notifyDataSetChanged();
                                Log.d("Chat", String.valueOf(messagesList.size()));
                                userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
                                addUnread();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private void SendMessage() {

        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "first write your message...", Toast.LENGTH_SHORT).show();
        } else {
            final String messageSenderRef = "/Groups/" + messageReceiverName + "/Messages/";

            DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child("Groups").child(messageReceiverName).child("Messages").push();
            String messagePushID = userMessageKeyRef.getKey();

            Map<String, String> messageTextBody = new HashMap<>();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderName);
            messageTextBody.put("to", messageReceiverName);
            messageTextBody.put("time", RecentMethods.getCurrentTime());
            messageTextBody.put("messageID", messagePushID);
            addLastMessage("text", messageText);

            Map<String, Object> messageBodyDetails = new HashMap<String, Object>();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    MessageInputText.setText("");
                }
            });
        }

    }


    private void SendVoice() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Voice");
        DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child("Groups").child(messageReceiverName).child("Messages").push();
        final String messagePushID = userMessageKeyRef.getKey();
        final StorageReference filePath = storageReference.child(messagePushID + "." + "3gp");
        myUrl = getExternalCacheDir().getAbsolutePath() + "/voice.3gp";
        Uri file = Uri.fromFile(new File(myUrl));
        uploadTask = filePath.putFile(file);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUrl = task.getResult();
                myUrl = downloadUrl.toString();
                final String messageSenderRef = "/Groups/" + messageReceiverName + "/Messages/";

                DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child("Groups").child(messageReceiverName).child("Messages").push();
                String messagePushID = userMessageKeyRef.getKey();

                Map<String, String> messageTextBody = new HashMap<String, String>();
                messageTextBody.put("message", myUrl);
                messageTextBody.put("type", "voice");
                messageTextBody.put("from", messageSenderName);
                messageTextBody.put("to", messageReceiverName);
                messageTextBody.put("time", RecentMethods.getCurrentTime());
                messageTextBody.put("messageID", messagePushID);
                addLastMessage("voice", myUrl);

                Map<String, Object> messageBodyDetails = new HashMap<>();
                messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                    }
                });
            }
        });
    }



    private void IntializeVoice()
    {
        ImageView voice = findViewById(R.id.voiceinput);

        voice.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        try {
                            startRecording();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        stopRecording();
                        break;
                }
                return true;
            }
        });
    }


    private void startRecording() throws IOException {
        myUrl = getExternalCacheDir().getAbsolutePath() + "/voice.3gp";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(myUrl);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.prepare();
        recorder.start();
        Log.d("Voice", "Recording started");
    }
    private void stopRecording() {
        if (recorder != null) {
            recorder.reset();
            recorder.release();
            recorder = null;

            try {
                duration = getDuration(myUrl);}
            catch (Exception e){
                e.printStackTrace();
            }
            Log.d("voice", "Recording stop" + duration);
            if (duration <= 9) Log.d("Voice", "Voice too small");
            else {
                duration = duration / 10;
                SendVoice();
            }
        }
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }



    private void addLastMessage(String type, String Message){

        switch (type) {
            case "text":
                firebaseModel.getUsersReference().child("Groups").child(messageSenderName).child("LastMessage").setValue(messageSenderName + ":" + Message);
                break;
            case "voice":
                firebaseModel.getUsersReference().child("Groups").child(messageSenderName).child("LastMessage").setValue(messageSenderName + ":Голосовое сообщение");
                break;
            case "image":
                firebaseModel.getUsersReference().child("Groups").child(messageSenderName).child("LastMessage").setValue(messageSenderName + ":Фотография");
                break;
        }
        Calendar calendar = Calendar.getInstance();
        firebaseModel.getUsersReference().child("Groups").child(messageSenderName).child("LastTime").setValue(RecentMethods.getCurrentTime());
        firebaseModel.getUsersReference().child("Groups").child(messageSenderName).child("TimeMill").setValue(calendar.getTimeInMillis() * -1);
    }

    public void addUnread() {
        final long[] value = new long[1];
        DatabaseReference ref = firebaseModel.getUsersReference().child("Groups").child(messageSenderName).child("LastMessage").child("Unread");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    value[0] = (long) dataSnapshot.getValue();
                    value[0] = value[0] + 1;
                    dataSnapshot.getRef().setValue(value[0]);}
                else dataSnapshot.getRef().setValue(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}

