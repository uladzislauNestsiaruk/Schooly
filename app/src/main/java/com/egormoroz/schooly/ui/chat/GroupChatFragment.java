package com.egormoroz.schooly.ui.chat;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
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


public final class GroupChatFragment extends Fragment {

    UserInformation userInformation;
    Bundle bundle;
    Fragment fragment;
    Chat chat;
    MessageAdapter.ItemClickListener itemClickListener;

    public GroupChatFragment(UserInformation userInformation,Bundle bundle,Fragment fragment,Chat chat) {
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
        this.chat=chat;
    }

    public static GroupChatFragment newInstance(UserInformation userInformation, Bundle bundle, Fragment fragment,Chat chat) {
        return new GroupChatFragment(userInformation,bundle,fragment,chat);

    }
    private String messageReceiverName, messageReceiverImage, messageSenderName;

    private TextView  userLastSeen,groupName;
    private ImageView userImage;
    FirebaseModel firebaseModel = new FirebaseModel();

    ImageView back,info;
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
    int chatCheckValue;
    private StorageTask uploadTask;
    int a=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        firebaseModel.initAll();
        RootRef = firebaseModel.getUsersReference();
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return inflater.inflate(R.layout.activity_chat_group, container, false);
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageSenderName=userInformation.getNick();
        messageReceiverName=chat.getName();

