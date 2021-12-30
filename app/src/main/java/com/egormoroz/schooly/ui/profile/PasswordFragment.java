package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PasswordFragment extends Fragment {

    public static PasswordFragment newInstance() {
        return new PasswordFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    TextView userNumber,userPassword,next,errorText,editUsePassword;
    String passwordFromBase;
    ImageView backToSettings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View root =inflater.inflate(R.layout.fragment_password,container,false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        backToSettings=view.findViewById(R.id.back_tosettings);
        backToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(SettingsFragment.newInstance(), getActivity());
            }
        });
        next=view.findViewById(R.id.next);
        errorText=view.findViewById(R.id.errorText);
        editUsePassword=view.findViewById(R.id.edittextenterpassword);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordEditText = editUsePassword.getText().toString();
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick)
                                .child("password");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                passwordFromBase=snapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                if(passwordEditText.equals(passwordFromBase)){
                    Log.d("#####", "suck");
                }else {
                    errorText.setText(R.string.errortext);
                }
            }
        });

    }
}
