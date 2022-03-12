package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.profile.Look;
import com.egormoroz.schooly.ui.profile.LooksAdapter;
import com.egormoroz.schooly.ui.profile.LooksFragmentProfileOther;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileReader;
import java.util.ArrayList;

public class AcceptNewLook extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    RelativeLayout publish;
    TextView lookPrice;
    EditText descriptionLook;

    String model;

    public AcceptNewLook(String model) {
        this.model = model;
    }

    public static AcceptNewLook newInstance(String model) {
        return new AcceptNewLook(model);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_acceptnewlook, container, false);
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

        publish=view.findViewById(R.id.publish);
        lookPrice=view.findViewById(R.id.lookPrice);
        descriptionLook=view.findViewById(R.id.addDescriptionEdit);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        String lookId=firebaseModel.getUsersReference().child(nick).child("looks").push().getKey();
                        firebaseModel.getUsersReference().child(nick).child("looks").child(lookId)
                                .setValue(new NewsItem(model, descriptionLook.getText().toString(), "0", lookId,
                                        "", "", 1200, 0,"",nick));
                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("user", nick, AcceptNewLook.newInstance("")), getActivity());
                    }
                });
            }
        });

        ImageView backfromwardrobe=view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(), getActivity());
                    }
                });
            }
        });
    }

}
