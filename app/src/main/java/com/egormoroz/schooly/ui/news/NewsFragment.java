package com.egormoroz.schooly.ui.news;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    FirebaseModel firebaseModel=new FirebaseModel();

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        firebaseModel.initAll();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getSubscribersList(nick, firebaseModel
                        , new Callbacks.getSubscribersList() {
                            @Override
                            public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                                Log.d("###", "a"+subscribers);
                                subscribers.add(new Subscriber("katysha"));
                                firebaseModel.getUsersReference().child(nick).child("subscribers")
                                        .setValue(subscribers);
                            }
                        });
            }
        });
        TextView news=root.findViewById(R.id.newsText);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.getSubscribersList(nick, firebaseModel
                                , new Callbacks.getSubscribersList() {
                                    @Override
                                    public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                                        subscribers.add(new Subscriber("katyshenka"));
                                        firebaseModel.getUsersReference().child(nick).child("subscribers").setValue(subscribers);
                                    }
                                });
                    }
                });
            }
        });
        return root;
    }


}