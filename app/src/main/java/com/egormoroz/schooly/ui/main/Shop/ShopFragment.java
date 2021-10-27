package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.RecentMethods;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ShopFragment extends Fragment {
    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    RecyclerView clothes;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        loadModelInBase();
        clothes=root.findViewById(R.id.newchlothesinshop);
        loadClothesFromBase();
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ImageView backtoprofileshop=view.findViewById(R.id.back_toprofile);
        backtoprofileshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(MainFragment.newInstance());
            }
        });
    }

    public void loadModelInBase(){
        Clothes clothes=new Clothes("shoes", "jordan.jpg", 100);
        ArrayList<Clothes> allClothes=new ArrayList<>();
        allClothes.add(clothes);
        firebaseModel.getReference().child("AppData").child("Clothes").setValue(allClothes);
        Log.d("#####", "ggvp  ");
    }

    public void loadClothesFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getClothes(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        clothesArrayList.addAll(allClothes);
                        NewClothesAdapter newClothesAdapter=new NewClothesAdapter(clothesArrayList);
                        clothes.setAdapter(newClothesAdapter);
                        Log.d("#####", "ggvppp  "+clothesArrayList);
                    }
                });
            }
        });
    }
}