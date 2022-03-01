package com.egormoroz.schooly.ui.main.MyClothes;

import android.os.Bundle;
import android.util.Log;
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
            ,perSentToday,perSentAll,clothesPrice;
    ImageView clothesImageCV, backToShop, coinsImage,coinsImageAll,coinsImagePurple;
    long schoolyCoins;
    RelativeLayout checkBasket,resale,present;
    String clothesPriceString,purchaseTodayString,profitTodayString,profitAllString;
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
        coinsImagePurple=view.findViewById(R.id.coinImagePrice);
        description = view.findViewById(R.id.description);
        clothesPrice=view.findViewById(R.id.clothesPricecv);
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
                purchaseTodayString=String.valueOf(clothesViewing.getPurchaseToday());
                if(clothes.getPurchaseToday()<1000){
                    purchaseToday.setText(String.valueOf(clothes.getPurchaseToday()));
                }else if(clothes.getPurchaseToday()>1000 && clothes.getPurchaseToday()<10000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 1)+"."+purchaseTodayString.substring(1, 2)+"K");
                }
                else if(clothes.getPurchaseToday()>10000 && clothes.getPurchaseToday()<100000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 2)+"."+purchaseTodayString.substring(2,3)+"K");
                }
                else if(clothes.getPurchaseToday()>10000 && clothes.getPurchaseToday()<100000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 2)+"."+purchaseTodayString.substring(2,3)+"K");
                }else if(clothes.getPurchaseToday()>100000 && clothes.getPurchaseToday()<1000000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 3)+"K");
                }
                else if(clothes.getPurchaseToday()>1000000 && clothes.getPurchaseToday()<10000000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 1)+"KK");
                }
                else if(clothes.getPurchaseToday()>10000000 && clothes.getPurchaseToday()<100000000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 2)+"KK");
                }
                clothesPriceString=String.valueOf(clothes.getPurchaseNumber());
                if(clothes.getPurchaseNumber()<1000){
                    purchaseAll.setText(String.valueOf(clothes.getPurchaseNumber()));
                }else if(clothes.getPurchaseNumber()>1000 && clothes.getPurchaseNumber()<10000){
                    purchaseAll.setText(clothesPriceString.substring(0, 1)+"."+clothesPriceString.substring(1, 2)+"K");
                }
                else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
                    purchaseAll.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
                }
                else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
                    purchaseAll.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
                }else if(clothes.getPurchaseNumber()>100000 && clothes.getPurchaseNumber()<1000000){
                    purchaseAll.setText(clothesPriceString.substring(0, 3)+"K");
                }
                else if(clothes.getPurchaseNumber()>1000000 && clothes.getPurchaseNumber()<10000000){
                    purchaseAll.setText(clothesPriceString.substring(0, 1)+"KK");
                }
                else if(clothes.getPurchaseNumber()>10000000 && clothes.getPurchaseNumber()<100000000){
                    purchaseAll.setText(clothesPriceString.substring(0, 2)+"KK");
                }
                profitTodayString=String.valueOf(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday());
                Log.d("####", "ff "+profitTodayString);
                profitAllString=String.valueOf(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice());
                if (clothesViewing.getCurrencyType().equals("dollar")){
                    clothesPrice.setText("$"+String.valueOf(clothes.getClothesPrice()));
                    coinsImagePurple.setVisibility(View.GONE);
                    coinsImage.setVisibility(View.GONE);
                    coinsImageAll.setVisibility(View.GONE);
                    ////
                    if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000){
                        profitToday.setText("+"+profitTodayString+"$");
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000
                            && clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"."+profitTodayString.substring(1, 2)+"K"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K"+"$");
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>100000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 3)+"K"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"KK"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"KK"+"$");
                    }
                    ////////
                    if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000){
                        profitAll.setText("+"+profitAllString+"$");
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000
                            && clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"."+profitAllString.substring(1, 2)+"K"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K"+"$");
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>100000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000000){
                        profitAll.setText("+"+profitAllString.substring(0, 3)+"K"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"KK"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"KK"+"$");
                    }
                }else {
                    clothesPrice.setText(String.valueOf(clothes.getClothesPrice()));
                    if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000){
                        profitToday.setText("+"+profitTodayString);
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000
                            && clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"."+profitTodayString.substring(1, 2)+"K");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K");
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>100000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 3)+"K");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"KK");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"KK");
                    }
                    ////////
                    if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000){
                        profitAll.setText("+"+profitAllString);
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000
                            && clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"."+profitAllString.substring(1, 2)+"K");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K");
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>100000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000000){
                        profitAll.setText("+"+profitAllString.substring(0, 3)+"K");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"KK");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"KK");
                    }
                }
                if (clothesViewing.getPurchaseNumber()==0){
                    perCent=0;
                }else {
                    perCent=clothesViewing.getPurchaseToday()*100/clothesViewing.getPurchaseNumber();
                }
                perSentToday.setText("("+String.valueOf(perCent)+"%)");
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
