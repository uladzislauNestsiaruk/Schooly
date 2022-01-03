package com.egormoroz.schooly.ui.profile;

import static android.os.Looper.getMainLooper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;

import com.egormoroz.schooly.ModelRenderer;
import com.egormoroz.schooly.ModelSurfaceView;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.SceneLoader;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.ChatActivity;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeFragment;
import com.google.android.filament.Filament;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private static String sendNickString;
    FirebaseModel firebaseModel = new FirebaseModel();
    Context profileContext;
    String type,nicknameCallback;
    UserInformation info;
    TextView nickname,message,biographyTextView,looksCount,subscriptionsCount,subscribersCount,otherLooksCount,otherSubscriptionCount,
            otherSubscribersCount,createNewLookText,createNewLook,otherUserBiography,subscribeClose,addFriend,looksText
            ,subscribeFirst,closeAccount,noClothes,buyClothesProfile,noLooksOther;
    DatabaseReference user;
    NewClothesAdapter.ItemClickListener itemClickListener;
    SceneLoader scene;
    LinearLayout linearLooks,linearSubscribers,linearSubscriptions;
    ModelSurfaceView modelSurfaceView;
    GLSurfaceView mainLook;
    ModelRenderer modelRenderer;
    RecyclerView looksRecycler,wardrobeRecycler,looksRecyclerOther;
    int looksListSize,profileValue;
    private float[] backgroundColor = new float[]{0f, 0f, 0f, 1.0f};
    private Handler handler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        profileContext = context;
    }

    public ProfileFragment(String type, UserInformation info) {
        this.type = type;
        this.info = info;
    }

    public static ProfileFragment newInstance(String type, UserInformation info) {
        return new ProfileFragment(type, info);
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
                ///////// I want GM on CF
                ImageView arrowtowardrobe = view.findViewById(R.id.arrowtowardrobe);
                arrowtowardrobe.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
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
                itemClickListener=new NewClothesAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Clothes clothes) {
                        ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance());
                    }
                };
                wardrobeRecycler=view.findViewById(R.id.recyclerProfileToWardrobe);
                noClothes=view.findViewById(R.id.noClothesText);
                buyClothesProfile=view.findViewById(R.id.buyClothesProfile);
                checkWardrobe();
                //////////////////////////////////////
                /////////////////LOOKS///////////////
                createNewLook=view.findViewById(R.id.CreateYourLook);
                createNewLookText=view.findViewById(R.id.textCreateYourLook);
                looksRecycler=view.findViewById(R.id.looksRecycler);
                looksRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.getLooksList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                            @Override
                            public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                                looksListSize=subscribers.size();
                                if (looksListSize==0){
                                    createNewLookText.setVisibility(View.VISIBLE);
                                    createNewLook.setVisibility(View.VISIBLE);
                                    looksRecycler.setVisibility(View.GONE);
                                }else {
                                    LooksAdapter looksAdapter=new LooksAdapter(subscribers);
                                    looksRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                                    looksRecycler.setAdapter(looksAdapter);
                                }
                            }
                        });
                    }
                });
                ///////////////////////////////////////

                handler = new Handler(getMainLooper());
                scene = new SceneLoader(this);
                //               scene.init(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2FSciFiHelmet.gltf?alt=media&token=a82512c1-14bf-4faf-8f67-abeb70da7697"));
                mainLook=view.findViewById(R.id.mainlookview);
                try {
                    modelRenderer=new ModelRenderer(mainLook);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainLook.setRenderer(modelRenderer);

                firebaseModel.getUsersReference().child("tyomaa6").child("subscribers")
                        .child("spaccacrani").setValue("spaccacrani");


                break;
            case "other":
                nickname.setText(info.getNick());
                sendNickString=info.getNick();
                user = firebaseModel.getUsersReference().child(info.getNick());
                otherUserBiography=view.findViewById(R.id.otheruserbiography);
                looksRecycler=view.findViewById(R.id.looksRecycler);
                subscribeClose=view.findViewById(R.id.subscribeClose);
                otherUserBiography.setText(info.getBio());
                addFriend=view.findViewById(R.id.addFriend);
                subscribeFirst=view.findViewById(R.id.SubscribeFirst);
                closeAccount=view.findViewById(R.id.closeAccount);
                otherLooksCount=view.findViewById(R.id.looksCountOther);
                otherSubscriptionCount=view.findViewById(R.id.subscriptionCountOther);
                otherSubscribersCount=view.findViewById(R.id.subsCountOther);
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("subscription")
                                .child(info.getNick());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    profileValue=1;
                                }else {profileValue=-1;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                setCountsOther();
                if (message != null) {
                    message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            open();
                        }
                    });
                }
                Log.d("######", "v "+profileValue);
                if (profileValue!=0) {
                    if (!(!info.getAccountType().equals("open") || profileValue == 1)) {
                        otherLooksCount = view.findViewById(R.id.looksCountOther);
                        otherSubscriptionCount = view.findViewById(R.id.subscriptionCountOther);
                        otherSubscribersCount = view.findViewById(R.id.subsCountOther);
                        looksRecycler=view.findViewById(R.id.looksRecyclerOther);
                        looksText=view.findViewById(R.id.looksText);
                        noLooksOther=view.findViewById(R.id.noLooksOther);
                        linearSubscriptions = view.findViewById(R.id.subscriptionLinear);
                        linearSubscribers = view.findViewById(R.id.subscribersLinear);
                        checkLooksOther();
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
                                            addFriend.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                            addFriend.setTextColor(Color.parseColor("#F3A2E5"));
                                            addFriend.setText("Отписаться");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                        addFriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                    @Override
                                    public void PassUserNick(String nick) {
                                        Query query = firebaseModel.getUsersReference().child(nick).child("subscription")
                                                .child(info.getNick());
                                        query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    firebaseModel.getReference().child("users")
                                                            .child(info.getNick()).child("subscribers")
                                                            .child(nick).removeValue();
                                                    firebaseModel.getReference().child("users")
                                                            .child(nick).child("subscription")
                                                            .child(info.getNick()).removeValue();
                                                    addFriend.setBackgroundResource(R.drawable.corners10dpappcolor);
                                                    addFriend.setText("Подпиматься");
                                                    addFriend.setTextColor(Color.parseColor("#FFFEFE"));
                                                } else {
                                                    firebaseModel.getReference().child("users")
                                                            .child(info.getNick()).child("subscribers")
                                                            .child(nick).setValue(nick);
                                                    firebaseModel.getReference().child("users")
                                                            .child(info.getNick()).child("nontifications")
                                                            .child(nick).setValue(nick);
                                                    firebaseModel.getReference().child("users")
                                                            .child(info.getNick()).child("nontificationsRecycler")
                                                            .child(nick).setValue(nick);
                                                    firebaseModel.getReference().child("users")
                                                            .child(nick).child("subscription")
                                                            .child(info.getNick()).setValue(info.getNick());
                                                    addFriend.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                                    addFriend.setText("Отписаться");
                                                    addFriend.setTextColor(Color.parseColor("#F3A2E5"));
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
                    } else {
                        subscribeClose.setVisibility(View.VISIBLE);
                        closeAccount.setVisibility(View.VISIBLE);
                        subscribeFirst.setVisibility(View.VISIBLE);
                        subscribeFirst.setText("Подпишись на " + " " + info.getNick() + " !");
                        message.setVisibility(View.GONE);
                        addFriend.setVisibility(View.GONE);
                        looksText.setVisibility(View.GONE);
                        looksRecycler.setVisibility(View.GONE);
                    }
                }

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
                            NewClothesAdapter newClothesAdapter=new NewClothesAdapter(allClothes, itemClickListener);
                            wardrobeRecycler.setAdapter(newClothesAdapter);
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

    public void checkLooksOther(){
        RecentMethods.getLooksList(info.getNick(), firebaseModel, new Callbacks.getSubscribersList() {
            @Override
            public void getSubscribersList(ArrayList<Subscriber> subscribers) { ;
                if (subscribers.size()==0){
                    noLooksOther.setVisibility(View.VISIBLE);
                    looksRecyclerOther.setVisibility(View.GONE);
                }else {
                    LooksAdapter looksAdapter=new LooksAdapter(subscribers);
                    looksRecyclerOther.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    looksRecyclerOther.setAdapter(looksAdapter);
                }
                Log.d("#####", "sf  "+subscribers.size());
            }
        });
    }

    public GLSurfaceView getGLView() {
        return mainLook;
    }

    public SceneLoader getScene() {
        return scene;
    }
    public ModelRenderer getModelRenderer(){
        return modelRenderer;
    }

    public float[] getBackgroundColor() {
        return backgroundColor;
    }

//    public class ModelSurfaceView extends GLSurfaceView {
//
//        private ProfileFragment parent;
//        private ModelRenderer mRenderer;
//        private TouchController touchHandler;
//
//        public ModelSurfaceView(ProfileFragment parent) throws IllegalAccessException, IOException {
//            super(parent.getContext());
//
//            // parent component
//            this.parent = parent;
//
//            // Create an OpenGL ES 2.0 context.
//            setEGLContextClientVersion(2);
//
//            // This is the actual renderer of the 3D space
//            mRenderer = new ModelRenderer(this);
//            setRenderer(mRenderer);
//
//            // Render the view only when there is a change in the drawing data
//            // TODO: enable this?
//            // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//
//            touchHandler = new TouchController(this, mRenderer);
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            return touchHandler.onTouchEvent(event);
//        }
//
//        public ModelActivity getModelActivity() {
//            return parent;
//        }
//
//        public ModelRenderer getModelRenderer(){
//            return mRenderer;
//        }
//
//    }

}