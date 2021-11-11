package com.egormoroz.schooly.ui.main.Nontifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NontificationFragment extends Fragment {
    public static NontificationFragment newInstance() {
        return new NontificationFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView nontsRecyclerView;
    ArrayList<Subscriber> arrayListNonts=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nontifications, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backToMain = view.findViewById(R.id.backtomainfromnonts);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setCurrentFragment(MainFragment.newInstance());
            }
        });
        nontsRecyclerView=view.findViewById(R.id.nontificationsrecyclerview);
        getNontificationList();
    }

    public  void  getNontificationList(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getNontificationsList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                    @Override
                    public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                        arrayListNonts.addAll(subscribers);
                        NontificationAdapter nontificationAdapter=new NontificationAdapter(arrayListNonts);
                        nontsRecyclerView.setAdapter(nontificationAdapter);
                        Log.d("####","ddddddff"+subscribers);
                    }
                });
            }
        });
    }
}
