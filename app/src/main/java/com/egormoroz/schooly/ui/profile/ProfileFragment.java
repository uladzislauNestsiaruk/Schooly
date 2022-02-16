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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeAdapterProfile;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeClothes;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeFragment;
import com.google.android.filament.Filament;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class ProfileFragment extends Fragment {
    private static String sendNickString;
    FirebaseModel firebaseModel = new FirebaseModel();
    Context profileContext, context;
    String type,nicknameCallback;
    UserInformation info;
    WardrobeAdapterProfile.ItemClickListener itemClickListenerWardrobe;
    TextView nickname,message,biographyTextView,looksCount,subscriptionsCount,subscribersCount,otherLooksCount,otherSubscriptionCount,
            otherSubscribersCount,otherUserBiography,subscribeClose,subscribe
            ,subscribeFirst,closeAccount,noClothes,buyClothesProfile,blockedAccount;
    DatabaseReference user;
    WardrobeAdapterProfile.ItemClickListener itemClickListener;
   // SceneLoader scene;
    LinearLayout linearLooks,linearSubscribers,linearSubscriptions;
   // ModelSurfaceView modelSurfaceView;
    SceneView mainLook, otherMainLook;
   // ModelRenderer modelRenderer;
    RecyclerView looksRecycler,wardrobeRecycler,looksRecyclerOther;
    ImageView moreSquare,back,newLook;
    int profileValue;
    String sendNick;
    Fragment fragment;
    ViewPager2 viewPager,viewPagerOther;
    FragmentAdapter fragmentAdapter;
    FragmentAdapterOther fragmentAdapterOther;
    TabLayout tabLayout,tabLayoutOther;
    private float[] backgroundColor = new float[]{0f, 0f, 0f, 1.0f};
    private Handler handler;
    int a,profileCheckValue;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        profileContext = context;
    }

    public ProfileFragment(String type, String sendNick,Fragment fragment) {
        this.type = type;
        this.sendNick=sendNick;
        this.fragment=fragment;
    }

    public static ProfileFragment newInstance(String type, String sendNick,Fragment fragment) {
        return new ProfileFragment(type, sendNick,fragment);
    }


    public void open() {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                {
                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.child(nick).exists()) {
                                AcceptChatRequest();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                Intent i = new Intent(getActivity(), ChatActivity.class);
                //Getting information about user(friend)
                i.putExtra("othUser", info.getNick());
                i.putExtra("curUser", nick);
                i.putExtra("groupName", "one");
                i.putExtra("visit_user_id", info.getUid());
                i.putExtra("visit_image", ChatActivity.class);
                startActivity(i);
            }
        });
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View root = type.equals("user") ? inflater.inflate(R.layout.fragment_profile, container, false) :
                inflater.inflate(R.layout.fragment_otheruser, container, false);
        Filament.init();
