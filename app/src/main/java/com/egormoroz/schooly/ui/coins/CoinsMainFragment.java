package com.egormoroz.schooly.ui.coins;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.CreateCharacter.CharacterAdapter;
import com.egormoroz.schooly.ui.main.CreateCharacter.EyebrowsFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoinsMainFragment extends Fragment {
    private FirebaseModel firebaseModel = new FirebaseModel();
    LinearLayout oneLinear,twoLinear,fiveLinear
            ,sevenLinear,tenLinear,twentyLinear;
    TextView oneS,twoS,fiveS
            ,sevenS,tenS,twentyS;
    RelativeLayout transferMoney;
    TextView oneD,twoD,fiveD
            ,sevenD,tenD,twentyD;
    ImageView oneImage,twoImage,fiveImage
            ,sevenImage,tenImage,twentyImage;

    public static CoinsMainFragment newInstance() {
        return new CoinsMainFragment();
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

        oneLinear=view.findViewById(R.id.oneThousand);
        twoLinear=view.findViewById(R.id.twoThousand);
        fiveLinear=view.findViewById(R.id.fiveThousand);
        sevenLinear=view.findViewById(R.id.sevenThousand);
        tenLinear=view.findViewById(R.id.tenThousand);
        twentyLinear=view.findViewById(R.id.twentyThousand);
        transferMoney=view.findViewById(R.id.transferMoney);
        oneS=view.findViewById(R.id.oneCoinsText);
        twoS=view.findViewById(R.id.twoCoinsText);
        fiveS=view.findViewById(R.id.fiveCoinsText);
        sevenS=view.findViewById(R.id.sevenCoinsText);
        tenS=view.findViewById(R.id.tenCoinsText);
        twentyS=view.findViewById(R.id.twentyCoinsText);
        oneD=view.findViewById(R.id.oneDollarText);
        twoD=view.findViewById(R.id.twoDollarText);
        fiveD=view.findViewById(R.id.fiveDollarText);
        sevenD=view.findViewById(R.id.sevenDollarText);
        tenD=view.findViewById(R.id.tenDollarText);
        twentyD=view.findViewById(R.id.twentyDollarText);
        oneImage=view.findViewById(R.id.oneCoinsImage);
        twoImage=view.findViewById(R.id.twoCoinsImage);
        fiveImage=view.findViewById(R.id.threeCoinsImage);
        sevenImage=view.findViewById(R.id.sevenCoinsImage);
        tenImage=view.findViewById(R.id.tenCoinsImage);
        twentyImage=view.findViewById(R.id.twentyCoinsImage);
        transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(TransferMoneyFragment.newInstance(CoinsMainFragment.newInstance()), getActivity());
            }
        });
        oneLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BuyCoinsFragment
                        .newInstance(oneS.getText().toString(),oneD.getText().toString() ), getActivity());
            }
        });
        twoLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BuyCoinsFragment
                        .newInstance(twoS.getText().toString(),twoD.getText().toString() ), getActivity());
            }
        });
        fiveLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BuyCoinsFragment
                        .newInstance(fiveS.getText().toString(),fiveD.getText().toString() ), getActivity());
            }
        });
        sevenLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BuyCoinsFragment
                        .newInstance(sevenS.getText().toString(),sevenD.getText().toString() ), getActivity());
            }
        });
        tenLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BuyCoinsFragment
                        .newInstance(tenS.getText().toString(),tenD.getText().toString() ), getActivity());
            }
        });
        twentyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BuyCoinsFragment
                        .newInstance(twentyS.getText().toString(),twentyD.getText().toString() ), getActivity());
            }
        });
    }

}
