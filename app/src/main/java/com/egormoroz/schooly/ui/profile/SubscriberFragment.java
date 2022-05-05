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

import androidx.activity.OnBackPressedCallback;
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
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    String userNameToProfile,userName;
    EditText searchUser;

    String type,nick;
    Fragment fragment;
    UserInformation userInformation;

    public SubscriberFragment(String type,Fragment fragment,UserInformation userInformation) {
        this.type = type;
        this.fragment=fragment;
        this.userInformation=userInformation;
    }

    public static SubscriberFragment newInstance(String type, Fragment fragment,UserInformation userInformation) {
        return new SubscriberFragment(type,fragment,userInformation);

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
        nick=userInformation.getNick();
        recyclerView=view.findViewById(R.id.subscribersRecycler);
        back=view.findViewById(R.id.back_toprofile);
        emptyList=view.findViewById(R.id.emptySubscribersList);
        searchUser=view.findViewById(R.id.searchuser);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type,nick,fragment,userInformation),getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type,nick,fragment,userInformation), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        putSubscribersListInAdapter();
        initUserEnter();
    }

    public void putSubscribersListInAdapter() {
        if (userInformation.getSubscribers() == null) {
            RecentMethods.getSubscribersList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                @Override
                public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                    if (subscribers.size() == 0) {
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        SubscribersAdapter subscribersAdapter = new SubscribersAdapter(subscribers);
                        recyclerView.setAdapter(subscribersAdapter);
                        SubscribersAdapter.ItemClickListener clickListener =
                                new SubscribersAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Subscriber user = subscribersAdapter.getItem(position);
                                        userNameToProfile = user.getSub();
                                        if (userNameToProfile.equals(nick)) {
                                            RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback", nick, fragment, userInformation), getActivity());
                                        } else {
                                            RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile, SubscriberFragment.newInstance(type, fragment, userInformation), userInformation),
                                                    getActivity());
                                        }
                                    }
                                };
                        subscribersAdapter.setClickListener(clickListener);
                    }
                }
            });
        } else {
            if (userInformation.getSubscribers().size() == 0) {
                emptyList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                SubscribersAdapter subscribersAdapter = new SubscribersAdapter(userInformation.getSubscribers());
                recyclerView.setAdapter(subscribersAdapter);
                SubscribersAdapter.ItemClickListener clickListener =
                        new SubscribersAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Subscriber user = subscribersAdapter.getItem(position);
                                userNameToProfile = user.getSub();
                                if (userNameToProfile.equals(nick)) {
                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback", nick, fragment, userInformation), getActivity());
                                } else {
                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile, SubscriberFragment.newInstance(type, fragment, userInformation), userInformation),
                                            getActivity());
                                }
                            }
                        };
                subscribersAdapter.setClickListener(clickListener);
            }
        }
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
                firebaseModel.getUsersReference().child(nick).child("subscribers")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot= task.getResult();
                            ArrayList<Subscriber> userFromBase = new ArrayList<>();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Subscriber subscriber = new Subscriber();
                                subscriber.setSub(snap.getValue(String.class));
                                String nick = subscriber.getSub();
                                int valueLetters = userName.length();
                                nick = nick.toLowerCase();
                                if (nick.length() < valueLetters) {
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                } else {
                                    nick = nick.substring(0, valueLetters);
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                }

                            }
                            if (userFromBase.size()==0){
                                emptyList.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else {
                                emptyList.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SubscribersAdapter subscribersAdapter = new SubscribersAdapter(userFromBase);
                                recyclerView.setAdapter(subscribersAdapter);
                                SubscribersAdapter.ItemClickListener clickListener =
                                        new SubscribersAdapter.ItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                Subscriber user = subscribersAdapter.getItem(position);
                                                userNameToProfile=user.getSub();
                                                if(userNameToProfile.equals(nick)){
                                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback",nick,fragment,userInformation),getActivity());
                                                }else {
                                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile,SubscriberFragment.newInstance(type,fragment,userInformation),userInformation),
                                                            getActivity());
                                                }
                                            }
                                        };
                                subscribersAdapter.setClickListener(clickListener);
                            }
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
