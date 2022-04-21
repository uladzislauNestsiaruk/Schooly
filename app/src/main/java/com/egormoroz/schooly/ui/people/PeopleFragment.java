package com.egormoroz.schooly.ui.people;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {
    ArrayList<UserInformation> listAdapterPeople=new ArrayList<UserInformation>();
    RecyclerView peopleRecyclerView;
    EditText searchUser;
    String userName,userNameToProfile,avatar,bio;


    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }
    FirebaseModel firebaseModel=new FirebaseModel();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        peopleRecyclerView=view.findViewById(R.id.peoplerecycler);
        searchUser=view.findViewById(R.id.searchuser);
        firebaseModel.initAll();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query3=firebaseModel.getUsersReference().child(nick).child("alreadySearched");
                query3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<UserPeopleAdapter> searchedUserFromBase=new ArrayList<>();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            UserPeopleAdapter upaSearch=new UserPeopleAdapter();
                            upaSearch.setNick(snap.child("nick").getValue(String.class));
                            upaSearch.setBio(snap.child("bio").getValue(String.class));
                            upaSearch.setAvatar(snap.child("avatar").getValue(String.class));
                            searchedUserFromBase.add(upaSearch);
                        }
                        AlreadySearchAdapter alreadySearchAdapter=new AlreadySearchAdapter(searchedUserFromBase);
                        peopleRecyclerView.setAdapter(alreadySearchAdapter);
                        AlreadySearchAdapter.ItemClickListener itemClickListener=new AlreadySearchAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                UserPeopleAdapter user = alreadySearchAdapter.getItem(position);
                                userNameToProfile = user.getNick();
                                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                    @Override
                                    public void PassUserNick(String nick) {
                                        if (userNameToProfile.equals(nick)) {
                                            RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback", nick, PeopleFragment.newInstance()), getActivity());
                                        } else {
                                            RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile, PeopleFragment.newInstance()),
                                                    getActivity());
                                        }
                                    }
                                });
                            }
                        };
                        alreadySearchAdapter.setClickListener(itemClickListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        initUserEnter();
        setPeopleData();
    }

    public void setPeopleData(){
        listAdapterPeople.add(new UserInformation("nick", "fidjfif", "gk",
                "6", "password", "Helicopter", 1000, "Miners",1,100,0, ""
                , "", ""," ","open","open","open","open","","regular", "",0));
    }
    public void initUserEnter(){
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (String.valueOf(searchUser.getText()).trim().length()==0){
                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                        @Override
                        public void PassUserNick(String nick) {
                            Query query3=firebaseModel.getUsersReference().child(nick).child("alreadySearched");
                            query3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList<UserPeopleAdapter> searchedUserFromBase=new ArrayList<>();
                                    for (DataSnapshot snap : snapshot.getChildren()) {
                                        UserPeopleAdapter upa=new UserPeopleAdapter();
                                        upa.setNick(snap.child("nick").getValue(String.class));
                                        upa.setBio(snap.child("bio").getValue(String.class));
                                        upa.setAvatar(snap.child("avatar").getValue(String.class));
                                        searchedUserFromBase.add(upa);
                                    }
                                    AlreadySearchAdapter alreadySearchAdapter=new AlreadySearchAdapter(searchedUserFromBase);
                                    peopleRecyclerView.setAdapter(alreadySearchAdapter);
                                    AlreadySearchAdapter.ItemClickListener itemClickListener=new AlreadySearchAdapter.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            UserPeopleAdapter user = alreadySearchAdapter.getItem(position);
                                            userNameToProfile = user.getNick();
                                            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                                @Override
                                                public void PassUserNick(String nick) {
                                                    if (userNameToProfile.equals(nick)) {
                                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback", nick, PeopleFragment.newInstance()), getActivity());
                                                    } else {
                                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile, PeopleFragment.newInstance()),
                                                                getActivity());
                                                    }
                                                }
                                            });
                                        }
                                    };
                                    alreadySearchAdapter.setClickListener(itemClickListener);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }else {
                    userName = String.valueOf(searchUser.getText()).trim();
                    userName = userName.toLowerCase();
                    Query query = firebaseModel.getReference("usersNicks");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<UserPeopleAdapter> userFromBase = new ArrayList<>();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                UserPeopleAdapter upa = new UserPeopleAdapter();
                                upa.setNick(snap.child("nick").getValue(String.class));
                                upa.setBio(snap.child("bio").getValue(String.class));
                                upa.setAvatar(snap.child("avatar").getValue(String.class));
                                String nickName = upa.getNick();
                                String nick = nickName;
                                int valueLetters = userName.length();
                                nick = nick.toLowerCase();
                                if (nick.length() < valueLetters) {
                                    if (nick.equals(userName))
                                        userFromBase.add(upa);
                                } else {
                                    nick = nick.substring(0, valueLetters);
                                    if (nick.equals(userName))
                                        userFromBase.add(upa);
                                }

                            }
                            PeopleAdapter peopleAdapter = new PeopleAdapter(userFromBase);
                            peopleRecyclerView.setAdapter(peopleAdapter);
                            PeopleAdapter.ItemClickListener clickListener =
                                    new PeopleAdapter.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            UserPeopleAdapter user = peopleAdapter.getItem(position);
                                            userNameToProfile = user.getNick();
                                            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                                @Override
                                                public void PassUserNick(String nick) {
                                                    if (userNameToProfile.equals(nick)) {
                                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback", nick, PeopleFragment.newInstance()), getActivity());
                                                    } else {
                                                        Query querySearchedAvatar = firebaseModel.getUsersReference().child(userNameToProfile).child("avatar");
                                                        querySearchedAvatar.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                avatar = snapshot.getValue(String.class);
                                                                Query querySearchedBio = firebaseModel.getUsersReference().child(userNameToProfile).child("bio");
                                                                querySearchedBio.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        bio = snapshot.getValue(String.class);
                                                                        firebaseModel.getUsersReference().child(nick).child("alreadySearched").child(userNameToProfile)
                                                                                .setValue(new UserPeopleAdapter(userNameToProfile, avatar, bio));
                                                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile, PeopleFragment.newInstance()),
                                                                                getActivity());
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    };
                            peopleAdapter.setClickListener(clickListener);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}