        OnBackPressedCallback callback1 = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback1);

        itemClickListener=new MessageAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes, NewsItem newsItem) {
                if(clothes!=null){
                    RecentMethods.setCurrentFragment(ViewingClothesChat.newInstance(GroupChatFragment.newInstance(userInformation, bundle, fragment, chat), userInformation, bundle), getActivity());
                }else {

                }
            }
        };
        messageSenderName=userInformation.getNick();
        messageReceiverName=chat.getName();
        firebaseModel.getReference().child("groups").child(chat.getChatId()).child("Messages")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Message messages = new Message();
                        messages.setMessage(dataSnapshot.child("message").getValue(String.class));
                        messages.setMessageID(dataSnapshot.child("messageID").getValue(String.class));
                        messages.setTime(dataSnapshot.child("time").getValue(String.class));
                        messages.setType(dataSnapshot.child("type").getValue(String.class));
                        messages.setFrom(dataSnapshot.child("from").getValue(String.class));
                        messages.setTo(dataSnapshot.child("to").getValue(String.class));
                        if(messages.getType().equals("clothes")){
                            messages.setClothes(dataSnapshot.child("clothes").getValue(Clothes.class));
                        }else if(messages.getType().equals("look")){
                            messages.setNewsItem(dataSnapshot.child("look").getValue(NewsItem.class));
                        }
                        messagesList.add(messages);
                        if(messagesList.size()>0 && a==0&& !messages.getFrom().equals(userInformation.getNick())){
                            Log.d("#####", messageReceiverName);
                            firebaseModel.getUsersReference().child(messageSenderName).child("Dialogs")
                                    .child(chat.getChatId()).child("unreadMessages").setValue(0);
                            a++;
                        }
                        messageAdapter.notifyDataSetChanged();
                        userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
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
        userImage = view.findViewById(R.id.custom_profile_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                //
                //

            }
        });
        groupName = view.findViewById(R.id.custom_profile_name);
        userLastSeen = view.findViewById(R.id.custom_user_last_seen);


        SendMessageButton = view.findViewById(R.id.send_message_btn);
        SendFilesButton = view.findViewById(R.id.send_files_btn);
        MessageInputText = view.findViewById(R.id.input_message);

        messageAdapter = new MessageAdapter(messagesList, messageSenderName, messageReceiverName,itemClickListener);
        userMessagesList = view.findViewById(R.id.private_messages_list_of_users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);
        userMessagesList.setItemViewCacheSize(20);
        back=view.findViewById(R.id.backtoalldialogs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });


        IntializeVoice(view);
        if (chatCheckValue != 0) {
            if (chatCheckValue == -1) {
                ///////все окей//////
            } else if (chatCheckValue == 1 || chatCheckValue == 2) {
                MessageInputText.setText("Невозможно отправить сообщение");
                SendFilesButton.setVisibility(View.GONE);
                SendMessageButton.setVisibility(View.GONE);
            }
        }
        userMessagesList.scrollToPosition(userMessagesList.getAdapter().getItemCount());
        info=view.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(ChatInformationFragment.newInstance(userInformation,bundle,GroupChatFragment.newInstance(userInformation, bundle, fragment, chat),chat),getActivity());
            }
        });
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 443 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            if (checker.equals("image")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images");

                DatabaseReference userMessageKeyRef = firebaseModel.getReference().child("groups").child(chat.getChatId()).child("Messages").push();
                final String messagePushID = userMessageKeyRef.getKey();

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
                        Send(myUrl, "image",0);

                    }
                });
            }
        }
    }



    private void SendMessage() {

        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(getContext(), "first write your message...", Toast.LENGTH_SHORT).show();
        } else {
            Send(messageText, "text",0);
        }

    }


    private void SendVoice(long duration) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Voice");
        DatabaseReference userMessageKeyRef = firebaseModel.getReference().child("groups").child(chat.getChatId()).child("Messages").push();
        final String messagePushID = userMessageKeyRef.getKey();
        final StorageReference filePath = storageReference.child(messagePushID + "." + "3gp");
        myUrl = getActivity().getExternalCacheDir().getAbsolutePath() + "/voice.3gp";
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
                if(task.isSuccessful()){
                    Uri downloadUrl = task.getResult();
                    myUrl = downloadUrl.toString();
                    Send(myUrl, "voice",duration);
                }
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void IntializeVoice(View v) {
        ImageView voice = v.findViewById(R.id.voiceinput);
        ImageView circleVoice = v.findViewById(R.id.voiceActivityCircle);
        voice.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        try {
                            circleVoice.setVisibility(View.VISIBLE);
                            voice.setVisibility(View.GONE);
                            startRecording();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        circleVoice.setVisibility(View.GONE);
                        voice.setVisibility(View.VISIBLE);
                        stopRecording();
                        break;
                }
                return true;
            }
        });
    }


    private void startRecording() throws IOException {
        myUrl = getActivity().getExternalCacheDir().getAbsolutePath() + "/voice.3gp";

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
                duration = getDuration(myUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (duration <= 9) Log.d("Voice", "Voice too small");
            else {
                duration = duration / 10;
                SendVoice(duration);
            }
        }
    }

    public int getDuration(String mUri) {
        int duration;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mUri);
        String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = Integer.parseInt(time) / 100;
        mmr.release();
        return duration;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) RecentMethods.setCurrentFragment(fragment, getActivity());
    }


    private void addLastMessage(String type, String Message,String name) {

        Log.d("####",type);
        switch (type) {
            case "text":
                //addType("text");
                Log.d("###", "gg"+messageSenderName+"   "+messageReceiverName+"  "+Message);
                firebaseModel.getUsersReference().child(name).child("Dialogs").child(chat.getChatId()).child("lastMessage").setValue(Message);

                break;
            case "voice":
                //addType("voice");
                firebaseModel.getUsersReference().child(name).child("Dialogs").child(chat.getChatId()).child("lastMessage").setValue("Голосовое сообщение");

                break;
            case "image":
                firebaseModel.getUsersReference().child(name).child("Dialogs").child(chat.getChatId()).child("lastMessage").setValue("Фотография");

                //addType("image");
                break;
        }
        Calendar calendar = Calendar.getInstance();
        firebaseModel.getUsersReference().child(name).child("Dialogs").child(chat.getChatId()).child("lastTime").setValue(RecentMethods.getCurrentTime());
        Map<String,String> map=new HashMap<>();
        map= ServerValue.TIMESTAMP;
        firebaseModel.getUsersReference().child(name).child("Dialogs").child(chat.getChatId()).child("timeMill").setValue(map);
    }

    public void addUnread(String name) {
        final long[] value = new long[1];
        DatabaseReference ref = firebaseModel.getUsersReference().child(name).child("Dialogs").child(chat.getChatId()).child("unreadMessages");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    value[0] = (long) dataSnapshot.getValue();
                    value[0] = value[0] + 1;
                    dataSnapshot.getRef().setValue(value[0]);
                    firebaseModel.getUsersReference().child(name).child("Dialogs")
                            .child(chat.getChatId()).child("unreadMessages").setValue(0);
                } else dataSnapshot.getRef().setValue(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }


    public void addType(String type) {
        final long[] value = new long[1];
        DatabaseReference ref = firebaseModel.getUsersReference().child(messageReceiverName).child("Chats").child(messageSenderName).child(type);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    value[0] = (long) dataSnapshot.getValue();
                    value[0] = value[0] + 1;
                    dataSnapshot.getRef().setValue(value[0]);
                } else dataSnapshot.getRef().setValue(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }


    public void Send(String message, String type,long duration) {
        String messageSenderRef = chat.getChatId() + "/Messages";



        for(int i=0;i<chat.getMembers().size();i++){
            String nick=chat.getMembers().get(i).getNick();
            addLastMessage(type, message,nick);
            addUnread(nick);
            if(type.equals("image")){
                String uid=firebaseModel.getUsersReference().child(nick).child("Dialogs")
                        .child(chat.getChatId()).child("dialogueMaterials").push().getKey();
                firebaseModel.getUsersReference().child(nick).child("Dialogs")
                        .child(chat.getChatId()).child("dialogueMaterials").child(uid).setValue(message);
                firebaseModel.getUsersReference().child(nick).child("Dialogs")
                        .child(chat.getChatId()).child("dialogueMaterials").child(uid).setValue(message);
            }
        }

        DatabaseReference userMessageKeyRef = firebaseModel.getReference().child("groups").child(chat.getChatId()).child("Messages").push();
        String messagePushID = userMessageKeyRef.getKey();
        Map<String, String> messageTextBody = new HashMap<String, String>();
        messageTextBody.put("message", message);
        messageTextBody.put("type", type);
        messageTextBody.put("from", messageSenderName);
        messageTextBody.put("to", messageReceiverName);
        messageTextBody.put("time", RecentMethods.getCurrentTime());
        messageTextBody.put("messageID", messagePushID);
        Map<String, Object> messageBodyDetails = new HashMap<>();
        messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);

        firebaseModel.getReference().child("groups").updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                MessageInputText.setText("");
            }
        });
    }


}

