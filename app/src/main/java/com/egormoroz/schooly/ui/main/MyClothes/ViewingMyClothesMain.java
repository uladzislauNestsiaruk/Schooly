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


    TextView clothesPriceCV, clothesTitleCV, schoolyCoinCV, buyClothesBottom, purchaseNumber, creator, description, noDescription;
    ImageView clothesImageCV, backToShop, coinsImage, dollarImage, inBasket, notInBasket;
    long schoolyCoins, clothesPrise;
    RelativeLayout checkBasket;
    int a = 0;
    Clothes clothesViewing;
    private FirebaseModel firebaseModel = new FirebaseModel();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewingclothes, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;

    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        schoolyCoinCV = view.findViewById(R.id.schoolycoincvfrag);
        clothesImageCV = view.findViewById(R.id.clothesImagecv);
        inBasket = view.findViewById(R.id.inBasketClothes);
        notInBasket = view.findViewById(R.id.notInBasketClothes);
        coinsImage = view.findViewById(R.id.coinsImage);
        noDescription = view.findViewById(R.id.noDescription);
        dollarImage = view.findViewById(R.id.dollarImage);
        clothesTitleCV = view.findViewById(R.id.clothesTitlecv);
        description = view.findViewById(R.id.description);
        creator = view.findViewById(R.id.creator);
        checkBasket = view.findViewById(R.id.checkBasket);
        clothesPriceCV = view.findViewById(R.id.clothesPricecv);
        backToShop = view.findViewById(R.id.back_toshop);
        buyClothesBottom = view.findViewById(R.id.buyClothesBottom);
        purchaseNumber = view.findViewById(R.id.purchaseNumberViewing);
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
                clothesPriceCV.setText(String.valueOf(clothes.getClothesPrice()));
                clothesTitleCV.setText(clothes.getClothesTitle());
                clothesPrise = clothes.getClothesPrice();
                creator.setText(clothesViewing.getCreator());
                creator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                if (clothesViewing.getCreator().equals(nick)) {
                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("user", nick, ViewingClothes.newInstance(fragment)), getActivity());
                                } else {
                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", clothesViewing.getCreator(), ViewingClothes.newInstance(fragment)), getActivity());
                                }
                            }
                        });
                    }
                });
                if (clothesViewing.getDescription().trim().length() == 0) {
                    noDescription.setVisibility(View.VISIBLE);
                    description.setVisibility(View.GONE);
                } else {
                    description.setText(clothesViewing.getDescription());
                }
                purchaseNumber.setText(String.valueOf(clothesViewing.getPurchaseNumber()));
                Picasso.get().load(clothes.getClothesImage()).into(clothesImageCV);
                if (clothesViewing.getCurrencyType().equals("dollar")) {
                    dollarImage.setVisibility(View.VISIBLE);
                    coinsImage.setVisibility(View.GONE);
                }
            }
        });
    }
}

