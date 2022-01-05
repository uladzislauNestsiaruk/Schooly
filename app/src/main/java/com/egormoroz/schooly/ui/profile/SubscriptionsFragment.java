package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SubscriptionsFragment extends Fragment {
    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    ImageView back;
    TextView emptyList;

    public static SubscriptionsFragment newInstance() {
        return new SubscriptionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.friendsRecycler);
        emptyList=view.findViewById(R.id.emptySubscriptionList);
        back=view.findViewById(R.id.back_toprofile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(ProfileFragment.newInstance("user",new UserInformation()),getActivity());
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getSubscriptionList(nick, firebaseModel, new Callbacks.getFriendsList() {
                    @Override
                    public void getFriendsList(ArrayList<Subscriber> friends) {
                        if (friends.size()==0){
                            emptyList.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            SubscriptionsAdapter subscribersAdapter = new SubscriptionsAdapter(friends);
                            recyclerView.setAdapter(subscribersAdapter);
                        }
                    }
                });
            }
        });
    }
}
