package com.egormoroz.schooly.ui.people;

import android.util.Log;
import android.util.LogPrinter;

import androidx.annotation.NonNull;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecomendationThread {
    String userNick;
    int subscription_limit = 1000;
    int users_in_process = 0;
    UserInformation userInformation;
    FirebaseModel model = new FirebaseModel();
    public RecomendationThread(String input, Callbacks.getRecommendationsThread recommendationsInterface,
                               UserInformation info){
        model.initAll();
        this.userNick = input;
        get_Subs(userNick, 1, recommendationsInterface);
        this.userInformation = info;
    }
    HashMap<UserPeopleAdapter, Integer> user_accurancy = new HashMap<>();
    public void get_Subs(String nick, int deep, Callbacks.getRecommendationsThread recommendationsInterface){
        Log.d("##### ", "GET SUBS CALLED FOR USER: " + nick);
        model.getUsersReference().child(nick).child("subscription").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();
                            for(DataSnapshot snap : snapshot.getChildren()) {
                                Subscriber current_user = ValidateSnap(snap);
                                if(deep == 2)
                                    validate_Subscriber(ValidateSnap(snap), recommendationsInterface);
                                else if(deep == 1){
                                    Log.d("###### " , "RECURSION STARTED");
                                    get_Subs(current_user.getSub(), deep + 1, recommendationsInterface);
                                }
                            }
                        }
                        else{
                            Log.d("########", "Subs recommendation error");
                        }
                    }
                });
    }
    public ArrayList<UserPeopleAdapter> get_Recommendations(){
        ArrayList<UserPeopleAdapter> result_list = new ArrayList<>();
        Set mapKeys = user_accurancy.keySet();
        Iterator keysIterator = mapKeys.iterator();
        while(keysIterator.hasNext())
            result_list.add((UserPeopleAdapter)keysIterator.next());
        result_list.sort((UserPeopleAdapter a, UserPeopleAdapter b) -> -(int)(user_accurancy.get(a)
                - user_accurancy.get(b)));
        Log.d("#####", "Recommendation list size: " + user_accurancy.size());
        return result_list;
    }
    public void validate_Subscriber(Subscriber sub, Callbacks.getRecommendationsThread recommendationsInterface){
        Log.d("#### ", "Validation started");
        UserPeopleAdapter userPeopleAdapter=new UserPeopleAdapter();
        userPeopleAdapter.setNick(sub.getSub());
        model.getUsersReference().child(sub.getSub()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.d("######", "user added to the list");
                            DataSnapshot snapshot = task.getResult();
                            userPeopleAdapter.setAvatar(snapshot.child("avatar").getValue(String.class));
                            userPeopleAdapter.setBio(snapshot.child("bio").getValue(String.class));
                            int acuur = 0;
                            if(user_accurancy.containsKey(userPeopleAdapter))
                                acuur = user_accurancy.get(userPeopleAdapter);
                            user_accurancy.remove(userPeopleAdapter);
                            user_accurancy.put(userPeopleAdapter, acuur + 1);
                            ArrayList<UserPeopleAdapter> users = get_Recommendations();
                            AlreadySearchAdapter alreadySearchAdapter=new AlreadySearchAdapter(users,userInformation);
                            recommendationsInterface.getRecommendationsInterface(alreadySearchAdapter);
                        }
                        else
                            Log.d("########", "Avatar not found");
                    }
                });
    }
    public Subscriber ValidateSnap(DataSnapshot snap){
        Subscriber subscriber = new Subscriber();
        subscriber.setSub(snap.getValue(String.class));
        return subscriber;
    }
}
