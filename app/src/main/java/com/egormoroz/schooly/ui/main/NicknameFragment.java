package com.egormoroz.schooly.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
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
    TextView nicknameTextView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nickname, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        initFirebase();
        initElements(root);
        saveNick();
        return root;
    }
    ///////////////////////////// INITIALIZATION //////////////
    public void initFirebase(){
        authenticationDatabase = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        userId = authenticationDatabase.getCurrentUser().getUid();
        ref = database.getReference();
        ref = ref.child("users");
    }
    public void initElements(View root){
        nicknameTextView = root.findViewById(R.id.errornick);
        nicknameEditText = root.findViewById(R.id.nickname);
        continueButton = root.findViewById(R.id.continueButton);
        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nickname = String.valueOf(nicknameEditText.getText()).trim();
                RecentMethods.isNickCorrect(nickname, ref, nicknameTextView);
                String error = String.valueOf(nicknameTextView.getText()).trim();
                Log.d("##########", "error: " + error);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    ///////////////////////////// SAVE DATA //////////////////
    public void saveNick(){
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = String.valueOf(nicknameEditText.getText()).trim();
                String error = String.valueOf(nicknameTextView.getText()).trim();
                if(error.isEmpty()) {
                    RecentMethods.saveData(ref, authenticationDatabase.getCurrentUser(), nickname);
                    RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
                }
            }
        });
    }
}