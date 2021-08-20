package com.egormoroz.schooly.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MyMinersFragment extends Fragment {

    private String TAG;
    ArrayList<Miner> listAdapter=new ArrayList<Miner>();
    public static MyMinersFragment newInstanse(){return new MyMinersFragment();}
    RecyclerView recyclerviewMining;
    TextView useMiner;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myminers, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView backtomoning=view.findViewById(R.id.back_tomining);
        backtomoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(MiningFragment.newInstanse());
            }
        });
        setData();
        recyclerviewMining=view.findViewById(R.id.recyclerviewmyminers);
        useMiner=view.findViewById(R.id.use);
        MyMinersAdapter myminersAdapter=new MyMinersAdapter(listAdapter);
        recyclerviewMining.setAdapter(myminersAdapter);
        Log.d(TAG,"Изображение установлено");
    }

    public void setData(){
        listAdapter.add(new Miner(120,1333,50));
    }
}
