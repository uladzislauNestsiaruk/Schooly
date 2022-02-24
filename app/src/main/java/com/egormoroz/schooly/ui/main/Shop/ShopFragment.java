package com.egormoroz.schooly.ui.main.Shop;

import android.media.TimedText;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.RecentMethods;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.main.ChatsFragment;
import com.egormoroz.schooly.ui.main.DialogsActivity;
import com.egormoroz.schooly.ui.main.GroupsFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopFragment extends Fragment {
    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    TextView coinsshop;
    private ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    ImageView basket;
    EditText searchClothes;
    static String editGetText;
    RecyclerView searchRecycler;
    TabLayout tabLayout;
    TextView notFound;
    ArrayList<Clothes> searchClothesArrayList=new ArrayList<Clothes>();
    PopularClothesAdapter.ItemClickListener itemClickListenerPopular;
    LinearLayout coinsLinear;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        coinsshop=root.findViewById(R.id.schoolycoinshopfrag);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ImageView backtoprofileshop=view.findViewById(R.id.back_toprofile);
        backtoprofileshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(MainFragment.newInstance());
            }
        });
        coinsLinear=view.findViewById(R.id.linearCoins);
        coinsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CoinsMainFragment.newInstance(), getActivity());
            }
        });

        searchRecycler=view.findViewById(R.id.searchRecycler);
        notFound=view.findViewById(R.id.notFound);
        searchClothes=view.findViewById(R.id.searchClothes);
        searchClothes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editGetText=searchClothes.getText().toString();
                editGetText=editGetText.toLowerCase();
                if (editGetText.length()>0) {
                    viewPager.setVisibility(View.GONE);
                    searchRecycler.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.GONE);
                    loadSearchClothes(editGetText);
                    itemClickListenerPopular=new PopularClothesAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(Clothes clothes) {
                            ((MainActivity)getActivity()).setCurrentFragment(ViewingClothesPopular.newInstance());
                        }
                    };
                }else if(editGetText.length()==0){
                    viewPager.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    tabLayout = view.findViewById(R.id.tabLayoutShop);
                    viewPager=view.findViewById(R.id.frcontshop);
                    notFound.setVisibility(View.GONE);
                    FragmentManager fm = getChildFragmentManager();
                    fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
                    viewPager.setAdapter(fragmentAdapter);

                    tabLayout.addTab(tabLayout.newTab().setText("Главная"));
                    tabLayout.addTab(tabLayout.newTab().setText("Обувь"));
                    tabLayout.addTab(tabLayout.newTab().setText("Одежда"));
                    tabLayout.addTab(tabLayout.newTab().setText("Головные уборы"));
                    tabLayout.addTab(tabLayout.newTab().setText("Акскссуары"));

                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            viewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });


                    viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            tabLayout.selectTab(tabLayout.getTabAt(position));
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        coinsshop.setText(String.valueOf(money));
                    }
                });
            }
        });
        tabLayout = view.findViewById(R.id.tabLayoutShop);
        viewPager=view.findViewById(R.id.frcontshop);
        FragmentManager fm = getChildFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Главная"));
        tabLayout.addTab(tabLayout.newTab().setText("Обувь"));
        tabLayout.addTab(tabLayout.newTab().setText("Одежда"));
        tabLayout.addTab(tabLayout.newTab().setText("Головные уборы"));
        tabLayout.addTab(tabLayout.newTab().setText("Акскссуары"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        basket=view.findViewById(R.id.basket);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BasketFragment.newInstance(), getActivity());
            }
        });

    }


    public void loadSearchClothes(String editTextText){
        Query query=firebaseModel.getReference("AppData/Clothes/AllClothes");
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
                    searchRecycler.setVisibility(View.GONE);
                    notFound.setVisibility(View.VISIBLE);
                }else {
                    PopularClothesAdapter popularClothesAdapter=new PopularClothesAdapter(clothesFromBase,itemClickListenerPopular);
                    searchRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    searchRecycler.setAdapter(popularClothesAdapter);
                    notFound.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void loadModelInBase(){
//        Clothes clothes=new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                , 120,"Jordan 1");
//        Clothes clothes2=new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                , 100,"Jordan 4");
//        Clothes clothes3=new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                , 10,"Jordan 6");
//        firebaseModel.getReference().child("AppData").child("Clothes")
//                .child("AllClothes").child(clothes.getClothesTitle()).setValue(clothes);
//        firebaseModel.getReference().child("AppData").child("Clothes")
//                .child("Popular").child(clothes.getClothesTitle()).setValue(clothes);
//        firebaseModel.getReference().child("AppData").child("Clothes")
//                .child("AllNew").child(clothes.getClothesTitle()).setValue(clothes);
////        firebaseModel.getReference().child("AppData").child("Clothes")
////                .child("AllClothes").child(clothes2.getClothesTitle()).setValue(clothes2);
//        firebaseModel.getReference().child("AppData").child("Clothes")
//                .child("Popular").child(clothes2.getClothesTitle()).setValue(clothes2);
//        firebaseModel.getReference().child("AppData").child("Clothes")
//                .child("AllNew").child(clothes2.getClothesTitle()).setValue(clothes2);
//        firebaseModel.getReference().child("AppData").child("Clothes")
//                .child("AllNew").child(clothes3.getClothesTitle()).setValue(clothes3);
//    }


    public class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @NonNull
        @Override
        public Fragment createFragment ( int position){


            switch (position) {
                case 1:
                    return new ShoesFargment();
                case 2:
                    return new ClothesFragment();
                case 3:
                    return new HatsFragment();
                case 4:
                    return new AccessoriesFragment();
            }
            return new PopularFragment();
        }


        @Override
        public int getItemCount() {
            return 5;
        }
    }


}