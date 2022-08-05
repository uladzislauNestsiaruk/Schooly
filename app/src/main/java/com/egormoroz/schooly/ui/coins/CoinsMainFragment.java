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
        Float transformRatio=9.9f;
        Float x1=-0.05f;
        Float y1=1.37f;
        Float z1=-0.27f;
        Float transformRatio1=9.9f;

        String uid8=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid7=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid9=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid10=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid8)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fgrount%20short.png?alt=media&token=200478d6-56a7-462c-a296-f880f2af84c4"
//                , 60, "ground shorts", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fground%20shorts.glb?alt=media&token=e2ed65b6-5c3a-4da5-8858-d36740c5ba87",
//                        0, "leg", uid8, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid9)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fdill%20green%20shorts.png?alt=media&token=c0653015-fe13-43a3-a3d9-fa93dbdecc83"
//                        , 50, "dill green shorts", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fdill%20green%20shorts.glb?alt=media&token=d5a792b3-d019-4bd3-a70c-bfb0acfad4f0",
//                        0, "leg", uid9, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid7)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Facid%20%20shorts.png?alt=media&token=0b339562-e6c2-4a5b-bcf6-c1046a27aeaa"
//                        , 70, "acid shorts", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fmilky%20shorts.glb?alt=media&token=7f98bfcb-f1e7-48c0-97ac-202d1f70d753",
//                        0, "leg", uid7, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid10)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fblue%20short.png?alt=media&token=6648798f-1561-4335-af3f-cfece14425cb"
//                        , 50, "blue shorts", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fblue%20shorts.glb?alt=media&token=da94571c-9174-46ac-a10a-2ee59a207feb",
//                        0, "leg", uid10, "no",buffer , x1,y1,z1,transformRatio1));


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
