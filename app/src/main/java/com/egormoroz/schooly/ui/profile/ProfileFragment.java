package com.egormoroz.schooly.ui.profile;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class ProfileFragment extends Fragment {
    FirebaseModel firebaseModel;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        AppBarLayout abl=getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        return root;
    }
    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nickname = view.findViewById(R.id.usernick);
        ///////////////////////// set nickname /////////////////////

        //////////////////////////////////////////////////
        ImageView imageView = view.findViewById(R.id.settingsIcon);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(SettingsFragment.newInstance());
            }
        });
        ImageView arrowtowardrobe = view.findViewById(R.id.arrowtowardrobe);
        arrowtowardrobe.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
            }
        });
        TextView editing=view.findViewById(R.id.redact);
        editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(EditingFragment.newInstance());
            }
        });
        TextView texttowardrobe=view.findViewById(R.id.shielf);
        texttowardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
            }
        });

    }
}