package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HatsFragment extends Fragment {
  public static ShoesFargment newInstance() {
    return new ShoesFargment();
  }
  FirebaseModel firebaseModel=new FirebaseModel();
  ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
  ArrayList<Clothes> hatsArrayList=new ArrayList<Clothes>();
  RecyclerView clothes;
  NewClothesAdapter.ItemClickListener itemClickListener;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.layoutwiewpagershop, container, false);
    BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
    bnv.setVisibility(bnv.GONE);
    firebaseModel.initAll();
    clothes=root.findViewById(R.id.newchlothesinshop);
    loadClothesFromBase();
    Log.d("######", "dd");
    return root;
  }

  @Override
  public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
    super.onViewCreated(view, savedInstanceState);

    itemClickListener=new NewClothesAdapter.ItemClickListener() {
      @Override
      public void onItemClick(Clothes clothes) {
        ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance());
      }
    };
  }


  public void loadClothesFromBase(){
    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
      @Override
      public void PassUserNick(String nick) {
        RecentMethods.getClothes(firebaseModel, new Callbacks.GetClothes() {
          @Override
          public void getClothes(ArrayList<Clothes> allClothes) {
            clothesArrayList.addAll(allClothes);
            for(int i=0;i<clothesArrayList.size();i++){
              Clothes cl=clothesArrayList.get(i);
              if (cl.getClothesType().equals("hats")){
                hatsArrayList.add(cl);
              }
//                           if (cl.getPurchaseNumber()==1){
//                               firebaseModel.getReference("AppData/Clothes/Popular").setValue()
//                            }
            }
            Log.d("#####", "size  "+clothesArrayList);
            NewClothesAdapter newClothesAdapter=new NewClothesAdapter(hatsArrayList,itemClickListener);
            clothes.setAdapter(newClothesAdapter);
          }
        });
      }
    });
  }
}