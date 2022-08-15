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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FacePart;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;

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
    long adCount=0;
    RelativeLayout adRelative;

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
        adRelative=view.findViewById(R.id.adRelative);
        adRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseModel.getUsersReference().child(userInformation.getNick()).child("adCount")
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot=task.getResult();
                            if(snapshot.exists()){
                                adCount=snapshot.getValue(Long.class);
                            }
                            if(adCount<5){
                                RecentMethods.setCurrentFragment(AdsFragment.newInstance(CoinsMainFragment.newInstance(userInformation, bundle), userInformation, bundle), getActivity());
                            }else{
                                Toast.makeText(getContext(),R.string.watch5ad , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        FirebaseModel firebaseModel=new FirebaseModel();

        firebaseModel.initAppDataDatabase();
        String uid=firebaseModel.getReference().child("parts").child("hair").push().getKey();
        firebaseModel.getReference().child("parts").child("hair").child(uid)
                .setValue(new FacePart("hair","hairOne","https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2F%D0%BF%D1%80%D0%B8%D1%87%D0%B03.glb?alt=media&token=1fe8e688-7f84-48af-aafd-6a3fefa684fc"
                ,uid,null));
        Float x=-0.01f;
        Float y=0.21f;
        Float z=-0.17f;
        Float transformRatio=9.9f;
        Float x1=0f;
        Float y1=3.7f;
        Float z1=-0.4499f;
        Float transformRatio1=4.5f;
        String uid8=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid7=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid9=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid10=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid8)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fgray%20skirt.png?alt=media&token=cfad7fc3-05ec-41d7-a9a8-c6563aded022"
//                        , 40, "gray skirt", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fgray%20skirt.glb?alt=media&token=95d40256-1327-4c7d-8273-f1741dd7b75e",
//                        0, "leg", uid8, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid9)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fpink%20skirt.png?alt=media&token=d5af23d9-f5bd-4342-b813-25d607da3cb5"
//                        , 40, "pink skirt", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fpink%20skirt.glb?alt=media&token=f0ab1901-f02b-4a7a-ab06-07e1683c49da",
//                        0, "leg", uid9, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid7)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fblue%20skirt.png?alt=media&token=3590631d-8ca6-44b0-b66b-b09b5085ec5a"
//                        , 40, "blue skirt", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fblue%20skirt.glb?alt=media&token=5384cbbd-30aa-4698-badd-e55c7b3dc575",
//                        0, "leg", uid7, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid10)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fdark%20red%20long%20skini%20skirt.png?alt=media&token=f1081211-c1df-423d-a301-de2919852b3b"
//                        , 40, "dark red long skini skirt", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fdark%20red%20long%20skini%20skirt.glb?alt=media&token=54775e5d-8394-420f-8133-c0599f7cf428",
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
