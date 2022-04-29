package com.egormoroz.schooly;

import android.annotation.SuppressLint;
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
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.RegisrtationstartFragment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsFragment;
import com.egormoroz.schooly.ui.people.PeopleFragment;
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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth AuthenticationBase;
    public static String currentUserID;
    UserInformation userInformation;
    RelativeLayout relativeLayout;
    TextView s,loading;
    CoordinatorLayout fragmentContainer;
    OneTimeWorkRequest miningWorkRequest;
    FirebaseModel firebaseModel=new FirebaseModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = findViewById(R.id.fragment_container);
        relativeLayout=findViewById(R.id.rel);
        s=findViewById(R.id.s);
        loading=findViewById(R.id.load);
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
                                                        userInformation.setQueue(snapshot.child("queue").getValue(String.class));
                                                        //userInformation.setSubscription(snapshot.child("subscription").getValue(String.class));
                                                        userInformation.setmoney(snapshot.child("money").getValue(Long.class));
                                                        userInformation.setTodayMining(snapshot.child("todayMining").getValue(Double.class));
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
                                                                        setCurrentFragment(MainFragment.newInstance(userInformation));
//                        toolbarTitle.setText(getString(R.string.app_name));
//                        toolbarTitle.setTextColor(getColor(R.color.app_color));
//                        appBarLayout.setVisibility(View.VISIBLE);
                                                                        return true;
                                                                    case R.id.bottom_nav_news:
                                                                        setCurrentFragment(NewsFragment.newInstance());
//                        toolbarTitle.setText(getString(R.string.toolbar_news));
//                        toolbarTitle.setTextColor(getColor(R.color.black));
//                        appBarLayout.setVisibility(View.VISIBLE);
                                                                        return true;
                                                                    case R.id.bottom_nav_coins:
                                                                        setCurrentFragment(CoinsMainFragment.newInstance());
//                        toolbarTitle.setText(getString(R.string.toolbar_people));
//                        toolbarTitle.setTextColor(getColor(R.color.black));
//                        appBarLayout.setVisibility(View.VISIBLE);
                                                                        return true;
                                                                    case R.id.bottom_nav_people:
                                                                        setCurrentFragment(PeopleFragment.newInstance(userInformation));
//                        toolbarTitle.setText(getString(R.string.toolbar_people));
//                        toolbarTitle.setTextColor(getColor(R.color.black));
//                        appBarLayout.setVisibility(View.VISIBLE);
                                                                        return true;
                                                                    case R.id.bottom_nav_profile:
                                                                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                                                            @Override
                                                                            public void PassUserNick(String nick) {
                                                                                setCurrentFragment(ProfileFragment.newInstance("user", nick,MainFragment.newInstance(userInformation),userInformation));
                                                                            }
                                                                        });
//                        appBarLayout.setVisibility(View.GONE);
                                                                        CoordinatorLayout.LayoutParams coordinatorLayoutParams = (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
                                                                        coordinatorLayoutParams.setBehavior(null);
                                                                        return true;
                                                                }
                                                                return false;
                                                            }
                                                        });
                                                        setCurrentFragment(MainFragment.newInstance(userInformation));
                                                        final DatabaseReference connectedRef = database.getReference(".info/connected");
                                                        connectedRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot snapshot) {
                                                                boolean connected = snapshot.getValue(Boolean.class);
                                                                if (connected) {
                                                                    firebaseModel.getUsersReference().child(nick).child("Status")
                                                                            .setValue("Online").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                //WorkManager.getInstance(getApplicationContext()).cancelWorkById(miningWorkRequest.getId());
                                                                            }
                                                                        }
                                                                    });

                                                                    DatabaseReference presenceRef = firebaseModel.getReference().child("users").child(nick).child("Status");
                                                                    presenceRef.onDisconnect().setValue("Offline").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                // WorkManager.getInstance(getApplicationContext()).cancelWorkById(miningWorkRequest.getId());
                                                                                Log.d("AAA", "ddll");
                                                                            }
                                                                        }
                                                                    });
                                                                }else{
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError error) {
                                                            }
                                                        });
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    public interface GetTimeStamp{
        public void GetTimeStamp(long timestamp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                firebaseModel.getUsersReference().child(nick).child("timesTamp").setValue(ServerValue.TIMESTAMP);
            }
        });
    }
}