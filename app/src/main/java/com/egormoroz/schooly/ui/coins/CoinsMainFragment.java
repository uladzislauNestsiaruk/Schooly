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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.CreateCharacter.CharacterAdapter;
import com.egormoroz.schooly.ui.main.CreateCharacter.EyebrowsFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.Buffer;

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
    String nick;
    UserInformation userInformation;
    Bundle bundle;
    Buffer buffer;

    public CoinsMainFragment(UserInformation userInformation,Bundle bundle) {
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static CoinsMainFragment newInstance(UserInformation userInformation,Bundle bundle) {
        return new CoinsMainFragment(userInformation,bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coins, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        nick=userInformation.getNick();
        OnBackPressedCallback callback1 = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(MainFragment.newInstance(userInformation, bundle), getActivity());
            }
        };

        Float x=-0.01f;
        Float y=0.21f;
        Float z=-0.17f;
        Float transformRatio=5.65f;
        Float x1=-0.01f;
        Float y1=0.21f;
        Float z1=-0.12f;
        Float transformRatio1=5.55f;

        String uid8=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid7=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid9=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid8)
//                .setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fu%D0%90%D0%90tit%D0%90%D0%90led.png?alt=media&token=8a15061c-e753-4797-8cd3-81e660c97da8"
//                , 150, "Grossfan", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fgrossfan.glb?alt=media&token=32b887de-e60a-4f80-84ab-421690f9babf",
//                        0, "foot", uid8, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid7)
//                .setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Funtitled%20(3).png?alt=media&token=5514bcf2-c168-4501-a3fa-d46b9c66ba13"
//                        , 110, "Pinkey", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2FPinkey2.glb?alt=media&token=7e82e8bd-f989-483d-ab31-0b1a6bfcf022",
//                        0, "foot", uid7, "no",buffer , x,y,z,transformRatio));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid9)
//                .setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fhgf.png?alt=media&token=6b772427-3561-4fa3-8560-efb593ec196f"
//                        , 110, "Rawler's", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2FRawler's2.glb?alt=media&token=e359d230-1291-4a6f-bd59-74a8e1bc5212",
//                        0, "foot", uid9, "no",buffer , x,y,z,transformRatio));
//        firebaseModel.getUsersReference().child("tyomaa6").child("myClothes").child(uid8)
//                .setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Funtitled%20(7).png?alt=media&token=3b195cab-df73-4186-925f-88d382270c5b"
//                        , 270, "Plurixx", 0, 0, "tyomaa6", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fplurixx.glb?alt=media&token=9f879dc8-3b92-41a6-bf28-2c1095ca5865",
//                        0, "foot", uid8, "no",buffer , x,y,z,transformRatio));


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback1);
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
                RecentMethods.setCurrentFragment(TransferMoneyFragment.newInstance(CoinsMainFragment.newInstance(userInformation,bundle),userInformation,bundle), getActivity());
            }
        });
//        oneLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecentMethods.setCurrentFragment(BuyCoinsFragment
//                        .newInstance(oneS.getText().toString(),oneD.getText().toString() ), getActivity());
//            }
//        });
//        twoLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecentMethods.setCurrentFragment(BuyCoinsFragment
//                        .newInstance(twoS.getText().toString(),twoD.getText().toString() ), getActivity());
//            }
//        });
//        fiveLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecentMethods.setCurrentFragment(BuyCoinsFragment
//                        .newInstance(fiveS.getText().toString(),fiveD.getText().toString() ), getActivity());
//            }
//        });
//        sevenLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecentMethods.setCurrentFragment(BuyCoinsFragment
//                        .newInstance(sevenS.getText().toString(),sevenD.getText().toString() ), getActivity());
//            }
//        });
//        tenLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecentMethods.setCurrentFragment(BuyCoinsFragment
//                        .newInstance(tenS.getText().toString(),tenD.getText().toString() ), getActivity());
//            }
//        });
//        twentyLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecentMethods.setCurrentFragment(BuyCoinsFragment
//                        .newInstance(twentyS.getText().toString(),twentyD.getText().toString() ), getActivity());
//            }
//        });
    }

}
