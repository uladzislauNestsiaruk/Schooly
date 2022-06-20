package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.User;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.MyClothes.CreateClothesFragment;
import com.egormoroz.schooly.ui.main.MyClothes.MyClothesAdapter;
import com.egormoroz.schooly.ui.main.MyClothes.MyClothesFragment;
import com.egormoroz.schooly.ui.main.MyClothes.ViewingMyClothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.filament.Engine;
import com.google.android.filament.Filament;
import com.google.android.filament.Renderer;
import com.google.android.filament.Scene;
import com.google.android.filament.SwapChain;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FittingFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView back;
    UserInformation userInformation;
    Bundle bundle;
    Fragment fragment;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    public FittingFragment(Fragment fragment,UserInformation userInformation,Bundle bundle) {
        this.fragment = fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static FittingFragment newInstance(Fragment fragment, UserInformation userInformation,Bundle bundle) {
        return new FittingFragment(fragment,userInformation,bundle);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fitting, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        back=view.findViewById(R.id.backToViewingClothes);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        surfaceView=view.findViewById(R.id.surfaceView);
        Filament.init();
        Engine engine=Engine.create();
        surfaceHolder=surfaceView.getHolder();
        Surface surface=surfaceHolder.getSurface();
        SwapChain swapChain= engine.createSwapChain(surface);
        Renderer renderer=engine.createRenderer();
        Scene scene=engine.createScene();
        com.google.android.filament.View view1=engine.createView();
        view1.setScene(scene);
        if (renderer.beginFrame(swapChain)){}{
            renderer.render(view1);
            renderer.endFrame();
        }
    }
}
