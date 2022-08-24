package com.egormoroz.schooly.ui.main.CreateCharacter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Color;
import com.egormoroz.schooly.FacePart;
import com.egormoroz.schooly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

public class HairstyleColorFragment extends Fragment {

    RecyclerView recyclerView;
    ColorsAdapter.ItemClickListener itemClickListener;
    public static HairstyleColorFragment newInstance() {
        return new HairstyleColorFragment();
    }
    CircularProgressIndicator progressIndicator;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.viewpagerskincolour, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        progressIndicator=view.findViewById(R.id.progressIndicator);
        recyclerView=view.findViewById(R.id.recyclerSkinColour);
        ArrayList<Color> colorsArrayList=new ArrayList<>();
        Color color=new Color(1f, 1f, 1f,255, 255, 255);
        colorsArrayList.add(color);
        Color color1=new Color(0f, 0f, 0f,0, 0, 0);
        colorsArrayList.add(color1);
        Color color2=new Color(0.255f, 0.232f, 0.120f,255, 232, 120);
        colorsArrayList.add(color2);
        Color color3=new Color(0.255f, 0.228f, 0.094f,255, 228, 94);
        colorsArrayList.add(color3);
        Color color4=new Color(0.252f, 0.137f, 0.48f,252, 137, 48);
        colorsArrayList.add(color4);
        Color colorMilky=new Color(0.230f,0.230f, 0.230f,230, 230, 230);
        colorsArrayList.add(colorMilky);
        Color color5=new Color(0.184f, 0.184f, 0.184f,184, 184, 184);
        colorsArrayList.add(color5);
        Color color6=new Color(0.138f, 0.138f, 0.138f,138, 138, 138);
        colorsArrayList.add(color6);
        Color colorBrownLighter=new Color( 0.84f, 0.36f, 0f,84, 36, 0);
        colorsArrayList.add(colorBrownLighter);
        Color colorBrown=new Color(0.48f, 0.24f, 0.5f,48, 24, 5);
        colorsArrayList.add(colorBrown);
        Color colorBrownBlack=new Color(0.36f, 0.27f, 0.20f,36, 27, 20);
        colorsArrayList.add(colorBrownBlack);
        Color colorRed=new Color(0.194f, 0.23f,0.14f,194, 23, 14);
        colorsArrayList.add(colorRed);
        Color colorLightRed=new Color(0.240f, 0.81f, 0.97f,240, 81, 97);
        colorsArrayList.add(colorLightRed);
        Color colorYellow=new Color(0.194f, 0.182f, 0.14f,194, 182, 14);
        colorsArrayList.add(colorYellow);
        Color colorGreen=new Color(0.86f, 0.194f, 0.14f,86, 194, 14);
        colorsArrayList.add(colorGreen);
        Color colorGreen2=new Color(0.14f, 0.194f, 0.101f,14, 194, 101);
        colorsArrayList.add(colorGreen2);
        Color colorBlue=new Color(0.14f, 0.194f,0.170f,14, 194, 170);
        colorsArrayList.add(colorBlue);
        Color colorBlueMore=new Color(0.14f, 0.101f, 0.194f,14, 101, 194);
        colorsArrayList.add(colorBlueMore);
        Color colorPurple=new Color(0.74f, 0.14f, 0.194f,74, 14, 194);
        colorsArrayList.add(colorPurple);
        Color colorPurpleLight=new Color(0.205f, 0.81f, 0.240f,205, 81, 240);
        colorsArrayList.add(colorPurpleLight);
        Color colorPinkLight=new Color(0.240f, 0.81f, 0.219f,240, 81, 219);
        colorsArrayList.add(colorPinkLight);
        progressIndicator.setVisibility(View.GONE);
        ColorsAdapter colorsAdapter=new ColorsAdapter(colorsArrayList, itemClickListener, "hair");
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(colorsAdapter);

    }
}
