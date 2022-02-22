package com.egormoroz.schooly.ui.main.MyClothes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class ViewingMyClothesMain extends Fragment {
    Fragment fragment;

    public ViewingMyClothesMain(Fragment fragment) {
        this.fragment = fragment;
    }

    public static ViewingMyClothesMain newInstance(Fragment fragment) {
        return new ViewingMyClothesMain(fragment);

    }


    TextView  clothesTitleCV, description, noDescription,purchaseToday,purchaseAll,profitToday,profitAll
            ,perSentToday,perSentAll;
    ImageView clothesImageCV, backToShop, coinsImage,coinsImageAll;
    long schoolyCoins, clothesPrise;
    RelativeLayout checkBasket,resale,present;
    int a = 0;
    Clothes clothesViewing;
    private FirebaseModel firebaseModel = new FirebaseModel();
    NewClothesAdapter.ViewHolder viewHolder;
    double perCent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewingmyclothes, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;

    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clothesImageCV = view.findViewById(R.id.clothesImagecv);
        coinsImage = view.findViewById(R.id.coinsImage);
        coinsImageAll = view.findViewById(R.id.coinsImageAll);
        noDescription = view.findViewById(R.id.noDescription);
        clothesTitleCV = view.findViewById(R.id.clothesTitlecv);
        description = view.findViewById(R.id.description);
        backToShop = view.findViewById(R.id.back_toshop);
        purchaseToday=view.findViewById(R.id.purchasesToday);
        purchaseAll=view.findViewById(R.id.purchasesAll);
        profitToday=view.findViewById(R.id.profit);
        profitAll=view.findViewById(R.id.profitAll);
        perSentToday=view.findViewById(R.id.perSentPurchase);
        perSentAll=view.findViewById(R.id.perSentPurchaseAll);
        resale=view.findViewById(R.id.resaleClothes);
        present=view.findViewById(R.id.presentClothes);
        backToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });

        MyClothesAdapterMain.singeClothesInfo(new MyClothesAdapterMain.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                clothesViewing = clothes;
                clothesTitleCV.setText(clothes.getClothesTitle());
                purchaseToday.setText(String.valueOf(clothesViewing.getPurchaseToday()));
                purchaseAll.setText(String.valueOf(clothesViewing.getPurchaseNumber()));
                if (clothesViewing.getCurrencyType().equals("dollar")){
                    coinsImage.setVisibility(View.GONE);
                    coinsImageAll.setVisibility(View.GONE);
                    profitToday.setText("+"+String.valueOf(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday())+"$");
                    profitAll.setText("+"+String.valueOf(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice())+"$");
                }else {
                    profitToday.setText("+"+String.valueOf(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()));
                    profitAll.setText("+"+String.valueOf(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()));
                }
                if (clothesViewing.getPurchaseNumber()==0){
                    perCent=0;
                }else {
                    perCent=clothesViewing.getPurchaseToday()*100/clothesViewing.getPurchaseNumber();
                }
                perSentToday.setText("("+String.valueOf(perCent)+"%)");
                clothesPrise = clothes.getClothesPrice();
                Picasso.get().load(clothesViewing.getClothesImage()).into(clothesImageCV);
                if (clothesViewing.getDescription().trim().length() == 0) {
                    noDescription.setVisibility(View.VISIBLE);
                    description.setVisibility(View.GONE);
                } else {
                    description.setText(clothesViewing.getDescription());
                }
            }
        });
    }
}
