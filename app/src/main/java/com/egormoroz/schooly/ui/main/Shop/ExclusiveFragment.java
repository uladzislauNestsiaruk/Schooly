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
    Bundle bundle;

    public ExclusiveFragment(String version,UserInformation userInformation,Bundle bundle) {
        this.version = version;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static ExclusiveFragment newInstance(String version,UserInformation userInformation,Bundle bundle) {
        return new ExclusiveFragment(version,userInformation,bundle);

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
            itemClickListener=new NewClothesAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Clothes clothes) {
                    ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance(ShopFragment.newInstance(userInformation,bundle),userInformation,bundle));
                }
            };
            itemClickListenerPopular=new PopularClothesAdapter.ItemClickListener() {
                @Override
                public void onItemClick(Clothes clothes) {
                    ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesPopular.newInstance(userInformation,bundle));
                }
            };
            loadClothesFromBase();
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
        if(bundle.getSerializable("EXCLUSIVE_NEW")!=null){
            ArrayList<Clothes> newClothesArrayList= (ArrayList<Clothes>) bundle.getSerializable("EXCLUSIVE_NEW");
            NewClothesAdapter newClothesAdapter=new NewClothesAdapter(newClothesArrayList,itemClickListener);
            clothes.setAdapter(newClothesAdapter);
        }else{
            ArrayList<Clothes> allClothes= (ArrayList<Clothes>) bundle.getSerializable("ALL_CLOTHES");
            for(int i=0;i<allClothes.size();i++){
                Clothes cl=allClothes.get(i);
                if (cl.getExclusive().equals("exclusive")){
                    exclusiveClothesArrayList.add(cl);
                }

            }
            bundle.putSerializable("EXCLUSIVE_NEW",exclusiveClothesArrayList);
            NewClothesAdapter newClothesAdapter=new NewClothesAdapter(exclusiveClothesArrayList,itemClickListener);
            clothes.setAdapter(newClothesAdapter);
        }
        if(bundle.getSerializable("EXCLUSIVE_POPULAR")!=null){
            popularClothesArrayList= (ArrayList<Clothes>) bundle.getSerializable("EXCLUSIVE_POPULAR");
            PopularClothesAdapter popularClothesAdapter=new PopularClothesAdapter(popularClothesArrayList,itemClickListenerPopular);
            popularClothes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            popularClothes.setNestedScrollingEnabled(false);
            popularClothes.setAdapter(popularClothesAdapter);
        }else{
            ArrayList<Clothes> allClothes= (ArrayList<Clothes>) bundle.getSerializable("ALL_CLOTHES");
            for(int i=0;i<allClothes.size();i++){
                Clothes cl=allClothes.get(i);
                if (cl.getExclusive().equals("exclusive")){
                    exclusivePopularArrayList.add(cl);
                }

            }
            bundle.putSerializable("EXCLUSIVE_POPULAR",exclusivePopularArrayList);
            PopularClothesAdapter popularClothesAdapter=new PopularClothesAdapter(exclusivePopularArrayList,itemClickListenerPopular);
            popularClothes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            popularClothes.setNestedScrollingEnabled(false);
            popularClothes.setAdapter(popularClothesAdapter);
        }
    }
}
