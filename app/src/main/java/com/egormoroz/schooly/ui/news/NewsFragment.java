package com.egormoroz.schooly.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.Chat;
import com.egormoroz.schooly.ui.chat.holders.ImageViewerActivity;
import com.egormoroz.schooly.ui.main.ChatActivity;
import com.egormoroz.schooly.ui.main.ChatsFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import  com.egormoroz.schooly.ui.news.NewsAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    FirebaseModel firebaseModel = new FirebaseModel();
    NewsAdapter newsAdapter;
    final List<NewsItem> remoteImages = new ArrayList<>();
    ViewPager2 viewPager2;
    TextView newsText;
    ImageView addNews;
    DatabaseReference ref;
    UserInformation userInformation;
    Bundle bundle;

    public NewsFragment(UserInformation userInformation,Bundle bundle) {
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static NewsFragment newInstance(UserInformation userInformation, Bundle bundle) {
        return new NewsFragment(userInformation,bundle);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        firebaseModel.initAll();

        OnBackPressedCallback callback1 = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(MainFragment.newInstance(userInformation, bundle), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback1);

        viewPager2 = root.findViewById(R.id.picturenewspager);
        ref = FirebaseDatabase.getInstance().getReference("news");
        viewPager2.setAdapter(new NewsAdapter(userInformation.getLooks(), userInformation, bundle,NewsFragment.newInstance(userInformation, bundle)));

//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @Override
//            public void PassUserNick(String nick) {
//                remoteImages.clear();
//                Query query=firebaseModel.getReference("news").orderByChild("TimeMill");
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChildren()) {
//                            remoteImages.clear();
//                            for (DataSnapshot data : snapshot.getChildren()) {
//                                Log.d("news", data.toString());
//                                DataSnapshot dataSnapshot = data;
//                                String from = data.child("from").getValue().toString();
//                                isSubscribed(from, dataSnapshot);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//        });

        return root;
    }


    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void isSubscribed(String username, DataSnapshot data) {

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Log.d("###", username);
                Query query = firebaseModel.getUsersReference().child(nick).child("subscription").child(username);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            remoteImages.add(new NewsItem(data.child("imageUrl").getValue().toString(),
//                                    data.child("itemDescription").getValue().toString(),
//                                    data.child("likesCount").getValue().toString(),
//                                    data.child("newsID").getValue().toString()));
//                            viewPager2.setAdapter(new NewsAdapter(remoteImages));
//
//                            viewPager2.setOffscreenPageLimit(3);
//                        } else if (nick.equals(username))
//                            remoteImages.add(new NewsItem(data.child("imageUrl").getValue().toString(),
//                                    data.child("itemDescription").getValue().toString(),
//                                    data.child("likesCount").getValue().toString(),
//                                    data.child("newsID").getValue().toString()));
//                        viewPager2.setAdapter(new NewsAdapter(remoteImages));
//
//                        Log.d("news", String.valueOf(remoteImages.size()));
//                        viewPager2.setOffscreenPageLimit(3);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void isOpen(String username, DataSnapshot data){
        Query query = firebaseModel.getUsersReference().child(username).child("accountType");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.equals("open")) {
//                    remoteImages.add(new NewsItem(data.child("imageUrl").getValue().toString(),
//                            data.child("itemDescription").getValue().toString(),
//                            data.child("likesCount").getValue().toString(),
//                            data.child("newsID").getValue().toString()));
//                    viewPager2.setAdapter(new NewsAdapter(remoteImages));
//
//                    Log.d("news", String.valueOf(remoteImages.size()));
//                    viewPager2.setOffscreenPageLimit(3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}