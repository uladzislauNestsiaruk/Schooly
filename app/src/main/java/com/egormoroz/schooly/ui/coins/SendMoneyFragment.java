package com.egormoroz.schooly.ui.coins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.profile.ComplainFragmentToBase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ServerValue;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class SendMoneyFragment extends Fragment {

    ImageView backToCoins;
    TextView otherUserNickText,sum;
    String otherUserNick,sumText;
    long moneyBase,moneyBaseOther,sumLong;
    RelativeLayout transfer;

    FirebaseModel firebaseModel=new FirebaseModel();
    Fragment fragment;

    public SendMoneyFragment(String otherUserNick,Fragment fragment) {
        this.otherUserNick = otherUserNick;
        this.fragment=fragment;
    }

    public static SendMoneyFragment newInstance(String otherUserNick,Fragment fragment) {
        return new SendMoneyFragment(otherUserNick,fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sendcoins, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        firebaseModel.initAll();
        otherUserNickText=view.findViewById(R.id.toWhomTransfer);
        sum=view.findViewById(R.id.sum);
        otherUserNickText.setText(otherUserNick);
        transfer=view.findViewById(R.id.createBigButtonRecycler);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        moneyBase=money;
                    }
                });
            }
        });
        RecentMethods.GetMoneyFromBase(otherUserNick, firebaseModel, new Callbacks.MoneyFromBase() {
            @Override
            public void GetMoneyFromBase(long money) {
                moneyBaseOther=money;
            }
        });
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumText=sum.getText().toString();
                if (sumText.length()==0){
                    Toast.makeText(getContext(), "Укажите сумму перевода", Toast.LENGTH_SHORT).show();
                }else {
                    sumLong=Long.valueOf(sumText);
                    if(sumLong==0){
                        Toast.makeText(getContext(), "Сумма перевода должна быть больше нуля", Toast.LENGTH_SHORT).show();
                    }else if(sumLong>moneyBase){
                        Toast.makeText(getContext(), "Недостаточно средств для перевода", Toast.LENGTH_SHORT).show();
                    }else if(sumLong<moneyBase && !sumText.equals("0")){
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                firebaseModel.getUsersReference().child(otherUserNick).child("money")
                                        .setValue(sumLong+moneyBaseOther);
                                firebaseModel.getUsersReference().child(nick).child("money")
                                        .setValue(moneyBase-sumLong);
                                Toast.makeText(getContext(), "Перевод выполнен", Toast.LENGTH_SHORT).show();
                                Random random = new Random();
                                long num =random.nextInt(1000000000);
                                Date date = new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd hh:mm a");
                                String dateAndTime = formatter.format(date);
                                firebaseModel.getUsersReference().child(otherUserNick).child("transferHistory")
                                        .child(String.valueOf(num)).setValue(new Transfer(sumLong, nick, "from"));
                                firebaseModel.getUsersReference().child(nick).child("transferHistory")
                                        .child(String.valueOf(num)).setValue(new Transfer(sumLong, otherUserNick, "to"));
                                String uid=firebaseModel.getReference().child("users")
                                        .child(otherUserNick).child("nontifications").push().getKey();
                                firebaseModel.getReference().child("users")
                                        .child(otherUserNick).child("nontifications")
                                        .child(uid).setValue(new Nontification(nick,"не отправлено","перевод"
                                        , dateAndTime," "," ","не просмотрено",String.valueOf(sumLong)));
                                RecentMethods.setCurrentFragment(TransferMoneyFragment.newInstance(fragment), getActivity());
                            }
                        });
                    }
                }
            }
        });
        backToCoins=view.findViewById(R.id.backtocoins);
        backToCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(TransferMoneyFragment.newInstance(SendMoneyFragment.newInstance(otherUserNick,fragment)), getActivity());
            }
        });

    }
}
