package com.egormoroz.schooly.ui.main;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class RegisrtationstartFragment extends Fragment {
    public static RegisrtationstartFragment newInstance(){return new RegisrtationstartFragment();}
    Button RegistrationButton;
    Button EnterButton;
    FirebaseAuth AuthBase;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reg, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        AuthBase = FirebaseAuth.getInstance();
        RegistrationButton = root.findViewById(R.id.registr);
        EnterButton = root.findViewById(R.id.enter);
        ////////////Is user Logged in
        //isUserLoggedIn();
        RegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(RegFragment.newInstance(), getActivity());
            }
        });
        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(EnterFragment.newInstance(), getActivity());
            }
        });
        return root;
    }
    void isUserLoggedIn(){
        if(AuthBase.getCurrentUser() != null)
            RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
    }
}
