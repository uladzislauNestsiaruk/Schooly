package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.PopularClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.Consumer;

public class WardrobeFragment extends Fragment {
    String type,nick;
    Fragment fragment;
    UserInformation userInformation;
    Bundle bundle;

    public WardrobeFragment(String type,Fragment fragment,UserInformation userInformation,Bundle bundle) {
        this.type = type;
        this.fragment=fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static WardrobeFragment newInstance(String type,Fragment fragment,UserInformation userInformation,Bundle bundle) {
        return new WardrobeFragment(type,fragment,userInformation,bundle);

    }
    ArrayList<Clothes> clothesArrayListWardrobe=new ArrayList<Clothes>();
    FirebaseModel firebaseModel=new FirebaseModel();
    private ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    RecyclerView searchRecycler;
    EditText searchText;
    TabLayout tabLayout;
    TextView notFound;
    SceneView sceneView;
    WardrobeClothesAdapter.ItemClickListener itemClickListener;


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
        nick= userInformation.getNick();
        sceneView = view.findViewById(R.id.sceneViewWardrobe);
        searchText=view.findViewById(R.id.searchClothesWardrobe);
        searchRecycler=view.findViewById(R.id.searchRecycler);
        notFound=view.findViewById(R.id.notFound);
        loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"), sceneView, WardrobeFragment.this);
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
                    tabLayout = view.findViewById(R.id.tabLayoutWardrobe);
                    viewPager=view.findViewById(R.id.frcontwardrobe);
                    FragmentManager fm = getChildFragmentManager();
                    fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
                    viewPager.setAdapter(fragmentAdapter);

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

        ImageView backfromwardrobe=view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick,fragment,userInformation,bundle), getActivity());
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type,nick,fragment,userInformation,bundle), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        tabLayout = view.findViewById(R.id.tabLayoutWardrobe);
        viewPager=view.findViewById(R.id.frcontwardrobe);
        FragmentManager fm = getChildFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(fragmentAdapter);

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

    public void loadSearchClothes(String editTextText){
        firebaseModel.getUsersReference().child(nick).child("clothes")
        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot= task.getResult();
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
                        WardrobeClothesAdapter wardrobeClothesAdapter=new WardrobeClothesAdapter(clothesFromBase,itemClickListener);
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
                    return new WardrobeClothes(type,fragment,userInformation,bundle);
                case 2 :
                    return new WardrobeHats(type,fragment,userInformation,bundle);
                case 3 :
                    return new WardrobeAccessories(type,fragment,userInformation,bundle);
            }

            return new WardrobeShoes(type,fragment,userInformation,bundle);
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadModels(Uri url, SceneView sceneView, Fragment fragment) {
        ModelRenderable.builder()
                .setSource(
                        fragment.getContext(), new RenderableSource.Builder().setSource(
                                fragment.getContext(),
                                url,
                                RenderableSource.SourceType.GLB
                        ).setScale(0.25f)
                                .setRecenterMode(RenderableSource.RecenterMode.CENTER)
                                .build()
                )
                .setRegistryId(url)
                .build()
                .thenAccept(new Consumer<ModelRenderable>() {
                    @Override
                    public void accept(ModelRenderable modelRenderable) {
                        addNode(modelRenderable, sceneView);
                    }
                });
    }

    public void addNode(ModelRenderable modelRenderable, SceneView sceneView) {
        Node modelNode1 = new Node();
        modelNode1.setRenderable(modelRenderable);
        modelNode1.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));
        modelNode1.setLocalRotation(Quaternion.multiply(
                Quaternion.axisAngle(new Vector3(1f, 0f, 0f), 45),
                Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 75)));
        modelNode1.setLocalPosition(new Vector3(0f, 0f, -0.9f));
        sceneView.getScene().addChild(modelNode1);
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sceneView.pause();

    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            sceneView.resume();


        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }

    }
}