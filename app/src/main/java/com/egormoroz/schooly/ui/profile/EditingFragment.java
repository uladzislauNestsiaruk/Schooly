package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditingFragment extends Fragment {
    FirebaseModel firebaseModel=new FirebaseModel();
    EditText nickEdit,bioEdit;
    String nickname;
    RelativeLayout agree;
    String type;
    Fragment fragment;

    public EditingFragment(String type,Fragment fragment) {
        this.type = type;
        this.fragment=fragment;
    }

    public static EditingFragment newInstance(String type, Fragment fragment) {
        return new EditingFragment(type,fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editing, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView arrowtoprofileediting = view.findViewById(R.id.back_toprofile);
        arrowtoprofileediting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick,fragment), getActivity());
                    }
                });
            }
        });

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick,fragment), getActivity());
                    }
                };

                requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
            }
        });

        nickEdit=view.findViewById(R.id.edittextnickname);
        bioEdit=view.findViewById(R.id.edittextbio);
        agree=view.findViewById(R.id.agree);

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                nickEdit.setText(nick);
                nickname=nick;
                Query query=firebaseModel.getUsersReference().child(nickname).child("bio");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bioEdit.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        changeNick();
        changeBio();
    }

    public void changeNick(){
        nickEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void changeBio(){
        bioEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                agree.setVisibility(View.VISIBLE);
                agree.setOnClickListener(new View.OnClickListener() {
                    String bioText= String.valueOf(bioEdit.getText().toString().trim());
                    public void onClick(View v) {
                        firebaseModel.getUsersReference().child(nickname).child("bio").setValue(bioText);
                        Toast.makeText(getContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}