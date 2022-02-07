package com.egormoroz.schooly.ui.coins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BuyCoinsFragment extends Fragment {

    private FirebaseModel firebaseModel = new FirebaseModel();
    String schoolyCoin, dollar,image;
    TextView customer,purchase;

    public BuyCoinsFragment(String schoolyCoin,String dollar) {
        this.schoolyCoin = schoolyCoin;
        this.dollar=dollar;
    }

    public static BuyCoinsFragment newInstance(String schoolyCoin,String dollar) {
        return new BuyCoinsFragment(schoolyCoin,dollar);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coins, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        firebaseModel.initAll();
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }
}
