package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubscriptionsFragmentOther extends Fragment {
    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    ImageView back;
    String otherUserNick,userNameToProfile,userName;
    TextView emptyList;
    EditText searchUser;

    public static SubscriptionsFragmentOther newInstance() {
        return new SubscriptionsFragmentOther();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscriptionother, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.friendsRecycler);
        emptyList=view.findViewById(R.id.emptySubscriptionListOther);
        back=view.findViewById(R.id.back_toprofile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query1=firebaseModel.getReference().child("users").child(otherUserNick);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserInformation userData=new UserInformation();
                        userData.setAge(snapshot.child("age").getValue(Long.class));
                        userData.setAvatar(snapshot.child("avatar").getValue(Long.class));
                        userData.setGender(snapshot.child("gender").getValue(String.class));
                        //////////////////userData.setMiners();
                        userData.setNick(snapshot.child("nick").getValue(String.class));
                        userData.setPassword(snapshot.child("password").getValue(String.class));
                        userData.setPhone(snapshot.child("phone").getValue(String.class));
                        userData.setUid(snapshot.child("uid").getValue(String.class));
                        userData.setQueue(snapshot.child("queue").getValue(String.class));
                        userData.setAccountType(snapshot.child("accountType").getValue(String.class));
//                    userData.setSubscribers(snapshot.child("subscribers").getValue(String.class));
//                                                userData.setFriends(snapshot.child("friends").getValue(String.class));
                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userData),
                                getActivity());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        ProfileFragment.sendNickToAdapter(new ProfileFragment.sendNick() {
            @Override
            public void sendNick(String nick) {
                otherUserNick=nick;
                RecentMethods.getSubscriptionList(nick, firebaseModel, new Callbacks.getFriendsList() {
                    @Override
                    public void getFriendsList(ArrayList<Subscriber> friends) {
                        if (friends.size()==0){
                            emptyList.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            SubscriptionsAdapterOther subscriptionsAdapterOther = new SubscriptionsAdapterOther(friends);
                            recyclerView.setAdapter(subscriptionsAdapterOther);
                            SubscriptionsAdapterOther.ItemClickListener clickListener =
                                    new SubscriptionsAdapterOther.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Subscriber user = subscriptionsAdapterOther.getItem(position);
                                            userNameToProfile=user.getSub();
                                            Log.d("###","n "+userNameToProfile);
                                            Query query1=firebaseModel.getReference().child("users").child(userNameToProfile);
                                            query1.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    UserInformation userData=new UserInformation();
                                                    userData.setAge(snapshot.child("age").getValue(Long.class));
                                                    userData.setAvatar(snapshot.child("avatar").getValue(Long.class));
                                                    userData.setGender(snapshot.child("gender").getValue(String.class));
                                                    //////////////////userData.setMiners();
                                                    userData.setNick(snapshot.child("nick").getValue(String.class));
                                                    userData.setPassword(snapshot.child("password").getValue(String.class));
                                                    userData.setPhone(snapshot.child("phone").getValue(String.class));
                                                    userData.setUid(snapshot.child("uid").getValue(String.class));
                                                    userData.setQueue(snapshot.child("queue").getValue(String.class));
                                                    userData.setAccountType(snapshot.child("accountType").getValue(String.class));
                                                    userData.setBio(snapshot.child("bio").getValue(String.class));
                                                    //                                               userData.setSubscribers(snapshot.child("subscribers").getValue(String.class));
//                                                userData.setFriends(snapshot.child("friends").getValue(String.class));
                                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userData),
                                                            getActivity());
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    };
                            subscriptionsAdapterOther.setClickListener(clickListener);
                        }
                    }
                });
            }
        });
        searchUser=view.findViewById(R.id.searchuser);
        initUserEnter();
    }
    public void initUserEnter() {
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userName = String.valueOf(searchUser.getText()).trim();
                userName = userName.toLowerCase();
                Query query = firebaseModel.getUsersReference().child(otherUserNick).child("subscription");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Subscriber> userFromBase = new ArrayList<>();
                        Log.d("####", "un " + userName);
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Subscriber subscriber = new Subscriber();
                            subscriber.setSub(snap.getValue(String.class));
                            String nick = subscriber.getSub();
                            int valueLetters = userName.length();
                            Log.d("####", "un " + userName);
                            nick = nick.toLowerCase();
                            if (nick.length() < valueLetters) {
                                if (nick.equals(userName))
                                    userFromBase.add(subscriber);
                                Log.d("####", "nb " + nick);
                            } else {
                                nick = nick.substring(0, valueLetters);
                                if (nick.equals(userName))
                                    userFromBase.add(subscriber);
                                Log.d("####", "nb " + nick);
                            }
                            Log.d("####", "cc " + userFromBase);

                        }
                        SubscriptionsAdapterOther subscriptionsAdapterOther = new SubscriptionsAdapterOther(userFromBase);
                        recyclerView.setAdapter(subscriptionsAdapterOther);
                        SubscriptionsAdapterOther.ItemClickListener clickListener =
                                new SubscriptionsAdapterOther.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Subscriber subscriber = subscriptionsAdapterOther.getItem(position);
                                        userNameToProfile = subscriber.getSub();
                                        Log.d("###", "n " + userNameToProfile);
                                        Query query1 = firebaseModel.getReference().child("users").child(userNameToProfile);
                                        query1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                UserInformation userData = new UserInformation();
                                                userData.setAge(snapshot.child("age").getValue(Long.class));
                                                userData.setAvatar(snapshot.child("avatar").getValue(Long.class));
                                                userData.setGender(snapshot.child("gender").getValue(String.class));
                                                //////////////////userData.setMiners();
                                                userData.setNick(snapshot.child("nick").getValue(String.class));
                                                userData.setPassword(snapshot.child("password").getValue(String.class));
                                                userData.setPhone(snapshot.child("phone").getValue(String.class));
                                                userData.setUid(snapshot.child("uid").getValue(String.class));
                                                userData.setQueue(snapshot.child("queue").getValue(String.class));
                                                userData.setAccountType(snapshot.child("accountType").getValue(String.class));
                                                userData.setBio(snapshot.child("bio").getValue(String.class));
                                                //                                               userData.setSubscribers(snapshot.child("subscribers").getValue(String.class));
//                                                userData.setFriends(snapshot.child("friends").getValue(String.class));
                                                RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userData),
                                                        getActivity());
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                };
                        subscriptionsAdapterOther.setClickListener(clickListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                    }
                });
            }
        });
    }
}
