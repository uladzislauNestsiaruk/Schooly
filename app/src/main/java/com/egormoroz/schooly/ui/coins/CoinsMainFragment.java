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

//        Float x=-0.01f;
//        Float y=0.21f;
//        Float z=-0.17f;
//        Float transformRatio=9.9f;
//        Float x1=0.08f;
//        Float y1=3.47f;
//        Float z1=-0.375f;
//        Float transformRatio1=8.1079f;
//        String uid8=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid7=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid9=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid10=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid8)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fred%20hoodie.png?alt=media&token=82863fd4-e2c7-472b-a550-45feac5149fb"
//                        , 100, "red hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fred%20hoodie.glb?alt=media&token=40a7f4c3-4b30-4319-b4c0-c496bbe6c785",
//                        0, "body", uid8, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid9)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fblue%20hoodie.png?alt=media&token=a93bbeb8-50f6-4597-9a5d-c91c7099ec20"
//                        , 100, "blue hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fblue%20hoodie.glb?alt=media&token=ffec4fc6-07d4-4687-92c3-43bdc18e867d",
//                        0, "body", uid9, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid7)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fyellow%20hoodie.png?alt=media&token=92be9dd5-5c6e-43a4-bd9d-cf65b4d1fb56"
//                        , 100, "yellow hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fyellow%20hoodie.glb?alt=media&token=d6d35af0-80a8-43b4-9f57-92eb5a2a4426",
//                        0, "body", uid7, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid10)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fgreen%20hoodie.png?alt=media&token=7e6dce41-23af-493d-bfbe-921cf1f166f7"
//                        , 100, "green hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fgreen%20hoodie.glb?alt=media&token=93e69a7a-5943-43b9-a1d6-c6b6b7ddbb1b",
//                        0, "body", uid10, "no",buffer , x1,y1,z1,transformRatio1));
/////
//        String uid81=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid71=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid91=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid101=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid81)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fblack%20hoodie.png?alt=media&token=d6281309-476a-49a3-b1eb-58e1a6b476a9"
//                        , 100, "black hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fblack%20hoodie.glb?alt=media&token=a3254bd5-db0c-4e40-b010-65ca9b19d31b",
//                        0, "body", uid81, "no",buffer ,
//        x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid91)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fbrown%20hoodie.png?alt=media&token=1c7293bb-1991-4f12-8b74-b387e3a263f1"
//                        , 100, "brown hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fbrown%20hoodie.glb?alt=media&token=ec7ea09c-ce23-44cd-a3d3-4c1a07ea078b",
//                        0, "body", uid91, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid71)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fwhite%20hoodie.png?alt=media&token=998c39c2-eabb-47c2-84a7-e8875fe3c686"
//                        , 100, "white hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fwhite%20hoodie.glb?alt=media&token=7a9d08f1-3de7-4902-a693-8d517066b90c",
//                        0, "body", uid71, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid101)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Flight%20blue%20hoodie.png?alt=media&token=2e3cf43d-6210-4b45-a568-d10766e86d30"
//                        , 100, "blue hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Flight%20blue%20hoodie.glb?alt=media&token=18ac89ac-160d-4326-ad6f-2e871ee2b48f",
//                        0, "body", uid101, "no",buffer , x1,y1,z1,transformRatio1));
/////
//        String uid82=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid72=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid92=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid102=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid82)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fburgundy%20hoodie.png?alt=media&token=5228b34e-708d-4a58-b45b-e661495aa56d"
//                        , 100, "burgundy hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fburgundy%20hoodie.glb?alt=media&token=5237b62d-6988-40dd-b4bb-7cd66062d75b",
//                        0, "body", uid82, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid92)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fmarsh%20hoodie.png?alt=media&token=b06e4b49-f93a-4809-981d-b6e8cf340558"
//                        , 100, "marsh hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fmarsh%20hoodie.glb?alt=media&token=76339712-afec-4c64-ac6e-70503d0c467e",
//                        0, "body", uid92, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid72)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Forange%20hoodie.png?alt=media&token=cec85c06-7a67-43e4-a55f-bd439dfb5038"
//                        , 100, "orange hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Forange%20hoodie.glb?alt=media&token=39c5023f-6dc6-4b8e-9dc8-bfdd6dc92549",
//                        0, "body", uid72, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child
//        ("AllClothes").child(uid102)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fpink%20hoodie%20(1).png?alt=media&token=5e294290-b2a4-4e15-b37f-2f618440e587"
//                        , 100, "pink hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fpink%20hoodie.glb?alt=media&token=d6c43bfe-6237-45be-994f-2744c1dec26a",
//                        0, "body", uid102, "no",buffer , x1,y1,z1,transformRatio1));
/////
//        String uid83=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid73=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid93=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        String uid103=firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").push().getKey();
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid83)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fpurple%20hoodie.png?alt=media&token=9b450b89-d5fd-48b1-98df-c099bc213869"
//                        , 100, "purple hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fpurple%20hoodie.glb?alt=media&token=5f87cfca-94e9-4853-955e-f7c7cf05ddf4",
//                        0, "body", uid83, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid93)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fgray%20hoodie.png?alt=media&token=7fd93dd3-0846-49d1-b240-1edde8a9e37e"
//                        , 100, "gray hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fgray%20hoodie.glb?alt=media&token=82c43943-3112-426d-8cd6-13016fa370a2",
//                        0, "body", uid93, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid73)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fwhite%20hoodie.png?alt=media&token=998c39c2-eabb-47c2-84a7-e8875fe3c686"
//                        , 100, "white hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fwhite%20hoodie.glb?alt=media&token=7a9d08f1-3de7-4902-a693-8d517066b90c",
//                        0, "body", uid73, "no",buffer , x1,y1,z1,transformRatio1));
//        firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child(uid103)
//                .setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Flight%20blue%20hoodie.png?alt=media&token=2e3cf43d-6210-4b45-a568-d10766e86d30"
//                        , 100, "green hoodie", 0, 0, "Sholly", "coin", "", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Flight%20blue%20hoodie.glb?alt=media&token=18ac89ac-160d-4326-ad6f-2e871ee2b48f",
//                        0, "body", uid103, "no",buffer , x1,y1,z1,transformRatio1));



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
