package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.news.ViewingClothesNews;
import com.egormoroz.schooly.ui.profile.Look;
import com.egormoroz.schooly.ui.profile.LooksAdapter;
import com.egormoroz.schooly.ui.profile.LooksFragmentProfileOther;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileReader;
import java.util.ArrayList;

public class AcceptNewLook extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    RelativeLayout publish;
    TextView lookPrice,lookPriceDollar;
    EditText descriptionLook;
    ImageView schoolyCoin;
    RecyclerView recyclerView;
    long lookPriceLong,lookPriceDollarLong;
    String lookPriceString,lookPriceDollarString;
    String model;
    ConstituentsAdapter.ItemClickListener itemClickListener;
    String type;
    Fragment fragment;
    UserInformation userInformation;

    public AcceptNewLook(String model,String type,Fragment fragment,UserInformation userInformation) {
        this.model = model;
        this.type = type;
        this.fragment=fragment;
        this.userInformation=userInformation;
    }

    public static AcceptNewLook newInstance(String model,String type,Fragment fragment,UserInformation userInformation) {
        return new AcceptNewLook(model,type,fragment,userInformation);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_acceptnewlook, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        publish=view.findViewById(R.id.publish);
        lookPrice=view.findViewById(R.id.lookPrice);
        lookPriceDollar=view.findViewById(R.id.lookPriceDollar);
        descriptionLook=view.findViewById(R.id.addDescriptionEdit);
        recyclerView=view.findViewById(R.id.constituentsRecycler);
        schoolyCoin=view.findViewById(R.id.schoolyCoin);

        itemClickListener=new ConstituentsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingClothesNews.newInstance(AcceptNewLook.newInstance(model,type,fragment, userInformation),userInformation), getActivity());
            }
        };

        ImageView backfromwardrobe=view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(type,fragment,userInformation), getActivity());
                    }
                });
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(type,fragment,userInformation), getActivity());
                    }
                };

                requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
            }
        });
        getLookClothes();
    }

    public void getLookClothes(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("lookClothes");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Clothes> lookClothesFromBase=new ArrayList<>();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Clothes clothes = new Clothes();
                            clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                            clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                            clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                            clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                            clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                            clothes.setCreator(snap.child("creator").getValue(String.class));
                            clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
                            clothes.setDescription(snap.child("description").getValue(String.class));
                            clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                            clothes.setModel(snap.child("model").getValue(String.class));
                            clothes.setBodyType(snap.child("bodyType").getValue(String.class));
                            clothes.setUid(snap.child("uid").getValue(String.class));
                            lookClothesFromBase.add(clothes);
                            if(clothes.getCurrencyType().equals("dollar")){
                                lookPriceDollarLong+=clothes.getClothesPrice();
                            }else {
                                lookPriceLong+=clothes.getClothesPrice();
                            }
                        }
                        if(lookPriceDollarLong>0 || lookPriceLong>0){
                            setTextInLookPrice();
                        }
                        ConstituentsAdapter constituentsAdapter=new ConstituentsAdapter(lookClothesFromBase,itemClickListener);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(constituentsAdapter);
                        publish.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                    @Override
                                    public void PassUserNick(String nick) {
                                        String lookId=firebaseModel.getUsersReference().child(nick).child("looks").push().getKey();
                                        firebaseModel.getUsersReference().child(nick).child("looks").child(lookId)
                                                .setValue(new NewsItem(model, descriptionLook.getText().toString(), "0", lookId,
                                                        "", lookClothesFromBase, 1200, 0,"",nick,0));
                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick, fragment,userInformation), getActivity());
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void setTextInLookPrice(){
        if (lookPriceDollarLong==0 && lookPriceLong>0){
            lookPriceDollar.setVisibility(View.GONE);
        }else{
            lookPriceDollarString=String.valueOf(lookPriceDollarLong);
            if(lookPriceDollarLong<1000){
                lookPriceDollar.setText(" + "+lookPriceDollarString+"$");
            }else if(lookPriceDollarLong>1000 && lookPriceDollarLong<10000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 1)+"."+lookPriceDollarString.substring(1, 2)+"K"+"$");
            }
            else if(lookPriceDollarLong>10000 && lookPriceDollarLong<100000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
            }
            else if(lookPriceDollarLong>10000 && lookPriceDollarLong<100000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
            }else if(lookPriceDollarLong>100000 && lookPriceDollarLong<1000000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 3)+"K"+"$");
            }
            else if(lookPriceDollarLong>1000000 && lookPriceDollarLong<10000000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 1)+"KK"+"$");
            }
            else if(lookPriceDollarLong>10000000 && lookPriceDollarLong<100000000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"KK"+"$");
            }
        }
        if(lookPriceLong==0){
            schoolyCoin.setVisibility(View.GONE);
            lookPrice.setVisibility(View.GONE);
            lookPriceDollarString=String.valueOf(lookPriceDollarLong);
            if(lookPriceDollarLong<1000){
                lookPriceDollar.setText(lookPriceDollarString+"$");
            }else if(lookPriceDollarLong>1000 && lookPriceDollarLong<10000){
                lookPriceDollar.setText(lookPriceDollarString.substring(0, 1)+"."+lookPriceDollarString.substring(1, 2)+"K"+"$");
            }
            else if(lookPriceDollarLong>10000 && lookPriceDollarLong<100000){
                lookPriceDollar.setText(lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
            }
            else if(lookPriceDollarLong>10000 && lookPriceDollarLong<100000){
                lookPriceDollar.setText(lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
            }else if(lookPriceDollarLong>100000 && lookPriceDollarLong<1000000){
                lookPriceDollar.setText(lookPriceDollarString.substring(0, 3)+"K"+"$");
            }
            else if(lookPriceDollarLong>1000000 && lookPriceDollarLong<10000000){
                lookPriceDollar.setText(lookPriceDollarString.substring(0, 1)+"KK"+"$");
            }
            else if(lookPriceDollarLong>10000000 && lookPriceDollarLong<100000000){
                lookPriceDollar.setText(lookPriceDollarString.substring(0, 2)+"KK"+"$");
            }
        }else {
            lookPriceString=String.valueOf(lookPriceLong);
            if(lookPriceLong<1000){
                lookPrice.setText(String.valueOf(lookPriceString));
            }else if(lookPriceLong>1000 && lookPriceLong<10000){
                lookPrice.setText(lookPriceString.substring(0, 1)+"."+lookPriceString.substring(1, 2)+"K");
            }
            else if(lookPriceLong>10000 && lookPriceLong<100000){
                lookPrice.setText(lookPriceString.substring(0, 2)+"."+lookPriceString.substring(2,3)+"K");
            }
            else if(lookPriceLong>10000 && lookPriceLong<100000){
                lookPrice.setText(lookPriceString.substring(0, 2)+"."+lookPriceString.substring(2,3)+"K");
            }else if(lookPriceLong>100000 && lookPriceLong<1000000){
                lookPrice.setText(lookPriceString.substring(0, 3)+"K");
            }
            else if(lookPriceLong>1000000 && lookPriceLong<10000000){
                lookPrice.setText(lookPriceString.substring(0, 1)+"KK");
            }
            else if(lookPriceLong>10000000 && lookPriceLong<100000000){
                lookPrice.setText(lookPriceString.substring(0, 2)+"KK");
            }
        }
    }

}