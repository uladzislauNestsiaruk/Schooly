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
import android.widget.Toast;

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
import java.sql.Time;
import java.util.ArrayList;

public class AcceptNewLook extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    RelativeLayout publish;
    TextView lookPrice,lookPriceDollar;
    EditText descriptionLook;
    ImageView schoolyCoin;
    RecyclerView recyclerView;
    long lookPriceLong,lookPriceDollarLong;
    String lookPriceString,lookPriceDollarString,nick,model;
    ConstituentsAdapter.ItemClickListener itemClickListener;
    String type;
    Fragment fragment;
    UserInformation userInformation;
    Bundle bundle;

    public AcceptNewLook(String model,String type,Fragment fragment,UserInformation userInformation,Bundle bundle) {
        this.model = model;
        this.type = type;
        this.fragment=fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static AcceptNewLook newInstance(String model,String type,Fragment fragment,UserInformation userInformation,Bundle bundle) {
        return new AcceptNewLook(model,type,fragment,userInformation,bundle);

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
    public void onDestroyView() {
        super.onDestroyView();
        if(descriptionLook.getText().toString().length()>0){
            bundle.putString("EDIT_DESCRIPTION_LOOK",descriptionLook.getText().toString().trim());
        }
    }


    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        nick=userInformation.getNick();
        publish=view.findViewById(R.id.publish);
        lookPrice=view.findViewById(R.id.lookPrice);
        lookPriceDollar=view.findViewById(R.id.lookPriceDollar);
        descriptionLook=view.findViewById(R.id.addDescriptionEdit);
        recyclerView=view.findViewById(R.id.constituentsRecycler);
        schoolyCoin=view.findViewById(R.id.schoolyCoin);

        itemClickListener=new ConstituentsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingClothesNews.newInstance(AcceptNewLook.newInstance(model,type,fragment, userInformation,bundle),userInformation,bundle), getActivity());
            }
        };

        ImageView backfromwardrobe=view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(type,fragment,userInformation,bundle), getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(type,fragment,userInformation,bundle), getActivity());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        if(bundle!=null){
            if(bundle.getString("EDIT_DESCRIPTION_LOOK")!=null){
                String editDescriptionText=bundle.getString("EDIT_DESCRIPTION_LOOK");
                descriptionLook.setText(editDescriptionText);
            }
        }
        getLookClothes();
    }

    public void getLookClothes(){
        for (int i=0;i<userInformation.getLookClothes().size();i++){
            Clothes clothes=userInformation.getLookClothes().get(i);
            if(clothes.getCurrencyType().equals("dollar")){
                lookPriceDollarLong+=clothes.getClothesPrice();
            }else {
                lookPriceLong+=clothes.getClothesPrice();
            }
        }
        if(lookPriceDollarLong>0 || lookPriceLong>0){
            setTextInLookPrice();
        }
        ConstituentsAdapter constituentsAdapter=new ConstituentsAdapter(userInformation.getLookClothes(), itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(constituentsAdapter);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lookId=firebaseModel.getUsersReference().child(nick).child("looks").push().getKey();
                firebaseModel.getUsersReference().child(nick).child("looks").child(lookId)
                        .setValue(new NewsItem(model, descriptionLook.getText().toString(), "0", lookId,
                                "", userInformation.getLookClothes(), 1200, 0,"",nick,0));
                descriptionLook.getText().clear();
                Toast.makeText(getContext(), "Образ успешно опубликован!", Toast.LENGTH_SHORT).show();
                RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick, fragment,userInformation,bundle), getActivity());
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
            checkCounts(lookPriceDollar, lookPriceDollarLong, lookPriceDollarString);
        }else {
            lookPriceString=String.valueOf(lookPriceLong);
            checkCounts(lookPrice, lookPriceLong, lookPriceString);
        }
    }

    public void checkCounts(TextView textView,Long count,String stringCount){
        if(count<1000){
            textView.setText(String.valueOf(count));
        }else if(count>1000 && count<10000){
            textView.setText(stringCount.substring(0, 1)+"."+stringCount.substring(1, 2)+"K");
        }
        else if(count>10000 && count<100000){
            textView.setText(stringCount.substring(0, 2)+"."+stringCount.substring(2,3)+"K");
        }else if(count>100000 && count<1000000){
            textView.setText(stringCount.substring(0, 3)+"K");
        }
        else if(count>1000000 && count<10000000){
            textView.setText(stringCount.substring(0, 1)+"KK");
        }
        else if(count>10000000 && count<100000000){
            textView.setText(stringCount.substring(0, 2)+"KK");
        }
    }
}