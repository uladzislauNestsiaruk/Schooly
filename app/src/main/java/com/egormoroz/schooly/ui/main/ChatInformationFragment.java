package com.egormoroz.schooly.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.egormoroz.schooly.ui.profile.Complain;
import com.egormoroz.schooly.ui.profile.ComplainAdapter;
import com.egormoroz.schooly.ui.profile.ComplainFragment;
import com.egormoroz.schooly.ui.profile.ComplainFragmentToBase;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.egormoroz.schooly.ui.profile.Reason;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Random;

public class ChatInformationFragment extends Fragment {

    public ChatInformationFragment(String otherUserNick) {
        this.otherUserNick = otherUserNick;
    }

    public static ChatInformationFragment newInstance(String otherUserNick) {
        return new ChatInformationFragment(otherUserNick);
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    TextView otherUserNickView,deleteHistory;
    ImageView otherUserImage;
    String otherUserNick;
    SwitchMaterial

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chatsinfo, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        otherUserNickView=view.findViewById(R.id.userNick);
        otherUserImage=view.findViewById(R.id.userImage);
        recyclerView=view.findViewById(R.id.recycler);
        deleteHistory=view.findViewById(R.id.deleteHistory);
        otherUserNickView.setText(otherUserNick);
    }
}
