package com.egormoroz.schooly.ui.main.MyClothes;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.profile.Wardrobe.AcceptNewLook;
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MyClothesFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerMyClothes;
    RelativeLayout createAndGet,getMoney,createClothesBig,relativeFirstClothes;
    TextView createClothes;
    MyClothesAdapter.ItemClickListener itemClickListener;

    int clothesListSize;

    public MyClothesFragment(int clothesListSize) {
        this.clothesListSize = clothesListSize;
    }

    public static MyClothesFragment newInstance(int clothesListSize) {
        return new MyClothesFragment(clothesListSize);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myclothes, container, false);
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

        recyclerMyClothes=view.findViewById(R.id.recyclerMyClothes);
        createClothesBig=view.findViewById(R.id.createBigButtonRecycler);
        createClothes=view.findViewById(R.id.createClothesButton);
        relativeFirstClothes=view.findViewById(R.id.relativeFirstClothes);
        itemClickListener=new MyClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingMyClothes.newInstance(MyClothesFragment.newInstance(clothesListSize)), getActivity());
            }
        };
        ImageView backtomain=view.findViewById(R.id.back_tomain);
        backtomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
                    }
                });
            }
        });
        getMyClothes();
    }

    public void getMyClothes(){
        Log.d("####", "cl  "+clothesListSize);
        if(clothesListSize==0){
            recyclerMyClothes.setVisibility(View.GONE);
            createClothesBig.setVisibility(View.VISIBLE);
            relativeFirstClothes.setVisibility(View.VISIBLE);
            createClothes.setVisibility(View.GONE);
            createClothesBig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentMethods.setCurrentFragment(CreateClothesFragment.newInstance(MyClothesFragment.newInstance(clothesListSize)), getActivity());
                }
            });
        }else {
            recyclerMyClothes.setVisibility(View.VISIBLE);
            createClothesBig.setVisibility(View.GONE);
            relativeFirstClothes.setVisibility(View.GONE);
            createClothes.setVisibility(View.VISIBLE);
            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                @Override
                public void PassUserNick(String nick) {
                    RecentMethods.getMyClothes(nick, firebaseModel, new Callbacks.GetClothes() {
                        @Override
                        public void getClothes(ArrayList<Clothes> allClothes) {
                            MyClothesAdapter myClothesAdapter=new MyClothesAdapter(allClothes,itemClickListener);
                            recyclerMyClothes.setAdapter(myClothesAdapter);
                        }
                    });
                }
            });
            createClothes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentMethods.setCurrentFragment(CreateClothesFragment.newInstance(MyClothesFragment.newInstance(clothesListSize)), getActivity());
                }
            });
        }
    }
}
