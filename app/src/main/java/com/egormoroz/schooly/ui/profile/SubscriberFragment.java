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
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.Nontifications.NontificationAdapter;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.people.PeopleAdapter;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubscriberFragment extends Fragment {
    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    ImageView back;
    TextView emptyList;
    String userNameToProfile;

    public static SubscriberFragment newInstance() {
        return new SubscriberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscribers, container, false);
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
        emptyList=view.findViewById(R.id.emptySubscribersList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ProfileFragment.newInstance("user",new UserInformation()),getActivity());
            }
        });

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getSubscribersList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                    @Override
                    public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                        if (subscribers.size()==0){
                            emptyList.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            SubscribersAdapter subscribersAdapter = new SubscribersAdapter(subscribers);
                            recyclerView.setAdapter(subscribersAdapter);
                            SubscribersAdapter.ItemClickListener clickListener =
                                    new SubscribersAdapter.ItemClickListener() {
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
