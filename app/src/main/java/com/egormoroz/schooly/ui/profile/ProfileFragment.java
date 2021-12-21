package com.egormoroz.schooly.ui.profile;

import static android.os.Looper.getMainLooper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;

import com.egormoroz.schooly.ModelRenderer;
import com.egormoroz.schooly.ModelSurfaceView;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.SceneLoader;
import com.egormoroz.schooly.ui.main.ChatActivity;
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

public class ProfileFragment extends Fragment {
    private static String sendNickString;
    FirebaseModel firebaseModel = new FirebaseModel();
    Context profileContext;
    String type,nicknameCallback;
    UserInformation info;
    TextView nickname,message,biographyTextView,looksCount,subscriptionsCount,subscribersCount,otherLooksCount,otherSubscriptionCount,
    otherSubscribersCount;
    DatabaseReference user;
    SceneLoader scene;
    LinearLayout linearLooks,linearSubscribers,linearSubscriptions;
    ModelSurfaceView modelSurfaceView;
    GLSurfaceView mainLook;
    ModelRenderer modelRenderer;
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
                TextView texttowardrobe = view.findViewById(R.id.shielf);
                texttowardrobe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ((MainActivity) getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
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

                handler = new Handler(getMainLooper());
                scene = new SceneLoader(this);
//                scene.init(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2FSciFiHelmet.gltf?alt=media&token=a82512c1-14bf-4faf-8f67-abeb70da7697"));
                mainLook=view.findViewById(R.id.mainlookview);
                try {
                    modelRenderer=new ModelRenderer(mainLook);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainLook.setRenderer(modelRenderer);


                break;
            case "other":
                nickname.setText(info.getNick());
                sendNickString=info.getNick();
                user = firebaseModel.getUsersReference().child(info.getNick());
                if (message != null) {
                    message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            open();
                        }
                    });
                }
                otherLooksCount=view.findViewById(R.id.looksCountOther);
                otherSubscriptionCount=view.findViewById(R.id.subscriptionCountOther);
                otherSubscribersCount=view.findViewById(R.id.subsCountOther);
                otherSubscriptionCount.setText(String.valueOf(info.getSubscriptionCount()));
                otherLooksCount.setText(String.valueOf(info.getLooksCount()));
                otherSubscribersCount.setText(String.valueOf(info.getSubscribersCount()));


                linearSubscriptions=view.findViewById(R.id.subscriptionLinear);
                linearSubscribers=view.findViewById(R.id.subscribersLinear);
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

                TextView addFriend;
                addFriend=view.findViewById(R.id.addFriend);
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                @Override
                public void PassUserNick(String nick) {
                    Query query=firebaseModel.getUsersReference().child(nick).child("subscribers")
                            .child(info.getNick());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                addFriend.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                addFriend.setTextColor(Color.parseColor("#F3A2E5"));
                                addFriend.setText("Ответить");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

                addFriend.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                        @Override
                        public void PassUserNick(String nick) {
                            firebaseModel.getReference().child("users")
                                    .child(info.getNick()).child("subscribers")
                                    .child(nick).setValue(nick);
                            firebaseModel.getReference().child("users")
                                    .child(info.getNick()).child("nontifications")
                                    .child(nick).setValue(nick);
                            addFriend.setBackgroundResource(R.drawable.corners14dpappcolor2dpstroke);
                            addFriend.setText("Отписаться");
                            Query query=firebaseModel.getUsersReference().child(info.getNick())
                                    .child("subscribersCount");
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long subsCount=snapshot.getValue(Long.class);
                                    firebaseModel.getUsersReference().child(info.getNick())
                                            .child("subscribersCount").setValue(subsCount+1);
                                    Log.d("####", "1   "+subsCount);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                } });
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
                Query query=firebaseModel.getUsersReference().child(nick).
                        child("subscribersCount");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subscribersCount.setText(String.valueOf(snapshot.getValue(Long.class)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Query query1=firebaseModel.getUsersReference().child(nick).
                        child("subscriptionCount");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subscriptionsCount.setText(String.valueOf(snapshot.getValue(Long.class)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

//    public void setCountsOther() {
//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @Override
//            public void PassUserNick(String nick) {
//                Query query = firebaseModel.getUsersReference().child(info.getNick()).
//                        child("subscribersCount");
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        otherSubscribersCount.setText(String.valueOf(snapshot.getValue(Long.class)));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                Query query1 = firebaseModel.getUsersReference().child(info.getNick()).
//                        child("friendsCount");
//                query1.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        otherFriendsCount.setText(String.valueOf(snapshot.getValue(Long.class)));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                Query query2 = firebaseModel.getUsersReference().child(info.getNick()).
//                        child("looksCount");
//                query2.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        otherLooksCount.setText(String.valueOf(snapshot.getValue(Long.class)));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//    }

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