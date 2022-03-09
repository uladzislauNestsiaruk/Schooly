package com.egormoroz.schooly.ui.main.MyClothes;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.profile.Wardrobe.AcceptNewLook;
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyClothesFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerMyClothes;
    RelativeLayout createAndGet,getMoney,createClothesBig,relativeFirstClothes,createClothes;
    TextView totalProfitText,totalProfit;
    MyClothesAdapter.ItemClickListener itemClickListener;
    long totalProfitLong;
    String totalProfitString;


    int clothesListSize;

    public MyClothesFragment(int clothesListSize) {
        this.clothesListSize = clothesListSize;
    }

    public static MyClothesFragment newInstance(int clothesListSize) {
        return new MyClothesFragment(clothesListSize);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myclothes, container, false);
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

        recyclerMyClothes=view.findViewById(R.id.recyclerMyClothes);
        createClothesBig=view.findViewById(R.id.createBigButtonRecycler);
        createClothes=view.findViewById(R.id.createClothesButton);
        totalProfitText=view.findViewById(R.id.totalProfitText);
        totalProfit=view.findViewById(R.id.totalProfit);
        relativeFirstClothes=view.findViewById(R.id.relativeFirstClothes);
        itemClickListener=new MyClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingMyClothes.newInstance(MyClothesFragment.newInstance(clothesListSize)), getActivity());
            }
        };
        ImageView backtomain=view.findViewById(R.id.back_tomain);
        backtomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
                    }
                });
            }
        });
        getMyClothes();
    }

    public void getMyClothes(){
        if(clothesListSize==0){
            recyclerMyClothes.setVisibility(View.GONE);
            totalProfitText.setVisibility(View.GONE);
            totalProfit.setVisibility(View.GONE);
            createClothesBig.setVisibility(View.VISIBLE);
            relativeFirstClothes.setVisibility(View.VISIBLE);
            createClothes.setVisibility(View.GONE);
            createClothesBig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentMethods.setCurrentFragment(CreateClothesFragment.newInstance(MyClothesFragment.newInstance(clothesListSize)), getActivity());
                }
            });
        }else {
            recyclerMyClothes.setVisibility(View.VISIBLE);
            createClothesBig.setVisibility(View.GONE);
            relativeFirstClothes.setVisibility(View.GONE);
            createClothes.setVisibility(View.VISIBLE);
            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                @Override
                public void PassUserNick(String nick) {
                    RecentMethods.getMyClothes(nick, firebaseModel, new Callbacks.GetClothes() {
                        @Override
                        public void getClothes(ArrayList<Clothes> allClothes) {
                            MyClothesAdapter myClothesAdapter=new MyClothesAdapter(allClothes,itemClickListener);
                            recyclerMyClothes.setAdapter(myClothesAdapter);
                            Query query=firebaseModel.getUsersReference().child(nick)
                                    .child("myClothes");
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                                    for (DataSnapshot snap : snapshot.getChildren()) {
                                        Clothes clothes = new Clothes();
                                        clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                                        clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                                        totalProfitLong+=clothes.getPurchaseToday()*clothes.getClothesPrice();
                                    }
                                    totalProfitString=String.valueOf(totalProfitLong);
                                    if(totalProfitLong<1000){
                                        totalProfit.setText("+"+String.valueOf(totalProfitLong));
                                    }else if(totalProfitLong>1000 && totalProfitLong<10000){
                                        totalProfit.setText("+"+totalProfitString.substring(0, 1)+"."+totalProfitString.substring(1, 2)+"K");
                                    }
                                    else if(totalProfitLong>10000 && totalProfitLong<100000){
                                        totalProfit.setText("+"+totalProfitString.substring(0, 2)+"."+totalProfitString.substring(2,3)+"K");
                                    }
                                    else if(totalProfitLong>10000 && totalProfitLong<100000){
                                        totalProfit.setText("+"+totalProfitString.substring(0, 2)+"."+totalProfitString.substring(2,3)+"K");
                                    }else if(totalProfitLong>100000 && totalProfitLong<1000000){
                                        totalProfit.setText("+"+totalProfitString.substring(0, 3)+"K");
                                    }
                                    else if(totalProfitLong>1000000 && totalProfitLong<10000000){
                                        totalProfit.setText("+"+totalProfitString.substring(0, 1)+"KK");
                                    }
                                    else if(totalProfitLong>10000000 && totalProfitLong<100000000){
                                        totalProfit.setText("+"+totalProfitString.substring(0, 2)+"KK");
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
            createClothes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentMethods.setCurrentFragment(CreateClothesFragment.newInstance(MyClothesFragment.newInstance(clothesListSize)), getActivity());
                }
            });
        }
    }
}
