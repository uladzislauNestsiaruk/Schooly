package com.egormoroz.schooly.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

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
    boolean isNickCorrect(String nickname){
        DatabaseReference reference = database.getReference().child("users");
        Query query = reference.orderByChild("nick").equalTo(nickname);
        final boolean[] res = new boolean[1];
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               boolean temp = !snapshot.exists();
               res[0] = temp;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return res[0];
    }
    public void saveNick(){
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = String.valueOf(nicknameEditText.getText()).trim();
                if(isNickCorrect(nickname)) {
                    ref.child("nick").setValue(nickname);
                    setCurrentFragment(MainFragment.newInstance());
                }
            }
        });
    }
}
