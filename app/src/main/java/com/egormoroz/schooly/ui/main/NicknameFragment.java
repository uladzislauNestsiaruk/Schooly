package com.egormoroz.schooly.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NicknameFragment extends Fragment {
    public static NicknameFragment newInstance() {
        return new NicknameFragment();
    }
    FirebaseAuth authenticationDatabase;
    FirebaseDatabase database;
    DatabaseReference ref;
    String userId;
    EditText nicknameEditText;
    ImageView continueButton;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nickname, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        initFirebase();
        initElements(root);
        saveNick();
        return root;
    }
    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }
    public void initFirebase(){
        authenticationDatabase = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        userId = authenticationDatabase.getCurrentUser().getUid();
        ref = database.getReference();
        ref = ref.child("users").child(userId);
    }
    public void initElements(View root){
        nicknameEditText = root.findViewById(R.id.nickname);
        continueButton = root.findViewById(R.id.continueButton);
    }
    public boolean isNickCorrect(){
        return true;
    }
    public void saveNick(){
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = String.valueOf(nicknameEditText.getText()).trim();
                if(isNickCorrect()) {
                    ref.child("nick").setValue(nickname);
                    setCurrentFragment(MainFragment.newInstance());
                }
            }
        });
    }
}
