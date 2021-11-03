package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PopularFragment extends Fragment {
    public static PopularFragment newInstance() {
        return new PopularFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> popularClothesArrayList=new ArrayList<Clothes>();
    RecyclerView clothes,popularClothes;
    NewClothesAdapter.ItemClickListener itemClickListener;
    PopularClothesAdapter.ItemClickListener itemClickListenerPopular;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layoutwiewpagershop, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        clothes=root.findViewById(R.id.newchlothesinshop);
        popularClothes=root.findViewById(R.id.popularchlothesinshop);
        loadClothesFromBase();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        itemClickListener=new NewClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes, int position) {
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance());
            }
        };
    }


    public void loadClothesFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getNewClothes(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        clothesArrayList.addAll(allClothes);
                        NewClothesAdapter newClothesAdapter=new NewClothesAdapter(clothesArrayList,itemClickListener);
                        clothes.setAdapter(newClothesAdapter);
                        Log.d("#####", "ggvppp  "+clothesArrayList);
                    }
                });
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getPopular(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        popularClothesArrayList.addAll(allClothes);
                        PopularClothesAdapter newClothesAdapter=new PopularClothesAdapter(clothesArrayList,itemClickListenerPopular);
                        popularClothes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        popularClothes.setAdapter(newClothesAdapter);
                        Log.d("#####", "ggvppp  "+clothesArrayList);
                    }
                });
            }
        });
    }
}
