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

import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.ChatsFragment;
import com.egormoroz.schooly.ui.main.EnterFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class ViewingClothes extends Fragment {
    public static com.egormoroz.schooly.ui.main.Shop.ViewingClothes newInstance() {
        return new com.egormoroz.schooly.ui.main.Shop.ViewingClothes();

    }

    NewClothesAdapter.ItemClickListener itemClickListener;
    TextView clothesPriceCV,clothesTitleCV;
    ImageView clothesImageCV,backToShop;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewingclothes, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return root;

    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        clothesImageCV=view.findViewById(R.id.clothesImagecv);
        clothesTitleCV=view.findViewById(R.id.clothesTitlecv);
        clothesPriceCV=view.findViewById(R.id.clothesPricecv);
        backToShop=view.findViewById(R.id.back_toshop);
        backToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ShopFragment.newInstance(), getActivity());
            }
        });
        NewClothesAdapter.singeClothesInfo(new NewClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes, int position) {
                clothesPriceCV.setText(String.valueOf(clothes.getClothesPrice()));
                clothesTitleCV.setText(clothes.getClothesTitle());
                Picasso.get().load(clothes.getClothesImage()).into(clothesImageCV);
            }
        });
    }

}
