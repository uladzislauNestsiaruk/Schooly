package com.egormoroz.schooly.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.Dialog;
import com.egormoroz.schooly.ui.main.ChatActivity;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    FirebaseModel firebaseModel = new FirebaseModel();
    //   private DatabaseReference ChatRequestRef, UsersRef, ContactsRef;
    private FirebaseAuth mAuth;
    private String receiverUserID, senderUserID;
    String type;
    UserInformation info;
    TextView nickname;
    TextView message;

    public ProfileFragment(String type, UserInformation info) {
        this.type = type;
        this.info = info;
    }

    public static ProfileFragment newInstance(String type, UserInformation info) {
        return new ProfileFragment(type, info);
    }


    public void open() {
        Intent i = new Intent(getActivity(), ChatActivity.class);
        AcceptChatRequest();
        i.putExtra("name", info.getNick());
        i.putExtra("visit_user_id", info.getUid());
        i.putExtra("visit_image", ChatActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = type.equals("user") ? inflater.inflate(R.layout.fragment_profile, container, false) :
                inflater.inflate(R.layout.fragment_otheruser, container, false);
//        AppBarLayout abl=getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        nickname = type.equals("user") ? root.findViewById(R.id.usernick) :
                root.findViewById(R.id.otherusernick);
        message = type.equals("user") ? null :
                root.findViewById(R.id.message);
        return root;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseModel.initAll();
        switch (type) {
            case "user":
                ///////////////////////// set nickname /////////////////////
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(),
                        firebaseModel,
                        new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                nickname.setText(nick);
                            }
                        });
                //////////////////////////////////////////////////
                ImageView imageView = view.findViewById(R.id.settingsIcon);
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(SettingsFragment.newInstance());
                    }
                });
                ///////// I want GM on CF
                ImageView arrowtowardrobe = view.findViewById(R.id.arrowtowardrobe);
                arrowtowardrobe.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
                    }
                });
                TextView editing = view.findViewById(R.id.redact);
                editing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(EditingFragment.newInstance());
                    }
                });
                TextView texttowardrobe = view.findViewById(R.id.shielf);
                texttowardrobe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
                    }
                });

                break;
            case "other":
                nickname.setText(info.getNick());
                receiverUserID = info.getNick();
                senderUserID = firebaseModel.getUser().getDisplayName();
                Log.d("One", receiverUserID);
                Log.d("One", senderUserID);
                if (message != null) {
                    message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            open();
                        }
                    });
                }
                break;
        }
    }


    private void AcceptChatRequest()
    {
                    firebaseModel.getUsersReference().child(receiverUserID).child(senderUserID).child("Chat")
                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                                                if (task.isSuccessful())
                                                {
                                                    firebaseModel.getUsersReference().child(receiverUserID).child(senderUserID).child(receiverUserID)
                                                            .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task)
                                                                {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(getContext(), "New Contact Saved", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
