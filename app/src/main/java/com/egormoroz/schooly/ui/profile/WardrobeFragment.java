package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class WardrobeFragment extends Fragment {
    public static WardrobeFragment newInstance() {
        return new WardrobeFragment();
    }
    RecyclerView wardrobeRecyclerView;
    ArrayList<Clothes> clothesArrayListWardrobe=new ArrayList<Clothes>();
    FirebaseModel firebaseModel=new FirebaseModel();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View root =inflater.inflate(R.layout.fragment_wardrobe,container,false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        wardrobeRecyclerView=view.findViewById(R.id.recyclerWardrode);

        ImageView backfromwardrobe=view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(ProfileFragment.newInstance("user",
                        new UserInformation()));
            }
        });

        loadClothesInWardrobe();
    }

    public void loadClothesInWardrobe(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getClothesInWardrobe(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        clothesArrayListWardrobe.addAll(allClothes);
                        WardrobeClothesAdapter newClothesAdapter=new WardrobeClothesAdapter(clothesArrayListWardrobe);
                        wardrobeRecyclerView.setAdapter(newClothesAdapter);
                        Log.d("#####", "ggvppp  "+clothesArrayListWardrobe);
                    }
                });
            }
        });
    }
}
