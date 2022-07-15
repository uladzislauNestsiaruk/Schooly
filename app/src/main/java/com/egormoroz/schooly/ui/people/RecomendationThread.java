package com.egormoroz.schooly.ui.people;

import android.util.Log;

import androidx.annotation.NonNull;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Subscriber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class RecomendationThread {
    String userNick;
    int subscription_limit = 1000;
    RecomendationThread(String input){
        this.userNick = input;
    }
    FirebaseModel model = new FirebaseModel();
    public ArrayList<Subscriber> get_Subs(String nick){
        ArrayList<Subscriber> subscribers = new ArrayList<>();
        model.initAll();
        model.getUsersReference().child(nick).child("subscription").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();
                            for(DataSnapshot snap : snapshot.getChildren())
                                subscribers.add(ValidateSnap(snap));
                        }
                        else{
                            Log.d("########", "Subs recommendation error");
                        }
                    }
                });
        return subscribers;
    }
    public ArrayList<Subscriber> get_Recommendations(){
        Map<String, Integer> accurancy = new HashMap<>();
        ArrayList<Subscriber> my_subscribers = get_Subs(userNick);
        for(Subscriber current_subscriber : my_subscribers){
            ArrayList<Subscriber> temporary_subscribers = get_Subs(current_subscriber.getSub());
            for(int i = 0; i < Integer.min(subscription_limit, temporary_subscribers.size()); i++){
                String name = temporary_subscribers.get(i).getSub();
                int accur = accurancy.get(name) + 1;
                accurancy.remove(name);
                accurancy.put(name, accur);
            }
        }
        ArrayList<Subscriber> result_list = new ArrayList<>();
        for(Map.Entry entry : accurancy.entrySet())
            result_list.add(new Subscriber(String.valueOf(entry.getKey())));
        result_list.sort((Subscriber a, Subscriber b) -> -(int)(accurancy.get(a.getSub())
                - accurancy.get(b.getSub())));
        return result_list;
    }
    public ArrayList<UserPeopleAdapter> ValidateRcomendations(){
        ArrayList<Subscriber> recommendations = get_Recommendations();
        ArrayList<UserPeopleAdapter> validated_list = new ArrayList<>();
        for(Subscriber sub : recommendations)
            validated_list.add(validate_Subscriber(sub));
        return validated_list;
    }
    public UserPeopleAdapter validate_Subscriber(Subscriber sub){
        UserPeopleAdapter userPeopleAdapter=new UserPeopleAdapter();
        userPeopleAdapter.setNick(sub.getSub());
        model.getUsersReference().child(sub.getSub()).child("avatar").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            userPeopleAdapter.setAvatar(snapshot.getValue(String.class));
                        }
                        else
                            Log.d("########", "Avatar not found");
                    }
                });
        model.getUsersReference().child(sub.getSub()).child("bio").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            userPeopleAdapter.setBio(snapshot.getValue(String.class));
                        }
                        else
                            Log.d("########", "Bio not found");
                    }
                });
        return userPeopleAdapter;
    }
    public Subscriber ValidateSnap(DataSnapshot snap){
        Subscriber subscriber = new Subscriber();
        subscriber.setSub(snap.getValue(String.class));
        return subscriber;
    }
}
