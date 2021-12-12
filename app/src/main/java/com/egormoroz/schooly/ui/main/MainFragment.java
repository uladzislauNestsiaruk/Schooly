package com.egormoroz.schooly.ui.main;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.SchoolyService;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.main.Nontifications.NontificationFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainFragment extends Fragment{

    TextView todayMiningMain;
    private FirebaseModel firebaseModel = new FirebaseModel();
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    ArrayList<Clothes> popularClothesArrayList=new ArrayList<Clothes>();
    private UserInformation userData = new UserInformation();
    RecyclerView clothesRecyclerMain;
    NewClothesAdapter.ItemClickListener itemClickListener;
    private static final int NOTIFY_ID = 101;

    private static final String CHANNEL_ID = "Tyomaa channel";


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
        TextView getMore=view.findViewById(R.id.getMore);
        getMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/miners%2Ffims.png?alt=media&token=adafb44e-3ac1-43a3-bde6-6f7c4315ee0c");
//                Intent intent = new Intent(Intent.ACTION_SEND);
//
//                Intent chooser =Intent.createChooser(intent, "Hello");
//                try {
//                    startActivity(chooser);
//                } catch (ActivityNotFoundException e) {
//                    // Define what your app should do if no activity can handle the intent.
//                }
                Uri location = Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/miners%2Ffims.png?alt=media&token=adafb44e-3ac1-43a3-bde6-6f7c4315ee0c");
                Intent intent = new Intent(Intent.ACTION_VIEW, location);
                Intent chooser =Intent.createChooser(intent, "Hello");

// Try to invoke the intent.
                try {
                    startActivity(chooser);
                } catch (ActivityNotFoundException e) {
                    // Define what your app should do if no activity can handle the intent.
                }
            }
        });
        ImageView nontifications=view.findViewById(R.id.nontification);
        nontifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(NontificationFragment.newInstance());
//
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Jordan 1").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
                        ,120,"Jordan 1",0,123,"Schooly"));
            }
        });
        TextView shop=view.findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment((ShopFragment.newInstance()));
            }
        });
        TextView tests=view.findViewById(R.id.tests);
        tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        clothesRecyclerMain=view.findViewById(R.id.newchlothesinshop);
//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @Override
//            public void PassUserNick(String nick) {
//                RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
//                    @Override
//                    public void GetTodayMining(double todayMiningFromBase) {
//                        todayMiningMain.setText(String.valueOf(todayMiningFromBase));
//                    }
//                });
//            }
//        });
        itemClickListener=new NewClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance());
            }
        };
        TextView appName=view.findViewById(R.id.appname);
        createNotificationChannel();
        appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getActivity(),
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_schoolycoin)
                        .setContentTitle("tyomaa")
                        .setContentText("hello")
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(getActivity());
                notificationManager.notify(NOTIFY_ID, builder.build());
                Log.d("######", "good");
//                Intent i = new Intent(getActivity(), ModelActivity.class);
//                startActivity(i);
//                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
        todayMiningMain=view.findViewById(R.id.todayminingmain);
        SchoolyService.getAAA(new SchoolyService.transmitMiningMoney() {
            @Override
            public void transmitMoney(double money) {
                String todayMiningFormatted = new DecimalFormat("#0.00").format(money);
                todayMiningMain.setText(todayMiningFormatted);
            }
        });
        loadClothesFromBase();
    }

    public void loadClothesFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                firebaseModel.getReference("usersNicks").child("tyomaa").setValue(new UserPeopleAdapter("tyomaa", 5, "hello"));
                firebaseModel.getReference("usersNicks").child("spaccacrani").setValue(new UserPeopleAdapter("spaccacrani", 5, "hello"));
                firebaseModel.getReference("usersNicks").child("Vladcpp").setValue(new UserPeopleAdapter("Vladcpp", 5, "hello"));
                RecentMethods.getClothes(firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        clothesArrayList.addAll(allClothes);
                        for(int i=0;i<clothesArrayList.size();i++){
                            Clothes cl=clothesArrayList.get(i);
                            popularClothesArrayList.add(cl);
                            Log.d("######", "x "+popularClothesArrayList);
//                            if (cl.getPurchaseNumber()==1){
//                                firebaseModel.getReference("AppData/Clothes/Popular").setValue()
//                            }
                        }
                        Log.d("#####", "size  "+clothesArrayList);
                        NewClothesAdapter newClothesAdapter=new NewClothesAdapter(popularClothesArrayList,itemClickListener);
                        clothesRecyclerMain.setAdapter(newClothesAdapter);
                    }
                });
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.chanel_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(getActivity(),NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




}