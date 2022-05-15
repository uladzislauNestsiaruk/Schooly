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
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.people.PeopleAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubscribesFragmentOther extends Fragment {
    FirebaseModel firebaseModel = new FirebaseModel();
    RecyclerView recyclerView;
    ImageView back;
    String otherUserNick, userNameToProfile,userName,nick;
    TextView emptyList;
    EditText searchUser;
    UserInformation userInformation;
    Fragment fragment;
    Bundle bundle;

    public SubscribesFragmentOther(Fragment fragment,String otherUserNick,UserInformation userInformation,Bundle bundle) {
        this.fragment=fragment;
        this.otherUserNick=otherUserNick;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static SubscribesFragmentOther newInstance( Fragment fragment,String otherUserNick
            ,UserInformation userInformation,Bundle bundle) {
        return new SubscribesFragmentOther(fragment,otherUserNick,userInformation,bundle);

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
        nick=userInformation.getNick();
        recyclerView = view.findViewById(R.id.subscribersRecycler);
        back = view.findViewById(R.id.back_toprofile);
        emptyList = view.findViewById(R.id.emptySubscribersListOther);
        searchUser=view.findViewById(R.id.searchuser);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", otherUserNick,fragment,userInformation,bundle),
                        getActivity());
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other",otherUserNick,fragment,userInformation,bundle), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        RecentMethods.getSubscribersList(otherUserNick, firebaseModel, new Callbacks.getSubscribersList() {
            @Override
            public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                if (subscribers.size() == 0) {
                    emptyList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyList.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    SubscribersAdapterOther subscribersAdapter = new SubscribersAdapterOther(subscribers);
                    recyclerView.setAdapter(subscribersAdapter);
                    SubscribersAdapterOther.ItemClickListener clickListener =
                            new SubscribersAdapterOther.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Subscriber user = subscribersAdapter.getItem(position);
                                    userNameToProfile = user.getSub();
                                    if(userNameToProfile.equals(nick)){
                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback",nick,SubscribesFragmentOther.newInstance(fragment,otherUserNick,userInformation,bundle),userInformation,bundle),getActivity());
                                    }else {
                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile,SubscribesFragmentOther.newInstance(fragment,otherUserNick,userInformation,bundle),userInformation,bundle)
                                                ,getActivity());
                                    }
                                }
                            };
                    subscribersAdapter.setClickListener(clickListener);
                }
            }
        });
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
                firebaseModel.getUsersReference().child(otherUserNick).child("subscribers")
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
                            if (userFromBase.size() == 0) {
                                emptyList.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                emptyList.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SubscribersAdapterOther subscribersAdapterOther = new SubscribersAdapterOther(userFromBase);
                                recyclerView.setAdapter(subscribersAdapterOther);
                                SubscribersAdapterOther.ItemClickListener clickListener =
                                        new SubscribersAdapterOther.ItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                Subscriber subscriber = subscribersAdapterOther.getItem(position);
                                                userNameToProfile = subscriber.getSub();
                                                if(userNameToProfile.equals(nick)){
                                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback",nick,SubscribesFragmentOther.newInstance(fragment,otherUserNick,userInformation,bundle),userInformation,bundle),getActivity());
                                                }else {
                                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile,SubscribesFragmentOther.newInstance(fragment,otherUserNick,userInformation,bundle),userInformation,bundle),
                                                            getActivity());
                                                }
                                            }
                                        };
                                subscribersAdapterOther.setClickListener(clickListener);
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
