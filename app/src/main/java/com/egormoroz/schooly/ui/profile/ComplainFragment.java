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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComplainFragment extends Fragment {
    String otherUserNick;
    TextView sendReasons;

    public ComplainFragment(String otherUserNick) {
        this.otherUserNick = otherUserNick;
    }

    public static ComplainFragment newInstance(String otherUserNick) {
        return new ComplainFragment(otherUserNick);
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    TextView otherUserNickText;
    ImageView back;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View root =inflater.inflate(R.layout.fragment_complain,container,false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();

        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        otherUserNickText=view.findViewById(R.id.complainOtherUserText);
        otherUserNickText.setText(otherUserNick);
        recyclerView=view.findViewById(R.id.reasonsRecycler);
        back=view.findViewById(R.id.backtoOtherUser);
        RecentMethods.getComplainReasonList(firebaseModel, new Callbacks.getComplainReasonsList() {
            @Override
            public void getComplainReasonsList(ArrayList<Reason> reason) {
                ComplainAdapter complainAdapter=new ComplainAdapter(reason);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(complainAdapter);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query1=firebaseModel.getReference().child("users").child(otherUserNick);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserInformation userData=new UserInformation();
                        userData.setAge(snapshot.child("age").getValue(Long.class));
                        userData.setAvatar(snapshot.child("avatar").getValue(Long.class));
                        userData.setGender(snapshot.child("gender").getValue(String.class));
                        //////////////////userData.setMiners();
                        userData.setNick(snapshot.child("nick").getValue(String.class));
                        userData.setPassword(snapshot.child("password").getValue(String.class));
                        userData.setPhone(snapshot.child("phone").getValue(String.class));
                        userData.setUid(snapshot.child("uid").getValue(String.class));
                        userData.setQueue(snapshot.child("queue").getValue(String.class));
                        userData.setAccountType(snapshot.child("accountType").getValue(String.class));
                        userData.setBio(snapshot.child("bio").getValue(String.class));
                        //                                               userData.setSubscribers(snapshot.child("subscribers").getValue(String.class));
//                                                userData.setFriends(snapshot.child("friends").getValue(String.class));
                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userData),
                                getActivity());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        sendReasons=view.findViewById(R.id.sendReasons);
        ComplainAdapter.getReasonsList(new ComplainAdapter.reasonsList() {
            @Override
            public void sendList(ArrayList<Reason> reasons) {
                if(reasons.size()>0){
                    sendReasons.setBackgroundResource(R.drawable.corners14appcolor);
                }
            }
        });
    }
}