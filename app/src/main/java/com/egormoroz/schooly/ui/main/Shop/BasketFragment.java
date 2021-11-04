package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class BasketFragment extends Fragment {
    public static BasketFragment newInstance() {
        return new BasketFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    TextView numberOfClothes,schoolyCoin;
    ImageView backtoshop;
    RecyclerView basketRecycler;
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    BasketAdapter.ItemClickListener onItemClick;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        backtoshop=view.findViewById(R.id.back_toshopfrombasket);
        backtoshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ShopFragment.newInstance(), getActivity());
            }
        });
        schoolyCoin=view.findViewById(R.id.schoolycoinbasketfrag);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        schoolyCoin.setText(String.valueOf(money));
                    }
                });
            }
        });
        numberOfClothes=view.findViewById(R.id.numberofclothes);
        basketRecycler=view.findViewById(R.id.basketrecycler);
        loadClothesFromBasket();
        inViewingFrag();

    }

    public void loadClothesFromBasket(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getClothesInBasket(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        clothesArrayList.addAll(allClothes);
                        Log.d("######","vv "+clothesArrayList.size());
                        BasketAdapter basketAdapter=new BasketAdapter(clothesArrayList,onItemClick);
                        basketRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        basketRecycler.setAdapter(basketAdapter);
                        numberOfClothes.setText("Элементов в корзине:"+String.valueOf(clothesArrayList.size()));
                    }
                });
            }
        });
    }

    public void inViewingFrag(){
        onItemClick=new BasketAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesBasket.newInstance());
            }
        };
    }

}
