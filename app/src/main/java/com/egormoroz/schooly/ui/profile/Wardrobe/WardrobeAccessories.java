package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class WardrobeAccessories extends Fragment {
    public static WardrobeAccessories newInstance() {
        return new WardrobeAccessories();
    }
    FirebaseModel firebaseModel=new FirebaseModel();
    ArrayList<Clothes> clothesArrayListWardrobe=new ArrayList<Clothes>();
    ArrayList<Clothes> sortAccessoriesArrayListWardrobe=new ArrayList<Clothes>();
    RecyclerView wardrobeRecyclerView;
    WardrobeClothesAdapter.ItemClickListener itemClickListener;


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
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        itemClickListener=new WardrobeClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingClothesWardrobe.newInstance(), getActivity());
            }
        };
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
                            if (clType == "clothes"){
                                sortAccessoriesArrayListWardrobe.add(cl);
                            }
                        }
                        WardrobeClothesAdapter newClothesAdapter=new WardrobeClothesAdapter(sortAccessoriesArrayListWardrobe,itemClickListener);
                        wardrobeRecyclerView.setAdapter(newClothesAdapter);
                        Log.d("#####", "ggvppp  "+clothesArrayListWardrobe);
                    }
                });
            }
        });
    }
}