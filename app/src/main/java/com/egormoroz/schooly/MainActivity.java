package com.egormoroz.schooly;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.egormoroz.schooly.ui.chat.Dialog;
import com.egormoroz.schooly.ui.main.ChatFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.MainViewModel;
import com.egormoroz.schooly.ui.main.RegisrtationstartFragment;
import com.egormoroz.schooly.ui.news.NewsFragment;
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        com.egormoroz.schooly.ui.main.sendDialogs{
    private ArrayList<Dialog> dialogs = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth AuthenticationBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CoordinatorLayout fragmentContainer = findViewById(R.id.fragment_container);

        ///////////Authorization block

        ///////////
        if(IsEntered())
            setCurrentFragment(MainFragment.newInstance());
        else
            RegistrationOrEnter();
        ///////////
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
                        setCurrentFragment(MainFragment.newInstance());
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
                    case R.id.bottom_nav_people:
                        setCurrentFragment(PeopleFragment.newInstance());
//                        toolbarTitle.setText(getString(R.string.toolbar_people));
//                        toolbarTitle.setTextColor(getColor(R.color.black));
//                        appBarLayout.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.bottom_nav_profile:
                        setCurrentFragment(ProfileFragment.newInstance());
//                        appBarLayout.setVisibility(View.GONE);
                        CoordinatorLayout.LayoutParams coordinatorLayoutParams = (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
                        coordinatorLayoutParams.setBehavior(null);
                        return true;
                }
                return false;
            }
        });
        initFirebase();
     //   getDialogs();
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
    boolean IsEntered(){
        return false;
    }
    @Override
    public void setDialogs(ArrayList<Dialog> dialogs) {
        Fragment currentFragment = getSupportFragmentManager().
                findFragmentById(R.id.frame);
        Log.d("#######", dialogs.size() + " Transaction");
        if(currentFragment instanceof ChatFragment) {
            ChatFragment fragment = (ChatFragment) currentFragment;
            fragment.setDialogs(dialogs);
        }
    }
//    public void getDialogs(){
//        reference = database.getReference().child("users").
//                child(AuthenticationBase.getCurrentUser().getUid()).child("chats");
//        reference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
////                GenericTypeIndicator<HashMap<String, Dialog>> indicator =
////                        new GenericTypeIndicator<HashMap<String, Dialog>>(){};
//////                HashMap<String, Dialog> mapka = snapshot.getValue(indicator);
//////                Log.d("######", mapka.size() + " download");
////                for(Map.Entry<String, Dialog> cur : mapka.entrySet())
////                    dialogs.add(cur.getValue());
////            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }
    public void initFirebase(){
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        AuthenticationBase = FirebaseAuth.getInstance();
    }
}