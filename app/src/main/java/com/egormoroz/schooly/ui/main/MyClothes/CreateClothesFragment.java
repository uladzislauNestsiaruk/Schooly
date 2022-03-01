package com.egormoroz.schooly.ui.main.MyClothes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CreateClothesFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    EditText editTextClothes,editClothesPrice,addDescriptionEdit;
    ImageView addModelFile,addModelImage;
    TextView modelWay;
    RelativeLayout publish;
    RadioGroup radioGroup,radioGroupCurrency;
    private String checker = "";
    RadioButton radioButton1,radioButton2,radioButton3,radioButton4
            ,radioButton5,radioButton6,radioButton7,radioButton8
            ,radioButton9,radioButton10,radioButton11
            ,radioButton12,radioButton13,radioButtonCoin,radioButtonDollar;

    public static CreateClothesFragment newInstance() {
        return new CreateClothesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_createclothes, container, false);
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

        editClothesPrice=view.findViewById(R.id.editClothesPrice);
        editTextClothes=view.findViewById(R.id.editTextClothes);
        addDescriptionEdit=view.findViewById(R.id.addDescriptionEdit);
        radioGroupCurrency=view.findViewById(R.id.radioGroupCurrency);
        addModelFile=view.findViewById(R.id.addModelFile);
        addModelImage=view.findViewById(R.id.addModelImage);
        modelWay=view.findViewById(R.id.modelWay);
        publish=view.findViewById(R.id.publish);
        radioGroup=view.findViewById(R.id.radioGroup);
        radioButton1=view.findViewById(R.id.radio_button_1);
        radioButton2=view.findViewById(R.id.radio_button_2);
        radioButton3=view.findViewById(R.id.radio_button_3);
        radioButton4=view.findViewById(R.id.radio_button_4);
        radioButton5=view.findViewById(R.id.radio_button_5);
        radioButton6=view.findViewById(R.id.radio_button_6);
        radioButton7=view.findViewById(R.id.radio_button_7);
        radioButton8=view.findViewById(R.id.radio_button_8);
        radioButton9=view.findViewById(R.id.radio_button_9);
        radioButton10=view.findViewById(R.id.radio_button_10);
        radioButton11=view.findViewById(R.id.radio_button_11);
        radioButton12=view.findViewById(R.id.radio_button_12);
        radioButton13=view.findViewById(R.id.radio_button_13);
        radioButtonCoin=view.findViewById(R.id.schoolyCoinRadio);
        radioButtonDollar=view.findViewById(R.id.dollarRadio);
        addModelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "image";
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 443);
            }
        });
        ImageView backtoMyClothes=view.findViewById(R.id.back_tomyclothes);
        backtoMyClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(MyClothesFragment.newInstance(), getActivity());
                    }
                });
            }
        });
    }

}
