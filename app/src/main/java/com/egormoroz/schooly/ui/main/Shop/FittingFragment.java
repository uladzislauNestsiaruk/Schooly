package com.egormoroz.schooly.ui.main.Shop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
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

import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.LockableNestedScrollView;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.Wardrobe.ViewingClothesWardrobe;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeAccessories;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeClothes;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrodeClothesAdapter;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeHats;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeShoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FittingFragment extends Fragment {
    String type;
    static String nick;
    Fragment fragment;
    static UserInformation userInformation;
    static Bundle bundle;
    static Clothes clothesFitting;

    public FittingFragment( Fragment fragment, UserInformation userInformation, Bundle bundle,Clothes clothesFitting) {
        this.fragment = fragment;
        FittingFragment.userInformation = userInformation;
        FittingFragment.bundle = bundle;
        FittingFragment.clothesFitting=clothesFitting;
    }

    public static FittingFragment newInstance( Fragment fragment, UserInformation userInformation, Bundle bundle,Clothes clothesFitting) {
        return new FittingFragment( fragment, userInformation, bundle,clothesFitting);

    }

    static FirebaseModel firebaseModel = new FirebaseModel();
    private ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    ArrayList<Clothes> clothesFromBase;
    RecyclerView searchRecycler;
    EditText searchText;
    TabLayout tabLayout;
    int tabLayoutPosition;
    TextView notFound;
    SurfaceView surfaceView;
    WardrodeClothesAdapter.ItemClickListener itemClickListener;
    LockableNestedScrollView lockableNestedScrollView;
    static ArrayList<Clothes> lookClothesList;
    static byte[] buffer;
    static URI uri;
    static Future<Buffer> future;
    static Buffer buffer1,bufferToFilament,b;
    static FilamentModel filamentModel;
    static ArrayList<Clothes> clothesList=new ArrayList<>();
    static ArrayList<String> clothesUid=new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fitting, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        filamentModel=new FilamentModel();
        if(bundle.getSerializable("CLOTHESUID")!=null){
            clothesUid= (ArrayList<String>) bundle.getSerializable("CLOTHESUID");
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bundle.putString("EDIT_WARDROBE_TAG", searchText.getText().toString().trim());
        bundle.putInt("TAB_INT_WARDROBE", tabLayoutPosition);
        bundle.putSerializable("CLOTHESUID", clothesUid);
        bundle.putSerializable("ALLLOADCLOTHESLIST", clothesList);
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nick = userInformation.getNick();
        surfaceView=view.findViewById(R.id.surfaceViewWardrobe);
        searchText = view.findViewById(R.id.searchClothesWardrobe);
        searchRecycler = view.findViewById(R.id.searchRecycler);
        lockableNestedScrollView=view.findViewById(R.id.lockableNestedScrollView);
        if(bundle.getSerializable("ALLLOADCLOTHESLIST")!=null){
            clothesList= (ArrayList<Clothes>) bundle.getSerializable("ALLLOADCLOTHESLIST");
        }
        itemClickListener=new WardrodeClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes,String type,String fragmentString) {
                if(type.equals("view")){
                    RecentMethods.setCurrentFragment(ViewingClothesWardrobe.newInstance(type,FittingFragment.newInstance(fragment, userInformation, bundle,clothes),userInformation,bundle), getActivity());
                }else{
                    makeClothesInvisible(clothes);
                }
            }
        };
        try {
            if(bundle.getSerializable("CHARACTERMODEL")==null){
                loadBuffer(userInformation.getMainLook());
                bufferToFilament=future.get();
                ArrayList<Buffer> buffers=new ArrayList<>();
                buffers.add(bufferToFilament);
                bundle.putSerializable("CHARACTERMODEL",buffers);
                filamentModel.initFilament(surfaceView,bufferToFilament,true,lockableNestedScrollView,"regularRender",true);
            }else{
                ArrayList<Buffer> buffers= (ArrayList<Buffer>) bundle.getSerializable("CHARACTERMODEL");
                Buffer buffer3=buffers.get(0);
                filamentModel.initFilament(surfaceView,buffer3,true,lockableNestedScrollView,"regularRender",true);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        notFound = view.findViewById(R.id.notFound);
        tabLayout = view.findViewById(R.id.tabLayoutWardrobe);
        viewPager = view.findViewById(R.id.frcontwardrobe);

        if(clothesUid.contains(clothesFitting.getUid())){
            if(clothesFitting.getBuffer()!=null){
                filamentModel.populateScene(clothesFitting.getBuffer(), clothesFitting);
            }    else{
                addModelInScene(clothesFitting);
            }
        }else{
            addModelInScene(clothesFitting);
        }

        if (bundle != null) {
            tabLayoutPosition = bundle.getInt("TAB_INT_WARDROBE");
            if (bundle.getString("EDIT_WARDROBE_TAG") != null) {
                String bundleEditText = bundle.getString("EDIT_WARDROBE_TAG").trim();
                if (bundleEditText.length() != 0) {
                    searchText.setText(bundleEditText);
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    loadSearchClothes(bundleEditText);
                }
            }
        }

        loadLookClothes();
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchText.getText().toString().length() > 0) {
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    loadSearchClothes(searchText.getText().toString());
                } else {
                    viewPager.setVisibility(View.VISIBLE);
                    searchRecycler.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    notFound.setVisibility(View.GONE);
                    FragmentManager fm = getChildFragmentManager();
                    fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
                    viewPager.setAdapter(fragmentAdapter);
                    viewPager.setCurrentItem(tabLayoutPosition, false);

                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            tabLayoutPosition = tab.getPosition();
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
                            tabLayoutPosition = position;
                            tabLayout.selectTab(tabLayout.getTabAt(tabLayoutPosition));
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView backfromwardrobe = view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(fragment, getActivity());
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayoutPosition = tab.getPosition();
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
                tabLayoutPosition = position;
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public void loadSearchClothes(String editTextText) {
        firebaseModel.getUsersReference().child(nick).child("clothes")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (userInformation.getClothes() == null) {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        clothesFromBase = new ArrayList<>();
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
                            clothes.setX(snap.child("x").getValue(Float.class));
                            clothes.setY(snap.child("y").getValue(Float.class));
                            clothes.setZ(snap.child("z").getValue(Float.class));
                            clothes.setTransformRatio(snap.child("transformRatio").getValue(Float.class));
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
                            searchRecycler.setVisibility(View.GONE);
                            notFound.setVisibility(View.VISIBLE);
                        } else {
                            notFound.setVisibility(View.GONE);
                            searchRecycler.setVisibility(View.VISIBLE);
                            WardrodeClothesAdapter wardrobeClothesAdapter = new WardrodeClothesAdapter(clothesFromBase, itemClickListener, userInformation,"tryOn");
                            searchRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            searchRecycler.setAdapter(wardrobeClothesAdapter);
                        }
                    }
                } else {
                    clothesFromBase = new ArrayList<>();
                    for (int i = 0; i < userInformation.getClothes().size(); i++) {
                        Clothes clothes = userInformation.getClothes().get(i);
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
                        searchRecycler.setVisibility(View.GONE);
                        notFound.setVisibility(View.VISIBLE);
                    } else {
                        notFound.setVisibility(View.GONE);
                        searchRecycler.setVisibility(View.VISIBLE);
                        WardrodeClothesAdapter wardrobeClothesAdapter = new WardrodeClothesAdapter(clothesFromBase, itemClickListener, userInformation,"tryOn");
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

            switch (position) {
                case 1:
                    return new WardrobeClothes(type, FittingFragment.newInstance(fragment, userInformation, bundle,clothesFitting), userInformation, bundle,"tryOn");
                case 2:
                    return new WardrobeHats(type, FittingFragment.newInstance(fragment, userInformation, bundle,clothesFitting), userInformation, bundle,"tryOn");
                case 3:
                    return new WardrobeAccessories(type, FittingFragment.newInstance( fragment, userInformation, bundle,clothesFitting), userInformation, bundle,"tryOn");
            }

            return new WardrobeShoes(type, FittingFragment.newInstance(fragment, userInformation, bundle,clothesFitting), userInformation, bundle,"tryOn");
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    public void loadLookClothes(){
        if(clothesUid.size()==0) {
            firebaseModel.getUsersReference().child(nick).child("lookClothes")
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Clothes clothes1 = snap.getValue(Clothes.class);
                            if(!clothes1.getBodyType().equals(clothesFitting.getBodyType())){
                                Log.d("#####", "11");
                                addModelInScene(clothes1);
                            }else{
                                filamentModel.setMask(clothes1);
                            }
                        }
                    }
                }
            });
        }  else{
            Log.d("#####", "22"+clothesUid.size()+clothesList.size());
            for(int i=0;i<clothesList.size();i++ ){
                Clothes clothes1=clothesList.get(i);
                if(clothesUid.contains(clothes1.getUid())){
                    if(!clothes1.getBodyType().equals(clothesFitting.getBodyType())){
                        if(clothes1.getBuffer()!=null){
                            filamentModel.populateScene(clothes1.getBuffer(), clothes1);
                        }    else{
                            addModelInScene(clothes1);
                        }
                    }else{
                        filamentModel.setMask(clothes1);
                        Log.d("####", "vv  "+clothesUid.size());
                    }
                }
            }
        }
    }

    public static void tryOnClothes(Clothes clothes){
        int a=0;
        if(clothesList.size()!=0){
            for(int i=0;i<clothesList.size();i++){
                Clothes clothes1=clothesList.get(i);
                if(clothes.getUid().equals(clothes1.getUid()) && clothes.getBuffer()!=null){
                    filamentModel.populateScene(clothes.getBuffer(),clothes);
                    clothesUid.add(clothes.getUid());
                    break;
                }else if(a==0 && i==clothesList.size()-1){
                    addModelInScene(clothes);
                    break;
                }
            }
        }else {
            addModelInScene(clothes);
            a++;
        }
    }



    public static void makeClothesInvisible(Clothes clothes){
        if(!clothes.getBodyType().equals(clothesFitting.getBodyType())){
            String type=clothes.getBodyType();
            int a=0;
            int c=0;
            b=clothes.getBuffer();
            Clothes clothesToChange=new Clothes();
            if(userInformation.getLookClothes().size()==0){
                clothes.setBuffer(null);
                firebaseModel.getUsersReference().child(nick).child("lookClothes")
                        .child(clothes.getUid()).setValue(clothes);
                if(a==0){
                    clothes.setBuffer(b);
                    tryOnClothes(clothes);
                    a++;
                }
            }  else{
                for(int i=0;i<userInformation.getLookClothes().size();i++){
                    Clothes clothes1=userInformation.getLookClothes().get(i);
                    if(clothes1.getUid().equals(clothes.getUid())){
                        break;
                    }if(clothes1.getBodyType().equals(type)){
                        clothesToChange=clothes1;
                        c++;
                    }
                    if(i==userInformation.getLookClothes().size()-1){
                        if(c==1){
                            firebaseModel.getUsersReference().child(nick).child("lookClothes")
                                    .child(clothesToChange.getUid()).removeValue();
                            clothesUid.remove(clothesToChange.getUid());
                            clothes.setBuffer(null);
                            firebaseModel.getUsersReference().child(nick).child("lookClothes")
                                    .child(clothes.getUid()).setValue(clothes);
                            filamentModel.setMask(clothesToChange);
                        }     else{
                            clothes.setBuffer(null);
                            firebaseModel.getUsersReference().child(nick).child("lookClothes")
                                    .child(clothes.getUid()).setValue(clothes);
                        }
                        if(a==0){
                            clothes.setBuffer(b);
                            tryOnClothes(clothes);
                            a++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        filamentModel.postFrameCallback();
    }

    @Override
    public void onPause() {
        super.onPause();

        filamentModel.removeFrameCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        filamentModel.removeFrameCallback();
    }

    public static byte[] getBytes( URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream());
            byte[] byteChunk = new byte[4096];
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            Log.d("####", "Failed while reading bytes from %s: %s"+ url.toExternalForm()+ e.getMessage());
            e.printStackTrace ();
        }
        finally {
            if (is != null) { is.close(); }
        }
        return  baos.toByteArray();
    }

    public static void addModelInScene(Clothes clothes)  {
        loadBuffer(clothes.getModel());
        try {
            bufferToFilament= future.get();
            filamentModel.populateScene(bufferToFilament,clothes);
            clothes.setBuffer(bufferToFilament);
            clothesList.add(clothes);
            if(!clothes.getUid().equals(clothesFitting.getUid()))
            clothesUid.add(clothes.getUid());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void loadBuffer(String model){
        ExecutorService executorService= Executors.newCachedThreadPool();
        future = executorService.submit(new Callable(){
            public Buffer call() throws Exception {
                uri = new URI(model);
                buffer = getBytes(uri.toURL());
                buffer1= ByteBuffer.wrap(buffer);
                return buffer1;
            }
        });
    }
}
