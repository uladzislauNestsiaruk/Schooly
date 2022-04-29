package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.NestedScrollableHost;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ExclusiveFragment extends Fragment {

    String version;
    UserInformation userInformation;

    public ExclusiveFragment(String version,UserInformation userInformation) {
        this.version = version;
        this.userInformation=userInformation;
    }

    public static ExclusiveFragment newInstance(String version,UserInformation userInformation) {
        return new ExclusiveFragment(version,userInformation);

    }

    FirebaseModel firebaseModel=new FirebaseModel();
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> exclusiveClothesArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> exclusivePopularArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> popularClothesArrayList=new ArrayList<Clothes>();
    RecyclerView clothes,popularClothes;
    NewClothesAdapter.ItemClickListener itemClickListener;
    PopularClothesAdapter.ItemClickListener itemClickListenerPopular;
    TextView newText,popularText,noPremium;
    NestedScrollableHost nestedScrollableHost;
    RelativeLayout premium,buyPremium;


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
        popularText=view.findViewById(R.id.popular);
        nestedScrollableHost=view.findViewById(R.id.nestedHost);
        noPremium=view.findViewById(R.id.noPremium);
        premium=view.findViewById(R.id.premium);
        buyPremium=view.findViewById(R.id.buyPremium);

        if(version.equals("premium")){
            loadClothesFromBase();
            itemClickListener=new NewClothesAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Clothes clothes) {
                    ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance(ShopFragment.newInstance(userInformation),userInformation));
                }
            };
            itemClickListenerPopular=new PopularClothesAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Clothes clothes) {
                    ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesPopular.newInstance(userInformation));
                }
            };
        }else {
            premium.setVisibility(View.VISIBLE);
            noPremium.setVisibility(View.VISIBLE);
            buyPremium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            newText.setVisibility(View.GONE);
            popularText.setVisibility(View.GONE);
            clothes.setVisibility(View.GONE);
            popularClothes.setVisibility(View.GONE);
            nestedScrollableHost.setVisibility(View.GONE);
        }
    }


    public void loadClothesFromBase(){
        RecentMethods.getClothes(firebaseModel, new Callbacks.GetClothes() {
            @Override
            public void getClothes(ArrayList<Clothes> allClothes) {
                clothesArrayList.addAll(allClothes);
                for(int i=0;i<clothesArrayList.size();i++){
                    Clothes cl=clothesArrayList.get(i);
                    if (cl.getExclusive().equals("exclusive")){
                        exclusiveClothesArrayList.add(cl);
                    }

                }
                NewClothesAdapter newClothesAdapter=new NewClothesAdapter(exclusiveClothesArrayList,itemClickListener);
                clothes.setAdapter(newClothesAdapter);
            }
        });
        RecentMethods.getPopular( firebaseModel, new Callbacks.GetClothes() {
            @Override
            public void getClothes(ArrayList<Clothes> allClothes) {
                popularClothesArrayList.addAll(allClothes);
                for(int i=0;i<popularClothesArrayList.size();i++){
                    Clothes cl=popularClothesArrayList.get(i);
                    if (cl.getExclusive().equals("exclusive")){
                        exclusivePopularArrayList.add(cl);
                    }
                }
                PopularClothesAdapter popularClothesAdapter=new PopularClothesAdapter(exclusivePopularArrayList,itemClickListenerPopular);
                popularClothes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                popularClothes.setNestedScrollingEnabled(false);
                popularClothes.setAdapter(popularClothesAdapter);
            }
        });
    }
}
