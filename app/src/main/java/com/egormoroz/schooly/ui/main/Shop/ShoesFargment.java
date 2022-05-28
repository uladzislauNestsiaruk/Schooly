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
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class ShoesFargment extends Fragment {
    UserInformation userInformation;
    Bundle bundle;
    Fragment fragment;

    public ShoesFargment(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
    }

    public static ShoesFargment newInstance(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        return new ShoesFargment(userInformation,bundle,fragment);
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

        newText=view.findViewById(R.id.newnew);
        itemClickListener=new NewClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance(ShopFragment.newInstance(userInformation,bundle,fragment),userInformation,bundle));
            }
        };

        itemClickListenerPopular=new PopularClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesPopular.newInstance(userInformation,bundle,ShopFragment.newInstance(userInformation,bundle,fragment)));
            }
        };
        loadClothesFromBase();
    }


    public void loadClothesFromBase(){
        if(bundle.getSerializable("SHOES_NEW")!=null){
            ArrayList<Clothes> newClothesArrayList= (ArrayList<Clothes>) bundle.getSerializable("SHOES_NEW");
            NewClothesAdapter newClothesAdapter=new NewClothesAdapter(newClothesArrayList,itemClickListener,userInformation);
            clothes.setAdapter(newClothesAdapter);
        }else{
            ArrayList<Clothes> allClothes= (ArrayList<Clothes>) bundle.getSerializable("ALL_CLOTHES");
            for(int i=0;i<allClothes.size();i++){
                Clothes cl=allClothes.get(i);
                if (cl.getClothesType().equals("shoes")){
                    shoesArrayList.add(cl);
                }

            }
            bundle.putSerializable("SHOES_NEW",shoesArrayList);
            NewClothesAdapter newClothesAdapter=new NewClothesAdapter(shoesArrayList,itemClickListener,userInformation);
            clothes.setAdapter(newClothesAdapter);
        }
        if(bundle.getSerializable("SHOES_POPULAR")!=null){
            popularClothesArrayList= (ArrayList<Clothes>) bundle.getSerializable("SHOES_POPULAR");
            PopularClothesAdapter popularClothesAdapter=new PopularClothesAdapter(popularClothesArrayList,itemClickListenerPopular,userInformation);
            popularClothes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            popularClothes.setNestedScrollingEnabled(false);
            popularClothes.setAdapter(popularClothesAdapter);
        }else{
            ArrayList<Clothes> allClothes= (ArrayList<Clothes>) bundle.getSerializable("ALL_CLOTHES");
            for(int i=0;i<allClothes.size();i++){
                Clothes cl=allClothes.get(i);
                if (cl.getClothesType().equals("shoes")){
                    popularSortShoesArrayList.add(cl);
                }

            }
            bundle.putSerializable("SHOES_POPULAR",popularSortShoesArrayList);
            PopularClothesAdapter popularClothesAdapter=new PopularClothesAdapter(popularSortShoesArrayList,itemClickListenerPopular,userInformation);
            popularClothes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            popularClothes.setNestedScrollingEnabled(false);
            popularClothes.setAdapter(popularClothesAdapter);
        }
    }

    public static ArrayList<Integer> get_max(int amount, ArrayList<Integer> nums){
        Collections.sort(nums);
        ArrayList<Integer> res = new ArrayList<Integer>();
        for(int i = nums.size() - 1; i >= max(0, nums.size() - amount); i--)
            res.add(nums.get(i));
        return res;
    }
}