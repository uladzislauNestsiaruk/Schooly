package com.egormoroz.schooly.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatInformationFrgment extends Fragment {
    TextView clearStory, nick;
    ImageView avatar, back;
    RecyclerView rec;
    SwitchMaterial switchMaterial;
    FirebaseModel firebaseModel;
    boolean checkType;
    String photoNumber, otherUserNick, messageNumber, voiceNumber;
    public ChatInformationFrgment() {
    }


    public ChatInformationFrgment(String otherUserNick) {
        this.otherUserNick = otherUserNick;
    }

    public static ChatInformationFrgment newInstance(String otherUserNick) {
        return new ChatInformationFrgment(otherUserNick);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chatsinfo, container, false);
        firebaseModel.initAll();
        switchMaterial = root.findViewById(R.id.nontsSwitch);
        avatar = root.findViewById(R.id.userImage);
        nick = root.findViewById(R.id.userNick);
        clearStory = root.findViewById(R.id.deleteHistory);
        rec = root.findViewById(R.id.recycler);
        back = root.findViewById(R.id.back_tochat);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intentReceived = new Intent();
        Bundle data = intentReceived.getExtras();

        if (data != null) {

        }
        else {

        }
        nick.setText(otherUserNick);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("chatsNontsType");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String accountType = snapshot.getValue(String.class);
                        if(accountType.equals("close")){
                            switchMaterial.setChecked(true);
                        }else {
                            switchMaterial.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        switchMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkType=true;
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("chatsNontsType");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (checkType=switchMaterial.isChecked()){
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("chatsNontsType").setValue("close");
                                }else {
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("chatsNontsType").setValue("open");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });


        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("curUser", nick);
                                chatIntent.putExtra("othUser", otherUserNick);
                                //   chatIntent.putExtra("visit_image", retImage[0]);
                                startActivity(chatIntent);
                            }
                        });
                    }
                });
        clearStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        deleteChat(nick, otherUserNick);
                    }
                });
            }
        });

    }





    public void deleteChat(String currUser, String othUser){
        firebaseModel.getUsersReference().child(currUser).child("Chats").child(othUser).removeValue();
    }
}
