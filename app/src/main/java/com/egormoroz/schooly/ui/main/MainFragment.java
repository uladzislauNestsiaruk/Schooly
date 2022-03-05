package com.egormoroz.schooly.ui.main;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.egormoroz.schooly.MiningManager;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.SchoolyService;

import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.Model.SceneViewModelActivity;
import com.egormoroz.schooly.ui.coins.CoinsFragmentSecond;
import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.main.CreateCharacter.CreateCharacterFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.main.MyClothes.CreateClothesFragment;
import com.egormoroz.schooly.ui.main.MyClothes.MyClothesAdapter;
import com.egormoroz.schooly.ui.main.MyClothes.MyClothesAdapterMain;
import com.egormoroz.schooly.ui.main.MyClothes.MyClothesFragment;
import com.egormoroz.schooly.ui.main.MyClothes.ViewingMyClothes;
import com.egormoroz.schooly.ui.main.MyClothes.ViewingMyClothesMain;
import com.egormoroz.schooly.ui.main.Nontifications.NontificationFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.egormoroz.schooly.ui.profile.ComplainFragment;
import com.egormoroz.schooly.ui.profile.Reason;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.ServerValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainFragment extends Fragment{

    TextView todayMiningMain,circleNontifications,circleChat,myClothes,mining,getMore;
    private FirebaseModel firebaseModel = new FirebaseModel();
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    ArrayList<Nontification > noViewedNonts=new ArrayList<>();
    ArrayList<Clothes> popularClothesArrayList=new ArrayList<Clothes>();
    private UserInformation userData = new UserInformation();
    RecyclerView clothesRecyclerMain,myClothesRecycler;
    RelativeLayout relativeFirstLayout,createClothes;
    String todayMiningFormatted;
    int myClothesListSize;
    NewClothesAdapter.ItemClickListener itemClickListener;
    private static final int NOTIFY_ID = 101;
    RelativeLayout relativeShop,relativeMining,relativeMyClothes;
    CircularProgressIndicator circularProgressIndicator;
    MyClothesAdapterMain.ItemClickListener itemClickListenerMyClothes;
    LinearLayout coinsLinear;

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

        myClothesRecycler=view.findViewById(R.id.mychlothesmain);
        relativeFirstLayout=view.findViewById(R.id.relativeFirstClothes);
        createClothes=view.findViewById(R.id.createClothes);
        coinsLinear=view.findViewById(R.id.linearCoins);
        getMore=view.findViewById(R.id.getMore);
        getMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(MoreMoneyFragment.newInstance(MainFragment.newInstance()), getActivity());
            }
        });
        coinsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CoinsFragmentSecond.newInstance(MainFragment.newInstance()), getActivity());
            }
        });
        getMyClothes();
        relativeMyClothes=view.findViewById(R.id.relativeClothes);
        relativeMining=view.findViewById(R.id.relativeMining);
        relativeShop=view.findViewById(R.id.relativeshop);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getMyClothes(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        myClothesListSize=allClothes.size();
                    }
                });
            }
        });
        relativeMyClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myClothesListSize>-1) {
                    RecentMethods.setCurrentFragment(MyClothesFragment.newInstance(myClothesListSize), getActivity());
                }
            }
        });
        itemClickListenerMyClothes=new MyClothesAdapterMain.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingMyClothesMain.newInstance(MainFragment.newInstance()), getActivity());
            }
        };
        ImageView chat=view.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DialogsActivity.class);
                startActivity(i);
                //               ((Activity) getActivity()).overridePendingTransition(0, 0);
//                Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
//                Uri intentUri =
//                        Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
//                                .appendQueryParameter("file", "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf")
//                                .appendQueryParameter("mode", "3d_only")
//                                .build();
//                sceneViewerIntent.setData(intentUri);
//                sceneViewerIntent.setPackage("com.google.ar.core");
//                startActivity(sceneViewerIntent);
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                startActivity(intent);
            }
        });
        Random random = new Random();
        int num1 =random.nextInt(1000000000);
        int num2 =random.nextInt(1000000000);
        String numToBase=String.valueOf(num1+num2);
        int num3 =random.nextInt(1000000000);
        int num4=random.nextInt(1000000000);
        String numToBase1=String.valueOf(num3+num4);

        firebaseModel.getUsersReference().child("tyomaa6").child("subscription").child("Spaccacrani")
                .setValue("Spaccacrani");
        firebaseModel.getUsersReference().child("Spaccacrani").child("subscribers").child("tyomaa6")
                .setValue("tyomaa6");


//        ArrayList<Reason> reasonsArrayList=new ArrayList<>();
//        reasonsArrayList.add(new Reason("Мошенничество"));
//        reasonsArrayList.add(new Reason("Насилие или опасные организации"));
//        reasonsArrayList.add(new Reason("Враждебные высказывания или символы"));
//        reasonsArrayList.add(new Reason("Продажа незаконных товаров"));
//        reasonsArrayList.add(new Reason("Нарушение прав на интеллектуальную собственность"));
//        firebaseModel.getReference().child("AppData").child("complains").setValue(reasonsArrayList);
        circleChat=view.findViewById(R.id.circleChat);
        circleNontifications=view.findViewById(R.id.circleNontifications);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getNontificationsList(nick, firebaseModel, new Callbacks.getNontificationsList() {
                    @Override
                    public void getNontificationsList(ArrayList<Nontification> nontifications) {
                        for (int i=0;i<nontifications.size();i++){
                            Nontification nontification=nontifications.get(i);
                            if(nontification.getType().equals("не просмотрено")){
                                noViewedNonts.add(nontification);
                            }
                        }
                        if(noViewedNonts.size()>0){
                            circleNontifications.setVisibility(View.VISIBLE);
                            if(noViewedNonts.size()>9){
                                circleNontifications.setText("9+");
                            }else {
                                circleNontifications.setText(String.valueOf(noViewedNonts.size()));
                            }
                        }
                    }
                });
            }
        });
        circularProgressIndicator=view.findViewById(R.id.miningIndicator);

