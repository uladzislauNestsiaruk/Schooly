package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.util.Log;
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
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubscribesFragmentOther extends Fragment {
    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    ImageView back;
    String otherUserNick,userNameToProfile;
    TextView emptyList;
    public static SubscribesFragmentOther newInstance() {
        return new SubscribesFragmentOther();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscriberother, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.subscribersRecycler);
        back=view.findViewById(R.id.back_toprofile);
        emptyList=view.findViewById(R.id.emptySubscribersListOther);
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
                RecentMethods.getSubscribersList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                    @Override
                    public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                        if (subscribers.size()==0){
                            emptyList.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            SubscribersAdapterOther subscribersAdapter = new SubscribersAdapterOther(subscribers);
                            recyclerView.setAdapter(subscribersAdapter);
                            SubscribersAdapterOther.ItemClickListener clickListener =
                                    new SubscribersAdapterOther.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Subscriber user = subscribersAdapter.getItem(position);
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
                            subscribersAdapter.setClickListener(clickListener);
                        }
                   }
                });
            }
        });
    }
}
