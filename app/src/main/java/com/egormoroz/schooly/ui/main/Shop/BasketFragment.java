package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.egormoroz.schooly.ui.coins.CoinsFragmentSecond;
import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BasketFragment extends Fragment {
  public static BasketFragment newInstance() {
    return new BasketFragment();
  }

  FirebaseModel firebaseModel=new FirebaseModel();
  TextView numberOfClothes,schoolyCoin,notFound;
  ImageView backtoshop;
  RecyclerView basketRecycler;
  ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
  static BasketAdapter.ItemClickListener onItemClick;
  static String editGetText;
  LinearLayout coinsLinear;
  EditText editText;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_basket, container, false);
    BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
    bnv.setVisibility(bnv.GONE);
    firebaseModel.initAll();
    return root;
  }

  @Override
  public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
    super.onViewCreated(view, savedInstanceState);
    backtoshop=view.findViewById(R.id.back_toshopfrombasket);
    editText=view.findViewById(R.id.searchClothes);
    notFound=view.findViewById(R.id.notFound);
    backtoshop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RecentMethods.setCurrentFragment(ShopFragment.newInstance(), getActivity());
      }
    });
    schoolyCoin=view.findViewById(R.id.schoolycoinbasketfrag);
    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
      @Override
      public void PassUserNick(String nick) {
        RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
          @Override
          public void GetMoneyFromBase(long money) {
            schoolyCoin.setText(String.valueOf(money));
          }
        });
      }
    });
    coinsLinear=view.findViewById(R.id.linearCoins);
    coinsLinear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RecentMethods.setCurrentFragment(CoinsFragmentSecond.newInstance(BasketFragment.newInstance()), getActivity());
      }
    });
    numberOfClothes=view.findViewById(R.id.numberofclothes);
    basketRecycler=view.findViewById(R.id.basketrecycler);
    loadClothesFromBasket();
    onItemClick=new BasketAdapter.ItemClickListener() {
      @Override
      public void onItemClick(Clothes clothes) {
        ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesBasket.newInstance());
      }
    };
    onItemClick=new BasketAdapter.ItemClickListener() {
      @Override
      public void onItemClick(Clothes clothes) {
        ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesBasket.newInstance());
      }
    };
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        editGetText=editText.getText().toString();
        editGetText=editGetText.toLowerCase();
        if (editGetText.length()>0) {
          loadSearchClothes(editGetText);
        }else {
          notFound.setVisibility(View.GONE);
          basketRecycler.setVisibility(View.VISIBLE);
          BasketAdapter basketAdapter=new BasketAdapter(clothesArrayList,onItemClick);
          basketRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
          basketRecycler.setAdapter(basketAdapter);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

  }

  public void loadClothesFromBasket(){
    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
      @Override
      public void PassUserNick(String nick) {
        RecentMethods.getClothesInBasket(nick, firebaseModel, new Callbacks.GetClothes() {
          @Override
          public void getClothes(ArrayList<Clothes> allClothes) {
            clothesArrayList.addAll(allClothes);
            numberOfClothes.setText("Элементов в корзине:"+String.valueOf(clothesArrayList.size()));
            if(clothesArrayList.size()==0){
              notFound.setVisibility(View.VISIBLE);
              basketRecycler.setVisibility(View.GONE);
            }else {
              notFound.setVisibility(View.GONE);
              basketRecycler.setVisibility(View.VISIBLE);
              BasketAdapter basketAdapter=new BasketAdapter(clothesArrayList,onItemClick);
              basketRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
              basketRecycler.setAdapter(basketAdapter);
            }
          }
        });
      }
    });
  }

  public void loadSearchClothes(String editTextText){
    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
      @Override
      public void PassUserNick(String nick) {
        Query query=firebaseModel.getUsersReference().child(nick).child("basket");
        query.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            ArrayList<Clothes> clothesFromBase=new ArrayList<>();
            for (DataSnapshot snap : snapshot.getChildren()) {
              Clothes clothes = new Clothes();
              clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
              clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
              clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
              clothes.setClothesType(snap.child("clothesType").getValue(String.class));
              clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
              clothes.setCreator(snap.child("creator").getValue(String.class));
              clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
              clothes.setDescription(snap.child("description").getValue(String.class));
              clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
              clothes.setModel(snap.child("model").getValue(String.class));
              clothes.setBodyType(snap.child("bodyType").getValue(String.class));
              clothes.setUid(snap.child("uid").getValue(String.class));
              clothes.setExclusive(snap.child("exclusive").getValue(String.class));
              String clothesTitle=clothes.getClothesTitle();
              String title=clothesTitle;
              int valueLetters=editTextText.length();
              title=title.toLowerCase();
              if(title.length()<valueLetters){
                if(title.equals(editTextText))
                  clothesFromBase.add(clothes);
              }else{
                title=title.substring(0, valueLetters);
                if(title.equals(editTextText))
                  clothesFromBase.add(clothes);
              }
            }
            if (clothesFromBase.size()==0){
              basketRecycler.setVisibility(View.GONE);
              notFound.setVisibility(View.VISIBLE);
            }else {
              basketRecycler.setVisibility(View.VISIBLE);
              BasketAdapter basketAdapter=new BasketAdapter(clothesFromBase,onItemClick);
              basketRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
              basketRecycler.setAdapter(basketAdapter);
              notFound.setVisibility(View.GONE);
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
        });
      }
    });
  }

}