//        TextView getMore=view.findViewById(R.id.getMore);
//        getMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Uri stickerAssetUri =  Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/miners%2Ffimw.png?alt=media&token=9798e9ea-15a0-4ef2-869b-63ce4dc95b78");
////                String sourceApplication = "com.egormoroz.schooly";
////
////                Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
////                intent.putExtra("source_application", sourceApplication);
////
////                intent.setType("image/*");
////                intent.putExtra("interactive_asset_uri", stickerAssetUri);
////                intent.putExtra("top_background_color", "#33FF33");
////                intent.putExtra("bottom_background_color", "#FF00FF");
////
////// Instantiate activity and verify it will resolve implicit intent
////                Activity activity = getActivity();
////                activity.grantUriPermission(
////                        "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
////                    activity.startActivityForResult(intent, 0);
//                //               }
//
//// Instantiate implicit intent with ADD_TO_STORY action,
//// sticker asset, and background colors
////                Intent intent = new Intent(Intent.ACTION_SEND);
////                intent.putExtra("source_application", sourceApplication);
////
////                intent.setType("image/jpg");
////                intent.putExtra("interactive_asset_uri", stickerAssetUri);
////
////// Instantiate activity and verify it will resolve implicit intent
////                startActivity(intent);
//            }
//        });
        ImageView nontifications=view.findViewById(R.id.nontification);
        nontifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(NontificationFragment.newInstance());
//
            }
        });
        ImageView coin = view.findViewById(R.id.schoolycoin);
        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
//                firebaseModel.getReference().child("usersNicks").child("Spaccacrani").setValue(new UserPeopleAdapter("Spaccacrani", "5", "hello"));
//                firebaseModel.getUsersReference().child("tyomaa6").child("myClothes").child("Jordan 6").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,220,"Jordan 6",0,123,"tyomaa6","coin"," ","",0,"foot"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Jordan 1").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Jordan 1",0,123,"Schooly","dollar"," ","",0,"foot"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Jordan 1").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Jordan 1",0,123,"tyomaaa6","dollar"," ","",0,"foot"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Yeazzy").setValue(new Clothes("accessories", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Yeazzy",0,123,"Schooly","coin","great model","",0,"watches"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Y-3").setValue(new Clothes("hats", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Y-3",0,123,"Schooly","coin"," ","",0,"hat"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Prada").setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Prada",0,123,"Schooly","coin"," ","",0,"t-shirt"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Raf Simons").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Raf Simons",0,123,"Schooly","coin"," ","",0,"foot"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Martins").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Martins",0,123,"Schooly","coin"," ","",0,"foot"));

            }
        });

        relativeShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment((ShopFragment.newInstance()));
            }
        });

        relativeMining.setOnClickListener(new View.OnClickListener() {
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
        itemClickListener=new NewClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                ((MainActivity)getActivity()).setCurrentFragment(ViewingClothes.newInstance(MainFragment.newInstance()));
            }
        };
        TextView appName=view.findViewById(R.id.appname);
        createNotificationChannel();
        appName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CreateCharacterFragment.newInstance(), getActivity());
            }
        });
        todayMiningMain=view.findViewById(R.id.todayminingmain);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetTodayMining(nick, firebaseModel, new Callbacks.GetTodayMining() {
                    @Override
                    public void GetTodayMining(double todayMiningFromBase) {
                        todayMiningFormatted = new DecimalFormat("#0.00").format(todayMiningFromBase);
                        todayMiningMain.setText("+"+todayMiningFormatted);
                    }
                });
            }
        });
        loadClothesFromBase();
    }

    public void loadClothesFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
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

    public void getMyClothes(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getMyClothes(nick, firebaseModel, new Callbacks.GetClothes() {
                    @Override
                    public void getClothes(ArrayList<Clothes> allClothes) {
                        if(allClothes.size()==0){
                            relativeFirstLayout.setVisibility(View.VISIBLE);
                            myClothesRecycler.setVisibility(View.GONE);
                            createClothes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RecentMethods.setCurrentFragment(CreateClothesFragment.newInstance(MainFragment.newInstance()), getActivity());
                                }
                            });
                        }else {
                            MyClothesAdapterMain myClothesAdapterMain=new MyClothesAdapterMain(allClothes,itemClickListenerMyClothes);
                            myClothesRecycler.setAdapter(myClothesAdapterMain);
                        }
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

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getContext().getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    private void shareToInstagram() {
        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            shareIntent.putExtra(Intent.EXTRA_STREAM, "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/miners%2Ffims.png?alt=media&token=adafb44e-3ac1-43a3-bde6-6f7c4315ee0c");
            shareIntent.setType("image/jpeg");
            startActivity(shareIntent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
            startActivity(intent);
        }
    }

}