package com.egormoroz.schooly;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import com.egormoroz.schooly.ui.main.Mining.Miner;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SchoolyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    FirebaseModel firebaseModel=new FirebaseModel();
        Miner firstMiner,secondMiner,thirdMiner,fourthMiner,fifthMiner;
    double firstMinerHour,secondMinerHour,thirdMinerHour,fourthMinerHour,fifthMinerHour;
    double firstMinerInHour,secondMinerInHour,thirdMinerInHour,fourthMinerInHour,fifthMinerInHour;
    static double todayMining;
    Thread thread;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    @Override
    public void onCreate() {
        super.onCreate();
        firebaseModel.initAll();
        registerActivityLifecycleCallbacks(this);
    }

    public void startMining(){
        if(activityReferences==1){
            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                @Override
                public void PassUserNick(String nick) {
                    RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                        @Override
                        public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                            if (activeMinersFromBase.size() > 0) {
                                thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                                @Override
                                                public void PassUserNick(String nick) {
                                                    RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                                                        @Override
                                                        public void GetTodayMining(double todayMiningFromBase) {
                                                            todayMining = todayMiningFromBase;
                                                        }
                                                    });
                                                }
                                            });
                                            while (true) {
                                                Thread.sleep(1000);
                                                miningMoneyFun();
                                                Log.d("#####", "goofffffd" + todayMining);
                                            }
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                };

                                thread.start();
                            }
                        }
                    });
                }
            });
        }else{

        }
    }
    public void miningMoneyFun(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                    @Override
                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                        if (activeMinersFromBase.size()==1) {
                            firstMiner = activeMinersFromBase.get(0);
                            firstMinerHour = Double.valueOf(String.valueOf(firstMiner.getInHour()));
                            firstMinerInHour = firstMinerHour / 3600;
                            todayMining = todayMining + firstMinerInHour;
                            firebaseModel.getUsersReference().child(nick)
                                    .child("todayMining").setValue(todayMining);
                        }else if (activeMinersFromBase.size()==2){
                            secondMiner = activeMinersFromBase.get(1);
                            secondMinerHour = Double.valueOf(String.valueOf(secondMiner.getInHour()));
                            secondMinerInHour = secondMinerHour / 3600;
                            todayMining = todayMining + firstMinerInHour+secondMinerInHour;
                            firebaseModel.getUsersReference().child(nick)
                                    .child("todayMining").setValue(todayMining);
                        }else if (activeMinersFromBase.size()==3){
                            thirdMiner = activeMinersFromBase.get(2);
                            thirdMinerHour = Double.valueOf(String.valueOf(thirdMiner.getInHour()));
                            thirdMinerInHour = thirdMinerHour / 3600;
                            todayMining = todayMining + firstMinerInHour+secondMinerInHour+thirdMinerInHour;
                            firebaseModel.getUsersReference().child(nick)
                                    .child("todayMining").setValue(todayMining);
                        }
                        else if (activeMinersFromBase.size()==4){
                            fourthMiner = activeMinersFromBase.get(3);
                            fourthMinerHour = Double.valueOf(String.valueOf(fourthMiner.getInHour()));
                            fourthMinerInHour = fourthMinerHour / 3600;
                            todayMining = todayMining + firstMinerInHour+secondMinerInHour+thirdMinerInHour+fourthMinerInHour;
                            firebaseModel.getUsersReference().child(nick)
                                    .child("todayMining").setValue(todayMining);
                        }else if (activeMinersFromBase.size()==5){
                            fifthMiner = activeMinersFromBase.get(4);
                            fifthMinerHour = Double.valueOf(String.valueOf(fifthMiner.getInHour()));
                            fifthMinerInHour = fifthMinerHour / 3600;
                            todayMining = todayMining + firstMinerInHour+secondMinerInHour+thirdMinerInHour+fourthMinerInHour+fifthMinerInHour;
                            firebaseModel.getUsersReference().child(nick)
                                    .child("todayMining").setValue(todayMining);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            //startMining();
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            thread.stop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//
//        PeriodicWorkRequest miningWorkRequest = new
//                PeriodicWorkRequest.Builder(MiningManager.class,15,TimeUnit.MINUTES)
//                .setConstraints(constraints)
//                .build();
//
//        PeriodicWorkRequest notificationWorkRequest = new
//                PeriodicWorkRequest.Builder(NontificationManager.class, 15, TimeUnit.MINUTES)
//                .setConstraints(constraints
//                )
//                .build();
//
//        WorkManager.getInstance(getApplicationContext()).enqueue(notificationWorkRequest);
//        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("###", ExistingPeriodicWorkPolicy.KEEP,miningWorkRequest);
}

