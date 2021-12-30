package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
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
    TextView userNumber,userPassword,next,errorText,errorTextNewPassword,textCreateNewPassword,textRepeatNewPassword
            ,enterUsePassword;
    EditText editUsePassword,editTextCreateNewPassword,editTextRepeatNewPassword;
    String passwordFromBase;
    ImageView backToSettings;
    int a=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View root =inflater.inflate(R.layout.fragment_password,container,false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
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
        enterUsePassword=view.findViewById(R.id.textenterpassword);
        textCreateNewPassword=view.findViewById(R.id.textCreateNewPassword);
        textRepeatNewPassword=view.findViewById(R.id.textRepeatNewPassword);
        editUsePassword=view.findViewById(R.id.edittextenterpassword);
        editTextCreateNewPassword=view.findViewById(R.id.ediTextCreateNewPassword);
        editTextRepeatNewPassword=view.findViewById(R.id.editTextRepeatNewPassword);
        errorTextNewPassword=view.findViewById(R.id.errorTextNewPassword);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a==0) {
                    String passwordEditText = editUsePassword.getText().toString();
                    if (passwordEditText.equals(passwordFromBase)) {
                        editTextCreateNewPassword.setVisibility(View.VISIBLE);
                        editTextRepeatNewPassword.setVisibility(View.VISIBLE);
                        errorTextNewPassword.setVisibility(View.VISIBLE);
                        textCreateNewPassword.setVisibility(View.VISIBLE);
                        textRepeatNewPassword.setVisibility(View.VISIBLE);
                        editUsePassword.setVisibility(View.GONE);
                        errorText.setVisibility(View.GONE);
                        enterUsePassword.setVisibility(View.GONE);
                        next.setText(R.string.change);
                        a = 1;
                    } else {
                        errorText.setText(R.string.errortext);

                    }
                }else if(a==1){
                    String getNewPasswordOne=editTextCreateNewPassword.getText().toString();
                    String getNewPasswordTwo=editTextRepeatNewPassword.getText().toString();
                    if(getNewPasswordOne.equals(getNewPasswordTwo)){
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                firebaseModel.getUsersReference().child(nick).child("password")
                                        .removeValue();
                                firebaseModel.getUsersReference().child(nick).child("password")
                                        .setValue(getNewPasswordOne);
                            }
                        });
                    }else {
                        errorTextNewPassword.setText(R.string.passwordNotEquals);
                    }
                }
            }
        });

    }
}
