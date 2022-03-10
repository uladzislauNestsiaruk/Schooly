package com.egormoroz.schooly.ui.main.Nontifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.FittingFragment;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.sceneform.SceneView;

public class ClothesRequestFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView back;
    TextView clothesTitleCV,clothesPrice,clothesType,result,reason,addReason,reasonText,addReasonText;
    SceneView sceneView;
    ImageView clothesImageCV, coinsImage;

    Fragment fragment;

    public ClothesRequestFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public static ClothesRequestFragment newInstance(Fragment fragment) {
        return new ClothesRequestFragment(fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_clothesrequest, container, false);
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

        sceneView=view.findViewById(R.id.sceneView);
        clothesType=view.findViewById(R.id.clothesType);
        reason=view.findViewById(R.id.reason);
        result=view.findViewById(R.id.result);
        addReason=view.findViewById(R.id.add);
        reasonText=view.findViewById(R.id.reasonText);
        addReasonText=view.findViewById(R.id.addText);
        clothesImageCV = view.findViewById(R.id.clothesImagecv);
        coinsImage = view.findViewById(R.id.coinsImage);
        clothesTitleCV = view.findViewById(R.id.clothesTitlecv);
        clothesPrice=view.findViewById(R.id.clothesPricecv);
        back=view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });
    }
}
