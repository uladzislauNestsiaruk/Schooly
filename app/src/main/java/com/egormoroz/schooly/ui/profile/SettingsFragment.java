package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.RegisrtationstartFragment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    TextView  userNick,userNumber,userPassword,changePassword,blackList,exitAccout;
    String userNickString;
    SwitchMaterial privateAccountSwitch,chatsSwitch,groupChatsSwitch,profileSwitch;
    boolean checkType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
         View root =inflater.inflate(R.layout.fragment_settings,container,false);
         BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
         bnv.setVisibility(bnv.GONE);
         firebaseModel.initAll();
         return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        chatsSwitch=view.findViewById(R.id.chatsSwitch);
        groupChatsSwitch=view.findViewById(R.id.groupChatsSwitch);
        profileSwitch=view.findViewById(R.id.profileSwitch);

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("chatsNontsType");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String accountType = snapshot.getValue(String.class);
                        if(accountType.equals("close")){
                            chatsSwitch.setChecked(true);
                        }else {
                            chatsSwitch.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        exitAccout=view.findViewById(R.id.exitAccount);
        exitAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(RegisrtationstartFragment.newInstance(), getActivity());
            }
        });

        chatsSwitch.setOnClickListener(new View.OnClickListener() {
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
                                if (checkType=chatsSwitch.isChecked()){
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
                Query query=firebaseModel.getUsersReference().child(nick).child("groupChatsNontsType");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String accountType = snapshot.getValue(String.class);
                        if(accountType.equals("close")){
                            groupChatsSwitch.setChecked(true);
                        }else {
                            groupChatsSwitch.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        groupChatsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkType=true;
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("groupChatsNontsType");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (checkType=groupChatsSwitch.isChecked()){
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("groupChatsNontsType").setValue("close");
                                }else {
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("groupChatsNontsType").setValue("open");
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
                Query query=firebaseModel.getUsersReference().child(nick).child("profileNontsType");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String accountType = snapshot.getValue(String.class);
                        if(accountType.equals("close")){
                            profileSwitch.setChecked(true);
                        }else {
                            profileSwitch.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        profileSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkType=true;
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("profileNontsType");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (checkType=profileSwitch.isChecked()){
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("profileNontsType").setValue("close");
                                }else {
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("profileNontsType").setValue("open");
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




        privateAccountSwitch=view.findViewById(R.id.privateAccountSwitch);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("accountType");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String accountType = snapshot.getValue(String.class);
                       if(accountType.equals("open")){
                           privateAccountSwitch.setChecked(false);
                       }else {
                           privateAccountSwitch.setChecked(true);
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        privateAccountSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkType=true;
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        if (checkType=privateAccountSwitch.isChecked()){
                            firebaseModel.getUsersReference().child(nick)
                                    .child("accountType").setValue("close");
                        }else {
                            firebaseModel.getUsersReference().child(nick)
                                    .child("accountType").setValue("open");
                            /////////////
                        }
                    }
                });
            }
        });


        ImageView imageView = view.findViewById(R.id.backtomainfromsettings);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("user", nick,SettingsFragment.newInstance()), getActivity());
                    }
                });
            }
        });

        blackList=view.findViewById(R.id.blackList);
        blackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BlackListFragment.newInstance(), getActivity());
            }
        });

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query1=firebaseModel.getUsersReference().child(nick)
                        .child("number");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userNumber.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        userNick=view.findViewById(R.id.userNick);
        userNumber=view.findViewById(R.id.userNumber);


        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query1=firebaseModel.getUsersReference().child(nick)
                        .child("phone");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userNumber.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        userPassword=view.findViewById(R.id.userPassword);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick)
                        .child("password");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userPassword.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        changePassword=view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(PasswordFragment.newInstance(), getActivity());
            }
        });
        viewNick();

    }

    public void viewNick(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                userNick.setText(nick);
            }
        });
    }



}
