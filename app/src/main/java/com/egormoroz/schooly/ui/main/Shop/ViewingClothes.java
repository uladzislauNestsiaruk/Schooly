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

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.ChatsFragment;
import com.egormoroz.schooly.ui.main.EnterFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ViewingClothes extends Fragment {
    public static com.egormoroz.schooly.ui.main.Shop.ViewingClothes newInstance() {
        return new com.egormoroz.schooly.ui.main.Shop.ViewingClothes();

    }


    TextView clothesPriceCV,clothesTitleCV,schoolyCoinCV,buyClothesBottom,inBasket;
    ImageView clothesImageCV,backToShop;
    long schoolyCoins,clothesPrise;
    Clothes clothesViewing;
    private FirebaseModel firebaseModel = new FirebaseModel();
    NewClothesAdapter.ViewHolder viewHolder;

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
        schoolyCoinCV=view.findViewById(R.id.schoolycoincvfrag);
        clothesImageCV=view.findViewById(R.id.clothesImagecv);
        inBasket=view.findViewById(R.id.inBasketClothes);
        clothesTitleCV=view.findViewById(R.id.clothesTitlecv);
        clothesPriceCV=view.findViewById(R.id.clothesPricecv);
        backToShop=view.findViewById(R.id.back_toshop);
        buyClothesBottom=view.findViewById(R.id.buyClothesBottom);
        backToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ShopFragment.newInstance(), getActivity());
            }
        });

        NewClothesAdapter.singeClothesInfo(new NewClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                clothesViewing=clothes;
                Log.d("#####", "title2def  "+clothes);
                clothesPriceCV.setText(String.valueOf(clothes.getClothesPrice()));
                clothesTitleCV.setText(clothes.getClothesTitle());
                Log.d("#####", "title2  "+clothes.getClothesTitle());
                clothesPrise=clothes.getClothesPrice();
                Picasso.get().load(clothes.getClothesImage()).into(clothesImageCV);
            }
        });
        buyClothes();
        putInBasket();
        checkClothes();
        checkClothesOnBuy();
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
                            Query query=firebaseModel.getUsersReference().child(nick).child("basket").
                                    child(clothesViewing.getClothesTitle());
                            long purchaseNumber=clothesViewing.getPurchaseNumber()+1;
                            firebaseModel.getReference().child("AppData").child("Clothes").child("Popular")
                                    .child(clothesViewing.getClothesTitle()).child("purchaseNumber").setValue(purchaseNumber);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        firebaseModel.getUsersReference().child(nick).child("basket")
                                                .child(clothesViewing.getClothesTitle()).removeValue();
                                    }else{
                                        Log.d("######", "fuck  ");
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
                    Log.d("######", "fuck  ");
                }
            }
        });
    }

    public void putInBasket(){
        inBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("clothes")
                                .child(clothesViewing.getClothesTitle());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Log.d("######", "fuck  ");
                                }else {firebaseModel.getUsersReference().child(nick).child("basket")
                                        .child(clothesViewing.getClothesTitle()).setValue(clothesViewing);}
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

    public void checkClothes(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("basket").
                        child(String.valueOf(clothesViewing.getClothesTitle()));
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            inBasket.setText("В корзине");
                            inBasket.setBackgroundResource(R.drawable.corners14appcolor);
                        }else {
                            inBasket.setText("В корзину");
                            inBasket.setBackgroundResource(R.drawable.corners14appcolor);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
                        child("shoes").child(String.valueOf(clothesViewing.getClothesTitle()));
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