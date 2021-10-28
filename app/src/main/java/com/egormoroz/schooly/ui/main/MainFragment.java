package com.egormoroz.schooly.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.text.DecimalFormat;

public class MainFragment extends Fragment{
    protected DialogsListAdapter<IDialog> dialogsAdapter;
    protected ImageLoader imageLoader;
    private MainViewModel mainViewModel;
    private TextView todayMiningMain;
    private FirebaseModel firebaseModel = new FirebaseModel();
    private UserInformation userData = new UserInformation();

    public static MainFragment newInstance() {
        return new MainFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(View.VISIBLE);
        firebaseModel.initAll();
        return root;
    }
    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ImageView chat=view.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DialogsActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                startActivity(intent);
            }
        });
        ImageView nontifications=view.findViewById(R.id.nontification);
        nontifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(GenderFragment.newInstance());
//
            }
        });
        TextView shop=view.findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment((ShopFragment.newInstance()));
            }
        });
        TextView mining=view.findViewById(R.id.mining);
        mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(MiningFragment.newInstanse(), getActivity());
            }
        });

        TextView schoolycoins=view.findViewById(R.id.schoolycoins);
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                            @Override
                            public void GetMoneyFromBase(long money) {
                                schoolycoins.setText(String.valueOf(money));
                            }
                        });
                    }
                });
        todayMiningMain=view.findViewById(R.id.todayminingmain);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                    @Override
                    public void GetTodayMining(double todayMiningFromBase) {
                        String todayMiningFormatted = new DecimalFormat("#0.00").format(todayMiningFromBase);
                        todayMiningMain.setText(todayMiningFormatted);
                    }
                });
            }
        });
    }


}