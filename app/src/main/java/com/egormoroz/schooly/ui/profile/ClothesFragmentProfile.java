package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.GenderFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClothesFragmentProfile extends Fragment {

    RecyclerView looksRecycler;

    public static ClothesFragmentProfile newInstance() {
        return new ClothesFragmentProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.viewpager_profile, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


    }
}
