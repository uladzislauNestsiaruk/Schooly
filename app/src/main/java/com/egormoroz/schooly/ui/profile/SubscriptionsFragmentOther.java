package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    String otherUserNick;

    public static SubscriptionsFragmentOther newInstance() {
        return new SubscriptionsFragmentOther();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.friendsRecycler);

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
                        userData.setSubscriptionCount(snapshot.child("subscriptionCount").getValue(Long.class));
                        userData.setSubscribersCount(snapshot.child("subscribersCount").getValue(Long.class));
                        userData.setLooksCount(snapshot.child("looksCount").getValue(Long.class));
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
                        SubscriptionsAdapterOther subscribersAdapter=new SubscriptionsAdapterOther(friends);
                        recyclerView.setAdapter(subscribersAdapter);

                    }
                });
            }
        });
    }
}
