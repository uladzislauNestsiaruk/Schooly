package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
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
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClothesFragmentProfileOther extends Fragment {

    RecyclerView recyclerOther;
    TextView noLooksOther;
    FirebaseModel firebaseModel=new FirebaseModel();
    String otherUserNick;
    ClothesAdapterOther.ItemClickListener itemClickListener;
    Fragment fragment;

    public ClothesFragmentProfileOther(String otherUserNick,Fragment fragment) {
        this.otherUserNick = otherUserNick;
        this.fragment=fragment;
    }

    public static ClothesFragmentProfileOther newInstance(String otherUserNick,Fragment fragment) {
        return new ClothesFragmentProfileOther(otherUserNick,fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.viewpager_profileother, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        firebaseModel.initAll();
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        noLooksOther=view.findViewById(R.id.noLooks);
        recyclerOther=view.findViewById(R.id.Recycler);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                itemClickListener=new ClothesAdapterOther.ItemClickListener() {
                    @Override
                    public void onItemClick(Clothes clothes) {
                        RecentMethods.setCurrentFragment(ViewingClothes.newInstance(ProfileFragment.newInstance("other", otherUserNick, fragment)), getActivity());
                    }
                };
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(otherUserNick)
                        .child("myClothes");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Clothes> clothesFromBase=new ArrayList<>();
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
                            clothes.setBodyType(snap.child("bodyType").getValue(String.class));
                            clothes.setModel(snap.child("model").getValue(String.class));
                            clothes.setUid(snap.child("uid").getValue(String.class));
                            clothesFromBase.add(clothes);
                        }
                        if (clothesFromBase.size()==0){
                            noLooksOther.setVisibility(View.VISIBLE);
                            noLooksOther.setText(otherUserNick+" не создавал свою одежду :(");
                            recyclerOther.setVisibility(View.GONE);
                        }else {
                            recyclerOther.setVisibility(View.VISIBLE);
                            ClothesAdapterOther clothesAdapter=new ClothesAdapterOther(clothesFromBase,itemClickListener);
                            recyclerOther.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            recyclerOther.setAdapter(clothesAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}
