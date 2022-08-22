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
    RelativeLayout adRelative,allPremium;

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

        firebaseModel.initAll();
//        String uid=firebaseModel.getReference().child("parts").child("hair").push().getKey();
//        firebaseModel.getUsersReference().child("kloun").child("person").child("body")
//                .setValue(new FacePart("body","dody","https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2F%D0%BD%D1%83%20%D1%82%D0%BE%D1%87%D0%BD%D0%BE%20%D0%BB%D0%B0%D1%81%D1%82%D0%BE%D0%B2%D1%8B%D0%B9.glb?alt=media&token=70c0adb9-e73e-43ea-ab8c-f29ba62de911"
//                ,uid,null));
        Float x=-0.01f;
        Float y=0.21f;
        Float z=-0.17f;
        Float transformRatio=9.9f;
        Float x1=0.1f;
        Float y1=3.6599f;
        Float z1=-0.345f;
        Float transformRatio1=7.5409f;
        String uid8=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid7=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid9=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid10=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid8)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fgreen%20turtleneck.png?alt=media&token=a64b2533-f1c4-41b8-a0cc-442e4d1ea6b3"
                        , 60, "green turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fgreen%20turtleneck.glb?alt=media&token=50c3525d-ef93-4e45-8818-9da4014e5776",
                        0, "body", uid8, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid9)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Flight%20blue%20turtleneck.png?alt=media&token=9fe707da-776e-4c0b-b0bd-b38c1cf60a54"
                        , 60, "light blue turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Flight%20blue%20turtleneck.glb?alt=media&token=839ff8b9-5d55-4411-8b3f-b53f7941e7a3",
                        0, "body", uid9, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid7)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Forange%20turtleneck.glb?alt=media&token=4519c8f2-9e25-4abe-b267-13c9acc97d4f"
                        , 60, "orange turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Forange%20turtleneck.glb?alt=media&token=4519c8f2-9e25-4abe-b267-13c9acc97d4f",
                        0, "body", uid7, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid10)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fpeach%20turtleneck.png?alt=media&token=4c3b29e9-39f1-46f0-8126-ba368b298b05"
                        , 60, "peach turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fpeach%20turtleneck.glb?alt=media&token=f9a25d17-8342-49bd-a149-d013c959b902",
                        0, "body", uid10, "no",buffer , x1,y1,z1,transformRatio1));
        String uid81=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid71=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid91=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid101=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid81)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fpurple%20turtleneck.png?alt=media&token=8de83f3f-1056-4ced-bc28-9ddef11ea2ea"
                        , 60, "purple turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fpurple%20turtleneck.glb?alt=media&token=d1c6c9c5-3480-43e0-b5d1-970c007d648e",
                        0, "body", uid81, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid91)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fsoft%20pink%20turtleneck.png?alt=media&token=17d21ca2-449f-4264-8bd0-917177782184"
                        , 60, "soft pimk turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fsoft%20pink%20turtleneck.glb?alt=media&token=cc0ef384-7128-46e3-8fa4-f57f66ac171f",
                        0, "body", uid91, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid71)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fyellow%20turtleneck.png?alt=media&token=a0d941e8-1330-4230-9dba-9672d4f16c35"
                        , 60, "yellow turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fyellow%20turtleneck.glb?alt=media&token=b52a431a-d34d-4cbe-9ed8-98fc07ea6f5f",
                        0, "body", uid71, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid101)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fbeige%20turtleneck.png?alt=media&token=6680caca-0665-4157-9159-198566bd75fe"
                        , 60, " beige turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fbeige%20turtleneck.glb?alt=media&token=2bd88b99-9fee-4810-a8eb-d0c21cb9f9a7",
                        0, "body", uid101, "no",buffer , x1,y1,z1,transformRatio1));
///
        String uid82=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid72=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid92=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid102=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid82)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fbrown%20turtleneck.png?alt=media&token=d087447e-e9c5-4d7d-b543-ecdd549c1573"
                        , 60, "brown turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fbrown%20turtleneck.glb?alt=media&token=4976a846-e9e8-4197-ba02-38c2f9beaa3a",
                        0, "body", uid82, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid92)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fburgundy%20turtleneck.png?alt=media&token=98b5a304-f9cc-43b7-94ab-b63fa0dc26ee"
                        , 60, "burgundy turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fburgundy%20turtleneck.glb?alt=media&token=093e1672-e27f-4f7b-bbb4-543004ce1558",
                        0, "body", uid92, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid72)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fdark%20blue%20turtleneck.png?alt=media&token=e46ef404-c0eb-4f9f-8e16-124eeb015da3"
                        , 60, "dark blue turtleneck", 0, 0, "Sholly", "coin", "", "https:firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fdark%20blue%20turtleneck.glb?alt=media&token=2c2eaa00-eeea-4e3c-8396-d146f5d877e4",
        0, "body", uid72, "no",buffer , x1,y1,z1,transformRatio1));
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid102)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fdark%20green%20turtleneck.png?alt=media&token=e9d48432-57a0-4c60-9452-7b4f49c6ec40"
                        , 60, "dark green turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fdark%20green%20turtleneck.glb?alt=media&token=ae3f159b-c839-4570-a209-02f53a8b3be5",
                        0, "body", uid102, "no",buffer , x1,y1,z1,transformRatio1));
        String uid83=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid73=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid93=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        String uid103=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid83)
                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fgray%20turtleneck.png?alt=media&token=e62f2621-f46e-4e44-ae49-b62c9a6a5d7b"
                        , 60, "gray turtleneck", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fgray%20turtleneck.glb?alt=media&token=312aa2ea-b22c-4c24-b059-e6eb5aaa21f6",
                        0, "body", uid83, "no",buffer , x1,y1,z1,transformRatio1));




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
        allPremium=view.findViewById(R.id.allPremium);
        transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(TransferMoneyFragment.newInstance(CoinsMainFragment.newInstance(userInformation,bundle),userInformation,bundle), getActivity());
            }
        });
        allPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        oneLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        twoLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        fiveLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        sevenLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        tenLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        twentyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_layout_sell_clothes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout sellRelative=dialog.findViewById(R.id.sellRelative);


        sellRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
