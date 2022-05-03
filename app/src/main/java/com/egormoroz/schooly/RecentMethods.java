package com.egormoroz.schooly;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.Comment;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.profile.Look;
import com.egormoroz.schooly.ui.profile.Reason;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Queue;

public class RecentMethods {
    public static void isNickCorrect(String nickname, DatabaseReference reference, TextView errorTextnickname) {
        Log.d("########", "Method: isNickCorrect");
        Log.d("########", "Reference: " + String.valueOf(reference));
        if (nickname.length() < 4) {
            showErrorMessage(ErrorList.ERROR_NICK_IS_TO_SHORT, errorTextnickname);
            return;
        }
        isNickUniqueFun(nickname, reference, new Callbacks.UniqueNick() {
            @Override
            public void uniqueNicknameCallback(boolean isUnique) {
                Log.d("########", "Is nick unique: " + isUnique);
                if (!isUnique)
                    showErrorMessage(ErrorList.NICK_IS_USED, errorTextnickname);
                else
                    showErrorMessage(ErrorList.NOTHING, errorTextnickname);
            }
        });
    }
    public static void isNickUniqueFun(String nickname, DatabaseReference ref, final Callbacks.UniqueNick callback) {
        Query query = ref.orderByChild("nick").equalTo(nickname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.uniqueNicknameCallback(!snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public static void showErrorMessage(ErrorList tag, TextView errorText) {
        switch (tag) {
            case NOT_ENOUGH_SYMBOL_ERROR:
                errorText.setText("The minimum number of symbols is 8");
                break;
            case ONLY_ENGLISH_SYMBOLS_ERROR:
                errorText.setText("Оnly English symbols in the nickname");
                break;
            case INVALID_SYMBOLS_ERROR:
                errorText.setText("Invalid symbols");
                break;
            case NOT_ONLY_NUMBERS_ERROR:
                errorText.setText("The password must contain more than just numbers");
                break;
            case NOT_ONLY_LETTERS:
                errorText.setText("The password must contain more than just letters");
                break;
            case NOTHING:
                errorText.setText(" ");
                break;
            case NICK_IS_USED:
                errorText.setText("Nickname is already being used");
                break;
            case PHONE_ERROR:
                errorText.setText("Phone is not correct");
                break;
            case ERROR_EMPTY_PHONE:
                errorText.setText("You didn't enter your phone");
                break;
        }
    }
    public static void setCurrentFragment(Fragment fragment, Activity activity) {
        FragmentTransaction ft = ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }
    public static String makeEmail(String phone) {
        String email = "schooly";
        for (int i = 1; i < phone.length(); i++)
            email += phone.toCharArray()[i];
        email += "@gmail.com";
        return email;
    }
    public static String getPhone(String email) {
        String res = email;
        res = res.replace("schooly", "");
        res = res.replace("@gmail.com", "");
        return "+" + res;
    }
    public static boolean saveData(DatabaseReference ref, FirebaseUser user, String nick) {

        UserInformation res = new UserInformation(nick, "unknown", user.getUid(),
                "6", "unknown", "Helicopter", 1000
                , new ArrayList<>(),new ArrayList<>(), 1,100,0, "", "",
                "","","open","open","open",
                "open",new ArrayList<>(),"regular", "",0,new ArrayList<>(),new ArrayList<>());
        ref.child(nick).setValue(res);
        return nick.isEmpty();
    }
    public static void hasThisUserFun(FirebaseAuth AuthenticationBase, FirebaseUser user,
                                      Callbacks.hasGoogleUser callback) {
        if(user == null){
            callback.hasGoogleUserCallback(false);
            return;
        }
        String email = String.valueOf(user.getEmail());
        AuthenticationBase.fetchSignInMethodsForEmail(email).
                addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean has = !task.getResult().getSignInMethods().isEmpty();
                        callback.hasGoogleUserCallback(has);
                    }
                });
    }
    public static void hasThisUser(FirebaseAuth AuthenticationBase, FirebaseUser user,
                                   Callbacks.hasGoogleUser callback) {
        hasThisUserFun(AuthenticationBase, user, new Callbacks.hasGoogleUser() {
            @Override
            public void hasGoogleUserCallback(boolean hasThisUser) {
                callback.hasGoogleUserCallback(hasThisUser);
            }
        });
    }
    public static ArrayList<UserInformation> findUsers(String username) {
        ArrayList<UserInformation> result = new ArrayList<UserInformation>();

        return result;
    }
    public static void LoadUserDataByNickFun(FirebaseModel model,
                                             Callbacks.LoadUserDataInterface callback,
                                             String nick) {
        model.initAll();
        Query query = model.getReference("users").
                orderByChild("nick").equalTo(nick);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotParent) {
                ArrayList<UserInformation> data = new ArrayList<>();
                for (DataSnapshot snapshot : snapshotParent.getChildren()) {
                    UserInformation userData = new UserInformation();
                    userData.setAge(snapshot.child("age").getValue(Long.class));
                    userData.setAvatar(snapshot.child("avatar").getValue(String.class));
                    userData.setGender(snapshot.child("gender").getValue(String.class));
                    //////////////////userData.setMiners();
                    userData.setNick(snapshot.child("nick").getValue(String.class));
                    userData.setPassword(snapshot.child("password").getValue(String.class));
                    userData.setPhone(snapshot.child("phone").getValue(String.class));
                    userData.setUid(snapshot.child("uid").getValue(String.class));
                    userData.setQueue(snapshot.child("queue").getValue(String.class));
//                    userData.setSubscribers(snapshot.child("subscribers").getValue(String.class));
                    userData.setSubscription(snapshot.child("subscription").getValue(String.class));
                    userData.setmoney(snapshot.child("money").getValue(Long.class));
                    userData.setTodayMining(snapshot.child("todayMining").getValue(Double.class));
                    data.add(userData);
                }
                callback.LoadData(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void LoadUserDataByNick(FirebaseModel model,
                                          String nick,
                                          Callbacks.PassLoadUserDataInterface passData) {
        LoadUserDataByNickFun(model, new Callbacks.LoadUserDataInterface() {
            @Override
            public void LoadData(ArrayList<UserInformation> data) {
                passData.PassData(data);
            }
        }, nick);
    }

    public static void UserNickByUid(String uid, FirebaseModel model, Callbacks.GetUserNickByUid callback) {
        model.initAll();
        Query query = model.getUsersReference().orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren())
                    callback.PassUserNick(snap.child("nick").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void UserNickByUid1(String uid, FirebaseModel model, Callbacks.GetUserNickByUid callback) {
        model.initAll();
        model.getUsersReference().orderByChild("uid").equalTo(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Log.d("AAA", "uid  "+uid);
                        callback.PassUserNick(snap.child("nick").getValue(String.class));
                    }
                }
            }
        });

    }

    public static void hasUid(String uid, FirebaseModel model, Callbacks.HasUid callback){
        model.initAll();
        Query query = model.getReference("users")
                .orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.HasUidCallback(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getBio(String nick, FirebaseModel model, Callbacks.GetBio callback){
        model.initAll();
        Query query =model.getUsersReference().child(nick).child("bio");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.GetBiography(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ///////////////////////MINING///////////////////////////////

    public static void AllminersFromBase(FirebaseModel model, Callbacks.GetMinerFromBase callback) {
        model.initAll();
        Query query = model.getReference("AppData/AllMiners");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Miner> minersFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.child("Weak").getChildren()) {
                    Miner miner = new Miner();
                    miner.setInHour(snap.child("inHour").getValue(Long.class));
                    miner.setMinerPrice(snap.child("minerPrice").getValue(Long.class));
                    miner.setMinerImage(snap.child("minerImage").getValue(String.class));
                    minersFromBase.add(miner);
                }
                callback.GetMinerFromBase(minersFromBase);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void AverageMinersFromBase(FirebaseModel model, Callbacks.GetMinerFromBase callback) {
        model.initAll();
        Query query = model.getReference("AppData/AllMiners");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Miner> minersFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.child("Average").getChildren()) {
                    Miner miner = new Miner();
                    miner.setInHour(snap.child("inHour").getValue(Long.class));
                    miner.setMinerPrice(snap.child("minerPrice").getValue(Long.class));
                    miner.setMinerImage(snap.child("minerImage").getValue(String.class));
                    minersFromBase.add(miner);
                }
                callback.GetMinerFromBase(minersFromBase);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void StrongMinersFromBase(FirebaseModel model, Callbacks.GetMinerFromBase callback) {
        model.initAll();
        Query query = model.getReference("AppData/AllMiners");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Miner> minersFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.child("Strong").getChildren()) {
                    Miner miner = new Miner();
                    miner.setInHour(snap.child("inHour").getValue(Long.class));
                    miner.setMinerPrice(snap.child("minerPrice").getValue(Long.class));
                    miner.setMinerImage(snap.child("minerImage").getValue(String.class));
                    Log.d("####", "one more");
                    minersFromBase.add(miner);
                }
                callback.GetMinerFromBase(minersFromBase);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void buyWeakMiner(String currentMiner,FirebaseModel model,Callbacks.buyMiner callback){
        model.initAll();
        Query query=model.getReference("AppData/AllMiners/Weak")
                .orderByChild(currentMiner);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Miner miner=snapshot.child(currentMiner).getValue(Miner.class);
                callback.buyMiner(miner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void buyAverageMiner(String currentMiner,FirebaseModel model,Callbacks.buyMiner callback){
        model.initAll();
        Query query=model.getReference("AppData/AllMiners/Average")
                .orderByChild(currentMiner);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Miner miner=snapshot.child(currentMiner).getValue(Miner.class);
                callback.buyMiner(miner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void buyStrongMiner(String currentMiner,FirebaseModel model,Callbacks.buyMiner callback){
        model.initAll();
        Query query=model.getReference("AppData/AllMiners/Strong")
                .orderByChild(currentMiner);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Miner miner=snapshot.child(currentMiner).getValue(Miner.class);
                callback.buyMiner(miner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void MyMinersFromBase(String nick,FirebaseModel model, Callbacks.GetMyMinerFromBase callback) {
        model.initAll();
        Query query = model.getUsersReference().child(nick)
                .child("miners");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Miner> myMinersFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Miner miner = new Miner();
                    miner.setInHour(snap.child("inHour").getValue(Long.class));
                    miner.setMinerPrice(snap.child("minerPrice").getValue(Long.class));
                    miner.setMinerImage(snap.child("minerImage").getValue(String.class));
                    myMinersFromBase.add(miner);
                }
                callback.GetMyMinerFromBase(myMinersFromBase);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void GetActiveMiner(String nick,FirebaseModel model, Callbacks.GetActiveMiners callback) {
        model.initAll();
         model.getUsersReference().child(nick).child("activeMiners")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot snapshot=task.getResult();
                            ArrayList<Miner> activeMinersFromBase=new ArrayList<>();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Miner miner = new Miner();
                                miner.setInHour(snap.child("inHour").getValue(Long.class));
                                miner.setMinerPrice(snap.child("minerPrice").getValue(Long.class));
                                miner.setMinerImage(snap.child("minerImage").getValue(String.class));
                                activeMinersFromBase.add(miner);
                                long d=miner.getInHour();
                            }
                            callback.GetActiveMiners(activeMinersFromBase);
                        }
                    }
                });
    }

    public static void GetMoneyFromBase(String nick,FirebaseModel firebaseModel,Callbacks.MoneyFromBase callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick)
                .child("money");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    callback.GetMoneyFromBase(snapshot.getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void GetTodayMining(String nick,FirebaseModel model,Callbacks.GetTodayMining callback){
        model.initAll();
        model.getUsersReference().child(nick)
                .child("todayMining").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot=task.getResult();
                            if (snapshot.getValue(Double.class) != null)
                                callback.GetTodayMining(snapshot.getValue(Double.class));
                        }
                    }
                });
    }
    public static void GetTimesTamp(String nick, FirebaseModel firebaseModel,Callbacks.GetTimesTamp callback){
        firebaseModel.initAll();
        Query query =firebaseModel.getUsersReference().child(nick).child("timesTamp");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.GetTimesTamp(snapshot.getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void GetTimeStampNow(String nick, FirebaseModel firebaseModel,Callbacks.GetTimesTamp callback){
        firebaseModel.initAll();
        Query query =firebaseModel.getUsersReference().child(nick).child("serverTimeNow");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.GetTimesTamp(snapshot.getValue(Long.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //////////////////////////////////////////////////////////////
    ////////////////////SHOP//////////////////////////////////////

    public static void getClothes(FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getReference("AppData/Clothes/AllClothes");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
                    clothes.setDescription(snap.child("description").getValue(String.class));
                    clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                    clothes.setModel(snap.child("model").getValue(String.class));
                    clothes.setBodyType(snap.child("bodyType").getValue(String.class));
                    clothes.setUid(snap.child("uid").getValue(String.class));
                    clothes.setExclusive(snap.child("exclusive").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getPopular(FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getReference("AppData/Clothes/AllClothes");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
                    clothes.setDescription(snap.child("description").getValue(String.class));
                    clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                    clothes.setModel(snap.child("model").getValue(String.class));
                    clothes.setBodyType(snap.child("bodyType").getValue(String.class));
                    clothes.setUid(snap.child("uid").getValue(String.class));
                    clothes.setExclusive(snap.child("exclusive").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void getNewClothes(FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getReference("AppData/Clothes/AllNew");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void getShoes(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getReference("AppData/Clothes/Shoes");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void getClothesFromBase(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getReference("AppData/Clothes/Clothes");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getHats(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getReference("AppData/Clothes/Hats");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getAsseccories(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getReference("AppData/Clothes/Asseccories");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getClothesInBasket(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick).child("basket");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
                    clothes.setDescription(snap.child("description").getValue(String.class));
                    clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                    clothes.setModel(snap.child("model").getValue(String.class));
                    clothes.setUid(snap.child("uid").getValue(String.class));
                    clothes.setExclusive(snap.child("exclusive").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /////////////////////////Wardrobe////////////////////////
    public static void getClothesInWardrobe(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick)
                .child("clothes").orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothes.setDescription(snap.child("description").getValue(String.class));
                    clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                    clothes.setModel(snap.child("model").getValue(String.class));
                    clothes.setBodyType(snap.child("bodyType").getValue(String.class));
                    clothes.setUid(snap.child("uid").getValue(String.class));
                    clothes.setExclusive(snap.child("exclusive").getValue(String.class));
                   clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getShoesInWardrobe(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick)
                .child("clothes").child("shoes");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getHatsInWardrobe(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick)
                .child("clothes").child("hats");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getAccessoriesInWardrobe(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick)
                .child("clothes").child("accessories");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ////////////////////////// Friends System /////////////////////
//    public static void getFriendsList(String nickName, FirebaseModel model, Callbacks.getFriendsList callback){
//        model.initAll();
//        Query query = model.getUsersReference().orderByChild("nick").equalTo(nickName);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<String> friendsList = new ArrayList<>();
//                for(DataSnapshot snap : snapshot.getChildren()){
//                    String[] friends = (snap.child("friends").getValue(String.class)).split("#");
//                    for(String friend : friends)
//                        friendsList.add(friend);
//                }
//                callback.getFriendsList(friendsList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    public static void FriendQueryAccapted(String userName1, String userName2, FirebaseModel model){
        model.initAll();
        Query query = model.getUsersReference().child("nick").equalTo(userName1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String friends = snap.child("friends").getValue(String.class) + "#" + userName2;
                    String subscribers = snap.child("subscribers").getValue(String.class);
                    String queue = snap.child("queue").getValue(String.class);
                    queue = queue.replace("#" + userName2, "");
                    subscribers = subscribers.replace("#" + userName2, "");
                    model.getUsersReference().child(userName1).child("friends").setValue(friends);
                    model.getUsersReference().child(userName1).child("subscribers").setValue(subscribers);
                    model.getUsersReference().child(userName1).child("queue").setValue(queue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        query = model.getUsersReference().child("nick").equalTo(userName2);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String friends = snap.child("friends").getValue(String.class) + "#" + userName1;
                    model.getUsersReference().child(userName2).child("friends").setValue(friends);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getAmountOfFriends(String uid, FirebaseModel model, Callbacks.getAmountOfFriends callback){
        model.initAll();
        Query query = model.getUsersReference().child("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    int cnt = 0;
                    String friendsList = snap.child("friends").getValue(String.class);
                    if(friendsList.isEmpty()) {
                        callback.getAmountOfFriends(0);
                        break;
                    }
                    for(char c : friendsList.toCharArray())
                        if(c == '#')
                            cnt++;
                    callback.getAmountOfFriends(cnt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    ////////////////////////// Subscribers System /////////////////
//    public static void getSubscribersList(String nickName, FirebaseModel model, Callbacks.getSubscribersList callback){
//        model.initAll();
//        Query query = model.getUsersReference().child("nick").equalTo(nickName);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<String> subscribersList = new ArrayList<>();
//                for(DataSnapshot snap : snapshot.getChildren()){
//                    String[] subscribers = (snap.child("subscribers").getValue(String.class)).split("#");
//                    for(String subscriber : subscribers)
//                        subscribersList.add(subscriber);
//                }
//                Log.d("######", "int"+subscribersList);
//                callback.getSubscribersList(subscribersList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    public static void friendQuery(String userName1, String userName2, FirebaseModel model){
        model.initAll();
        Query query = model.getUsersReference().child("nick").equalTo(userName1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    String subscribers = snap.child("subscribers").getValue(String.class) + "#" + userName2;
                    String queue = snap.child("queue").getValue(String.class) + "#" + userName2;
                    model.getUsersReference().child(userName1).child("subscribers").setValue(subscribers);
                    model.getUsersReference().child(userName1).child("queue").setValue(queue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void getAmountOfSubscribers(String uid, FirebaseModel model, Callbacks.getAmountOfSubscribers callback){
        model.initAll();
        Query query = model.getUsersReference().child("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    int cnt = 0;
                    String subscribersList = snap.child("subscribers").getValue(String.class);
                    if(subscribersList.isEmpty()) {
                        callback.getAmountOfSubscribers(0);
                        break;
                    }
                    for(char c : subscribersList.toCharArray())
                        if(c == '#')
                            cnt++;
                    callback.getAmountOfSubscribers(cnt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void checkSubscribers(String nick, FirebaseModel model,  Callbacks.getSubscribersList callback){
        Query query=model.getUsersReference().child(nick).child("subscribers");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArrayList<Subscriber> subscribersList = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()){
                    Subscriber subscriber=new Subscriber();
                    subscriber.setSub(snap.child("sub").getValue(String.class));
                    subscribersList.add(subscriber);
                }
                Log.d("###", "name1"+subscribersList);
                callback.getSubscribersList(subscribersList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getSubscribersList(String nickName, FirebaseModel model, Callbacks.getSubscribersList callback){
        model.initAll();
        Query query=model.getUsersReference().child(nickName).child("subscribers");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Subscriber> subscribersList = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Subscriber subscriber=new Subscriber();
                    subscriber.setSub(snap.getValue(String.class));
                    subscribersList.add(subscriber);
                }
                callback.getSubscribersList(subscribersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static void getBlackList(String nickName, FirebaseModel model, Callbacks.getSubscribersList callback){
        model.initAll();
        Query query=model.getUsersReference().child(nickName).child("blackList");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Subscriber> subscribersList = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Subscriber subscriber=new Subscriber();
                    subscriber.setSub(snap.getValue(String.class));
                    subscribersList.add(subscriber);
                }
                callback.getSubscribersList(subscribersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getSavedLooks(String nickName, FirebaseModel model, Callbacks.getSavedLook callback){
        model.initAll();
        Query query=model.getUsersReference().child(nickName).child("saved").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<NewsItem> savedArrayList = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    NewsItem newsItem=new NewsItem();
                    newsItem=snap.getValue(NewsItem.class);
                    savedArrayList.add(newsItem);
                }
                callback.getSavedLook(savedArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void getLooksList(String nickName, FirebaseModel model, Callbacks.getLooksList callback){
        model.initAll();
        model.getUsersReference().child(nickName).child("looks").orderByKey().get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    ArrayList<NewsItem> lookList = new ArrayList<>();
                    for (DataSnapshot snap:snapshot.getChildren()){
                        NewsItem newsItem=new NewsItem();
                        newsItem.setImageUrl(snap.child("imageUrl").getValue(String.class));
                        newsItem.setLookPrice(snap.child("lookPrice").getValue(Long.class));
                        newsItem.setItem_description(snap.child("item_description").getValue(String.class));
                        newsItem.setNewsId(snap.child("newsId").getValue(String.class));
                        newsItem.setLikesCount(snap.child("likes_count").getValue(String.class));
                        newsItem.setViewCount(snap.child("viewCount").getValue(Long.class));
                        newsItem.setPostTime(snap.child("postTime").getValue(String.class));
                        newsItem.setNick(snap.child("nick").getValue(String.class));
                        newsItem.setLookPriceDollar(snap.child("lookPriceDollar").getValue(Long.class));
                        lookList.add(newsItem);
                    }
                    callback.getLooksList(lookList);
                }
            }
        });

    }

    public static void getNontificationsListAdded(String nickName, FirebaseModel model, Callbacks.getNontificationsList callback){
        model.initAll();
        Query query=model.getUsersReference().child(nickName).child("nontifications");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArrayList<Nontification> nontificationArrayList = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()){
                    Nontification nontification=new Nontification();
                    nontification.setNick(snap.child("nick").getValue(String.class));
                    nontification.setTypeDispatch(snap.child("typeDispatch").getValue(String.class));
                    nontification.setTypeView(snap.child("typeView").getValue(String.class));
                    nontification.setTimestamp(snap.child("timestamp").getValue(String.class));
                }
                callback.getNontificationsList(nontificationArrayList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static void getNontificationsList(String nickName, FirebaseModel model, Callbacks.getNontificationsList callback){
        model.initAll();
        model.getUsersReference().child(nickName).child("nontifications").orderByKey()
        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    ArrayList<Nontification> nontificationArrayList = new ArrayList<>();
                    for (DataSnapshot snap:snapshot.getChildren()){
                        Nontification nontification=new Nontification();
                        nontification.setNick(snap.child("nick").getValue(String.class));
                        nontification.setTypeDispatch(snap.child("typeDispatch").getValue(String.class));
                        nontification.setTypeView(snap.child("typeView").getValue(String.class));
                        nontification.setTimestamp(snap.child("timestamp").getValue(String.class));
                        nontification.setClothesName(snap.child("clothesName").getValue(String.class));
                        nontification.setClothesImage(snap.child("clothesImage").getValue(String.class));
                        nontification.setType(snap.child("type").getValue(String.class));
                        nontification.setUid(snap.child("uid").getValue(String.class));
                        nontification.setClothesProfit(snap.child("clothesProfit").getValue(Long.class));
                        nontificationArrayList.add(nontification);
                    }
                    callback.getNontificationsList(nontificationArrayList);
                }
            }
        });
    }

    public static void getComplainReasonList( FirebaseModel model, Callbacks.getComplainReasonsList callback){
        model.initAll();
        Query query=model.getReference().child("AppData").child("complains");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Reason> reasonsArrayList = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Reason reason=new Reason();
                    reason.setReason(snap.child("reason").getValue(String.class));
                    reasonsArrayList.add(reason);
                }
                callback.getComplainReasonsList(reasonsArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getSubscriptionList(String nickName, FirebaseModel model, Callbacks.getFriendsList callback){
        model.initAll();
        Query query=model.getUsersReference().child(nickName).child("subscription");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Subscriber> subscribersList = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Subscriber subscriber=new Subscriber();
                    subscriber.setSub(snap.getValue(String.class));
                    subscribersList.add(subscriber);
                }
                Log.d("###", "name2"+subscribersList);
                callback.getFriendsList(subscribersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static void getCommentsList(String nickName,String newsId, FirebaseModel model, Callbacks.getCommentsList callback){
        model.initAll();
        Query query=model.getUsersReference().child(nickName).child("looks")
                .child(newsId).child("comments");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Comment> commentsList = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Comment comment=new Comment();
                    comment=snap.getValue(Comment.class);
                    commentsList.add(comment);
                }
                callback.getCommentsList(commentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static void getLookClothes(String nick,String uid,FirebaseModel firebaseModel,Callbacks.getLookClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick).child("looks").child(uid)
                .child("clothesCreators");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesArrayList=new ArrayList<>();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Clothes clothes=new Clothes();
                    clothes=snap.getValue(Clothes.class);
                    clothesArrayList.add(clothes);
            }
                callback.getLookClothes(clothesArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ///////////////////MY CLOTHES//////////////////
    public static void getMyClothes(String nick,FirebaseModel firebaseModel,Callbacks.GetClothes callback){
        firebaseModel.initAll();
        Query query=firebaseModel.getUsersReference().child(nick)
                .child("myClothes").orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Clothes> clothesFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Clothes clothes = new Clothes();
                    clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                    clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                    clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                    clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                    clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                    clothes.setCreator(snap.child("creator").getValue(String.class));
                    clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
                    clothes.setDescription(snap.child("description").getValue(String.class));
                    clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                    clothes.setModel(snap.child("model").getValue(String.class));
                    clothes.setUid(snap.child("uid").getValue(String.class));
                    clothesFromBase.add(clothes);
                }
                callback.getClothes(clothesFromBase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //////////////////////////////////////////

    public static void setState (String state, String nick, FirebaseModel firebaseModel){
        firebaseModel.initAll();
        final long[] time = new long[1];

        if (state == "Online"){
            firebaseModel.getUsersReference().child(nick).child("Status").setValue(state);
            firebaseModel.getUsersReference().child(nick).child("Last Seen").setValue(state);
        }
        else {
            firebaseModel.getUsersReference().child(nick).child("Status").setValue(state);
            GetTimeStampNow(nick, firebaseModel, new Callbacks.GetTimesTamp() {
                @Override
                public void GetTimesTamp(long timesTamp) {
                    time[0] = timesTamp;
                }
            });
            firebaseModel.getUsersReference().child(nick).child("Status").setValue(time[0]);
        }
    }


    public static String getCurrentTime() {
        String time;
        final Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        String timeH, timeM;
        timeH = String.valueOf(hours);
        timeM = String.valueOf(minutes);
        if (minutes < 10)
            timeM = "0" + minutes;
        if (hours < 10)
            timeH = "0" + hours;
        time = timeH + ":" + timeM;
        return time;
    }
}