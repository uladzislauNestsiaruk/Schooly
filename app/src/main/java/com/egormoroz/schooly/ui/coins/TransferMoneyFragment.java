package com.egormoroz.schooly.ui.coins;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.CreateCharacter.BodyFragment;
import com.egormoroz.schooly.ui.main.CreateCharacter.CharacterAdapter;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.people.PeopleAdapter;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.egormoroz.schooly.ui.profile.SubscriberFragment;
import com.egormoroz.schooly.ui.profile.SubscribersAdapter;
import com.egormoroz.schooly.ui.profile.SubscriptionsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransferMoneyFragment extends Fragment {

    ArrayList<UserInformation> listAdapterPeople=new ArrayList<UserInformation>();
    RecyclerView peopleRecyclerView;
    EditText searchUser;
    FirebaseModel firebaseModel=new FirebaseModel();
    String userNameToProfile,userName;;
    ImageView backToCoins,transferHistory;
    TextView emptySubscriptionList,emptySearchSubscriptionList;

    Fragment fragment;

    public TransferMoneyFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public static TransferMoneyFragment newInstance(Fragment fragment) {
        return new TransferMoneyFragment(fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transfermoney, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        peopleRecyclerView=view.findViewById(R.id.peoplerecycler);
        searchUser=view.findViewById(R.id.searchuser);
        backToCoins=view.findViewById(R.id.backtocoins);
        emptySubscriptionList=view.findViewById(R.id.emptySubscriptionList);
        emptySearchSubscriptionList=view.findViewById(R.id.emptySearchSubscriptionList);
        transferHistory=view.findViewById(R.id.transferHistory);
        transferHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(TransferHistoryFragment.newInstance(fragment), getActivity());
            }
        });
        backToCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });
        firebaseModel.initAll();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getSubscriptionList(nick, firebaseModel, new Callbacks.getFriendsList() {
                    @Override
                    public void getFriendsList(ArrayList<Subscriber> friends) {
                        if (friends.size()==0){
                            emptySubscriptionList.setVisibility(View.VISIBLE);
                            peopleRecyclerView.setVisibility(View.GONE);
                        }else {
                            emptySubscriptionList.setVisibility(View.GONE);
                            peopleRecyclerView.setVisibility(View.VISIBLE);
                            TransferMoneyAdapter transferMoneyAdapter = new TransferMoneyAdapter(friends);
                            peopleRecyclerView.setAdapter(transferMoneyAdapter);
                            TransferMoneyAdapter.ItemClickListener itemClickListener = new TransferMoneyAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Subscriber user = transferMoneyAdapter.getItem(position);
                                    userNameToProfile = user.getSub();
                                    RecentMethods.setCurrentFragment(SendMoneyFragment.newInstance(userNameToProfile,fragment), getActivity());
                                }
                            };
                            transferMoneyAdapter.setClickListener(itemClickListener);
                        }
                    }
                });
            }
        });
        initUserEnter();
    }

    public void initUserEnter() {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                searchUser.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        userName = String.valueOf(searchUser.getText()).trim();
                        userName = userName.toLowerCase();
                        Query query = firebaseModel.getUsersReference().child(nick).child("subscription");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                                    emptySearchSubscriptionList.setVisibility(View.VISIBLE);
                                    peopleRecyclerView.setVisibility(View.GONE);
                                }else{
                                    emptySearchSubscriptionList.setVisibility(View.GONE);
                                    peopleRecyclerView.setVisibility(View.VISIBLE);
                                    TransferMoneyAdapter transferMoneyAdapter=new TransferMoneyAdapter(userFromBase);
                                    peopleRecyclerView.setAdapter(transferMoneyAdapter);
                                    TransferMoneyAdapter.ItemClickListener itemClickListener=new TransferMoneyAdapter.ItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Subscriber user = transferMoneyAdapter.getItem(position);
                                            userNameToProfile=user.getSub();
                                            RecentMethods.setCurrentFragment(SendMoneyFragment.newInstance(userNameToProfile,fragment), getActivity());
                                        }
                                    };
                                    transferMoneyAdapter.setClickListener(itemClickListener);
                                }
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
        });
    }
}
