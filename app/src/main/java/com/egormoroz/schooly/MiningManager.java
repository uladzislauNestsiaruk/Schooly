package com.egormoroz.schooly;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.google.firebase.database.ServerValue;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MiningManager extends Worker {
    FirebaseModel firebaseModel=new FirebaseModel();
    static double todayMining;
    double minInGap;
    double miningMoneyInGap;
    double todayMiningBase;
    double t;
    double getFirstMinerInHour;
    double getSecondMinerInHour;
    double getThirdMinerInHour;
    double getFourthMinerInHour;
    double getFifthMinerInHour;
    long a,d,min,getInHourMinerFirst,getInHourMinerSecond,getInHourMinerThird,getInHourMinerFourth,getInHourMinerFifth;
    Miner getFirstActiveMiner, getSecondActiveMiner,getThirdActiveMiner,getFourthActiveMiner,getFifthActiveMiner;
    int aee=8;
    int listSize;
    Miner firstMiner,secondMiner,thirdMiner,fourthMiner,fifthMiner;
    double firstMinerHour,secondMinerHour,thirdMinerHour,fourthMinerHour,fifthMinerHour;
    double firstMinerInHour,secondMinerInHour,thirdMinerInHour,fourthMinerInHour,fifthMinerInHour;
    static int aaa=1;
    public MiningManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        firebaseModel.initAll();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                    @Override
                    public void GetTodayMining(double todayMiningFromBase) {
                        t=todayMiningFromBase;
                    }

                });
                RecentMethods.setState("Online", nick, firebaseModel);
            }
        });
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
                                    Log.d("gool", "fuck");
                                    MiningMoneyGap();
                                }
                            });
                        }else {
                        }
                    }
                });
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                    @Override
                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                        if(activeMinersFromBase.size()>0){
                            Thread thread = new Thread()
                            {
                                @Override
                                public void run() {
                                    try {
                                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                            @Override
                                            public void PassUserNick(String nick) {
                                                RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                                                    @Override
                                                    public void GetTodayMining(double todayMiningFromBase) {
                                                        todayMining=todayMiningFromBase;
                                                    }
                                                });
                                            }
                                        });
                                        while(true) {
                                            Thread.sleep(1000);
                                            miningMoneyFun();
                                            Log.d("#####", "goofffffd");
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
        return Result.success();
    }

    public static void getAAA(MiningManager.transmitMiningMoney transmitMiningMoney){
        transmitMiningMoney.transmitMoney(todayMining);
    }
    public interface transmitMiningMoney{
        void transmitMoney(double money);
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
                            Log.d("g", "f");
                            getFirstActiveMiner = getActiveMinersArrayList.get(0);
                            getInHourMinerFirst = getFirstActiveMiner.getInHour();
                            getFirstMinerInHour = Double.valueOf(String.valueOf(getInHourMinerFirst));
                            miningMoneyInGap = minInGap * (getFirstMinerInHour / 60);
                            todayMining=miningMoneyInGap+t;
                        }else if (getActiveMinersArrayList.size()==3){
                            getSecondActiveMiner=getActiveMinersArrayList.get(1);
                            getInHourMinerSecond= getSecondActiveMiner.getInHour();
                            getSecondMinerInHour = Double.valueOf(String.valueOf(getInHourMinerSecond));
                            miningMoneyInGap = minInGap * ((getFirstMinerInHour+getSecondMinerInHour) / 60);
                            todayMining=miningMoneyInGap+t;
                        }else if (getActiveMinersArrayList.size()==3){
                            getThirdActiveMiner=getActiveMinersArrayList.get(2);
                            getInHourMinerThird= getThirdActiveMiner.getInHour();
                            getThirdMinerInHour = Double.valueOf(String.valueOf(getInHourMinerThird));
                            miningMoneyInGap = minInGap * ((getFirstMinerInHour+getSecondMinerInHour+getThirdMinerInHour) / 60);
                            todayMining=miningMoneyInGap+t;
                        }else if (getActiveMinersArrayList.size()==4){
                            getFourthActiveMiner=getActiveMinersArrayList.get(3);
                            getInHourMinerFourth= getFourthActiveMiner.getInHour();
                            getFourthMinerInHour = Double.valueOf(String.valueOf(getInHourMinerFourth));
                            miningMoneyInGap = minInGap * ((getFirstMinerInHour+getSecondMinerInHour+getThirdMinerInHour+getFourthMinerInHour) / 60);
                            todayMining=miningMoneyInGap+t;
                        }else if (getActiveMinersArrayList.size()==5){
                            getFifthActiveMiner=getActiveMinersArrayList.get(4);
                            getInHourMinerFifth= getFifthActiveMiner.getInHour();
                            getFifthMinerInHour = Double.valueOf(String.valueOf(getInHourMinerFifth));
                            miningMoneyInGap = minInGap * ((getFirstMinerInHour+getSecondMinerInHour+getThirdMinerInHour+getFourthMinerInHour+getFifthMinerInHour) / 60);
                            todayMining=miningMoneyInGap+t;
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
}
