package com.egormoroz.schooly.ui.main.Mining;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MiningManager;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.SchoolyService;
import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    String todayMiningFormatted;
    LinearLayout coinsLinear;
    TextView minerprice, schoolycoinminer, myminers, upgrade, todayminingText, morecoins,buy,numderOfActiveMiners,emptyActiveMiners,addActiveMiners;
    RecyclerView activeminersrecyclerview,weakminersrecyclerview,averageminersrecyclerview,strongminersrecyclerview;
    private static final String TAG = "###########";
    WeakMinersAdapter.ItemClickListener itemClickListener;
    StrongMinersAdapter.ItemClickListener itemClickListenerStrong;
    AverageMinersAdapter.ItemClickListener itemClickListenerAverage;
    long moneyAfterBuy,moneyOriginal;

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
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        moneyOriginal=money;
                        Log.d("######", "suck "+moneyOriginal);
                    }
                });
            }
        });
        coinsLinear=view.findViewById(R.id.linearCoins);
        coinsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CoinsMainFragment.newInstance(), getActivity());
            }
        });
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
        schoolycoinminer = view.findViewById(R.id.schoolycoin);
        todayminingText = view.findViewById(R.id.todaymining);
        numderOfActiveMiners=view.findViewById(R.id.numbersactiveminers);
        buy=view.findViewById(R.id.buy);
        addActiveMiners=view.findViewById(R.id.addActiveMiner);
        morecoins = view.findViewById(R.id.morecoins);
        emptyActiveMiners=view.findViewById(R.id.emptyMiners);


        GetDataFromBase();
        getActiveMinersFromBase();
        SetSchoolyCoin();


        todayminingText.setText(String.valueOf(0));
        weakminersrecyclerview=view.findViewById(R.id.allminersrecyclerview);
        averageminersrecyclerview=view.findViewById(R.id.averageminersrecyclerview);
        strongminersrecyclerview=view.findViewById(R.id.strongminersrecyclerview);

        setMiningMoney();

        itemClickListener=new WeakMinersAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position,Miner miner,String type,long money) {
                showDialog(position,miner,type,money);
            }
        };
        itemClickListenerStrong=new StrongMinersAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, Miner miner, String type,long money) {
                showDialog(position,miner,type,money);
            }
        };
        itemClickListenerAverage=new AverageMinersAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, Miner miner, String type,long money) {
                showDialog(position,miner,type,money);
            }
        };
    }


    public void GetDataFromBase(){
        RecentMethods.AllminersFromBase(firebaseModel, new Callbacks.GetMinerFromBase() {
            @Override
            public void GetMinerFromBase(ArrayList<Miner> minersFromBase) {
                listAdapterMiner.addAll(minersFromBase);
                WeakMinersAdapter allMinersAdapter=new WeakMinersAdapter(listAdapterMiner,itemClickListener);
                weakminersrecyclerview.setAdapter(allMinersAdapter);
                weakminersrecyclerview.addItemDecoration(new WeakMinersAdapter.SpaceItemDecoration());
            }
        });
        RecentMethods.AverageMinersFromBase(firebaseModel, new Callbacks.GetMinerFromBase() {
            @Override
            public void GetMinerFromBase(ArrayList<Miner> minersFromBase) {
                listAdapterAverageMiner.addAll(minersFromBase);
                AverageMinersAdapter avarageMinersAdapter=new AverageMinersAdapter(listAdapterAverageMiner,itemClickListenerAverage);
                averageminersrecyclerview.setAdapter(avarageMinersAdapter);
                averageminersrecyclerview.addItemDecoration(new AverageMinersAdapter.SpaceItemDecoration());
            }
        });
        RecentMethods.StrongMinersFromBase(firebaseModel, new Callbacks.GetMinerFromBase() {
            @Override
            public void GetMinerFromBase(ArrayList<Miner> minersFromBase) {
                listAdapterStrongMiner.addAll(minersFromBase);
                StrongMinersAdapter strongMinersAdapter=new StrongMinersAdapter(listAdapterStrongMiner,itemClickListenerStrong);
                strongminersrecyclerview.setAdapter(strongMinersAdapter);
                strongminersrecyclerview.addItemDecoration(new StrongMinersAdapter.SpaceItemDecoration());
            }
        });
    }


    public void getActiveMinersFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetActiveMiner(nick, firebaseModel,
                        new Callbacks.GetActiveMiners() {
                            @Override
                            public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                                numderOfActiveMiners.setText(String.valueOf(activeMinersFromBase.size())+"/5");
                                if(activeMinersFromBase.size()==0) {
                                    emptyActiveMiners.setVisibility(View.VISIBLE);
                                    addActiveMiners.setVisibility(View.VISIBLE);
                                    addActiveMiners.setOutlineAmbientShadowColor(Color.parseColor("#F3A2E5"));
                                    addActiveMiners.setOutlineAmbientShadowColor(Color.parseColor("#F3A2E5"));
                                    addActiveMiners.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            RecentMethods.setCurrentFragment(MyMinersFragment.newInstanse(), getActivity());
                                        }
                                    });
                                    emptyActiveMiners.setText("Добавь активные майнеры");
                                }else {
                                    emptyActiveMiners.setVisibility(View.GONE);
                                    addActiveMiners.setVisibility(View.GONE);
                                }
                                listAdapterActiveMiner.addAll(activeMinersFromBase);
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

    public void showDialog(int pos,Miner miner,String type,long money){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.buy_miner_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text=dialog.findViewById(R.id.acceptText);

        TextView no=dialog.findViewById(R.id.no);
        TextView yes=dialog.findViewById(R.id.yes);


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("weak")){
                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                        @Override
                        public void PassUserNick(String nick) {
                            RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                                @Override
                                public void GetMoneyFromBase(long money) {
                                    RecentMethods.buyWeakMiner(String.valueOf(pos), firebaseModel, new Callbacks.buyMiner() {
                                        @Override
                                        public void buyMiner(Miner miner) {
                                            firebaseModel.getReference("users").child(nick)
                                                    .child("miners").child(String.valueOf(pos)+type).setValue(miner);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else if(type.equals("medium")){
                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                        @Override
                        public void PassUserNick(String nick) {
                            RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                                @Override
                                public void GetMoneyFromBase(long money) {
                                    RecentMethods.buyAverageMiner(String.valueOf(pos), firebaseModel, new Callbacks.buyMiner() {
                                        @Override
                                        public void buyMiner(Miner miner) {
                                            firebaseModel.getReference("users").child(nick)
                                                    .child("miners").child(String.valueOf(pos)+type).setValue(miner);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else if(type.equals("strong")){
                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                        @Override
                        public void PassUserNick(String nick) {
                            RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                                @Override
                                public void GetMoneyFromBase(long money) {
                                    RecentMethods.buyStrongMiner(String.valueOf(pos), firebaseModel, new Callbacks.buyMiner() {
                                        @Override
                                        public void buyMiner(Miner miner) {
                                            firebaseModel.getReference("users").child(nick)
                                                    .child("miners").child(String.valueOf(pos)+type).setValue(miner);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        firebaseModel.getUsersReference().child(nick).child("money").setValue(money-miner.getMinerPrice());
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

//    public void getSchoolyCoin(){
//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @Override
//            public void PassUserNick(String nick) {
//                RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
//                    @Override
//                    public void GetTodayMining(double todayMiningFromBase) {
//                        String todayMiningFormatted = new DecimalFormat("#0.00").format(todayMiningFromBase);
//                        todayminingText.setText(todayMiningFormatted);
//                    }
//                });
//            }
//        });
//    }

    public void setMiningMoney(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                    @Override
                    public void GetTodayMining(double todayMiningFromBase) {
                        todayMiningFormatted = new DecimalFormat("#0.00").format(todayMiningFromBase);
                        todayminingText.setText("+"+todayMiningFormatted);
                    }
                });
            }
        });
    }

//    public void addWeakMiners(){
//        listAdapterMiner.add(new Miner(5,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,100));
//        listAdapterMiner.add(new Miner(7,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,150));
//        listAdapterMiner.add(new Miner(13,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,200));
//        listAdapterMiner.add(new Miner(17,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,240));
//        listAdapterMiner.add(new Miner(20,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,270));
//    }
//
//    public void addAverageMiners(){
//        listAdapterAverageMiner.add(new Miner(24,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,300));
//        listAdapterAverageMiner.add(new Miner(28,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,340));
//        listAdapterAverageMiner.add(new Miner(32,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,380));
//        listAdapterAverageMiner.add(new Miner(35,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,420));
//        listAdapterAverageMiner.add(new Miner(38,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,450));
//    }
//
//    public void addStrongMiners(){
//        listAdapterStrongMiner.add(new Miner(42,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,480));
//        listAdapterStrongMiner.add(new Miner(45,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,520));
//        listAdapterStrongMiner.add(new Miner(48,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,560));
//        listAdapterStrongMiner.add(new Miner(52,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,600));
//        listAdapterStrongMiner.add(new Miner(56,"https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                ,640));
//    }
//
//    public void puyInBase(){
//        firebaseModel.getReference().child("AppData").child("AllMiners").child("Weak").setValue(listAdapterMiner);
//        firebaseModel.getReference().child("AppData").child("AllMiners").child("Average").setValue(listAdapterAverageMiner);
//        firebaseModel.getReference().child("AppData").child("AllMiners").child("Strong").setValue(listAdapterStrongMiner);
//    }

}