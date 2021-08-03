package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
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
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.profile.settingscomponents.AboutusFragment;
import com.egormoroz.schooly.ui.profile.settingscomponents.NontificationsFragment;
import com.egormoroz.schooly.ui.profile.settingscomponents.PersonaldataFragment;
import com.egormoroz.schooly.ui.profile.settingscomponents.PrivacypolicyFragment;
import com.egormoroz.schooly.ui.profile.settingscomponents.SupportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class EditingFragment extends Fragment {
    FirebaseAuth AuthenticationBase;
    FirebaseDatabase database;
    DatabaseReference reference;
    public static EditingFragment newInstance() {
        return new EditingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editing, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFirebase();
        EditText nicknameedittext =view.findViewById(R.id.edittextnickname);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                nicknameedittext.setText(snapshot.child("nick").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        ImageView arrowtoprofileediting = view.findViewById(R.id.back_toprofile);
        arrowtoprofileediting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(ProfileFragment.newInstance());
            }
        });
    }

    public void initFirebase(){
        AuthenticationBase = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        reference = database.getReference("users").child(AuthenticationBase.getCurrentUser().getUid());
    }
}
