package com.egormoroz.schooly.ui.main.CreateCharacter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.GenderFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.main.MyClothes.CreateClothesFragment;
import com.egormoroz.schooly.ui.main.NicknameFragment;
import com.egormoroz.schooly.ui.main.RegFragment;
import com.egormoroz.schooly.ui.main.Shop.AccessoriesFragment;
import com.egormoroz.schooly.ui.main.Shop.ClothesFragment;
import com.egormoroz.schooly.ui.main.Shop.HatsFragment;
import com.egormoroz.schooly.ui.main.Shop.PopularFragment;
import com.egormoroz.schooly.ui.main.Shop.ShoesFargment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateCharacterFragment extends Fragment {

    TabLayout tabLayout;
    TextView ready;
    final String databaseUrl = CONST.RealtimeDatabaseUrl;
    ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    FirebaseAuth authenticationDatabase;
    FirebaseDatabase database;
    private FirebaseModel firebaseModel = new FirebaseModel();
    final int GOOGLE_SIGN_IN = 101;
    FirebaseAuth AuthenticationBase;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    DatabaseReference reference;
    Bundle bundle;
    UserInformation userInformation;
    Fragment fragment;

    public CreateCharacterFragment(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
    }

    public static CreateCharacterFragment newInstance(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        return new CreateCharacterFragment(userInformation,bundle,fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_createcharacter, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        AuthenticationBase = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(getActivity(), gso);
        database = FirebaseDatabase.getInstance(databaseUrl);
        reference = database.getReference("users");
        tabLayout=view.findViewById(R.id.tabsCharacter);
        ready=view.findViewById(R.id.ready);
        viewPager=view.findViewById(R.id.viewPagerCharacter);

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        ImageView leftarrowtoreg =view.findViewById(R.id.leftarrowtoreg);
        leftarrowtoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(GenderFragment.newInstance(userInformation, bundle, fragment));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                ((MainActivity)getActivity()).setCurrentFragment(GenderFragment.newInstance(userInformation, bundle, fragment));
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        FragmentManager fm = getChildFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.skincolour)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.body)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.faceshape)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.hairstyle)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.brows)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.eyes)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.nose)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.lips)));

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


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
            Log.d("#####", "ggds");
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
        }
    }

    public void showDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.accept_character_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout no=dialog.findViewById(R.id.no);
        RelativeLayout yes=dialog.findViewById(R.id.yes);
        TextView textView=dialog.findViewById(R.id.acceptText);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("#####", "gg"+bundle.getString("FRAGMENT"));
                if(bundle.getString("FRAGMENT").equals("reg")){
                    Log.d("#####", "ggd"+bundle.getString("PHONE"));
                    if(bundle.getString("PHONE")!=null&&bundle.getString("PASSWORD")!=null&bundle.getString("NICK")!=null) {
                        Log.d("#####", "gg");
                        createNewEmailUser(RecentMethods.makeEmail(bundle.getString("PHONE")),
                                bundle.getString("PASSWORD"), bundle.getString("NICK"));
                    }
                }else if(bundle.getString("FRAGMENT").equals("nick")){
                    if(bundle.getString("NICKNAMEFRAGMENT")!=null){
                        Log.d("#####", "ggd");
                        authenticationDatabase = FirebaseAuth.getInstance();
                        AuthorizationThrowGoogle();
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void createNewEmailUser(String email, String password, String nick) {
        AuthenticationBase.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("###", "createUserWithEmail:success");
                            FirebaseUser user = AuthenticationBase.getCurrentUser();
                            UserInformation res = new UserInformation(nick, RecentMethods.getPhone(email), user.getUid(),
                                    "6", password, "Helicopter", 1000, new ArrayList<>(),new ArrayList<>(),1,100,0
                                    , new ArrayList<>(), new ArrayList<>(), ""," ","open","open","open","open"
                                    ,new ArrayList<>(),"regular", new ArrayList<>(),0,new ArrayList<>(),new ArrayList<>()
                                    ,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
                            reference.child(nick).setValue(res);
                            FirebaseModel firebaseModel=new FirebaseModel();
                            firebaseModel.initAll();
                            firebaseModel.getReference("usersNicks")
                                    .child(nick).setValue(new UserPeopleAdapter(nick,"6"," "));
                            ((MainActivity)getActivity()).IsEntered();
                            ((MainActivity)getActivity()).checkMining();
                            RecentMethods.setCurrentFragment(MainFragment.newInstance(res,bundle), getActivity());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("###", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), getContext().getResources().getText(R.string.authenticationfailed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        AuthenticationBase.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    RecentMethods.saveData(reference, authenticationDatabase.getCurrentUser()
                            , bundle.getString("NICKNAMEFRAGMENT"),bundle,getActivity());
                }
            }
        });
    }
    public void AuthorizationThrowGoogle() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
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
                    return new BodyFragment();
                case 2:
                    return new FaceShapeFragment();
                case 3:
                    return new HairstyleFragment();
                case 4:
                    return new EyebrowsFragment();
                case 5:
                    return new EyesFragment();
                case 6:
                    return new NoseFragment();
                case 7:
                    return new LipsFragment();
            }
            return new SkinColourFragment();
        }


        @Override
        public int getItemCount() {
            return 8;
        }
    }
}
