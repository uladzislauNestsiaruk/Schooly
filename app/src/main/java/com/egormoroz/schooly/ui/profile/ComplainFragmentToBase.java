package com.egormoroz.schooly.ui.profile;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ComplainFragmentToBase extends Fragment {

    public ComplainFragmentToBase(String otherUserNick) {
        this.otherUserNick = otherUserNick;
    }

    public static ComplainFragmentToBase newInstance(String otherUserNick) {
        return new ComplainFragmentToBase(otherUserNick);
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    TextView reasonText,sendReason;
    ImageView back;
    String reasonTextString,otherUserNick,descriptionText;
    EditText addDescriptionEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View root =inflater.inflate(R.layout.fragment_complaintobase,container,false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();

        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        reasonText=view.findViewById(R.id.reasonText);
        sendReason=view.findViewById(R.id.sendReasons);
        addDescriptionEdit=view.findViewById(R.id.addDescriptionEdit);
        back=view.findViewById(R.id.backtoOtherUser);
        ComplainAdapter.complain(new ComplainAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Reason reason) {
                reasonTextString=reason.getReason();
            }
        });
        reasonText.setText(reasonTextString);
        sendReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        descriptionText=addDescriptionEdit.getText().toString();
                        Random random = new Random();
                        int num =random.nextInt(1000000000);
                        firebaseModel.getReference().child("complains").child(String.valueOf(num))
                                .setValue(new Complain(nick,otherUserNick, reasonTextString,descriptionText));
                        Toast.makeText(getContext(), "Жалоба отправлена", Toast.LENGTH_SHORT).show();
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                RecentMethods.setCurrentFragment(ProfileFragment.newInstance("user", nick, PeopleFragment.newInstance()), getActivity());
                            }
                        });
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ComplainFragment.newInstance(otherUserNick), getActivity());
            }
        });
    }
}
