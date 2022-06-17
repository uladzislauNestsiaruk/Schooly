package com.egormoroz.schooly.ui.profile;

import static android.os.Looper.getMainLooper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;

import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;

import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.Model.SceneViewModelActivity;
import com.egormoroz.schooly.ui.chat.User;
import com.egormoroz.schooly.ui.main.ChatActivity;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.AccessoriesFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.ClothesFragment;
import com.egormoroz.schooly.ui.main.Shop.HatsFragment;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.PopularFragment;
import com.egormoroz.schooly.ui.main.Shop.ShoesFargment;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeAdapterProfile;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeClothes;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeFragment;
import com.google.android.filament.Filament;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class ProfileFragment extends Fragment {
    FirebaseModel firebaseModel = new FirebaseModel();
    Context profileContext, context;
    EditText editText,messageEdit;
    UserInformation info;
    CircularProgressIndicator circularProgressIndicator;
    WardrobeAdapterProfile.ItemClickListener itemClickListenerWardrobe;
    TextView nickname,message,biographyTextView,looksCount,subscriptionsCount,subscribersCount,otherLooksCount,otherSubscriptionCount,
            otherSubscribersCount,otherUserBiography,subscribeClose,subscribe
            ,subscribeFirst,closeAccount,noClothes,buyClothesProfile,blockedAccount,emptyList;
    DatabaseReference user;
    ArrayList<Subscriber> userFromBase;
    // SceneLoader scene;
    LinearLayout linearSubscribers,linearSubscriptions
            ,linearLooksProfile,linearSubscribersProfile,linearSubscriptionsProfile;
    // ModelSurfaceView modelSurfaceView;
    SceneView mainLook, otherMainLook;
    SendLookAdapter.ItemClickListener itemClickListenerSendLookAdapter;
    // ModelRenderer modelRenderer;
    RecyclerView wardrobeRecycler,recyclerView;
    ImageView moreSquare,back,newLook,editMainLook,editMainLookBack;
    String sendNick,subscriptionsCountString,subscribersCountString
            ,otherSubscriptionCountString,
            otherSubscribersCountString,type,nicknameCallback,userName;
    Fragment fragment;
    View root;
    ViewPager2 viewPager,viewPagerOther;
    FragmentAdapter fragmentAdapter;
    FragmentAdapterOther fragmentAdapterOther;
    Handler handler;
    TabLayout tabLayout,tabLayoutOther;
    int tabLayoutPosition,tabLayoutPositionOther;
    private float[] backgroundColor = new float[]{0f, 0f, 0f, 1.0f};
    int a,profileCheckValue,checkOnSubscribeValue, b=0,v;
    UserInformation userInformation;
    Bundle bundle;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        profileContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(type.equals("user") || type.equals("userback")){
            bundle.putInt("TAB_INT_PROFILE", tabLayoutPosition);
        }else{
            bundle.putInt("TAB_INT_PROFILE_OTHER", tabLayoutPositionOther);
            checkProfileAfterQuit();
            firebaseModel.getUsersReference().child(userInformation.getNick()).child("subscription")
                    .child(info.getNick()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        checkOnSubscribeValue=1;
                    }else {
                        checkOnSubscribeValue=0;
                    }
                    bundle.putString(sendNick+"PROFILE_OTHER_CHECK_SUBSCRIBE_VALUE",String.valueOf(checkOnSubscribeValue));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public ProfileFragment(String type, String sendNick,Fragment fragment,UserInformation userInformation,Bundle bundle) {
        this.type = type;
        this.sendNick=sendNick;
        this.fragment=fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static ProfileFragment newInstance(String type, String sendNick, Fragment fragment
            , UserInformation userInformation,Bundle bundle) {
        return new ProfileFragment(type, sendNick,fragment,userInformation,bundle);
    }


    public void open() {
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(userInformation.getNick()).exists()) {
                    AcceptChatRequest();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent i = new Intent(getActivity(), ChatActivity.class);
        //Getting information about user(friend)
        i.putExtra("othUser", info.getNick());
        i.putExtra("curUser", userInformation.getNick());
        i.putExtra("groupName", "one");
        i.putExtra("visit_user_id", info.getUid());
        i.putExtra("visit_image", ChatActivity.class);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        if(type.equals("user")){
            root=inflater.inflate(R.layout.fragment_profile, container, false);
            nickname=root.findViewById(R.id.usernick);
        }else if(type.equals("other")){
            root=inflater.inflate(R.layout.fragment_otheruser, container, false);
            nickname=root.findViewById(R.id.otherusernick);
            root.findViewById(R.id.message);
        }else if(type.equals("userback")){
            root=inflater.inflate(R.layout.fragment_profileback, container, false);
            nickname=root.findViewById(R.id.usernick);
        }
        Filament.init();
//        AppBarLayout abl=getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        firebaseModel.initAll();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("3d models").child("untitled.glb");
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                nicknameCallback=nick;
            }
        });



        return root;
    }


    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseModel.initAll();
        switch (type) {
            case "user":
//                Bundle b = getActivity().getIntent().getExtras();
//                try {
//                    String[] backgroundColors = b.getString("backgroundColor").split(" ");
//                    backgroundColor[0] = Float.parseFloat(backgroundColors[0]);
//                    backgroundColor[1] = Float.parseFloat(backgroundColors[1]);
//                    backgroundColor[2] = Float.parseFloat(backgroundColors[2]);
//                    backgroundColor[3] = Float.parseFloat(backgroundColors[3]);
//                } catch (Exception ex) {
//                    // Assuming default background color
//                }

                ///////////////////////// set nickname /////////////////////
                nickname.setText(userInformation.getNick());
                //////////////////////////////////////////////////
                ImageView imageView = view.findViewById(R.id.settingsIcon);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(SettingsFragment.newInstance("user",fragment,userInformation,bundle));
                    }
                });
                newLook=view.findViewById(R.id.newLook);
                newLook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance("user",fragment,userInformation,bundle,"newlook"), getActivity());
                    }
                });
                ///////// I want GM on CF
                ImageView arrowtowardrobe = view.findViewById(R.id.arrowtowardrobe);
                arrowtowardrobe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(WardrobeFragment.newInstance("user",fragment,userInformation,bundle), getActivity());
                    }
                });
                TextView editing = view.findViewById(R.id.redact);
                editing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(EditingFragment.newInstance("user",fragment,userInformation,bundle));
                    }
                });
                if (bundle!=null){
                    tabLayoutPosition=bundle.getInt("TAB_INT_PROFILE");
                }
                editMainLook=view.findViewById(R.id.edit_main_look);
                editMainLook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance("user",fragment,userInformation,bundle,"mainlook"),getActivity());
                    }
                });
                //////////////////////////////
                viewPager=view.findViewById(R.id.viewPager);
                tabLayout=view.findViewById(R.id.tabsprofile);

                FragmentManager fm = getChildFragmentManager();
                fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
                viewPager.setAdapter(fragmentAdapter);
                viewPager.setCurrentItem(tabLayoutPosition, false);

                tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.looks)));
                tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.clothes)));

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

                biographyTextView=view.findViewById(R.id.biography);
                if(userInformation.getBio().length()==0){
                    biographyTextView.setText(getContext().getResources().getText(R.string.addadescription));
                }else {
                    biographyTextView.setText(userInformation.getBio());
                }
                looksCount=view.findViewById(R.id.looksCount);
                subscriptionsCount=view.findViewById(R.id.subscriptionsCount);
                subscribersCount=view.findViewById(R.id.subscribersCount);
                setCounts();
                linearLooksProfile=view.findViewById(R.id.linearLooksProfile);
                linearSubscribersProfile=view.findViewById(R.id.subscribersLinearProfile);
                linearSubscriptionsProfile=view.findViewById(R.id.subscriptionLinearProfile);
                linearSubscribersProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscriberFragment.newInstance("user",fragment,userInformation,bundle), getActivity());
                    }
                });
                linearSubscriptionsProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscriptionsFragment.newInstance("user",fragment,userInformation,bundle), getActivity());
                    }
                });
                ////////////////WARDROBE/////////////
                TextView texttowardrobe = view.findViewById(R.id.shielf);
                texttowardrobe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(WardrobeFragment
                                .newInstance("user",fragment,userInformation,bundle), getActivity());
                    }
                });
                wardrobeRecycler=view.findViewById(R.id.recyclerProfileToWardrobe);
                noClothes=view.findViewById(R.id.noClothesText);
                buyClothesProfile=view.findViewById(R.id.buyClothesProfile);
                itemClickListenerWardrobe=new WardrobeAdapterProfile.ItemClickListener() {
                    @Override
                    public void onItemClick(Clothes clothes) {
                        RecentMethods.setCurrentFragment(WardrobeFragment.newInstance("user",fragment,userInformation,bundle), getActivity());
                    }
                };
                checkWardrobe();
                //////////////////////////////////////

                handler = new Handler(getMainLooper());
                //      scene = new SceneLoader(this);
                //               scene.init(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2FSciFiHelmet.gltf?alt=media&token=a82512c1-14bf-4faf-8f67-abeb70da7697"));
                mainLook=view.findViewById(R.id.mainlookview);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Models");
                StorageReference islandRef = storageReference.child("models/untitled.gltf");
                File localFile = null;
                try {
                    localFile = File.createTempFile("model", ".gltf");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"), mainLook, ProfileFragment.this, 0.25f);
                loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"), mainLook, ProfileFragment.this, 0.5f);



                break;

            case "other":
                nickname.setText(sendNick);
                circularProgressIndicator=view.findViewById(R.id.profileIndicator);
                back=view.findViewById(R.id.back);
                moreSquare=view.findViewById(R.id.moresquare);
                back.setOnClickListener(new View.OnClickListener() {
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
                if(getActivity()!=null){
                    getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
                }
                if (bundle!=null){
                    tabLayoutPositionOther=bundle.getInt("TAB_INT_PROFILE_OTHER");
                }
                otherLooksCount = view.findViewById(R.id.looksCountOther);
                otherSubscriptionCount = view.findViewById(R.id.subscriptionCountOther);
                otherSubscribersCount = view.findViewById(R.id.subsCountOther);
                if(bundle!=null){
                    if(bundle.getSerializable(sendNick+"PROFILE_OTHER_BUNDLE")!=null){
                        info= (UserInformation) bundle.getSerializable(sendNick+"PROFILE_OTHER_BUNDLE");
                        b=1;
                        user = firebaseModel.getUsersReference().child(info.getNick());
                        if(bundle.getString(sendNick+"PROFILE_OTHER_CHECK_VALUE")==null){
                            firebaseModel.getUsersReference().child(info.getNick())
                                    .child("blackList").child(userInformation.getNick())
                                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DataSnapshot snapshot=task.getResult();
                                        if(snapshot.exists()){
                                            profileCheckValue=1;
                                        }else {
                                            profileCheckValue=2;
                                            if(info.getAccountType().equals("close")){
                                                profileCheckValue=3;
                                            }
                                        }
                                        tabLayoutOther=view.findViewById(R.id.tabsprofileother);
                                        viewPagerOther=view.findViewById(R.id.viewPagerOther);
                                        setFragmentOtherViewPager(profileCheckValue);
                                        setCountsOther();
                                        loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7")
                                                , otherMainLook, ProfileFragment.this, 0.25f);
                                        firebaseModel.getUsersReference().child(userInformation.getNick()).child("subscription")
                                                .child(info.getNick()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    checkOnSubscribeValue=1;
                                                }else {
                                                    checkOnSubscribeValue=0;
                                                }
                                                bundle.putString(sendNick+"PROFILE_OTHER_CHECK_VALUE",String.valueOf(profileCheckValue));
                                                bundle.putString(sendNick+"PROFILE_OTHER_CHECK_SUBSCRIBE_VALUE",String.valueOf(checkOnSubscribeValue));
                                                checkProfileValue(profileCheckValue,view,checkOnSubscribeValue);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });
                        }else{
                            tabLayoutOther=view.findViewById(R.id.tabsprofileother);
                            viewPagerOther=view.findViewById(R.id.viewPagerOther);
                            setCountsOther();
                            loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7")
                                    , otherMainLook, ProfileFragment.this, 0.25f);
                            if(bundle.getString(sendNick+"PROFILE_OTHER_CHECK_SUBSCRIBE_VALUE")!=null){
                                checkProfileValue(Integer.valueOf(bundle.getString(sendNick+"PROFILE_OTHER_CHECK_VALUE")),
                                        view,Integer.valueOf(bundle.getString(sendNick+"PROFILE_OTHER_CHECK_SUBSCRIBE_VALUE")));
                                setFragmentOtherViewPager(Integer.valueOf(bundle.getString(sendNick+"PROFILE_OTHER_CHECK_VALUE")));
                            }else{
                                firebaseModel.getUsersReference().child(userInformation.getNick()).child("subscription")
                                        .child(info.getNick()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            checkOnSubscribeValue=1;
                                        }else {
                                            checkOnSubscribeValue=0;
                                        }
                                        bundle.putString(sendNick+"PROFILE_OTHER_CHECK_VALUE",String.valueOf(profileCheckValue));
                                        bundle.putString(sendNick+"PROFILE_OTHER_CHECK_SUBSCRIBE_VALUE",String.valueOf(checkOnSubscribeValue));
                                        checkProfileValue(profileCheckValue,view,checkOnSubscribeValue);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }else{
                        firebaseModel.getReference().child("users").child(sendNick)
                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    b=2;
                                    DataSnapshot snapshot=task.getResult();
                                    if(bundle!=null){
                                        info=new UserInformation();
                                        info.setAge(snapshot.child("age").getValue(Long.class));
                                        info.setAvatar(snapshot.child("avatar").getValue(String.class));
                                        info.setGender(snapshot.child("gender").getValue(String.class));
                                        info.setNick(snapshot.child("nick").getValue(String.class));
                                        info.setPassword(snapshot.child("password").getValue(String.class));
                                        info.setPhone(snapshot.child("phone").getValue(String.class));
                                        info.setUid(snapshot.child("uid").getValue(String.class));
                                        info.setQueue(snapshot.child("queue").getValue(String.class));
                                        info.setAccountType(snapshot.child("accountType").getValue(String.class));
                                        info.setBio(snapshot.child("bio").getValue(String.class));
                                        bundle.putSerializable(sendNick+"PROFILE_OTHER_BUNDLE", (Serializable) info);
                                        firebaseModel.getUsersReference().child(info.getNick())
                                                .child("blackList").child(userInformation.getNick())
                                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DataSnapshot snapshot=task.getResult();
                                                    if(snapshot.exists()){
                                                        profileCheckValue=1;
                                                    }else {
                                                        profileCheckValue=2;
                                                        if(info.getAccountType().equals("close")){
                                                            profileCheckValue=3;
                                                        }
                                                    }
                                                    tabLayoutOther=view.findViewById(R.id.tabsprofileother);
                                                    viewPagerOther=view.findViewById(R.id.viewPagerOther);
                                                    setFragmentOtherViewPager(profileCheckValue);
                                                    setCountsOther();
                                                    loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7")
                                                            , otherMainLook, ProfileFragment.this, 0.25f);
                                                    firebaseModel.getUsersReference().child(userInformation.getNick()).child("subscription")
                                                            .child(info.getNick()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.exists()){
                                                                checkOnSubscribeValue=1;
                                                            }else {
                                                                checkOnSubscribeValue=0;
                                                            }
                                                            bundle.putString(sendNick+"PROFILE_OTHER_CHECK_VALUE",String.valueOf(profileCheckValue));
                                                            bundle.putString(sendNick+"PROFILE_OTHER_CHECK_SUBSCRIBE_VALUE",String.valueOf(checkOnSubscribeValue));
                                                            checkProfileValue(profileCheckValue,view,checkOnSubscribeValue);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
                break;
            case "userback":

                nickname.setText(userInformation.getNick());

                ImageView imageView1 = view.findViewById(R.id.settingsIcon);
                imageView1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(SettingsFragment.newInstance("userback",fragment,userInformation,bundle));
                    }
                });
                back=view.findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(fragment, getActivity());
                    }
                });
                OnBackPressedCallback callbackUserBack = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        RecentMethods.setCurrentFragment(fragment, getActivity());
                    }
                };

                requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callbackUserBack);

                if (bundle!=null){
                    tabLayoutPosition=bundle.getInt("TAB_INT_PROFILE");
                }
                newLook=view.findViewById(R.id.newLook);
                newLook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance("userback",fragment,userInformation,bundle,"newlook"), getActivity());
                    }
                });
                ///////// I want GM on CF
                ImageView arrowtowardrobe1 = view.findViewById(R.id.arrowtowardrobe);
                arrowtowardrobe1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(WardrobeFragment.newInstance("userback",fragment,userInformation,bundle), getActivity());
                    }
                });

                editMainLookBack=view.findViewById(R.id.edit_main_look_back);
                editMainLookBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance("userback",fragment,userInformation,bundle,"mainlook"),getActivity());
                    }
                });

                TextView editing1 = view.findViewById(R.id.redact);
                editing1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((MainActivity) getActivity()).setCurrentFragment(EditingFragment.newInstance("userback",fragment,userInformation,bundle));
                    }
                });
                //////////////////////////////
                viewPager=view.findViewById(R.id.viewPager);
                tabLayout=view.findViewById(R.id.tabsprofile);

                FragmentManager fm1 = getChildFragmentManager();
                fragmentAdapter = new FragmentAdapter(fm1, getLifecycle());
                viewPager.setAdapter(fragmentAdapter);
                viewPager.setCurrentItem(tabLayoutPosition, false);

                tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.looks)));
                tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.clothes)));

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

                biographyTextView=view.findViewById(R.id.biography);
                biographyTextView=view.findViewById(R.id.biography);
                if(userInformation.getBio().length()==0){
                    biographyTextView.setText("Добавь описание!");
                }else {
                    biographyTextView.setText(userInformation.getBio());
                }
                looksCount=view.findViewById(R.id.looksCount);
                subscriptionsCount=view.findViewById(R.id.subscriptionsCount);
                subscribersCount=view.findViewById(R.id.subscribersCount);
                setCounts();
                linearLooksProfile=view.findViewById(R.id.linearLooksProfile);
                linearSubscribersProfile=view.findViewById(R.id.subscribersLinearProfile);
                linearSubscriptionsProfile=view.findViewById(R.id.subscriptionLinearProfile);
                linearSubscribersProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscriberFragment.newInstance("userback",fragment,userInformation,bundle), getActivity());
                    }
                });
                linearSubscriptionsProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscriptionsFragment.newInstance("userback",fragment,userInformation,bundle), getActivity());
                    }
                });
                ////////////////WARDROBE/////////////
                TextView texttowardrobe1 = view.findViewById(R.id.shielf);
                texttowardrobe1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(WardrobeFragment
                                .newInstance("userback",fragment,userInformation,bundle), getActivity());
                    }
                });
                wardrobeRecycler=view.findViewById(R.id.recyclerProfileToWardrobe);
                noClothes=view.findViewById(R.id.noClothesText);
                buyClothesProfile=view.findViewById(R.id.buyClothesProfile);
                itemClickListenerWardrobe=new WardrobeAdapterProfile.ItemClickListener() {
                    @Override
                    public void onItemClick(Clothes clothes) {
                        RecentMethods.setCurrentFragment(WardrobeFragment.newInstance("userback",fragment,userInformation,bundle), getActivity());
                    }
                };
                checkWardrobe();
                //////////////////////////////////////

                handler = new Handler(getMainLooper());
                //      scene = new SceneLoader(this);
                //               scene.init(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2FSciFiHelmet.gltf?alt=media&token=a82512c1-14bf-4faf-8f67-abeb70da7697"));
                mainLook=view.findViewById(R.id.mainlookview);
                StorageReference storageReference1 = FirebaseStorage.getInstance().getReference().child("Models");
                StorageReference islandRef1 = storageReference1.child("models/untitled.gltf");
                File localFile1 = null;
                try {
                    localFile1 = File.createTempFile("model", ".gltf");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                islandRef1.getFile(localFile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"), mainLook, ProfileFragment.this, 0.25f);

                break;

        }
    }



    private void AcceptChatRequest() {
        firebaseModel.getUsersReference().child(info.getNick()).child("Chats").child(userInformation.getNick()).child("nick").setValue(userInformation.getNick());
        firebaseModel.getUsersReference().child(userInformation.getNick()).child("Chats").child(info.getNick()).child("nick").setValue(userInformation.getNick());
    }

    public void checkProfile(View view){
        firebaseModel.getUsersReference().child(info.getNick())
                .child("blackList").child(userInformation.getNick())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    firebaseModel.getUsersReference().child(info.getNick())
                            .child("accountType").get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DataSnapshot snapshot1=task.getResult();
                                        if(snapshot1.getValue(String.class).equals("close")){
                                            profileCheckValue=3;
                                        }else if(snapshot.exists()){
                                            profileCheckValue=1;
                                        }else if(!snapshot.exists() && !snapshot1.getValue(String.class).equals("close")){
                                            profileCheckValue=2;
                                        }
                                        firebaseModel.getUsersReference().child(userInformation.getNick()).child("subscription")
                                                .child(info.getNick()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    checkOnSubscribeValue=1;
                                                }else {
                                                    checkOnSubscribeValue=0;
                                                }
                                                setFragmentOtherViewPager(profileCheckValue);
                                                checkProfileValue(profileCheckValue,view,checkOnSubscribeValue);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }

    public void checkProfileAfterQuit(){
        firebaseModel.getUsersReference().child(info.getNick())
                .child("blackList").child(userInformation.getNick())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    if(snapshot.exists()){
                        profileCheckValue=1;
                    }else {
                        firebaseModel.getUsersReference().child(info.getNick())
                                .child("accountType").get()
                                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DataSnapshot snapshot1=task.getResult();
                                            if(snapshot1.getValue(String.class).equals("close")){
                                                profileCheckValue=3;
                                            }else {
                                                profileCheckValue=2;
                                            }
                                            info.setAccountType(snapshot1.getValue(String.class));
                                            bundle.putSerializable(sendNick+"PROFILE_OTHER_BUNDLE", (Serializable) info);
                                            bundle.putString(sendNick+"PROFILE_OTHER_CHECK_VALUE",String.valueOf(profileCheckValue));
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    public void setCounts(){
        if(userInformation.getSubscription()==null){
            RecentMethods.getSubscriptionList(userInformation.getNick(), firebaseModel, new Callbacks.getFriendsList() {
                @Override
                public void getFriendsList(ArrayList<Subscriber> friends) {
                    subscriptionsCountString=String.valueOf(friends.size());
                    checkCounts(subscriptionsCountString,subscriptionsCount);
                }
            });
        }else {
            subscriptionsCountString=String.valueOf(userInformation.getSubscription().size());
            checkCounts(subscriptionsCountString,subscriptionsCount);
        }
        if(userInformation.getSubscribers()==null){
            RecentMethods.getSubscribersList(userInformation.getNick(), firebaseModel, new Callbacks.getSubscribersList() {
                @Override
                public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                    subscribersCountString=String.valueOf(subscribers.size());
                    checkCounts(subscribersCountString,subscribersCount);
                }
            });
        }else{
            subscribersCountString=String.valueOf(userInformation.getSubscribers().size());
            checkCounts(subscribersCountString,subscribersCount);
        }
        if(userInformation.getLooks()==null){
            RecentMethods.getLooksList(userInformation.getNick(), firebaseModel, new Callbacks.getLooksList() {
                @Override
                public void getLooksList(ArrayList<NewsItem> look) {
                    userInformation.setLooks(look);
                    looksCount.setText(String.valueOf(look.size()));
                }
            });
        }else {
            looksCount.setText(String.valueOf(userInformation.getLooks().size()));
        }
    }

    public void showDialog(View view) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_layout_blacklist);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView complainTitle = dialog.findViewById(R.id.complainText);
        RelativeLayout no=dialog.findViewById(R.id.no);
        RelativeLayout yes=dialog.findViewById(R.id.yes);

        complainTitle.setText(getContext().getResources().getText(R.string.blockUser)+info.getNick()+"?");

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseModel.getUsersReference().child(userInformation.getNick())
                        .child("blackList").child(info.getNick())
                        .setValue(info.getNick());
                subscribeClose.setText(getContext().getResources().getText(R.string.unlock));
                subscribeClose.setTextColor(Color.parseColor("#F3A2E5"));
                subscribeClose.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                subscribe.setText(getContext().getResources().getText(R.string.unlock));
                subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                firebaseModel.getUsersReference().child(userInformation.getNick())
                        .child("subscription").child(info.getNick()).removeValue();
                firebaseModel.getUsersReference().child(info.getNick())
                        .child("subscribers").child(userInformation.getNick()).removeValue();
                firebaseModel.getUsersReference().child(info.getNick())
                        .child("subscription").child(userInformation.getNick()).removeValue();
                firebaseModel.getUsersReference().child(userInformation.getNick())
                        .child("subscribers").child(info.getNick()).removeValue();
                firebaseModel.getUsersReference().child(userInformation.getNick())
                        .child("requests").child(info.getNick()).removeValue();
                firebaseModel.getUsersReference().child(info.getNick())
                        .child("requests").child(userInformation.getNick()).removeValue();
                for (int i=0;i<info.getSubscribers().size();i++){
                    Subscriber subscriber=info.getSubscribers().get(i);
                    if(subscriber.getSub().equals(userInformation.getNick())){
                        if(b==1){
                            checkCounts(String.valueOf(info.getSubscribers().size()-1), otherSubscribersCount);
                        }
                    }
                }
                Toast.makeText(getContext(), getContext().getResources().getText(R.string.useraddedtoblacklist), Toast.LENGTH_SHORT).show();
                checkProfile(view);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void checkCounts(String countString, TextView textView){
        if(Long.valueOf(countString)<1000){
            textView.setText(countString);
        }else if(Long.valueOf(countString)>1000
                && Long.valueOf(countString)<10000){
            textView.setText(countString.substring(0, 1)+"."+countString.substring(1, 2)+"K");
        }
        else if(Long.valueOf(countString)>10000 &&
                Long.valueOf(countString)<100000){
            textView.setText(countString.substring(0, 2)+"."+countString.substring(2,3)+"K");
        }
        else if(Long.valueOf(countString)>10000 &&
                Long.valueOf(countString)<100000){
            textView.setText(countString.substring(0, 2)+"."+countString.substring(2,3)+"K");
        }else if(Long.valueOf(countString)>100000 &&
                Long.valueOf(countString)<1000000){
            textView.setText(countString.substring(0, 3)+"K");
        }
        else if(Long.valueOf(countString)>1000000 &&
                Long.valueOf(countString)<10000000){
            textView.setText(countString.substring(0, 1)+"KK");
        }
        else if(Long.valueOf(countString)>10000000 &&
                Long.valueOf(countString)<100000000){
            textView.setText(countString.substring(0, 2)+"KK");
        }
    }


    public void checkWardrobe(){
        if(userInformation.getClothes()==null){
            RecentMethods.getClothesInWardrobe(userInformation.getNick(), firebaseModel, new Callbacks.GetClothes() {
                @Override
                public void getClothes(ArrayList<Clothes> allClothes) {
                    if(allClothes.size()==0){
                        wardrobeRecycler.setVisibility(View.GONE);
                        noClothes.setVisibility(View.VISIBLE);
                        buyClothesProfile.setVisibility(View.VISIBLE);
                        buyClothesProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RecentMethods.setCurrentFragment(ShopFragment.newInstance(userInformation,bundle,ProfileFragment.newInstance(type, sendNick, fragment, userInformation, bundle)), getActivity());
                            }
                        });
                    }else {
                        Collections.reverse(allClothes);
                        WardrobeAdapterProfile wardrobeAdapter=new WardrobeAdapterProfile(allClothes,itemClickListenerWardrobe,getActivity());
                        wardrobeRecycler.setAdapter(wardrobeAdapter);
                    }
                }
            });
        }else{
            if(userInformation.getClothes().size()==0){
                wardrobeRecycler.setVisibility(View.GONE);
                noClothes.setVisibility(View.VISIBLE);
                buyClothesProfile.setVisibility(View.VISIBLE);
                buyClothesProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(ShopFragment.newInstance(userInformation,bundle,ProfileFragment.newInstance(type, sendNick, fragment, userInformation, bundle)), getActivity());
                    }
                });
            }else {
                Collections.reverse(userInformation.getClothes());
                WardrobeAdapterProfile wardrobeAdapter=new WardrobeAdapterProfile(userInformation.getClothes(),itemClickListenerWardrobe,getActivity());
                wardrobeRecycler.setAdapter(wardrobeAdapter);
            }
        }
    }

    public void setCountsOther() {
        if(info.getSubscription()==null){
            RecentMethods.getSubscriptionList(info.getNick(), firebaseModel, new Callbacks.getFriendsList() {
                @Override
                public void getFriendsList(ArrayList<Subscriber> friends) {
                    info.setSubscription(friends);
                    otherSubscriptionCountString=String.valueOf(friends.size());
                    checkCounts(otherSubscriptionCountString, otherSubscriptionCount);
                }
            });
        }else {
            otherSubscriptionCountString=String.valueOf(info.getSubscription().size());
            checkCounts(otherSubscriptionCountString, otherSubscriptionCount);
        }
        if(info.getSubscribers()==null){
            RecentMethods.getSubscribersList(info.getNick(), firebaseModel, new Callbacks.getSubscribersList() {
                @Override
                public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                    info.setSubscribers(subscribers);
                    otherSubscribersCountString=String.valueOf(subscribers.size());
                    checkCounts(otherSubscribersCountString, otherSubscribersCount);
                }
            });
        }else{
            otherSubscribersCountString=String.valueOf(info.getSubscribers().size());
            checkCounts(otherSubscribersCountString, otherSubscribersCount);
        }
        if(info.getLooks()==null){
            Query query2=firebaseModel.getUsersReference().child(info.getNick()).
                    child("looks");
            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<NewsItem> lookList = new ArrayList<>();
                    for (DataSnapshot snap:snapshot.getChildren()){
                        NewsItem newsItem=new NewsItem();
                        newsItem.setImageUrl(snap.child("ImageUrl").getValue(String.class));
                        newsItem.setLookPrice(snap.child("lookPrice").getValue(Long.class));
                        newsItem.setItem_description(snap.child("item_description").getValue(String.class));
                        newsItem.setNewsId(snap.child("newsId").getValue(String.class));
                        newsItem.setLikesCount(snap.child("likes_count").getValue(String.class));
                        newsItem.setViewCount(snap.child("viewCount").getValue(Long.class));
                        newsItem.setPostTime(snap.child("postTime").getValue(String.class));
                        newsItem.setNick(snap.child("nick").getValue(String.class));
                        lookList.add(newsItem);
                    }
                    info.setLooks(lookList);
                    otherLooksCount.setText(String.valueOf(lookList.size()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            otherLooksCount.setText(String.valueOf(info.getLooks().size()));
        }
    }

    public void checkProfileValue(int checkValue,View view,int subscribeValue){
        if (checkValue != 0) {
            circularProgressIndicator.setVisibility(View.GONE);
            otherUserBiography = view.findViewById(R.id.otheruserbiography);
            subscribeClose = view.findViewById(R.id.subscribeClose);
            otherMainLook = view.findViewById(R.id.mainlookview);
            otherUserBiography.setText(info.getBio());
            subscribe = view.findViewById(R.id.addFriend);
            subscribeFirst = view.findViewById(R.id.SubscribeFirst);
            closeAccount = view.findViewById(R.id.closeAccount);
            blockedAccount = view.findViewById(R.id.blockedAccount);
            message=view.findViewById(R.id.message);
            if (checkValue == 2 || subscribeValue==1) {
                message.setVisibility(View.VISIBLE);
                if (message != null) {
                    message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            open();
                        }
                    });
                }
                closeAccount.setVisibility(View.GONE);
                subscribeFirst.setVisibility(View.GONE);
                blockedAccount.setVisibility(View.GONE);
                subscribe.setVisibility(View.VISIBLE);
                subscribeClose.setVisibility(View.GONE);
                closeAccount.setVisibility(View.GONE);
                subscribeFirst.setVisibility(View.GONE);
                tabLayoutOther.setVisibility(View.VISIBLE);
                viewPagerOther.setVisibility(View.VISIBLE);

                moreSquare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialog(view);
                    }
                });
                linearSubscriptions = view.findViewById(R.id.subscriptionLinear);
                linearSubscribers = view.findViewById(R.id.subscribersLinear);
                linearSubscriptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscriptionsFragmentOther.newInstance(fragment, info.getNick(), userInformation,bundle), getActivity());
                    }
                });
                linearSubscribers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscribesFragmentOther.newInstance(fragment, info.getNick(), userInformation,bundle), getActivity());
                    }
                });

                if (subscribeValue==1) {
                    subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                    subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                    subscribe.setText(getContext().getResources().getText(R.string.unsubscride));
                } else {
                    subscribe.setText(getContext().getResources().getText(R.string.subscride));
                    subscribe.setTextColor(Color.parseColor("#FFFEFE"));
                    subscribe.setBackgroundResource(R.drawable.corners10dpappcolor);
                }
                for(int i=0;i<userInformation.getBlackList().size();i++){
                    Subscriber sub=userInformation.getBlackList().get(i);
                    if(sub.getSub().equals(info.getNick())){
                        a=5;
                        subscribe.setText(getContext().getResources().getText(R.string.unlock));
                        subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                        subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                    }
                }
                subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setClickSubscribe(view);
                    }
                });
            } else if (checkValue == 3) {
                subscribeClose.setVisibility(View.VISIBLE);
                closeAccount.setVisibility(View.VISIBLE);
                subscribeFirst.setVisibility(View.VISIBLE);
                blockedAccount.setVisibility(View.GONE);
                tabLayoutOther.setVisibility(View.GONE);
                viewPagerOther.setVisibility(View.GONE);
                subscribeFirst.setText(getContext().getResources().getText(R.string.subscribeto)+ info.getNick() + " !");
                message.setVisibility(View.GONE);
                subscribe.setVisibility(View.GONE);
                moreSquare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialog(view);
                    }
                });
                for(int i=0;i<userInformation.getBlackList().size();i++){
                    Subscriber sub=userInformation.getBlackList().get(i);
                    if(sub.getSub().equals(info.getNick())){
                        a=5;
                        subscribeClose.setText(getContext().getResources().getText(R.string.unlock));
                        subscribeClose.setTextColor(Color.parseColor("#F3A2E5"));
                        subscribeClose.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                    }
                }
                firebaseModel.getUsersReference().child(info.getNick())
                        .child("requests").child(userInformation.getNick()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            a = 3;
                            subscribeClose.setText(getContext().getResources().getText(R.string.requested));
                            subscribeClose.setTextColor(Color.parseColor("#F3A2E5"));
                            subscribeClose.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                        }else{
                            subscribeClose.setText(getContext().getResources().getText(R.string.subscride));
                            subscribeClose.setTextColor(Color.parseColor("#FFFEFE"));
                            subscribeClose.setBackgroundResource(R.drawable.corners10dpappcolor);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                subscribeClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (a == 3) {
                            firebaseModel.getUsersReference().child(info.getNick())
                                    .child("requests").child(userInformation.getNick()).removeValue();
                            a = 0;
                            checkProfile(view);
                        } else if(a!=3 && a!=5){
                            firebaseModel.getReference().child("users").child(info.getNick()).child("requests")
                                    .child(userInformation.getNick()).setValue(userInformation.getNick());
                            String numToBase = firebaseModel.getReference().child("users")
                                    .child(info.getNick()).child("nontifications")
                                    .push().getKey();
                            firebaseModel.getReference().child("users")
                                    .child(info.getNick()).child("nontifications")
                                    .child(numToBase).setValue(new Nontification(userInformation.getNick(), "не отправлено", "запрос"
                                    , "", " ", " ", "не просмотрено", numToBase, 0));
                            a = 0;
                            checkProfile(view);
                        }else if(a==5){
                            firebaseModel.getUsersReference().child(userInformation.getNick()).child("blackList")
                                    .child(info.getNick()).removeValue();
                            a = 0;
                            checkProfile(view);
                        }
                    }
                });
            } else if (checkValue == 1) {
                Log.d("###", "fggg");
                subscribeClose.setVisibility(View.VISIBLE);
                subscribeClose.setBackgroundResource(R.drawable.corners10grey);
                subscribeClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkProfile(view);
                    }
                });
                subscribeClose.setTextColor(Color.parseColor("#FEFEFE"));
                blockedAccount.setVisibility(View.VISIBLE);
                blockedAccount.setText(info.getNick() +getContext().getResources().getString(R.string.blockedyou));
                message.setVisibility(View.GONE);
                tabLayoutOther.setVisibility(View.GONE);
                viewPagerOther.setVisibility(View.GONE);
                closeAccount.setVisibility(View.GONE);
                subscribeFirst.setVisibility(View.GONE);
                subscribe.setVisibility(View.GONE);
                moreSquare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialog(view);
                    }
                });
            }
        }
    }

    public void setClickSubscribe(View view){
        firebaseModel.getUsersReference().child(userInformation.getNick())
                .child("subscription").child(info.getNick()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        a = 1;
                    } else {
                        a = 2;
                    }
                    firebaseModel.getUsersReference().child(info.getNick())
                            .child("requests").child(userInformation.getNick()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot snapshot = task.getResult();
                                if (snapshot.exists()) {
                                    a = 3;
                                }
                                firebaseModel.getUsersReference().child(info.getNick())
                                        .child("blackList").child(userInformation.getNick()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DataSnapshot snapshot = task.getResult();
                                            if (snapshot.exists()) {
                                                a = 4;
                                                v=1;
                                            }
                                            firebaseModel.getUsersReference().child(userInformation.getNick())
                                                    .child("blackList").child(info.getNick()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DataSnapshot snapshot = task.getResult();
                                                        if (snapshot.exists()) {
                                                            a = 5;
                                                        }
                                                        if (a != 0) {
                                                            firebaseModel.getUsersReference().child(info.getNick())
                                                                .child("accountType").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DataSnapshot snapshot1 = task.getResult();
                                                                            if (a == 2) {
                                                                                if (snapshot1.getValue(String.class).equals("open")) {
                                                                                    firebaseModel.getReference().child("users").child(userInformation.getNick()).child("subscription")
                                                                                            .child(info.getNick()).setValue(info.getNick());
                                                                                    firebaseModel.getReference().child("users").child(info.getNick()).child("subscribers")
                                                                                            .child(userInformation.getNick()).setValue(userInformation.getNick());
                                                                                    String numToBase = firebaseModel.getReference().child("users")
                                                                                            .child(info.getNick()).child("nontifications")
                                                                                            .push().getKey();
                                                                                    firebaseModel.getReference().child("users")
                                                                                            .child(info.getNick()).child("nontifications")
                                                                                            .child(numToBase).setValue(new Nontification(userInformation.getNick(), "не отправлено", "обычный"
                                                                                            , "", " ", " ", "не просмотрено", numToBase, 0));
                                                                                    if(b==1){
                                                                                        checkCounts(String.valueOf(info.getSubscribers().size()+1), otherSubscribersCount);
                                                                                    }
                                                                                    subscribe.setText(getContext().getResources().getText(R.string.unsubscride));
                                                                                    subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                                                                                    subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                                                                    a = 0;
                                                                                } else {
                                                                                    firebaseModel.getReference().child("users").child(info.getNick()).child("requests")
                                                                                            .child(userInformation.getNick()).setValue(userInformation.getNick());
                                                                                    String numToBase = firebaseModel.getReference().child("users")
                                                                                            .child(info.getNick()).child("nontifications")
                                                                                            .push().getKey();
                                                                                    firebaseModel.getReference().child("users")
                                                                                            .child(info.getNick()).child("nontifications")
                                                                                            .child(numToBase).setValue(new Nontification(userInformation.getNick(), "не отправлено", "запрос"
                                                                                            , "", " ", " ", "не просмотрено", numToBase, 0));
                                                                                    a = 0;
                                                                                    checkProfile(view);
                                                                                }
                                                                            }
                                                                            if (a == 1) {
                                                                                firebaseModel.getReference().child("users").child(userInformation.getNick()).child("subscription")
                                                                                        .child(info.getNick()).removeValue();
                                                                                firebaseModel.getReference().child("users").child(info.getNick()).child("subscribers")
                                                                                        .child(userInformation.getNick()).removeValue();
                                                                                if(b==1){
                                                                                    checkCounts(String.valueOf(info.getSubscribers().size()-1), otherSubscribersCount);
                                                                                }
                                                                                if(snapshot1.getValue(String.class).equals("open")){
                                                                                    subscribe.setText(getContext().getResources().getText(R.string.subscride));
                                                                                    subscribe.setTextColor(Color.parseColor("#FFFEFE"));
                                                                                    subscribe.setBackgroundResource(R.drawable.corners10dpappcolor);
                                                                                    a = 0;
                                                                                }else{
                                                                                    a = 0;
                                                                                    checkProfile(view);
                                                                                }
                                                                            }
                                                                            if (a == 3) {
                                                                                firebaseModel.getReference().child("users").child(info.getNick()).child("requests")
                                                                                        .child(userInformation.getNick()).removeValue();
                                                                                Log.d("#####","ss");
                                                                                a = 0;
                                                                                checkProfile(view);
                                                                            }
                                                                            if (a == 4) {
                                                                                Toast.makeText(getContext(), getContext().getResources().getText(R.string.theuserhasblockedyou), Toast.LENGTH_SHORT).show();
                                                                                a = 0;
                                                                                checkProfile(view);
                                                                            }
                                                                            if (a == 5) {
                                                                                firebaseModel.getUsersReference().child(userInformation.getNick()).child("blackList")
                                                                                        .child(info.getNick()).removeValue();
                                                                                if(snapshot1.getValue(String.class).equals("open") && v==0){
                                                                                    subscribe.setText(getContext().getResources().getText(R.string.subscride));
                                                                                    subscribe.setTextColor(Color.parseColor("#FFFEFE"));
                                                                                    subscribe.setBackgroundResource(R.drawable.corners10dpappcolor);
                                                                                    a = 0;
                                                                                }else{
                                                                                    a = 0;
                                                                                    checkProfile(view);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    public float[] getBackgroundColor() {
        return backgroundColor;
    }

//
//    @Override
//    public void onPause() {
//        super.onPause();
//        switch (type) {
//            case "user":
//                //loadModels(loadUrl);
//                mainLook.pause();
//                break;
//            case "other":
//                otherMainLook.pause();
//                break;
//        }
//
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        try {
//            switch (type) {
//                case "user":
//                    //loadModels(loadUrl);
//                    mainLook.resume();
//                    break;
//                case "other":
//                    otherMainLook.resume();
//                    break;
//            }
//
//
//        } catch (CameraNotAvailableException e) {
//            e.printStackTrace();
//        }
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadModels(Uri url, SceneView sceneView, Fragment fragment, float scale) {
        if(getContext()==null){

        }else {
            ModelRenderable.builder()
                    .setSource(
                            fragment.getContext(), new RenderableSource.Builder().setSource(
                                    fragment.getContext(),
                                    url,
                                    RenderableSource.SourceType.GLB
                            ).setScale(scale)
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
    }

    public void addNode(ModelRenderable modelRenderable, SceneView sceneView) {
        Node modelNode1 = new Node();
        modelNode1.setRenderable(modelRenderable);
        modelNode1.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));
//        modelNode1.setLocalRotation(Quaternion.multiply(
//                Quaternion.axisAngle(new Vector3(1f, 0f, 0f), 45),
//                Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 75)));
        modelNode1.setLocalPosition(new Vector3(0f, 0f, -0.9f));
        sceneView.getScene().addChild(modelNode1);
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @NonNull
        @Override
        public Fragment createFragment ( int position){
            switch (position) {
                case 1:
                    return new ClothesFragmentProfile(type,fragment,userInformation,bundle);
            }
            return new LooksFragmentProfile(type,fragment,userInformation,bundle);
        }


        @Override
        public int getItemCount() {
            return 2;
        }
    }

    public class FragmentAdapterOther extends FragmentStateAdapter {

        public FragmentAdapterOther(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @NonNull
        @Override
        public Fragment createFragment ( int position){

            switch (position) {
                case 1:
                    return new ClothesFragmentProfileOther(info.getNick(),fragment,userInformation,bundle);
            }
            return new LooksFragmentProfileOther(info.getNick(),fragment,userInformation,bundle);
        }


        @Override
        public int getItemCount() {
            return 2;
        }
    }

    public void setFragmentOtherViewPager(int checkValue){
        if(checkValue!=1 && checkValue!=3){
            if(getActivity()!=null && isAdded()){
                tabLayoutOther.setVisibility(View.VISIBLE);
                viewPagerOther.setVisibility(View.VISIBLE);
                FragmentManager fm = getChildFragmentManager();
                fragmentAdapterOther = new FragmentAdapterOther(fm, getLifecycle());
                viewPagerOther.setAdapter(fragmentAdapterOther);
                viewPagerOther.setCurrentItem(tabLayoutPositionOther, false);
            }


            if(tabLayoutOther.getTabCount()==2){

            }else {
                tabLayoutOther.addTab(tabLayoutOther.newTab().setText(getContext().getResources().getText(R.string.looks)));
                tabLayoutOther.addTab(tabLayoutOther.newTab().setText(getContext().getResources().getText(R.string.clothes)));
            }

            tabLayoutOther.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tabLayoutPositionOther=tab.getPosition();
                    viewPagerOther.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            tabLayoutOther.selectTab(tabLayoutOther.getTabAt(tabLayoutPositionOther));
            viewPagerOther.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    tabLayoutPositionOther=position;
                    tabLayoutOther.selectTab(tabLayoutOther.getTabAt(position));
                }
            });
        }
    }

    private void showBottomSheetDialog(View view) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_profile);

        TextView block,complain,deleteSubscriber,shareProfile;

        block=bottomSheetDialog.findViewById(R.id.block);
        complain=bottomSheetDialog.findViewById(R.id.complain);
        deleteSubscriber=bottomSheetDialog.findViewById(R.id.deleteSubscriber);
        shareProfile=bottomSheetDialog.findViewById(R.id.shareProfile);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(view);
            }
        });

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ComplainFragment.newInstance(info.getNick(),fragment,userInformation,bundle), getActivity());
                bottomSheetDialog.dismiss();
            }
        });

        deleteSubscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.getSubscription().size()>0){
                    for(int i=0;i<info.getSubscription().size();i++){
                        Subscriber subscriber=info.getSubscription().get(i);
                        if(subscriber.getSub().equals(userInformation.getNick())){
                            firebaseModel.getUsersReference().child(userInformation.getNick())
                                    .child("subscribers").child(info.getNick()).removeValue();
                            firebaseModel.getUsersReference().child(info.getNick())
                                    .child("subscription").child(userInformation.getNick()).removeValue();
                            if(b==1){
                                checkCounts(String.valueOf(info.getSubscription().size()-1), otherSubscriptionCount);
                            }
                            Toast.makeText(getContext(), getContext().getResources().getText(R.string.userremovedfromsubscribers), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), getContext().getResources().getText(R.string.theuserisnotfollowingyou), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(getContext(), getContext().getResources().getText(R.string.theuserisnotfollowingyou), Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialogSend();
            }
        });

        bottomSheetDialog.show();
    }

    private void showBottomSheetDialogSend() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        editText=bottomSheetDialog.findViewById(R.id.searchuser);
        recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyList=bottomSheetDialog.findViewById(R.id.emptySubscribersList);
        messageEdit=bottomSheetDialog.findViewById(R.id.message);

        itemClickListenerSendLookAdapter=new SendLookAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String otherUserNick, String type) {
                if(type.equals("send")){
                }else {
                }
            }
        };

        if(userInformation.getSubscription()==null){
            RecentMethods.getSubscriptionList(userInformation.getNick(), firebaseModel, new Callbacks.getFriendsList() {
                @Override
                public void getFriendsList(ArrayList<Subscriber> friends) {
                    if (friends.size()==0){
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        SendLookAdapter sendLookAdapter = new SendLookAdapter(friends,itemClickListenerSendLookAdapter);
                        recyclerView.setAdapter(sendLookAdapter);
                    }
                }
            });
        }else {
            if (userInformation.getSubscription().size()==0){
                emptyList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                SendLookAdapter sendLookAdapter = new SendLookAdapter(userInformation.getSubscription(),itemClickListenerSendLookAdapter);
                recyclerView.setAdapter(sendLookAdapter);
            }
        }

        initUserEnter();

        bottomSheetDialog.show();
    }


    public void initUserEnter() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userName = String.valueOf(editText.getText()).trim();
                userName = userName.toLowerCase();
                if(userInformation.getSubscription()==null){
                    Query query = firebaseModel.getUsersReference().child(userInformation.getNick()).child("subscription");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userFromBase = new ArrayList<>();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Subscriber subscriber = new Subscriber();
                                subscriber.setSub(snap.getValue(String.class));
                                String nick = subscriber.getSub();
                                int valueLetters = userName.length();
                                nick = nick.toLowerCase();
                                if (nick.length() < valueLetters) {
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                } else {
                                    nick = nick.substring(0, valueLetters);
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                }

                            }
                            if(userFromBase.size()==0){
                                emptyList.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else {
                                emptyList.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListenerSendLookAdapter);
                                recyclerView.setAdapter(sendLookAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    userFromBase=new ArrayList<>();
                    for (int s=0;s<userInformation.getSubscription().size();s++) {
                        Subscriber subscriber = userInformation.getSubscription().get(s);
                        String nick = subscriber.getSub();
                        int valueLetters = userName.length();
                        nick = nick.toLowerCase();
                        if (nick.length() < valueLetters) {
                            if (nick.equals(userName))
                                userFromBase.add(subscriber);
                        } else {
                            nick = nick.substring(0, valueLetters);
                            if (nick.equals(userName))
                                userFromBase.add(subscriber);
                        }

                    }
                    if(userFromBase.size()==0){
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        emptyList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListenerSendLookAdapter);
                        recyclerView.setAdapter(sendLookAdapter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}