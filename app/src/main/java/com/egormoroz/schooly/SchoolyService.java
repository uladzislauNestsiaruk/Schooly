package com.egormoroz.schooly;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Map;

public class SchoolyService extends Service {
    FirebaseModel firebaseModel=new FirebaseModel();
    double todayMining,minInGap;
    long a,d,min;
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
                                    Log.d("#######", "min  " + min);
                                    minInGap=Double.valueOf(String.valueOf(min));
                                    MiningMoneyGap();
                                }
                            });
                        }else {
                        }
                    }
                });
            }
        });
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
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
                        firebaseModel.getUsersReference().child(nick)
                                .child("todayMining").setValue(todayMining);
                        Log.d("######", "fgtg  "+todayMining);
                    }
                });
            }
        }, 1000);
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
                            Log.d("#######", "ddd  " + minInGap);
                            Log.d("#######", "a  " + getFirstMinerInHour);
                            double miningMoneyInGap = minInGap * (getFirstMinerInHour / 60);
                            RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                                @Override
                                public void GetTodayMining(double todayMiningFromBase) {
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("todayMining").setValue(miningMoneyInGap);
                                    Log.d("#######", "dd  " + miningMoneyInGap+todayMiningFromBase);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
