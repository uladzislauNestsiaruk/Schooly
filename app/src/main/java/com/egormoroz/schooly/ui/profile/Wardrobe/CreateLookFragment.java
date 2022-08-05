package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.LockableNestedScrollView;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.TaskRunner;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CreateLookFragment extends Fragment {
    static FirebaseModel firebaseModel = new FirebaseModel();
    private ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    RecyclerView searchRecycler;
    EditText searchText;
    TabLayout tabLayout;
    TextView notFound, ready;
    ArrayList<Clothes> clothesFromBase;
    WardrodeClothesAdapter.ItemClickListener itemClickListener;
    static String type, nick;
    static ArrayList<Buffer> buffers;
    Fragment fragment;
    int tabLayoutPosition;
    static UserInformation userInformation;
    Bundle bundle;
    SurfaceView surfaceView;
    String lookType;
    static byte[] buffer;
    static URI uri;
    static Buffer buffer1,bufferToFilament,b;
    static FilamentModel filamentModel=new FilamentModel();
    static ArrayList<Clothes> clothesList=new ArrayList<>();
    static ArrayList<String> clothesUid=new ArrayList<>();
    LockableNestedScrollView lockableNestedScrollView;
    static int loadValue;
    int a=0;

    public CreateLookFragment(String type, Fragment fragment, UserInformation userInformation, Bundle bundle, String lookType) {
        this.type = type;
        this.fragment = fragment;
        CreateLookFragment.userInformation = userInformation;
        this.bundle = bundle;
        this.lookType = lookType;
    }

    public static CreateLookFragment newInstance(String type, Fragment fragment, UserInformation userInformation, Bundle bundle, String lookType) {
        return new CreateLookFragment(type, fragment, userInformation, bundle, lookType);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newlook, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
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
        searchText = view.findViewById(R.id.searchClothesWardrobe);
        searchRecycler = view.findViewById(R.id.searchRecycler);
        notFound = view.findViewById(R.id.notFound);
        tabLayout = view.findViewById(R.id.tabLayoutWardrobe);
        viewPager = view.findViewById(R.id.frcontwardrobe);
        surfaceView=view.findViewById(R.id.surfaceViewCreateClothes);
        itemClickListener=new WardrodeClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes,String type,String fragmentType) {
                if(type.equals("view")){
                    if(loadValue==0)RecentMethods.setCurrentFragment(ViewingClothesWardrobe.newInstance(type,CreateLookFragment.newInstance(type, fragment, userInformation, bundle,lookType),userInformation,bundle), getActivity());
                }else{
                    if(loadValue==0) checkOnTryOn(clothes);
                }
            }
        };
        if (bundle != null) {
            tabLayoutPosition = bundle.getInt("TAB_INT_CREATE_LOOK");
            if (bundle.getString("EDIT_CREATE_LOOK_TAG") != null) {
                String bundleEditText = bundle.getString("EDIT_CREATE_LOOK_TAG").trim();
                if (bundleEditText.length() != 0) {
                    searchText.setText(bundleEditText);
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    loadSearchClothes(bundleEditText);
                }
            }
        }
        lockableNestedScrollView=view.findViewById(R.id.lockableNestedScrollView);
        loadPerson(userInformation, lockableNestedScrollView);
        if(bundle.getSerializable("ALLLOADCLOTHESLIST")!=null){
            clothesList= (ArrayList<Clothes>) bundle.getSerializable("ALLLOADCLOTHESLIST");
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
                    fragmentAdapter = new CreateLookFragment.FragmentAdapter(fm, getLifecycle());
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
        ready = view.findViewById(R.id.ready);
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseModel.getUsersReference().child(nick).child("lookClothes")
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            ArrayList<Clothes> lookClothesFromBase = new ArrayList<>();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Clothes clothes = snap.getValue(Clothes.class);
                                lookClothesFromBase.add(clothes);
                            }
                            if (lookClothesFromBase.size() != 0) {
                                userInformation.setLookClothes(lookClothesFromBase);
                                if(loadValue==0) {
                                    RecentMethods.setCurrentFragment(AcceptNewLook.newInstance("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"
                                            , type, fragment, userInformation, bundle, lookType, clothesUid, clothesList)
                                            , getActivity());
                                }
                            } else {
                                Toast.makeText(getContext(), getContext().getResources().getText(R.string.addlookcomponents), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        ImageView backfromwardrobe = view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loadValue==0) {
                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick, fragment, userInformation, bundle)
                            , getActivity());
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if(loadValue==0)RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick, fragment, userInformation, bundle), getActivity());
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

    public void loadSearchClothes (String editTextText){
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
                            WardrodeClothesAdapter wardrobeClothesAdapter = new WardrodeClothesAdapter(clothesFromBase, itemClickListener, userInformation,"createClothes");
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
                        WardrodeClothesAdapter wardrobeClothesAdapter = new WardrodeClothesAdapter(clothesFromBase, itemClickListener, userInformation,"createClothes");
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
                    return new WardrobeClothes(type, CreateLookFragment.newInstance(type, fragment, userInformation, bundle, lookType), userInformation, bundle,"createClothes");
                case 2:
                    return new WardrobeHats(type, CreateLookFragment.newInstance(type, fragment, userInformation, bundle, lookType), userInformation, bundle,"createClothes");
                case 3:
                    return new WardrobeAccessories(type, CreateLookFragment.newInstance(type, fragment, userInformation, bundle, lookType), userInformation, bundle,"createClothes");
            }

            return new WardrobeShoes(type, CreateLookFragment.newInstance(type, fragment, userInformation, bundle, lookType), userInformation, bundle,"createClothes");
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }


    public void loadLookClothes(){
        loadValue=1;
        if(clothesUid.size()==0) {
            RecentMethods.getMyLookClothesOnce(nick, firebaseModel, new Callbacks.getLookClothes() {
                @Override
                public void getLookClothes(ArrayList<Clothes> clothesArrayList) {
                    for(int i=0;i<clothesArrayList.size();i++){
                        Clothes clothes=clothesArrayList.get(i);
                        TaskRunner taskRunner=new TaskRunner();
                        int finalI = i;
                        taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
                            filamentModel.populateScene(data.getBuffer(), data);
                            if(finalI ==clothesArrayList.size()-1){
                                loadValue=0;
                                Log.d("####", "ss77 "+loadValue);
                            }
                        });
                    }
                }
            });
        } else{
            for(int i=0;i<clothesList.size();i++ ){
                Clothes clothes=clothesList.get(i);
                if(clothesUid.contains(clothes.getUid())&&clothes.getBuffer()!=null){
                    filamentModel.populateScene(clothes.getBuffer(), clothes);
                    if(a ==clothesUid.size()-1){
                        loadValue=0;
                    }
                    a++;
                } else if(clothesUid.contains(clothes.getUid())&&clothes.getBuffer()==null){
                    TaskRunner taskRunner=new TaskRunner();
                    int finalA = a;
                    taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
                        filamentModel.populateScene(data.getBuffer(), data);
                        if(finalA ==clothesUid.size()-1){
                            loadValue=0;
                        }
                    });
                    a++;
                }
            }
        }
    }

    public static void tryOnClothes(Clothes clothes,Clothes maskClothes){
        int a=0;
        if(clothesList.size()!=0){
            for(int i=0;i<clothesList.size();i++){
                Clothes clothes1=clothesList.get(i);
                if(clothes.getUid().equals(clothes1.getUid()) && clothes.getBuffer()!=null){
                    if(maskClothes!=null){
                        filamentModel.setMask(maskClothes);
                    }
                    filamentModel.populateScene(clothes.getBuffer(),clothes);
                    clothesUid.add(clothes.getUid());
                    loadValue=0;
                    Log.d("####", "q  "+loadValue);
                    break;
                }else if(a==0 && i==clothesList.size()-1){
                    TaskRunner taskRunner=new TaskRunner();
                    taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
                        if(maskClothes!=null){
                            filamentModel.setMask(maskClothes);
                        }
                        filamentModel.populateScene(data.getBuffer(), data);
                        loadValue=0;
                        Log.d("####", "w  "+loadValue);
                    });
                    break;
                }
            }
        }else {
            TaskRunner taskRunner=new TaskRunner();
            taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
                if(maskClothes!=null){
                    filamentModel.setMask(maskClothes);
                }
                filamentModel.populateScene(data.getBuffer(), data);
                loadValue=0;
                Log.d("####", "r  "+loadValue);
            });
            a++;
        }
    }

    public static void checkOnTryOn(Clothes clothes){
        Log.d("####", "a   "+loadValue);
        if(loadValue==0){
            loadValue++;
            Log.d("####", "b   "+loadValue);
            makeClothesInvisible(clothes);
        }
    }

    public static void sentToViewingFrag(String type, Fragment fragment, UserInformation userInformation, Bundle bundle, Activity activity){
        if(loadValue==0){
            RecentMethods.setCurrentFragment(ViewingClothesWardrobe.newInstance(type,fragment,userInformation,bundle), activity);
        }
    }

    public static void makeClothesInvisible(Clothes clothes){
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
                tryOnClothes(clothes,null);
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
                        Log.d("#####", "aaaa  "+clothesUid.size());
                        clothes.setBuffer(null);
                        firebaseModel.getUsersReference().child(nick).child("lookClothes")
                                .child(clothes.getUid()).setValue(clothes);
                    }     else{
                        clothes.setBuffer(null);
                        firebaseModel.getUsersReference().child(nick).child("lookClothes")
                                .child(clothes.getUid()).setValue(clothes);
                    }
                    if(a==0){
                        clothes.setBuffer(b);
                        tryOnClothes(clothes,clothesToChange);
                        a++;
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

    public static Clothes addModelInScene(Clothes clothes)  {
        try {
            uri = new URI(clothes.getModel());
            buffer = getBytes(uri.toURL());
            bufferToFilament= ByteBuffer.wrap(buffer);
            clothes.setBuffer(bufferToFilament);
            clothesList.add(clothes);
            clothesUid.add(clothes.getUid());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return clothes;
    }

    public void loadPerson(UserInformation userInformation,LockableNestedScrollView lockableNestedScrollView){
        try {
            if(bundle.getSerializable("PERSON"+userInformation.getNick())==null){
                uri = new URI(userInformation.getPerson().getBody());
                buffer = getBytes(uri.toURL());
                bufferToFilament= ByteBuffer.wrap(buffer);
                buffers=new ArrayList<>();
                buffers.add(bufferToFilament);
                bundle.putSerializable("PERSON"+userInformation.getNick(),buffers);
                filamentModel.initFilament(surfaceView,bufferToFilament,true,lockableNestedScrollView
                        ,"regularRender",true);
                loadBodyPart(userInformation.getPerson().getBrows());
                loadBodyPart(userInformation.getPerson().getEars());
                loadBodyPart(userInformation.getPerson().getEyes());
                loadBodyPart(userInformation.getPerson().getHair());
                loadBodyPart(userInformation.getPerson().getHead());
                loadBodyPart(userInformation.getPerson().getLips());
                loadBodyPart(userInformation.getPerson().getNose());
                loadBodyPart(userInformation.getPerson().getPirsing());
                loadBodyPart(userInformation.getPerson().getSkinColor());

            }else{
                ArrayList<Buffer> buffers= (ArrayList<Buffer>) bundle.getSerializable("PERSON"+userInformation.getNick());
                for(int i=0;i<buffers.size();i++){
                    Buffer buffer3=buffers.get(i);
                    if(i==0){
                        filamentModel.initFilament(surfaceView,buffer3 ,true,lockableNestedScrollView
                                ,"regularRender",true);
                    }else{
                        filamentModel.populateSceneFacePart(buffer3);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void loadBodyPart(String string)  {
        if(string!=null){
            try {
                uri = new URI(string);
                buffer = getBytes(uri.toURL());
                bufferToFilament= ByteBuffer.wrap(buffer);
                filamentModel.populateSceneFacePart(bufferToFilament);
                buffers.add(bufferToFilament);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


    static class LongRunningTask implements Callable<Clothes> {
        private Clothes clothes;

        public LongRunningTask(Clothes clothes) {
            this.clothes = clothes;
        }

        @Override
        public Clothes call() {
            return addModelInScene(clothes);
        }
    }

}
