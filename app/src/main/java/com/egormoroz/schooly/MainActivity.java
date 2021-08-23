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
import com.egormoroz.schooly.ui.main.RegisrtationstartFragment;
import com.egormoroz.schooly.ui.news.NewsFragment;
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
        initFirebase();
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
        return AuthenticationBase.getCurrentUser() != null;
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
    public void initFirebase(){
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        AuthenticationBase = FirebaseAuth.getInstance();
    }
}