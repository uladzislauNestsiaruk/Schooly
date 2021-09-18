package com.egormoroz.schooly.ui.main.Mining;

import android.os.Bundle;
import android.text.format.DateFormat;
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

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class MiningFragment extends Fragment {

    public static MiningFragment newInstanse() {
        return new MiningFragment();
    }

    ArrayList<Miner> listAdapterMiner = new ArrayList<Miner>();
    ArrayList<Miner> listAdapterAverageMiner = new ArrayList<Miner>();
    ArrayList<Miner> listAdapterStrongMiner = new ArrayList<Miner>();
    ArrayList<Miner> listAdapterActiveMiner = new ArrayList<Miner>();
    ArrayList<Miner> allminersarraylist = new ArrayList<Miner>();
    private FirebaseModel firebaseModel = new FirebaseModel();
    ImageView viewminer;
    double todayMining;
    Map<String,String> timeStamp;
    TextView minerprice, schoolycoinminer, myminers, upgrade, todayminingText, morecoins,buy;
    RecyclerView activeminersrecyclerview,weakminersrecyclerview,averageminersrecyclerview,strongminersrecyclerview;
    private static final String TAG = "###########";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mining, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myminers = view.findViewById(R.id.myminers);
        myminers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(MyMinersFragment.newInstanse(), getActivity());
            }
        });
        ImageView backtomainfrommining = view.findViewById(R.id.backtomainfrommining);
        backtomainfrommining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(MainFragment.newInstance(), getActivity());
            }
        });
        activeminersrecyclerview = view.findViewById(R.id.activeminersrecyclerview);
        viewminer = view.findViewById(R.id.viewmining);
        minerprice = view.findViewById(R.id.minerprice);
        schoolycoinminer = view.findViewById(R.id.schoolycoin);
        upgrade = view.findViewById(R.id.upgrade);
        todayminingText = view.findViewById(R.id.todaymining);
        buy=view.findViewById(R.id.buy);
        morecoins = view.findViewById(R.id.morecoins);
        GetDataFromBase();
        getActiveMinersFromBase();
        SetSchoolyCoin();
        miningMoney();
        todayminingText.setText(String.valueOf(0));
        weakminersrecyclerview=view.findViewById(R.id.allminersrecyclerview);
        averageminersrecyclerview=view.findViewById(R.id.averageminersrecyclerview);
        strongminersrecyclerview=view.findViewById(R.id.strongminersrecyclerview);
    }


    public void GetDataFromBase(){
        RecentMethods.AllminersFromBase(firebaseModel, new Callbacks.GetMinerFromBase() {
            @Override
            public void GetMinerFromBase(ArrayList<Miner> minersFromBase) {
                listAdapterMiner.addAll(minersFromBase);
                WeakMinersAdapter allMinersAdapter=new WeakMinersAdapter(listAdapterMiner);
                weakminersrecyclerview.setAdapter(allMinersAdapter);
                weakminersrecyclerview.addItemDecoration(new WeakMinersAdapter.SpaceItemDecoration());
            }
        });
        RecentMethods.AverageMinersFromBase(firebaseModel, new Callbacks.GetMinerFromBase() {
            @Override
            public void GetMinerFromBase(ArrayList<Miner> minersFromBase) {
                listAdapterAverageMiner.addAll(minersFromBase);
                AverageMinersAdapter avarageMinersAdapter=new AverageMinersAdapter(listAdapterAverageMiner);
                averageminersrecyclerview.setAdapter(avarageMinersAdapter);
                averageminersrecyclerview.addItemDecoration(new AverageMinersAdapter.SpaceItemDecoration());
            }
        });
        RecentMethods.StrongMinersFromBase(firebaseModel, new Callbacks.GetMinerFromBase() {
            @Override
            public void GetMinerFromBase(ArrayList<Miner> minersFromBase) {
                listAdapterStrongMiner.addAll(minersFromBase);
                StrongMinersAdapter strongMinersAdapter=new StrongMinersAdapter(listAdapterStrongMiner);
                strongminersrecyclerview.setAdapter(strongMinersAdapter);
                strongminersrecyclerview.addItemDecoration(new StrongMinersAdapter.SpaceItemDecoration());
            }
        });
    }

    public void miningMoney() {
        (new Thread(new Runnable(){
            @Override
            public void run(){
                while (!Thread.interrupted())
                    try{
                        Thread.sleep(1000);
                        if(getActivity() == null)
                            return;
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                    @Override
                                    public void PassUserNick(String nick) {
                                        RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                                            @Override
                                            public void GetTodayMining(double todayMiningFromBase) {
                                                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                                                    @Override
                                                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                                                        Miner rgktk =activeMinersFromBase.get(0);
                                                        double bbbb=Double.valueOf(String.valueOf(rgktk.getInHour()));
                                                        todayMining= todayMiningFromBase+bbbb;
                                                        Log.d("######", "ggggtg  "+todayMiningFromBase);
                                                    }
                                                });
                                            }
                                        });
                                        todayminingText.setText(String.valueOf(todayMining));
                                        firebaseModel.getUsersReference().child(nick)
                                                .child("todayMining").setValue(todayMining);

                                        Log.d("######", "fgtg  "+todayMining);
                                    }
                                });
                            }
                        });
                    }
                    catch (InterruptedException e){
                    }
            }
        })).start();
    }

    public void getActiveMinersFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetActiveMiner(nick, firebaseModel,
                        new Callbacks.GetActiveMiners() {
                            @Override
                            public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                                listAdapterActiveMiner.addAll(activeMinersFromBase);
                                Log.d("#########","list  "+activeMinersFromBase);
                                ActiveMinersAdapter activeMinersAdapter=new ActiveMinersAdapter(listAdapterActiveMiner);
                                activeminersrecyclerview.setAdapter(activeMinersAdapter);
                            }
                        });
            }
        });
    }

    public void SetSchoolyCoin(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        schoolycoinminer.setText(String.valueOf(money));
                    }
                });
            }
        });
    }
}