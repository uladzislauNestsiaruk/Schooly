package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateLookFragment extends Fragment {
    FirebaseModel firebaseModel=new FirebaseModel();
    private ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    RecyclerView searchRecycler;
    EditText searchText;
    TabLayout tabLayout;
    TextView notFound,ready;
    ArrayList<Clothes> clothesFromBase;
    WardrobeClothesAdapter.ItemClickListener itemClickListener;
    String type,nick;
    Fragment fragment;
    int tabLayoutPosition;
    UserInformation userInformation;
    Bundle bundle;
    String lookType;

    public CreateLookFragment(String type,Fragment fragment,UserInformation userInformation,Bundle bundle,String lookType) {
        this.type = type;
        this.fragment=fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.lookType=lookType;
    }

    public static CreateLookFragment newInstance(String type,Fragment fragment,UserInformation userInformation,Bundle bundle,String lookType) {
        return new CreateLookFragment(type,fragment,userInformation,bundle,lookType);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newlook, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bundle.putString("EDIT_CREATE_LOOK_TAG",searchText.getText().toString().trim());
        bundle.putInt("TAB_INT_CREATE_LOOK", tabLayoutPosition);
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        nick=userInformation.getNick();
        searchText=view.findViewById(R.id.searchClothesWardrobe);
        searchRecycler=view.findViewById(R.id.searchRecycler);
        notFound=view.findViewById(R.id.notFound);
        tabLayout = view.findViewById(R.id.tabLayoutWardrobe);
        viewPager=view.findViewById(R.id.frcontwardrobe);
        itemClickListener=new WardrobeClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingClothesWardrobe.newInstance(type,CreateLookFragment.newInstance(type, fragment, userInformation, bundle,lookType),userInformation,bundle), getActivity());
            }
        };
        if (bundle!=null){
            tabLayoutPosition=bundle.getInt("TAB_INT_CREATE_LOOK");
            if(bundle.getString("EDIT_CREATE_LOOK_TAG")!=null){
                String bundleEditText=bundle.getString("EDIT_CREATE_LOOK_TAG").trim();
                if(bundleEditText.length()!=0){
                    searchText.setText(bundleEditText);
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    loadSearchClothes(bundleEditText);
                }
            }
        }
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchText.getText().toString().length()>0){
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    loadSearchClothes(searchText.getText().toString());
                }else{
                    viewPager.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    notFound.setVisibility(View.GONE);
                    FragmentManager fm = getChildFragmentManager();
                    fragmentAdapter = new CreateLookFragment.FragmentAdapter(fm, getLifecycle());
                    viewPager.setAdapter(fragmentAdapter);
                    viewPager.setCurrentItem(tabLayoutPosition, false);

                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            tabLayoutPosition=tab.getPosition();
                            viewPager.setCurrentItem(tabLayoutPosition);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });

                    tabLayout.selectTab(tabLayout.getTabAt(tabLayoutPosition));
                    viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            tabLayoutPosition=position;
                            tabLayout.selectTab(tabLayout.getTabAt(tabLayoutPosition));
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ready=view.findViewById(R.id.ready);
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseModel.getUsersReference().child(nick).child("lookClothes")
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot= task.getResult();
                            ArrayList<Clothes> lookClothesFromBase=new ArrayList<>();
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
                                lookClothesFromBase.add(clothes);
                            }
                            if(lookClothesFromBase.size()!=0){
                                userInformation.setLookClothes(lookClothesFromBase);
                                RecentMethods.setCurrentFragment(AcceptNewLook.newInstance("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"
                                        ,type,fragment,userInformation,bundle,lookType)
                                        , getActivity());
                            }else {
                                Toast.makeText(getContext(), getContext().getResources().getText(R.string.addlookcomponents), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        ImageView backfromwardrobe=view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick,fragment,userInformation,bundle)
                        , getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type,nick,fragment,userInformation,bundle), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        FragmentManager fm = getChildFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(tabLayoutPosition, false);

        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.shoes)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.clothes)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.hats)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.accessories)));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayoutPosition=tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.selectTab(tabLayout.getTabAt(tabLayoutPosition));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayoutPosition=position;
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public void loadSearchClothes(String editTextText){
        firebaseModel.getUsersReference().child(nick).child("clothes")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(userInformation.getClothes()==null){
                    if(task.isSuccessful()){
                        DataSnapshot snapshot= task.getResult();
                        clothesFromBase=new ArrayList<>();
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
                        if(clothesFromBase.size()==0){
                            searchRecycler.setVisibility(View.GONE);
                            notFound.setVisibility(View.VISIBLE);
                        }else{
                            notFound.setVisibility(View.GONE);
                            searchRecycler.setVisibility(View.VISIBLE);
                            WardrobeClothesAdapter wardrobeClothesAdapter=new WardrobeClothesAdapter(clothesFromBase,itemClickListener,userInformation);
                            searchRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            searchRecycler.setAdapter(wardrobeClothesAdapter);
                        }
                    }
                }else {
                    clothesFromBase=new ArrayList<>();
                    for (int i=0;i<userInformation.getClothes().size();i++) {
                        Clothes clothes = userInformation.getClothes().get(i);
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
                    if(clothesFromBase.size()==0){
                        searchRecycler.setVisibility(View.GONE);
                        notFound.setVisibility(View.VISIBLE);
                    }else{
                        notFound.setVisibility(View.GONE);
                        searchRecycler.setVisibility(View.VISIBLE);
                        WardrobeClothesAdapter wardrobeClothesAdapter=new WardrobeClothesAdapter(clothesFromBase,itemClickListener,userInformation);
                        searchRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        searchRecycler.setAdapter(wardrobeClothesAdapter);
                    }
                }
            }
        });
    }


    public class FragmentAdapter extends FragmentStateAdapter {
        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position)
            {
                case 1 :
                    return new WardrobeClothes(type,CreateLookFragment.newInstance(type, fragment, userInformation, bundle,lookType),userInformation,bundle);
                case 2 :
                    return new WardrobeHats(type,CreateLookFragment.newInstance(type, fragment, userInformation, bundle,lookType),userInformation,bundle);
                case 3 :
                    return new WardrobeAccessories(type,CreateLookFragment.newInstance(type, fragment, userInformation, bundle,lookType),userInformation,bundle);
            }

            return new WardrobeShoes(type,CreateLookFragment.newInstance(type, fragment, userInformation, bundle,lookType),userInformation,bundle);
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
