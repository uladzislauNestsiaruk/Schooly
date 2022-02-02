package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewingClothesBasket extends Fragment {
  public static ViewingClothesBasket newInstance() {
    return new ViewingClothesBasket();

  }

  NewClothesAdapter.ItemClickListener itemClickListener;
  TextView clothesPriceCV,clothesTitleCV,schoolyCoinCV,buyClothesBottom,inBasket,purchaseNumber;
  ImageView clothesImageCV,backToShop;
  long schoolyCoins,clothesPrise;
  Clothes clothesViewing;
  private FirebaseModel firebaseModel = new FirebaseModel();

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_viewingclothes, container, false);
    BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
    bnv.setVisibility(bnv.GONE);
    firebaseModel.initAll();
    return root;

  }

  @Override
  public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
    super.onViewCreated(view, savedInstanceState);
    getCoins();
    checkClothesOnBuy();
    schoolyCoinCV=view.findViewById(R.id.schoolycoincvfrag);
    clothesImageCV=view.findViewById(R.id.clothesImagecv);
    inBasket=view.findViewById(R.id.inBasketClothes);
    clothesTitleCV=view.findViewById(R.id.clothesTitlecv);
    purchaseNumber=view.findViewById(R.id.purchaseNumberViewing);
    clothesPriceCV=view.findViewById(R.id.clothesPricecv);
    backToShop=view.findViewById(R.id.back_toshop);
    buyClothesBottom=view.findViewById(R.id.buyClothesBottom);
    backToShop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RecentMethods.setCurrentFragment(BasketFragment.newInstance(), getActivity());
      }
    });
    BasketAdapter.singeClothesInfo(new BasketAdapter.ItemClickListener() {
      @Override
      public void onItemClick(Clothes clothes) {
        clothesViewing=clothes;
        clothesPriceCV.setText(String.valueOf(clothes.getClothesPrice()));
        clothesTitleCV.setText(clothes.getClothesTitle());
        purchaseNumber.setText(String.valueOf(clothesViewing.getPurchaseNumber()));
        clothesPrise=clothes.getClothesPrice();
        Picasso.get().load(clothes.getClothesImage()).into(clothesImageCV);
      }
    });
    buyClothes();
    checkClothes();
  }

  public void getCoins(){
    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
      @Override
      public void PassUserNick(String nick) {
        RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
          @Override
          public void GetMoneyFromBase(long money) {
            schoolyCoins=money;
            schoolyCoinCV.setText(String.valueOf(money));
          }
        });
      }
    });
  }

  public void buyClothes(){
    buyClothesBottom.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(schoolyCoins>=clothesPrise){
          RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
              firebaseModel.getUsersReference().child(nick).child("clothes")
                      .child(clothesViewing.getClothesTitle()).setValue(clothesViewing);
              if(clothesViewing.getCreator().equals("Schooly")){

              }else {
                firebaseModel.getReference().child("users")
                        .child(clothesViewing.getCreator()).child("nontifications")
                        .child(nick).setValue(new Nontification(nick,"не отправлено","одежда"
                        , ServerValue.TIMESTAMP.toString(),clothesViewing.getClothesTitle(),clothesViewing.getClothesImage(),"не просмотрено"));
              }
              Query query=firebaseModel.getUsersReference().child(nick).child("basket").
                      child(String.valueOf(clothesViewing.getClothesTitle()));
              query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if(snapshot.exists()){
                    firebaseModel.getUsersReference().child(nick).child("basket")
                            .child(clothesViewing.getClothesTitle()).removeValue();
                  }else{
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
              });
              schoolyCoins=schoolyCoins-clothesPrise;
              firebaseModel.getUsersReference().child(nick).child("money").setValue(schoolyCoins);
            }
          });
        }else{
        }
      }
    });
  }


  public void checkClothes(){
    inBasket.setText("Убрать");
    inBasket.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
          @Override
          public void PassUserNick(String nick) {
            Query query=firebaseModel.getUsersReference().child(nick).child("basket").
                    child(String.valueOf(clothesViewing.getClothesTitle()));
            query.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                  firebaseModel.getUsersReference().child(nick).child("basket")
                          .child(clothesViewing.getClothesTitle()).removeValue();
                  inBasket.setText("Убрано");
                  inBasket.setBackgroundResource(R.drawable.corners14grey);
                }else {
                }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
            });
          }
        });
      }
    });
  }

  public void checkClothesOnBuy(){
    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
      @Override
      public void PassUserNick(String nick) {
        Query query=firebaseModel.getUsersReference().child(nick).child("clothes").
                child(String.valueOf(clothesViewing.getClothesTitle()));
        query.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
              buyClothesBottom.setText("Куплено");
              buyClothesBottom.setBackgroundResource(R.drawable.corners14grey);
              inBasket.setBackgroundResource(R.drawable.corners14grey);
            }else {
              buyClothesBottom.setText("Купить");
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