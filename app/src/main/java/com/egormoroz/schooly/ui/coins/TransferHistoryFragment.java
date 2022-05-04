package com.egormoroz.schooly.ui.coins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class TransferHistoryFragment extends Fragment {

    ArrayList<UserInformation> listAdapterPeople=new ArrayList<UserInformation>();
    RecyclerView historyRecyclerView;
    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView backToCoins;
    TextView noTransfer;
    Fragment fragment;
    UserInformation userInformation;

    public TransferHistoryFragment(Fragment fragment,UserInformation userInformation) {
        this.fragment = fragment;
        this.userInformation=userInformation;
    }

    public static TransferHistoryFragment newInstance(Fragment fragment,UserInformation userInformation) {
        return new TransferHistoryFragment(fragment,userInformation);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transferhistory, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        backToCoins=view.findViewById(R.id.backtocoins);
        historyRecyclerView=view.findViewById(R.id.hisroryRecycler);
        noTransfer=view.findViewById(R.id.noTransfer);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(TransferMoneyFragment.newInstance(fragment,userInformation), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        backToCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(TransferMoneyFragment.newInstance(fragment,userInformation), getActivity());
            }
        });
        firebaseModel.initAll();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("transferHistory").orderByKey();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Transfer> transferHistoryBase=new ArrayList<>();
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            Transfer transfer=new Transfer();
                            transfer.setSum(snap.child("sum").getValue(Long.class));
                            transfer.setType(snap.child("type").getValue(String.class));
                            transfer.setWho(snap.child("who").getValue(String.class));
                            transferHistoryBase.add(transfer);
                        }
                        if (transferHistoryBase.size()==0){
                            noTransfer.setVisibility(View.VISIBLE);
                        }else {
                            Collections.reverse(transferHistoryBase);
                            TransferHistoryAdapter transferHistoryAdapter=new TransferHistoryAdapter(transferHistoryBase);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            historyRecyclerView.setLayoutManager(layoutManager);
                            historyRecyclerView.setAdapter(transferHistoryAdapter);
                            noTransfer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
