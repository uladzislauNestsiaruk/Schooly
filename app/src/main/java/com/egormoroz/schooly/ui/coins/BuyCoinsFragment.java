package com.egormoroz.schooly.ui.coins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BuyCoinsFragment extends Fragment {

    private FirebaseModel firebaseModel = new FirebaseModel();
    String schoolyCoin, dollar,image;
    TextView customer,purchase,textAcceptPayment;
    ImageView backImage;

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
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
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

        customer=view.findViewById(R.id.customer);
        purchase=view.findViewById(R.id.purchase);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                customer.setText(nick);
            }
        });
        purchase.setText("+"+schoolyCoin);
        textAcceptPayment=view.findViewById(R.id.textAcceptPayment);
        textAcceptPayment.setText("Оплатить "+dollar);
        backImage=view.findViewById(R.id.back_coins);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CoinsMainFragment.newInstance(), getActivity());
            }
        });
    }
}
