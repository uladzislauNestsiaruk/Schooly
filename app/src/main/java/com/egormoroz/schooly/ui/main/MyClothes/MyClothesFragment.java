package com.egormoroz.schooly.ui.main.MyClothes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
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
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.PopularClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothesPopular;
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
    TextView totalProfitText,totalProfit,totalProfitDollar,clothes,totalPurchaseText
            ,totalPurchase,notFound;
    MyClothesAdapter.ItemClickListener itemClickListener;
    long totalProfitLong,totalPurchaseLong,totalProfitDollarLong;
    String totalProfitString,totalPurchaseString,totalProfitDollarString,editGetText;
    ImageView schoolyCoin;
    EditText searchMyClothes;
    LinearLayout linearSearch;


    int clothesListSize;

    public MyClothesFragment(int clothesListSize,long totalProfitLong,long totalPurchaseLong,
                             long totalProfitDollarLong) {
        this.clothesListSize = clothesListSize;
        this.totalProfitLong=totalProfitLong;
        this.totalPurchaseLong=totalPurchaseLong;
        this.totalProfitDollarLong=totalProfitDollarLong;
    }

    public static MyClothesFragment newInstance(int clothesListSize,long totalProfitLong,long totalPurchaseLong,
                                                long totalProfitDollarLong) {
        return new MyClothesFragment(clothesListSize,totalProfitLong,totalPurchaseLong,totalProfitDollarLong);

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        recyclerMyClothes=view.findViewById(R.id.recyclerMyClothes);
        createClothesBig=view.findViewById(R.id.createBigButtonRecycler);
        createClothes=view.findViewById(R.id.createClothesButton);
        totalProfitText=view.findViewById(R.id.totalProfitText);
        searchMyClothes=view.findViewById(R.id.searchMyClothes);
        totalProfit=view.findViewById(R.id.totalProfit);
        linearSearch=view.findViewById(R.id.linearsearch);
        notFound=view.findViewById(R.id.noClothes);
        totalPurchaseText=view.findViewById(R.id.totalPurchaseText);
        totalPurchase=view.findViewById(R.id.totalPurchase);
        totalProfitDollar=view.findViewById(R.id.totalProfitDollar);
        schoolyCoin=view.findViewById(R.id.schoolycoinGreen);
        clothes=view.findViewById(R.id.clothes);
        relativeFirstClothes=view.findViewById(R.id.relativeFirstClothes);
        itemClickListener=new MyClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingMyClothes.newInstance(MyClothesFragment.newInstance(clothesListSize
                        ,totalProfitLong,totalPurchaseLong,totalProfitDollarLong)), getActivity());
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
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                searchMyClothes.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        editGetText=searchMyClothes.getText().toString();
                        editGetText=editGetText.toLowerCase();
                        if (editGetText.length()>0) {
                            searchMyClothes.setVisibility(View.VISIBLE);
                            searchMyClothes(editGetText);
                        }else if(editGetText.length()==0){
                            recyclerMyClothes.setVisibility(View.VISIBLE);
                            notFound.setVisibility(View.GONE);
                            RecentMethods.getMyClothes(nick, firebaseModel, new Callbacks.GetClothes() {
                                @Override
                                public void getClothes(ArrayList<Clothes> allClothes) {
                                    MyClothesAdapter myClothesAdapter=new MyClothesAdapter(allClothes,itemClickListener);
                                    recyclerMyClothes.setAdapter(myClothesAdapter);
                                }
                            });
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });
        getMyClothes();
        setCounts();
    }

    public void getMyClothes(){
        if(clothesListSize==0){
            recyclerMyClothes.setVisibility(View.GONE);
            totalProfitText.setVisibility(View.GONE);
            totalProfit.setVisibility(View.GONE);
            totalPurchaseText.setVisibility(View.GONE);
            totalPurchase.setVisibility(View.GONE);
            linearSearch.setVisibility(View.GONE);
            schoolyCoin.setVisibility(View.GONE);
            clothes.setVisibility(View.GONE);
            searchMyClothes.setVisibility(View.GONE);
            createClothesBig.setVisibility(View.VISIBLE);
            relativeFirstClothes.setVisibility(View.VISIBLE);
            createClothes.setVisibility(View.GONE);
            createClothesBig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentMethods.setCurrentFragment(CreateClothesFragment.newInstance(MyClothesFragment.newInstance(clothesListSize
                            ,totalProfitLong,totalPurchaseLong,totalProfitDollarLong)), getActivity());
                }
            });
        }else {
            recyclerMyClothes.setVisibility(View.VISIBLE);
            createClothesBig.setVisibility(View.GONE);
            relativeFirstClothes.setVisibility(View.GONE);
            createClothes.setVisibility(View.VISIBLE);
            clothes.setText("Одежда "+String.valueOf(clothesListSize)+":");
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
                                        clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
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
                    RecentMethods.setCurrentFragment(CreateClothesFragment.newInstance(MyClothesFragment.newInstance(clothesListSize
                            ,totalProfitLong,totalPurchaseLong,totalProfitDollarLong)), getActivity());
                }
            });
        }
    }

    public void searchMyClothes(String editTextText){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query = firebaseModel.getUsersReference().child(nick)
                        .child("myClothes");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Clothes> clothesFromBase = new ArrayList<>();
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
                            String clothesTitle = clothes.getClothesTitle();
                            String title = clothesTitle;
                            int valueLetters = editTextText.length();
                            title = title.toLowerCase();
                            if (title.length() < valueLetters) {
                                if (title.equals(editTextText))
                                    clothesFromBase.add(clothes);
                            } else {
                                title = title.substring(0, valueLetters);
                                if (title.equals(editTextText))
                                    clothesFromBase.add(clothes);
                            }
                        }
                        if (clothesFromBase.size() == 0) {
                            recyclerMyClothes.setVisibility(View.GONE);
                            notFound.setVisibility(View.VISIBLE);
                        } else {
                            recyclerMyClothes.setVisibility(View.VISIBLE);
                            MyClothesAdapter myClothesAdapter=new MyClothesAdapter(clothesFromBase,itemClickListener);
                            recyclerMyClothes.setAdapter(myClothesAdapter);
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

    public  void setCounts(){
        if(totalPurchaseLong==0 || totalProfitDollarLong==0){
            totalProfitDollar.setVisibility(View.GONE);
        }
        if(totalProfitLong==0 && totalProfitDollarLong>0){
            totalProfit.setVisibility(View.GONE);
            schoolyCoin.setVisibility(View.GONE);
            totalProfitDollarString = String.valueOf(totalProfitDollarLong);
            if (totalProfitDollarLong < 1000) {
                totalProfitDollar.setText("+" + totalProfitDollarString+"$");
            } else if (totalProfitDollarLong > 1000 && totalProfitDollarLong < 10000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 1) + "." + totalProfitDollarString.substring(1, 2) + "K"+"$");
            } else if (totalProfitDollarLong > 10000 && totalProfitDollarLong < 100000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 2) + "." + totalProfitDollarString.substring(2, 3) + "K"+"$");
            } else if (totalProfitDollarLong > 10000 && totalProfitDollarLong < 100000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 2) + "." + totalProfitDollarString.substring(2, 3) + "K"+"$");
            } else if (totalProfitDollarLong > 100000 && totalProfitDollarLong < 1000000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 3) + "K"+"$");
            } else if (totalProfitDollarLong > 1000000 && totalProfitDollarLong < 10000000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 1) + "KK"+"$");
            } else if (totalProfitDollarLong > 10000000 && totalProfitDollarLong < 100000000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 2) + "KK"+"$");
            }
        }else{
            totalProfitDollarString = String.valueOf(totalProfitDollarLong);
            if (totalProfitDollarLong < 1000) {
                totalProfitDollar.setText("+" + totalProfitDollarString+"$");
            } else if (totalProfitDollarLong > 1000 && totalProfitDollarLong < 10000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 1) + "." + totalProfitDollarString.substring(1, 2) + "K"+"$");
            } else if (totalProfitDollarLong > 10000 && totalProfitDollarLong < 100000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 2) + "." + totalProfitDollarString.substring(2, 3) + "K"+"$");
            } else if (totalProfitDollarLong > 10000 && totalProfitDollarLong < 100000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 2) + "." + totalProfitDollarString.substring(2, 3) + "K"+"$");
            } else if (totalProfitDollarLong > 100000 && totalProfitDollarLong < 1000000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 3) + "K"+"$");
            } else if (totalProfitDollarLong > 1000000 && totalProfitDollarLong < 10000000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 1) + "KK"+"$");
            } else if (totalProfitDollarLong > 10000000 && totalProfitDollarLong < 100000000) {
                totalProfitDollar.setText("+" + totalProfitDollarString.substring(0, 2) + "KK"+"$");
            }
        }
        totalProfitString = String.valueOf(totalProfitLong);
        totalPurchaseString = String.valueOf(totalPurchaseLong);
        if (totalProfitLong < 1000) {
            totalProfit.setText("+" + totalProfitString);
        } else if (totalProfitLong > 1000 && totalProfitLong < 10000) {
            totalProfit.setText("+" + totalProfitString.substring(0, 1) + "." + totalProfitString.substring(1, 2) + "K");
        } else if (totalProfitLong > 10000 && totalProfitLong < 100000) {
            totalProfit.setText("+" + totalProfitString.substring(0, 2) + "." + totalProfitString.substring(2, 3) + "K");
        } else if (totalProfitLong > 10000 && totalProfitLong < 100000) {
            totalProfit.setText("+" + totalProfitString.substring(0, 2) + "." + totalProfitString.substring(2, 3) + "K");
        } else if (totalProfitLong > 100000 && totalProfitLong < 1000000) {
            totalProfit.setText("+" + totalProfitString.substring(0, 3) + "K");
        } else if (totalProfitLong > 1000000 && totalProfitLong < 10000000) {
            totalProfit.setText("+" + totalProfitString.substring(0, 1) + "KK");
        } else if (totalProfitLong > 10000000 && totalProfitLong < 100000000) {
            totalProfit.setText("+" + totalProfitString.substring(0, 2) + "KK");
        }
        //////////////////////////////////////
        if(totalPurchaseLong<1000){
            totalPurchase.setText(totalPurchaseString);
        }else if(totalPurchaseLong>1000 && totalPurchaseLong<10000){
            totalPurchase.setText(totalPurchaseString.substring(0, 1)+"."+totalPurchaseString.substring(1, 2)+"K");
        }
        else if(totalPurchaseLong>10000 && totalPurchaseLong<100000){
            totalPurchase.setText(totalPurchaseString.substring(0, 2)+"."+totalPurchaseString.substring(2,3)+"K");
        }
        else if(totalPurchaseLong>10000 && totalPurchaseLong<100000){
            totalPurchase.setText(totalPurchaseString.substring(0, 2)+"."+totalPurchaseString.substring(2,3)+"K");
        }else if(totalPurchaseLong>100000 && totalPurchaseLong<1000000){
            totalPurchase.setText(totalPurchaseString.substring(0, 3)+"K");
        }
        else if(totalPurchaseLong>1000000 && totalPurchaseLong<10000000){
            totalPurchase.setText(totalPurchaseString.substring(0, 1)+"KK");
        }
        else if(totalPurchaseLong>10000000 && totalPurchaseLong<100000000){
            totalPurchase.setText(totalPurchaseString.substring(0, 2)+"KK");
        }
    }
}
