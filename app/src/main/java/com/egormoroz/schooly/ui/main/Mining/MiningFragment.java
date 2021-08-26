package com.egormoroz.schooly.ui.main.Mining;

import android.nfc.Tag;
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
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Mining.MiningAdapter;
import com.egormoroz.schooly.ui.main.Mining.MyMinersFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MiningFragment extends Fragment {

    public static MiningFragment newInstanse() {
        return new MiningFragment();
    }

    ArrayList<Miner> listAdapterMiner = new ArrayList<Miner>();
    ArrayList<Miner> allminersarraylist = new ArrayList<Miner>();
    private FirebaseModel firebaseModel = new FirebaseModel();
    FirebaseAuth authenticationDatabase;
    FirebaseDatabase database;
    final String databaseUrl = CONST.RealtimeDatabaseUrl;
    DatabaseReference reference,ref;
    String nick;
    int money = 100;
    int moneyaftermaining = 0;
    int prise = 50;
    private View imageworkingminers;
    ImageView viewminer;
    TextView minerprise, schoolycoin, myminers, upgrade, todaymining, morecoins,buy;
    RelativeLayout noactiveminers;
    FirebaseAuth AuthenticationBase;
    RecyclerView minersrecyclerview,allminersrecyclerview;
    int minerMoney;
    Miner minersInBase;
    DataSnapshot dataSnapshot;
    private static final String TAG = "###########";
    Miner adapterData=new Miner(120, 120, 50);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mining, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        initFirebase();
        buy=root.findViewById(R.id.buy);
//        buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buyMiner();
//            }
//        });
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
        minersrecyclerview = view.findViewById(R.id.minersrecyclerview);
        MiningAdapter miningAdapter = new MiningAdapter(listAdapterMiner);
        minersrecyclerview.setAdapter(miningAdapter);
        allminersrecyclerview=view.findViewById(R.id.allminersrecyclerview);
        AllMinersAdapter allMinersAdapter=new AllMinersAdapter(listAdapterMiner);
        allminersrecyclerview.setAdapter(allMinersAdapter);
        viewminer = view.findViewById(R.id.viewmining);
        minerprise = view.findViewById(R.id.minerprise);
//        minerprise.setText(minerr.getMinerPrice());
        schoolycoin = view.findViewById(R.id.schoolycoin);
        schoolycoin.setText(String.valueOf(money));
        upgrade = view.findViewById(R.id.upgrade);
        todaymining = view.findViewById(R.id.todaymining);
        buy=view.findViewById(R.id.buy);
        morecoins = view.findViewById(R.id.morecoins);
        morecoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyMiner();
            }
        });
        SendDataInBase();
        setMinersData();
        miningMoney();
    }

    public void initFirebase() {
        AuthenticationBase = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(databaseUrl);
        reference = database.getReference("AppData");
        ref = database.getReference("users");
    }

    public void AllMiners() {
        allminersarraylist.add(new Miner(5, 0, 130));
        allminersarraylist.add(new Miner(7, 0, 145));
//        listAdapterMiner.add(new Miner(7, 0,135));
//        listAdapterMiner.add(new Miner(9, 0,150));
//        listAdapterMiner.add(new Miner(11, 0,175));
//        listAdapterMiner.add(new Miner(15, 0,200));
    }

    public void setMinersData(){
        listAdapterMiner.add(minersInBase);
    }

    public void SendDataInBase(){
        if(allminersarraylist!=null&&allminersarraylist.size()>=1)
        reference.child("AllMiners").push().setValue(allminersarraylist.get(0));
    }

    public void GetDataFromBase(){
        RecentMethods.MinerByNick("minerPrice", firebaseModel, new Callbacks.GetMinerByMinerPrice() {
            @Override
            public void PassMiner(String miner) {
                minersInBase=dataSnapshot.child("AllMiners").getValue(Miner.class);
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
                            public void run()
                            { minerMoney++;
                                todaymining.setText(String.valueOf(minerMoney));
                            }
                        });
                    }
                    catch (InterruptedException e){
                    }
            }
        })).start();
    }

    public void buyMiner(){
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(String.valueOf(firebaseModel.AuthenticationBase.getCurrentUser().getUid()),
                        firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                ref.child(nick).child("Miners").push().setValue(minersInBase);
                            }
                        });
            }
        });
    }
}