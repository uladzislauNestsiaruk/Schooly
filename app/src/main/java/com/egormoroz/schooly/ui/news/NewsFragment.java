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

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        firebaseModel.initAll();

        viewPager2 = root.findViewById(R.id.picturenewspager);
        addNews = root.findViewById(R.id.add_news);
        ref = FirebaseDatabase.getInstance().getReference("news");
        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(AddNewsFragment.newInstance(), getActivity());
            }
        });

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                remoteImages.clear();
                Query query=firebaseModel.getReference("news").orderByChild("TimeMill");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            remoteImages.clear();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                if (data.child("from").getValue().toString().equals(nick))
                                    remoteImages.add(new NewsItem(data.child("imageUrl").getValue().toString(),
                                            data.child("itemDescription").getValue().toString(),
                                            data.child("likesCount").getValue().toString(),
                                            data.child("newsID").getValue().toString()));
                                viewPager2.setAdapter(new NewsAdapter(remoteImages));

                                Log.d("news", String.valueOf(remoteImages.size()));
                                viewPager2.setOffscreenPageLimit(3);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        return root;
    }


    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//
//        FirebaseDatabase.getInstance().getReference().child("news").child("spaccacrani")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Log.d("news", "error");
//                        for (DataSnapshot data : snapshot.getChildren()) {
//                            remoteImages.add(new NewsItem(data.child("item_description").getValue().toString(),
//                                    (data.child("ImageUrl").getValue().toString()), data.child("likes_count").getValue().toString()));
//                            Log.d("news", data.child("ImageUrl").getValue().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.d("news", "error");
//                    }
//                });
//        viewPager2.setAdapter(new NewsAdapter(remoteImages));
//    }


    }
}