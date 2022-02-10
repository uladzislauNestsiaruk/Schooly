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
import com.egormoroz.schooly.MiningManager;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.SchoolyService;

import com.egormoroz.schooly.ui.Model.SceneViewModelActivity;
import com.egormoroz.schooly.ui.main.CreateCharacter.CreateCharacterFragment;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
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

public class MainFragment extends Fragment{

    TextView todayMiningMain,circleNontifications,circleChat;
    private FirebaseModel firebaseModel = new FirebaseModel();
    ArrayList<Clothes> clothesArrayList=new ArrayList<Clothes>();
    ArrayList<Nontification > noViewedNonts=new ArrayList<>();
    ArrayList<Clothes> popularClothesArrayList=new ArrayList<Clothes>();
    private UserInformation userData = new UserInformation();
    RecyclerView clothesRecyclerMain;
    NewClothesAdapter.ItemClickListener itemClickListener;
    private static final int NOTIFY_ID = 101;
    CircularProgressIndicator circularProgressIndicator;

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
//        firebaseModel.getReference().child("users")
//                .child("Spaccacrani").child("nontifications")
//                .child("tyomaa6").setValue(new Nontification("tyomaa6","не отправлено","запрос"
//                ,ServerValue.TIMESTAMP.toString()," "," ","не просмотрено"));
//        firebaseModel.getReference().child("users")
//                .child("Spaccacrani").child("nontifications")
//                .child("tyomaa6").setValue(new Nontification("tyomaa6","не отправлено","одежда"
//                ,ServerValue.TIMESTAMP.toString()," "," ","не просмотрено"));

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

        TextView getMore=view.findViewById(R.id.getMore);
        getMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri stickerAssetUri =  Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/miners%2Ffimw.png?alt=media&token=9798e9ea-15a0-4ef2-869b-63ce4dc95b78");
//                String sourceApplication = "com.egormoroz.schooly";
//
//                Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
//                intent.putExtra("source_application", sourceApplication);
//
//                intent.setType("image/*");
//                intent.putExtra("interactive_asset_uri", stickerAssetUri);
//                intent.putExtra("top_background_color", "#33FF33");
//                intent.putExtra("bottom_background_color", "#FF00FF");
//
//// Instantiate activity and verify it will resolve implicit intent
//                Activity activity = getActivity();
//                activity.grantUriPermission(
//                        "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
//                    activity.startActivityForResult(intent, 0);
                //               }

// Instantiate implicit intent with ADD_TO_STORY action,
// sticker asset, and background colors
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.putExtra("source_application", sourceApplication);
//
//                intent.setType("image/jpg");
//                intent.putExtra("interactive_asset_uri", stickerAssetUri);
//
//// Instantiate activity and verify it will resolve implicit intent
//                startActivity(intent);
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
        ImageView coin = view.findViewById(R.id.schoolycoin);
        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                firebaseModel.getReference().child("usersNicks").child("Spaccacrani").setValue(new UserPeopleAdapter("Spaccacrani", "5", "hello"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Jordan 1").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Jordan 1",0,123,"Schooly","dollar"," "));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Yeazzy").setValue(new Clothes("accessories", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Yeazzy",0,123,"Schooly","coin","great model"));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Y-3").setValue(new Clothes("hats", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Y-3",0,123,"Schooly","coin"," "));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Prada").setValue(new Clothes("clothes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Prada",0,123,"Schooly","coin"," "));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Raf Simons").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Raf Simons",0,123,"Schooly","coin"," "));
//                firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes").child("Martins").setValue(new Clothes("shoes", "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/clothes%2Fjordan.jpg?alt=media&token=823b2a10-1dcd-47c5-8170-b5a4fb155500"
//                        ,120,"Martins",0,123,"Schooly","coin"," "));

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
        MiningManager.getAAA(new MiningManager.transmitMiningMoney() {
            @Override
            public void transmitMoney(double money) {
                if(money!=-1) {
                    String todayMiningFormatted = new DecimalFormat("#0.00").format(money);
                    todayMiningMain.setText(todayMiningFormatted);
                }
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