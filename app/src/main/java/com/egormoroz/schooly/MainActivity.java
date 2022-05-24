package com.egormoroz.schooly;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.coins.Transfer;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.RegisrtationstartFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsFragment;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth AuthenticationBase;
    public static String currentUserID;
    UserInformation userInformation;
    RelativeLayout relativeLayout;
    TextView s,loading;
    CoordinatorLayout fragmentContainer;
    String TAG="###";
    Bundle bundle=new Bundle();
    private static final String CHANNEL_ID = "channel";
    FirebaseModel firebaseModel=new FirebaseModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.fragment_container);
        relativeLayout=findViewById(R.id.rel);
        s=findViewById(R.id.s);
        loading=findViewById(R.id.load);
        bundle.putString("START_BUNDLE", "Schooly");
        initFirebase();
        firebaseModel.initAll();
        ///////////Authorization block
        IsEntered();
        FirebaseUser mFirebaseUser = AuthenticationBase.getCurrentUser();
        if(mFirebaseUser != null) {
            currentUserID = mFirebaseUser.getUid();
        }else{
        }
        ///////////
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_nav, menu);
        return true;
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }
    void RegistrationOrEnter(){
        setCurrentFragment(RegisrtationstartFragment.newInstance());
    }
    void IsEntered(){
        FirebaseUser user = AuthenticationBase.getCurrentUser();
        RecentMethods.hasThisUser(AuthenticationBase, user,
                new Callbacks.hasGoogleUser() {
                    @Override
                    public void hasGoogleUserCallback(boolean hasThisUser) {
                        if(hasThisUser) {
                            Log.d("AAA", "current user: " + user.getEmail());
                            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                @Override
                                public void PassUserNick(String nick) {
                                    firebaseModel.getUsersReference().child(nick).get()
                                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        DataSnapshot snapshot=task.getResult();
                                                        userInformation=new UserInformation();
                                                        userInformation.setAge(snapshot.child("age").getValue(Long.class));
                                                        userInformation.setAvatar(snapshot.child("avatar").getValue(String.class));
                                                        userInformation.setGender(snapshot.child("gender").getValue(String.class));
                                                        userInformation.setNick(snapshot.child("nick").getValue(String.class));
                                                        userInformation.setPassword(snapshot.child("password").getValue(String.class));
                                                        userInformation.setPhone(snapshot.child("phone").getValue(String.class));
                                                        userInformation.setUid(snapshot.child("uid").getValue(String.class));
                                                        userInformation.setBio(snapshot.child("bio").getValue(String.class));
                                                        userInformation.setVersion(snapshot.child("version").getValue(String.class));
                                                        userInformation.setQueue(snapshot.child("queue").getValue(String.class));
                                                        userInformation.setChatsNontsType(snapshot.child("chatsNontsType").getValue(String.class));
                                                        userInformation.setGroupChatsNontsType(snapshot.child("groupChatsNontsType").getValue(String.class));
                                                        userInformation.setProfileNontsType(snapshot.child("profileNontsType").getValue(String.class));
                                                        userInformation.setAccountType(snapshot.child("accountType").getValue(String.class));
                                                        userInformation.setmoney(snapshot.child("money").getValue(Long.class));
                                                        userInformation.setTodayMining(snapshot.child("todayMining").getValue(Double.class));
                                                        getMyClothes(nick);
                                                        final DatabaseReference connectedRef = database.getReference(".info/connected");
//                                                        connectedRef.addValueEventListener(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(DataSnapshot snapshot) {
//                                                                boolean connected = snapshot.getValue(Boolean.class);
//                                                                if (connected) {
//                                                                    firebaseModel.getUsersReference().child(nick).child("Status")
//                                                                            .setValue("Online").addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                                            if(task.isSuccessful()){
//                                                                                //WorkManager.getInstance(getApplicationContext()).cancelWorkById(miningWorkRequest.getId());
//                                                                            }
//                                                                        }
//                                                                    });
//
//                                                                    DatabaseReference presenceRef = firebaseModel.getReference().child("users").child(nick).child("Status");
//                                                                    presenceRef.onDisconnect().setValue("Offline").addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                                            if(task.isSuccessful()){
//                                                                                // WorkManager.getInstance(getApplicationContext()).cancelWorkById(miningWorkRequest.getId());
//                                                                            }
//                                                                        }
//                                                                    });
//                                                                }else{
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(DatabaseError error) {
//                                                            }
//                                                        });
                                                        getLists();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                        else
                            RegistrationOrEnter();
                    }
                });
    }


    public void initFirebase(){
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        AuthenticationBase = FirebaseAuth.getInstance();
    }

    public void getMyClothes(String nick){
        RecentMethods.getMyClothes(nick, firebaseModel, new Callbacks.GetClothes() {
            @Override
            public void getClothes(ArrayList<Clothes> allClothes) {
                Collections.reverse(allClothes);
                userInformation.setMyClothes(allClothes);
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                s.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_nav_home:
                                setCurrentFragment(MainFragment.newInstance(userInformation,bundle));
                                return true;
                            case R.id.bottom_nav_news:
                                setCurrentFragment(NewsFragment.newInstance(userInformation,bundle));
                                return true;
                            case R.id.bottom_nav_coins:
                                setCurrentFragment(CoinsMainFragment.newInstance(userInformation,bundle));
                                return true;
                            case R.id.bottom_nav_people:
                                setCurrentFragment(PeopleFragment.newInstance(userInformation,bundle));
                                return true;
                            case R.id.bottom_nav_profile:
                                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                    @Override
                                    public void PassUserNick(String nick) {
                                        setCurrentFragment(ProfileFragment.newInstance("user", nick,MainFragment.newInstance(userInformation,bundle),userInformation,bundle));
                                    }
                                });
                                CoordinatorLayout.LayoutParams coordinatorLayoutParams = (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
                                coordinatorLayoutParams.setBehavior(null);
                                return true;
                        }
                        return false;
                    }
                });
                setCurrentFragment(MainFragment.newInstance(userInformation,bundle));
            }
        });
    }

    public void getLists(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getClothesInBasket(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        userInformation.setClothesBasket(allClothes);
                    }
                });
                RecentMethods.getClothesInWardrobe(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        userInformation.setClothes(allClothes);
                    }
                });
                RecentMethods.getSubscribersList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                    @Override
                    public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                        userInformation.setSubscribers(subscribers);
                    }
                });
                RecentMethods.getSubscriptionList(nick, firebaseModel, new Callbacks.getFriendsList() {
                    @Override
                    public void getFriendsList(ArrayList<Subscriber> friends) {
                        userInformation.setSubscription(friends);
                    }
                });
                RecentMethods.getNontificationsList(nick, firebaseModel, new Callbacks.getNontificationsList() {
                    @Override
                    public void getNontificationsList(ArrayList<Nontification> nontifications) {
                        Collections.reverse(nontifications);
                        userInformation.setNotifications(nontifications);
                    }
                });
                RecentMethods.getLooksList(nick, firebaseModel, new Callbacks.getLooksList() {
                    @Override
                    public void getLooksList(ArrayList<NewsItem> look) {
                        Collections.reverse(look);
                        userInformation.setLooks(look);
                    }
                });
                RecentMethods.getTransferHistory(nick, firebaseModel, new Callbacks.getTransferHistory() {
                    @Override
                    public void getTransferHistory(ArrayList<Transfer> transfers) {
                        Collections.reverse(transfers);
                        userInformation.setTransfers(transfers);
                    }
                });
                RecentMethods.getSavedLooks(nick, firebaseModel, new Callbacks.getSavedLook() {
                    @Override
                    public void getSavedLook(ArrayList<NewsItem> newsItems) {
                        Collections.reverse(newsItems);
                        userInformation.setSavedLooks(newsItems);
                    }
                });
                RecentMethods.getBlackList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                    @Override
                    public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                        userInformation.setBlackList(subscribers);
                    }
                });
                RecentMethods.getAlreadySearched(nick, firebaseModel, new Callbacks.GetAlreadySearched() {
                    @Override
                    public void getAlreadySearched(ArrayList<UserPeopleAdapter> searchedUserFromBase) {
                        userInformation.setAlreadySearched(searchedUserFromBase);
                    }
                });
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        userInformation.setmoney(money);
                    }
                });
                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                    @Override
                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                        userInformation.setMiners(activeMinersFromBase);
                    }
                });
                RecentMethods.MyMinersFromBase(nick, firebaseModel, new Callbacks.GetMyMinerFromBase() {
                    @Override
                    public void GetMyMinerFromBase(ArrayList<Miner> myMinersFromBase) {
                        userInformation.setMyMiners(myMinersFromBase);
                    }
                });
                RecentMethods.getClothes(firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        bundle.putSerializable("ALL_CLOTHES", allClothes);
                    }
                });

            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @Override
//            public void PassUserNick(String nick) {
//                firebaseModel.getUsersReference().child(nick).child("timesTamp").setValue(ServerValue.TIMESTAMP);
//            }
//        });
    }
}