//        AppBarLayout abl=getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        firebaseModel.initAll();
        nickname = type.equals("user") ? root.findViewById(R.id.usernick) :
                root.findViewById(R.id.otherusernick);
        message = type.equals("user") ? null :
                root.findViewById(R.id.message);
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
                Bundle b = getActivity().getIntent().getExtras();
                try {
                    String[] backgroundColors = b.getString("backgroundColor").split(" ");
                    backgroundColor[0] = Float.parseFloat(backgroundColors[0]);
                    backgroundColor[1] = Float.parseFloat(backgroundColors[1]);
                    backgroundColor[2] = Float.parseFloat(backgroundColors[2]);
                    backgroundColor[3] = Float.parseFloat(backgroundColors[3]);
                } catch (Exception ex) {
                    // Assuming default background color
                }

                ///////////////////////// set nickname /////////////////////
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(),
                        firebaseModel,
                        new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                nickname.setText(nick);
                            }
                        });
                //////////////////////////////////////////////////
                ImageView imageView = view.findViewById(R.id.settingsIcon);
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(SettingsFragment.newInstance());
                    }
                });
                newLook=view.findViewById(R.id.newLook);
                newLook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(), getActivity());
                    }
                });
                ///////// I want GM on CF
                ImageView arrowtowardrobe = view.findViewById(R.id.arrowtowardrobe);
                arrowtowardrobe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(WardrobeFragment.newInstance(), getActivity());
                    }
                });
                TextView editing = view.findViewById(R.id.redact);
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.getBio(nick, firebaseModel, new Callbacks.GetBio() {
                            @Override
                            public void GetBiography(String bio) {
                                if(bio.equals(null)){
                                    editing.setText("Добавить описание");
                                }
                            }
                        });
                    }
                });
                editing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((MainActivity) getActivity()).setCurrentFragment(EditingFragment.newInstance());
                    }
                });
                //////////////////////////////
                viewPager=view.findViewById(R.id.viewPager);
                tabLayout=view.findViewById(R.id.tabsprofile);

                FragmentManager fm = getChildFragmentManager();
                fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
                viewPager.setAdapter(fragmentAdapter);

                tabLayout.addTab(tabLayout.newTab().setText("Образы"));
                tabLayout.addTab(tabLayout.newTab().setText("Одежда"));

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

                biographyTextView=view.findViewById(R.id.biography);
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.getBio(nicknameCallback, firebaseModel, new Callbacks.GetBio() {
                            @Override
                            public void GetBiography(String bio) {
                                biographyTextView.setText(bio);
                            }
                        });
                    }
                });
                looksCount=view.findViewById(R.id.looksCount);
                subscriptionsCount=view.findViewById(R.id.subscriptionsCount);
                subscribersCount=view.findViewById(R.id.subscribersCount);
                setCounts();
                subscribersCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscriberFragment.newInstance(), getActivity());
                    }
                });
                subscriptionsCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(SubscriptionsFragment.newInstance(), getActivity());
                    }
                });
                ////////////////WARDROBE/////////////
                TextView texttowardrobe = view.findViewById(R.id.shielf);
                texttowardrobe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(WardrobeFragment
                                .newInstance(), getActivity());
                    }
                });
                wardrobeRecycler=view.findViewById(R.id.recyclerProfileToWardrobe);
                noClothes=view.findViewById(R.id.noClothesText);
                buyClothesProfile=view.findViewById(R.id.buyClothesProfile);
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
                Query query1=firebaseModel.getReference().child("users").child(sendNick);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        info=new UserInformation();
                        info.setAge(snapshot.child("age").getValue(Long.class));
                        info.setAvatar(snapshot.child("avatar").getValue(String.class));
                        info.setGender(snapshot.child("gender").getValue(String.class));
                        //////////////////userData.setMiners();
                        info.setNick(snapshot.child("nick").getValue(String.class));
                        info.setPassword(snapshot.child("password").getValue(String.class));
                        info.setPhone(snapshot.child("phone").getValue(String.class));
                        info.setUid(snapshot.child("uid").getValue(String.class));
                        info.setQueue(snapshot.child("queue").getValue(String.class));
                        info.setAccountType(snapshot.child("accountType").getValue(String.class));
                        info.setBio(snapshot.child("bio").getValue(String.class));
                        nickname.setText(info.getNick());
                        sendNickString=info.getNick();
                        user = firebaseModel.getUsersReference().child(info.getNick());
                        otherUserBiography=view.findViewById(R.id.otheruserbiography);
                        subscribeClose=view.findViewById(R.id.subscribeClose);
                        back=view.findViewById(R.id.back);
                        back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RecentMethods.setCurrentFragment(fragment, getActivity());
                            }
                        });
                        otherMainLook = view.findViewById(R.id.mainlookview);
                        otherUserBiography.setText(info.getBio());
                        subscribe=view.findViewById(R.id.addFriend);
                        subscribeFirst=view.findViewById(R.id.SubscribeFirst);
                        closeAccount=view.findViewById(R.id.closeAccount);
                        tabLayoutOther=view.findViewById(R.id.tabsprofileother);
                        viewPagerOther=view.findViewById(R.id.viewPagerOther);
                        moreSquare=view.findViewById(R.id.moresquare);
                        otherLooksCount=view.findViewById(R.id.looksCountOther);
                        otherSubscriptionCount=view.findViewById(R.id.subscriptionCountOther);
                        otherSubscribersCount=view.findViewById(R.id.subsCountOther);
                        blockedAccount=view.findViewById(R.id.blockedAccount);
                        if (info.getAccountType().equals("open")){
                            checkOtherUserProfile();
                        }else {
                            profileCheckValue=3;
                        }
                        setCountsOther();
                        if (message != null) {
                            message.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    open();
                                }
                            });
                        }
                        loadModels(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"), otherMainLook, ProfileFragment.this, 0.25f);
                        Log.d("######", "v "+profileValue);
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                Query query=firebaseModel.getUsersReference().child(nick).child("subscription")
                                        .child(info.getNick());
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(profileCheckValue!=0){
                                        if (profileCheckValue==2 || snapshot.exists()) {
                                            otherLooksCount = view.findViewById(R.id.looksCountOther);
                                            otherSubscriptionCount = view.findViewById(R.id.subscriptionCountOther);
                                            otherSubscribersCount = view.findViewById(R.id.subsCountOther);
                                            closeAccount.setVisibility(View.GONE);
                                            subscribeFirst.setVisibility(View.GONE);
                                            blockedAccount.setVisibility(View.GONE);

                                            FragmentManager fm = getChildFragmentManager();
                                            fragmentAdapterOther = new FragmentAdapterOther(fm, getLifecycle());
                                            viewPagerOther.setAdapter(fragmentAdapterOther);

                                            Log.d("#####", "tab1");

                                            tabLayoutOther.addTab(tabLayoutOther.newTab().setText("Образы"));
                                            tabLayoutOther.addTab(tabLayoutOther.newTab().setText("Одежда"));

                                            tabLayoutOther.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                @Override
                                                public void onTabSelected(TabLayout.Tab tab) {
                                                    viewPagerOther.setCurrentItem(tab.getPosition());
                                                }

                                                @Override
                                                public void onTabUnselected(TabLayout.Tab tab) {

                                                }

                                                @Override
                                                public void onTabReselected(TabLayout.Tab tab) {

                                                }
                                            });


                                            viewPagerOther.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                                @Override
                                                public void onPageSelected(int position) {
                                                    tabLayoutOther.selectTab(tabLayoutOther.getTabAt(position));
                                                }
                                            });
                                            moreSquare.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    PopupMenu popup = new PopupMenu(getActivity(), moreSquare);
                                                    popup.getMenuInflater()
                                                            .inflate(R.menu.other_user_menu, popup.getMenu());

                                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                        public boolean onMenuItemClick(MenuItem item) {
                                                            String itemTitle= item.getTitle().toString().trim();

                                                            int itemID=item.getItemId();
                                                            switch(itemID){
                                                                case R.id.one :
                                                                    showDialog();
                                                                    return true;
                                                                case R.id.two:
                                                                    RecentMethods.setCurrentFragment(ComplainFragment.newInstance(info.getNick()), getActivity());
                                                                    return true;
                                                                case R.id.three:
                                                                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                                                        @Override
                                                                        public void PassUserNick(String nick) {
                                                                            Query query3=firebaseModel.getUsersReference().child(nick)
                                                                                    .child("subscribers").child(info.getNick());
                                                                            query3.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    if(snapshot.exists()){
                                                                                        Log.d("#####", "peace of shit");
                                                                                        firebaseModel.getUsersReference().child(nick)
                                                                                                .child("subscribers").child(info.getNick()).removeValue();
                                                                                        firebaseModel.getUsersReference().child(info.getNick())
                                                                                                .child("subscription").child(nick).removeValue();
                                                                                        Toast.makeText(getContext(), "Пользователь удален из подписчиков", Toast.LENGTH_SHORT).show();
                                                                                    }else {
                                                                                        Log.d("#####", "suck my dick");
                                                                                        Toast.makeText(getContext(), "Пользователь не подписан на тебя", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                    return true;
                                                            }
                                                            return true;
                                                        }
                                                    });

                                                    popup.show();
                                                }
                                            });
                                            linearSubscriptions = view.findViewById(R.id.subscriptionLinear);
                                            linearSubscribers = view.findViewById(R.id.subscribersLinear);
                                            linearSubscriptions.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    RecentMethods.setCurrentFragment(SubscriptionsFragmentOther.newInstance(), getActivity());
                                                }
                                            });
                                            linearSubscribers.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    RecentMethods.setCurrentFragment(SubscribesFragmentOther.newInstance(), getActivity());
                                                }
                                            });

                                            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                                @Override
                                                public void PassUserNick(String nick) {
                                                    Query query = firebaseModel.getUsersReference().child(nick).child("subscription")
                                                            .child(info.getNick());
                                                    query.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                                                subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                                                                subscribe.setText("Отписаться");
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            });

                                            subscribe.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                                        @Override
                                                        public void PassUserNick(String nick) {
                                                            Query query=firebaseModel.getUsersReference().child(nick)
                                                                    .child("subscription").child(info.getNick());
                                                            query.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        a=1;
                                                                        Log.d("#####", "c  "+a);

                                                                    }else{
                                                                        a=2;

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                            Query queryRequest=firebaseModel.getUsersReference().child(info.getNick())
                                                                    .child("requests").child(nick);
                                                            queryRequest.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        a=3;

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                            Log.d("#####", "ff  "+a);
                                                            if(a!=0) {
                                                                if (a == 2) {
                                                                    Log.d("#####", "ab  " + a);
                                                                    Query query1=firebaseModel.getUsersReference().child(info.getNick())
                                                                            .child("accountType");
                                                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if(snapshot.getValue(String.class).equals("open")){
                                                                                firebaseModel.getReference().child("users").child(nick).child("subscription")
                                                                                        .child(info.getNick()).setValue(info.getNick());
                                                                                firebaseModel.getReference().child("users").child(info.getNick()).child("subscribers")
                                                                                        .child(nick).setValue(nick);
                                                                                Random random = new Random();
                                                                                int num1 =random.nextInt(1000000000);
                                                                                int num2 =random.nextInt(1000000000);
                                                                                String numToBase=String.valueOf(num1+num2);
                                                                                firebaseModel.getReference().child("users")
                                                                                        .child(info.getNick()).child("nontifications")
                                                                                        .child(numToBase).setValue(new Nontification(nick,"не отправлено","обычный"
                                                                                        ,ServerValue.TIMESTAMP.toString()," "," ","не просмотрено",numToBase));
                                                                                subscribe.setText("Отписаться");
                                                                                subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                                                                                subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                                                                a=0;
                                                                            }else {
                                                                                firebaseModel.getReference().child("users").child(info.getNick()).child("requests")
                                                                                        .child(nick).setValue(nick);
                                                                                Random random = new Random();
                                                                                int num1 =random.nextInt(1000000000);
                                                                                int num2 =random.nextInt(1000000000);
                                                                                String numToBase=String.valueOf(num1+num2);
                                                                                firebaseModel.getReference().child("users")
                                                                                        .child(info.getNick()).child("nontifications")
                                                                                        .child(numToBase).setValue(new Nontification(nick,"не отправлено","запрос"
                                                                                        ,ServerValue.TIMESTAMP.toString()," "," ","не просмотрено",numToBase));
                                                                                subscribe.setText("Запрошено");
                                                                                subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                                                                                subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                                                                a=0;
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                                if (a == 1) {
                                                                    Log.d("#####", "one  " + a);
                                                                    firebaseModel.getReference().child("users").child(nick).child("subscription")
                                                                            .child(info.getNick()).removeValue();
                                                                    firebaseModel.getReference().child("users").child(info.getNick()).child("subscribers")
                                                                            .child(nick).removeValue();
                                                                    subscribe.setText("Подписаться");
                                                                    subscribe.setTextColor(Color.parseColor("#FFFEFE"));
                                                                    subscribe.setBackgroundResource(R.drawable.corners10dpappcolor);
                                                                    a=0;

                                                                }
                                                                if (a == 3) {
                                                                    firebaseModel.getReference().child("users").child(info.getNick()).child("requests")
                                                                            .child(nick).removeValue();
                                                                    subscribe.setText("Подписаться");
                                                                    subscribe.setTextColor(Color.parseColor("#FFFEFE"));
                                                                    subscribe.setBackgroundResource(R.drawable.corners10dpappcolor);
                                                                    a=0;

                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else if(profileCheckValue==3) {
                                            subscribeClose.setVisibility(View.VISIBLE);
                                            closeAccount.setVisibility(View.VISIBLE);
                                            subscribeFirst.setVisibility(View.VISIBLE);
                                            blockedAccount.setVisibility(View.GONE);
                                            tabLayoutOther.setVisibility(View.GONE);
                                            viewPagerOther.setVisibility(View.GONE);
                                            Log.d("#####", "tab2");
                                            subscribeFirst.setText("Подпишись на " + " " + info.getNick() + " !");
                                            message.setVisibility(View.GONE);
                                            subscribe.setVisibility(View.GONE);
                                            moreSquare.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    PopupMenu popup = new PopupMenu(getActivity(), moreSquare);
                                                    popup.getMenuInflater()
                                                            .inflate(R.menu.other_user_menu, popup.getMenu());

                                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                        public boolean onMenuItemClick(MenuItem item) {
                                                            String itemTitle= item.getTitle().toString().trim();

                                                            Log.d("####", "hell"+itemTitle);
                                                            int itemID=item.getItemId();
                                                            switch(itemID){
                                                                case R.id.one :
                                                                    showDialog();
                                                                    return true;
                                                                case R.id.two:
                                                                    RecentMethods.setCurrentFragment(ComplainFragment.newInstance(info.getNick()), getActivity());
                                                                    return true;
                                                                case R.id.three:
                                                                    return true;
                                                            }
                                                            return true;
                                                        }
                                                    });

                                                    popup.show();
                                                }
                                            });
                                            Query queryRequest=firebaseModel.getUsersReference().child(info.getNick())
                                                    .child("requests").child(nick);
                                            queryRequest.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        a=3;
                                                        subscribeClose.setText("Запрошено");
                                                        subscribeClose.setTextColor(Color.parseColor("#F3A2E5"));
                                                        subscribeClose.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            subscribeClose.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(a==3){
                                                        firebaseModel.getUsersReference().child(info.getNick())
                                                                .child("requests").child(nick).removeValue();
                                                        subscribeClose.setText("Подписаться");
                                                        subscribeClose.setTextColor(Color.parseColor("#FEFEFE"));
                                                        subscribeClose.setBackgroundResource(R.drawable.corners10dpappcolor);
                                                        a=0;
                                                    }else {
                                                        firebaseModel.getReference().child("users").child(info.getNick()).child("requests")
                                                                .child(nick).setValue(nick);
                                                        Random random = new Random();
                                                        int num1 =random.nextInt(1000000000);
                                                        int num2 =random.nextInt(1000000000);
                                                        String numToBase=String.valueOf(num1+num2);
                                                        firebaseModel.getReference().child("users")
                                                                .child(info.getNick()).child("nontifications")
                                                                .child(numToBase).setValue(new Nontification(nick,"не отправлено","запрос"
                                                                ,ServerValue.TIMESTAMP.toString()," "," ","не просмотрено",numToBase));
                                                        subscribeClose.setText("Запрошено");
                                                        subscribeClose.setTextColor(Color.parseColor("#F3A2E5"));
                                                        subscribeClose.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                                        a=0;
                                                    }
                                                }
                                            });
                                        }else if(profileCheckValue==1) {
                                            subscribeClose.setVisibility(View.VISIBLE);
                                            subscribeClose.setBackgroundResource(R.drawable.corners10grey);
                                            subscribeClose.setTextColor(Color.parseColor("#FEFEFE"));
                                            blockedAccount.setVisibility(View.VISIBLE);
                                            blockedAccount.setText(info.getNick()+" заблокировал тебя");
                                            message.setVisibility(View.GONE);
                                            tabLayoutOther.setVisibility(View.GONE);
                                            viewPagerOther.setVisibility(View.GONE);
                                            Log.d("#####", "tab3");
                                            closeAccount.setVisibility(View.GONE);
                                            subscribeFirst.setVisibility(View.GONE);
                                            subscribe.setVisibility(View.GONE);
                                            moreSquare.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    PopupMenu popup = new PopupMenu(getActivity(), moreSquare);
                                                    popup.getMenuInflater()
                                                            .inflate(R.menu.other_user_menu, popup.getMenu());

                                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                        public boolean onMenuItemClick(MenuItem item) {
                                                            String itemTitle= item.getTitle().toString().trim();

                                                            Log.d("####", "hell"+itemTitle);
                                                            int itemID=item.getItemId();
                                                            switch(itemID){
                                                                case R.id.one :
                                                                    showDialog();
                                                                    return true;
                                                                case R.id.two:
                                                                    RecentMethods.setCurrentFragment(ComplainFragment.newInstance(info.getNick()), getActivity());
                                                                    return true;
                                                                case R.id.three:
                                                                    return true;
                                                            }
                                                            return true;
                                                        }
                                                    });

                                                    popup.show();
                                                }
                                            });
                                        }
                                    }}


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
    }



    private void AcceptChatRequest() {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                {
                    firebaseModel.getUsersReference().child(info.getNick()).child("Chats").child(nick).child("nick").setValue(nick);
                    firebaseModel.getUsersReference().child(nick).child("Chats").child(info.getNick()).child("nick").setValue(nick);
                }
            }
        });
    }
    public static void sendNickToAdapter(sendNick sendNick){
        sendNick.sendNick(sendNickString);
    }

    public void subscribePeople(){

    }

    public interface sendNick{
        void sendNick(String nick);
    }

    public void setCounts(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getSubscriptionList(nick, firebaseModel, new Callbacks.getFriendsList() {
                    @Override
                    public void getFriendsList(ArrayList<Subscriber> friends) {
                        subscriptionsCount.setText(String.valueOf(friends.size()));
                    }
                });
                RecentMethods.getSubscribersList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                    @Override
                    public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                        subscribersCount.setText(String.valueOf(subscribers.size()));
                    }
                });
                Query query2=firebaseModel.getUsersReference().child(nick).
                        child("looksCount");
                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        looksCount.setText(String.valueOf(snapshot.getValue(Long.class)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_layout_blacklist);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView complainTitle = dialog.findViewById(R.id.complainText);
        TextView no=dialog.findViewById(R.id.no);
        TextView yes=dialog.findViewById(R.id.yes);

        complainTitle.setText("Заблокировать "+info.getNick()+"?");

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        firebaseModel.getUsersReference().child(nick)
                                .child("blackList").child(info.getNick())
                                .setValue(info.getNick());
                        firebaseModel.getUsersReference().child(nick)
                                .child("subscription").child(info.getNick()).removeValue();
                        firebaseModel.getUsersReference().child(info.getNick())
                                .child("subscribers").child(nick).removeValue();
                        firebaseModel.getUsersReference().child(info.getNick())
                                .child("subscription").child(nick).removeValue();
                        firebaseModel.getUsersReference().child(nick)
                                .child("subscribers").child(info.getNick()).removeValue();
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void checkWardrobe(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getClothesInWardrobe(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        if(allClothes.size()==0){
                            wardrobeRecycler.setVisibility(View.GONE);
                            noClothes.setVisibility(View.VISIBLE);
                            buyClothesProfile.setVisibility(View.VISIBLE);
                            buyClothesProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RecentMethods.setCurrentFragment(ShopFragment.newInstance(), getActivity());
                                }
                            });
                        }else {
                            WardrobeAdapterProfile wardrobeAdapter=new WardrobeAdapterProfile(allClothes,itemClickListenerWardrobe,getActivity());
                            wardrobeRecycler.setAdapter(wardrobeAdapter);
                        }
                    }
                });
            }
        });
    }

    public void setCountsOther() {
        RecentMethods.getSubscriptionList(info.getNick(), firebaseModel, new Callbacks.getFriendsList() {
            @Override
            public void getFriendsList(ArrayList<Subscriber> friends) {
                otherSubscriptionCount.setText(String.valueOf(friends.size()));
            }
        });
        RecentMethods.getSubscribersList(info.getNick(), firebaseModel, new Callbacks.getSubscribersList() {
            @Override
            public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                otherSubscribersCount.setText(String.valueOf(subscribers.size()));
            }
        });
        Query query2=firebaseModel.getUsersReference().child(info.getNick()).
                child("looksCount");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                otherLooksCount.setText(String.valueOf(snapshot.getValue(Long.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void checkOtherUserProfile(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query queryBlackList=firebaseModel.getUsersReference().child(info.getNick())
                        .child("blackList").child(nick);
                queryBlackList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            profileCheckValue=1;
                        }else {
                            profileCheckValue=2;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                    return new ClothesFragmentProfile();
            }
            return new LooksFragmentProfile();
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
                    return new ClothesFragmentProfileOther(info.getNick());
            }
            return new LooksFragmentProfileOther(info.getNick());
        }


        @Override
        public int getItemCount() {
            return 2;
        }
    }


}