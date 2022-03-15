package com.egormoroz.schooly.ui.main.MyClothes;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.coins.SendMoneyFragment;
import com.egormoroz.schooly.ui.coins.TransferHistoryFragment;
import com.egormoroz.schooly.ui.coins.TransferMoneyAdapter;
import com.egormoroz.schooly.ui.coins.TransferMoneyFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class PresentClothesFragment extends Fragment {

    ArrayList<UserInformation> listAdapterPeople=new ArrayList<UserInformation>();
    RecyclerView peopleRecyclerView;
    EditText searchUser;
    FirebaseModel firebaseModel=new FirebaseModel();
    String userNameToProfile,userName;;
    ImageView back,transferHistory;
    TextView emptySubscriptionList;

    Clothes clothes;
    Fragment fragment;

    public PresentClothesFragment(Clothes clothes,Fragment fragment) {
        this.clothes = clothes;
        this.fragment=fragment;
    }

    public static PresentClothesFragment newInstance(Clothes clothes,Fragment fragment) {
        return new PresentClothesFragment(clothes,fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_presentclothes, container, false);
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
        back=view.findViewById(R.id.backtocoins);
        emptySubscriptionList=view.findViewById(R.id.emptySubscriptionList);
        back.setOnClickListener(new View.OnClickListener() {
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
                        }else {
                            emptySubscriptionList.setVisibility(View.GONE);
                            PresentClothesAdapter presentClothesAdapter = new PresentClothesAdapter(friends);
                            peopleRecyclerView.setAdapter(presentClothesAdapter);
                            PresentClothesAdapter.ItemClickListener itemClickListener = new PresentClothesAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Subscriber user = presentClothesAdapter.getItem(position);
                                    userNameToProfile = user.getSub();
                                    showDialog();
                                }
                            };
                            presentClothesAdapter.setClickListener(itemClickListener);
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
                                PresentClothesAdapter presentClothesAdapter=new PresentClothesAdapter(userFromBase);
                                peopleRecyclerView.setAdapter(presentClothesAdapter);
                                PresentClothesAdapter.ItemClickListener itemClickListener=new PresentClothesAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Subscriber user = presentClothesAdapter.getItem(position);
                                        userNameToProfile=user.getSub();
                                        showDialog();
                                    }
                                };
                                presentClothesAdapter.setClickListener(itemClickListener);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
            }
        });
    }

    public void showDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.buy_miner_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text=dialog.findViewById(R.id.acceptText);
        text.setText("Подарить "+clothes.getClothesTitle()+" "+userNameToProfile);

        RelativeLayout no=dialog.findViewById(R.id.no);
        RelativeLayout yes=dialog.findViewById(R.id.yes);


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(userNameToProfile)
                                .child("clothes").child(clothes.getUid());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.equals(null)){

                                }else {
                                    if (snapshot.exists()) {
                                        Toast.makeText(getContext(), "У " + userNameToProfile + " уже есть этот предмет одежды", Toast.LENGTH_SHORT).show();
                                    } else {
                                        firebaseModel.getUsersReference().child(userNameToProfile).child("clothes")
                                                .child(clothes.getUid()).setValue(clothes);
                                        String numToBase = firebaseModel.getReference().child("users")
                                                .child(userNameToProfile).child("nontifications").push().getKey();
                                        firebaseModel.getReference().child("users")
                                                .child(userNameToProfile).child("nontifications")
                                                .child(numToBase).setValue(new Nontification(nick, "не отправлено", "подарок"
                                                , ServerValue.TIMESTAMP.toString(), clothes.getClothesTitle(), clothes.getClothesImage(), "не просмотрено", numToBase));
                                        Toast.makeText(getContext(), "Подарок отправлен", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
