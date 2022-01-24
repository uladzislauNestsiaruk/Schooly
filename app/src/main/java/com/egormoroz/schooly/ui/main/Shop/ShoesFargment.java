package com.egormoroz.schooly.ui.main.Shop;

import static java.lang.Double.max;

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
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class ShoesFargment extends Fragment {
    public static ShoesFargment newInstance() {
        return new ShoesFargment();
    }
    FirebaseModel firebaseModel=new FirebaseModel();
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> shoesArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> popularClothesArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> popularSortShoesArrayList=new ArrayList<Clothes>();
    RecyclerView clothes,popularClothes;
    NewClothesAdapter.ItemClickListener itemClickListener;
    PopularClothesAdapter.ItemClickListener itemClickListenerPopular;
    TextView newText;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layoutwiewpagershop, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        clothes=root.findViewById(R.id.newchlothesinshop);
        popularClothes=root.findViewById(R.id.popularchlothesinshop);
        loadClothesFromBase();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Log.d("#####", "cccddd");
        newText=view.findViewById(R.id.newnew);
        itemClickListener=new NewClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                Log.d("#####", "cccaaa");
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance());
            }
        };

        itemClickListenerPopular=new PopularClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesPopular.newInstance());
            }
        };
    }


    public void loadClothesFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getClothes(firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        clothesArrayList.addAll(allClothes);
                        for(int i=0;i<clothesArrayList.size();i++){
                            Clothes cl=clothesArrayList.get(i);
                            if (cl.getClothesType().equals("shoes")){
                                shoesArrayList.add(cl);
                            }

                        }
                        Log.d("#####", "size  "+clothesArrayList);
                        NewClothesAdapter newClothesAdapter=new NewClothesAdapter(shoesArrayList,itemClickListener);
                        clothes.setAdapter(newClothesAdapter);
                    }
                });
            }
        });
        RecentMethods.getPopular( firebaseModel, new Callbacks.GetClothes() {
            @Override
            public void getClothes(ArrayList<Clothes> allClothes) {
                popularClothesArrayList.addAll(allClothes);
                for(int i=0;i<popularClothesArrayList.size();i++){
                    Clothes cl=popularClothesArrayList.get(i);
                    if (cl.getClothesType().equals("shoes")){
                        popularSortShoesArrayList.add(cl);
                    }
                }
                PopularClothesAdapter popularClothesAdapter=new PopularClothesAdapter(popularSortShoesArrayList,itemClickListenerPopular);
                popularClothes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                popularClothes.setAdapter(popularClothesAdapter);
            }
        });
    }

    public static ArrayList<Integer> get_max(int amount, ArrayList<Integer> nums){
        Collections.sort(nums);
        ArrayList<Integer> res = new ArrayList<Integer>();
        for(int i = nums.size() - 1; i >= max(0, nums.size() - amount); i--)
            res.add(nums.get(i));
        return res;
    }
}