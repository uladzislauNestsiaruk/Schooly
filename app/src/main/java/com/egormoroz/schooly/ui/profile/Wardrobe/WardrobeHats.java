package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class WardrobeHats extends Fragment {
    String type;
    Fragment fragment;
    UserInformation userInformation;

    public WardrobeHats(String type,Fragment fragment,UserInformation userInformation) {
        this.type = type;
        this.fragment=fragment;
        this.userInformation=userInformation;
    }

    public static WardrobeHats newInstance(String type,Fragment fragment,UserInformation userInformation) {
        return new WardrobeHats(type,fragment,userInformation);

    }
    FirebaseModel firebaseModel=new FirebaseModel();
    ArrayList<Clothes> clothesArrayListWardrobe=new ArrayList<Clothes>();
    ArrayList<Clothes> sortHatsArrayListWardrobe=new ArrayList<Clothes>();
    RecyclerView wardrobeRecyclerView;
    WardrobeClothesAdapter.ItemClickListener itemClickListener;
    TextView buyToShop,noClothesText;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.viewpagerwardrobe, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        wardrobeRecyclerView=root.findViewById(R.id.recyclerwardrobe);
        loadClothesInWardrobe();
        Log.d("######", "dd");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().requestLayout();

    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        itemClickListener=new WardrobeClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingClothesWardrobe.newInstance(type,fragment,userInformation), getActivity());
            }
        };
        buyToShop=view.findViewById(R.id.buyToShop);
        noClothesText=view.findViewById(R.id.noClothesText);
    }


    public void loadClothesInWardrobe(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getClothesInWardrobe(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        clothesArrayListWardrobe.addAll(allClothes);
                        for(int i=0;i<clothesArrayListWardrobe.size();i++){
                            Clothes cl=clothesArrayListWardrobe.get(i);
                            String clType=cl.getClothesType();
                            if (clType.equals("hats")){
                                sortHatsArrayListWardrobe.add(cl);
                            }
                        }
                        if (sortHatsArrayListWardrobe.size()==0){
                            buyToShop.setVisibility(View.VISIBLE);
                            noClothesText.setVisibility(View.VISIBLE);
                            wardrobeRecyclerView.setVisibility(View.GONE);
                            buyToShop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RecentMethods.setCurrentFragment(ShopFragment.newInstance(userInformation), getActivity());
                                }
                            });
                        }else {
                            Collections.reverse(sortHatsArrayListWardrobe);
                            WardrobeClothesAdapter newClothesAdapter = new WardrobeClothesAdapter(sortHatsArrayListWardrobe, itemClickListener);
                            wardrobeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            wardrobeRecyclerView.setAdapter(newClothesAdapter);
                        }
                    }
                });
            }
        });
    }
}