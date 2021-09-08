package com.egormoroz.schooly;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
                6, "unknown", "Helicopter", 1000, "Miner", 1);
        ref.child(nick).setValue(res);
        return nick.isEmpty();
    }
    public static void hasThisUserFun(FirebaseAuth AuthenticationBase, FirebaseUser user,
                                      Callbacks.hasGoogleUser callback) {
        if (user == null) {
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
                    userData.setAvatar(snapshot.child("avatar").getValue(Long.class));
                    userData.setGender(snapshot.child("gender").getValue(String.class));
                    //////////////////userData.setMiners();
                    userData.setNick(snapshot.child("nick").getValue(String.class));
                    userData.setPassword(snapshot.child("password").getValue(String.class));
                    userData.setPhone(snapshot.child("phone").getValue(String.class));
                    userData.setUid(snapshot.child("uid").getValue(String.class));
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
                    miner.setMinerImage(snap.child("minerImage").getValue(Long.class));
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
                    miner.setMinerImage(snap.child("minerImage").getValue(Long.class));
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
                    miner.setMinerImage(snap.child("minerImage").getValue(Long.class));
                    minersFromBase.add(miner);
                }
                callback.GetMinerFromBase(minersFromBase);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public static void buyWeakMiner(String currentMiner,FirebaseModel model,Callbacks.buyMiner callback){
        model.initAll();
        Query query=model.getReference("AppData/AllMiners/Weak")
                .orderByChild(currentMiner);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("#########", "minerrr  "+currentMiner);
                Miner miner=snapshot.child(currentMiner).getValue(Miner.class);
                Log.d("#########", "minerrr  "+miner);
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
                Log.d("#########", "minerrr  "+currentMiner);
                Miner miner=snapshot.child(currentMiner).getValue(Miner.class);
                Log.d("#########", "minerrr  "+miner);
                callback.buyMiner(miner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void buyStrongMiner(String currentMiner,FirebaseModel model,Callbacks.buyMiner callback){
        model.initAll();
        Query query=model.getReference("AppData/AllMiners/Weak")
                .orderByChild(currentMiner);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("#########", "minerrr  "+currentMiner);
                Miner miner=snapshot.child(currentMiner).getValue(Miner.class);
                Log.d("#########", "minerrr  "+miner);
                callback.buyMiner(miner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void MyMinersFromBase(String uid,FirebaseModel model, Callbacks.GetMyMinerFromBase callback) {
        model.initAll();
        Query query = model.getUsersReference().child("uid")
                .child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Miner> myMinersFromBase=new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Miner miner = new Miner();
                    miner.setInHour(snap.child("inHour").getValue(Long.class));
                    miner.setMinerPrice(snap.child("minerPrice").getValue(Long.class));
                    miner.setMinerImage(snap.child("minerImage").getValue(Long.class));
                    myMinersFromBase.add(miner);
                }
                callback.GetMyMinerFromBase(myMinersFromBase);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}