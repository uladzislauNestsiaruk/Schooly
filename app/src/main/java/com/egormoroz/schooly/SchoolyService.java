package com.egormoroz.schooly;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class SchoolyService extends Service {
    FirebaseModel firebaseModel=new FirebaseModel();
    double todayMining,minInGap,miningMoneyInGap;
    long a,d,min;
    int aee=8;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        firebaseModel.initAll();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                    @Override
                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                        ArrayList<Miner> getActiveMinersArrayList=new ArrayList<>();
                        getActiveMinersArrayList=activeMinersFromBase;
                        if(getActiveMinersArrayList.size()>0) {
                            firebaseModel.getUsersReference().child(nick).child("serverTimeNow")
                                    .setValue(ServerValue.TIMESTAMP);
                            RecentMethods.GetTimeStampNow(nick, firebaseModel, new Callbacks.GetTimesTamp() {
                                @Override
                                public void GetTimesTamp(long timesTamp) {
                                    a = timesTamp;
                                }
                            });
                            RecentMethods.GetTimesTamp(nick, firebaseModel, new Callbacks.GetTimesTamp() {
                                @Override
                                public void GetTimesTamp(long timesTamp) {
                                    d = timesTamp;
                                    long timeGap = a - d;
                                    long days = (timeGap / (1000 * 60 * 60 * 24));
                                    long hours = ((timeGap - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                                    min = (timeGap - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                                    minInGap=Double.valueOf(String.valueOf(min+hours*60+days*24*60));
                                    MiningMoneyGap();
                                    Log.d("########", "vv  "+minInGap);
                                }
                            });
                        }else {
                        }
                    }
                });
            }
        });
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(1000);
                        miningMoneyFun();
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void MiningMoneyGap(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                    @Override
                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                        ArrayList<Miner> getActiveMinersArrayList=new ArrayList<>();
                        getActiveMinersArrayList=activeMinersFromBase;
                        if(getActiveMinersArrayList.size()==1) {
                            Miner getFirstActiveMiner = getActiveMinersArrayList.get(0);
                            long getInHourMiner = getFirstActiveMiner.getInHour();
                            double getFirstMinerInHour = Double.valueOf(String.valueOf(getInHourMiner));
                            miningMoneyInGap = minInGap * (getFirstMinerInHour / 60);
                            firebaseModel.getUsersReference().child(nick)
                                    .child("todayMining").setValue(miningMoneyInGap);
                            Log.d("##########","gaaap  "+miningMoneyInGap);
                        }
                    }
                });
            }
        });
    }

    public void miningMoneyFun(){
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                            @Override
                            public void GetTodayMining(double todayMiningFromBase) {
                                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                                    @Override
                                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                                        Miner firstMiner =activeMinersFromBase.get(0);
                                        double minerInHour=Double.valueOf(String.valueOf(firstMiner.getInHour()));
                                        double firstMinerInHour=minerInHour/60;
                                        todayMining= todayMiningFromBase+firstMinerInHour;
                                        Log.d("#####","base  "+todayMiningFromBase);
                                    }
                                });
                            }
                        });
                        firebaseModel.getUsersReference().child(nick)
                                .child("todayMining").setValue(todayMining);
                        Log.d("##########","today  "+todayMining);
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
