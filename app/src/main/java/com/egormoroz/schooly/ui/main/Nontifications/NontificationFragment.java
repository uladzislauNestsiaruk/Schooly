package com.egormoroz.schooly.ui.main.Nontifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class NontificationFragment extends Fragment {
    public static NontificationFragment newInstance() {
        return new NontificationFragment();
    }

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView nontsRecyclerView;
    TextView emptyNonts;
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
        emptyNonts=view.findViewById(R.id.emptyNonts);
        getNontificationList();
    }

    public  void  getNontificationList(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getNontificationsList(nick, firebaseModel, new Callbacks.getNontificationsList() {
                    @Override
                    public void getNontificationsList(ArrayList<Nontification> nontifications) {
                        if (nontifications.size()==0){
                            emptyNonts.setVisibility(View.VISIBLE);
                            emptyNonts.setText("Пока нет уведомлений :)");
                        }else {
                            emptyNonts.setVisibility(View.GONE);
                        }
                        Collections.reverse(nontifications);
                        NontificationAdapter nontificationAdapter=new NontificationAdapter(nontifications);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        nontsRecyclerView.setLayoutManager(layoutManager);
                        nontsRecyclerView.setAdapter(nontificationAdapter);
                        NontificationAdapter.ItemClickListener itemClickListener=new NontificationAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(String clothesUid) {
                                RecentMethods.setCurrentFragment(ClothesRequestFragment.newInstance(NontificationFragment.newInstance(),clothesUid), getActivity());
                            }
                        };
                        nontificationAdapter.setClickListener(itemClickListener);
                    }
                });
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }


}
