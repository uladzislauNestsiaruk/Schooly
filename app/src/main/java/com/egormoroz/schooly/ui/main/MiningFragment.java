package com.egormoroz.schooly.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MiningFragment extends Fragment {
    public MiningFragment() {
    }

    public static MiningFragment newInstanse(){return new MiningFragment();}
    ArrayList<AdapterData> listAdapterMiner=new ArrayList<AdapterData>();
    FirebaseAuth authenticationDatabase;
    FirebaseDatabase database;
    DatabaseReference ref;
    int money=100;
    int moneyaftermaining=0;
    int prise=50;
    private View imageworkingminers;
    int viewminer;
    TextView minerprise,schoolycoin,myminers,upgrade,todaymining,morecoins;
    RelativeLayout noactiveminers;
    FirebaseAuth AuthenticationBase;
    RecyclerView minersrecyclerview;
    private static final String TAG = "###########";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mining, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myminers=view.findViewById(R.id.myminers);
        myminers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(MyMinersFragment.newInstanse(), getActivity());
            }
        });
        ImageView backtomainfrommining=view.findViewById(R.id.backtomainfrommining);
        backtomainfrommining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
            }
        });
        MyMinersFragment myMinersFragment=new MyMinersFragment();
        TextView buy=view.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money>=prise){
                    schoolycoin.setText(String.valueOf(money-prise));
                }
            }
        });
        setMinerData();
        minersrecyclerview=view.findViewById(R.id.minersrecyclerview);
        MiningAdapter miningAdapter= new MiningAdapter(listAdapterMiner);
        minersrecyclerview.setAdapter(miningAdapter);
        viewminer=requireViewById(R.id.viewmining);
        minerprise=view.findViewById(R.id.minerprise);
        minerprise.setText(prise+"S");
        schoolycoin=view.findViewById(R.id.schoolycoin);
        schoolycoin.setText(String.valueOf(money));
        upgrade=view.findViewById(R.id.upgrade);
        todaymining=view.findViewById(R.id.todaymining);
        morecoins=view.findViewById(R.id.morecoins);
        miningMoney();
    }

    private int requireViewById(int viewmining) {
        return viewmining;
    }

    public void setMinerData(){
        listAdapterMiner.add(new AdapterData("120S в час",1333));
    }

    public void miningMoney() {
        morecoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread run = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        int everyminute=1;
                        int minermoney=+everyminute;
                        todaymining.setText(String.valueOf(minermoney));
                        while(true){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                });
                run.start();
            }
        });
    }
}
