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
        newsText = root.findViewById(R.id.news);
        ref = FirebaseDatabase.getInstance().getReference("news");
        newsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(AddNewsFragment.newInstance(), getActivity());
            }
        });

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {

                Query query=firebaseModel.getReference("news/spaccacrani");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren())
                            remoteImages.add(new NewsItem(data.child("item_description").getValue().toString(),
                                    data.child("ImageUrl").getValue().toString()
                                    ,data.child("likes_count").getValue().toString()));
                        Log.d("####", "news  "+remoteImages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                viewPager2.setAdapter(new NewsAdapter(remoteImages));
                Log.d("news", String.valueOf(remoteImages.size()));
            }
        });

        return root;
    }


//    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @Override
//            public void PassUserNick(String nick) {
//
//                Query query=firebaseModel.getReference("news/spaccacrani");
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot data : snapshot.getChildren())
//                            remoteImages.add(new NewsItem(data.child("item_description").getValue().toString(),
//                                    data.child("ImageUrl").getValue().toString(),
//                                    data.child("likes_count").getValue().toString()));
//                        Log.d("####", "news  "+remoteImages);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                viewPager2.setAdapter(new NewsAdapter(remoteImages));
//                Log.d("news", String.valueOf(remoteImages.size()));
//            }
//        });
//        //////////////
//        ////SECOND////
//        //////////////
//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @Override
//            public void PassUserNick(String nick) {
//                FirebaseRecyclerOptions<NewsItem> options =
//                        new FirebaseRecyclerOptions.Builder<NewsItem>()
//                                .setQuery(ref.orderByChild("TimeMill"), NewsItem.class)
//                                .build();
//
//                FirebaseRecyclerAdapter<NewsItem, NewsAdapter.ImageViewHolder> adapter =
//                        new FirebaseRecyclerAdapter<NewsItem, NewsAdapter.ImageViewHolder>(options) {
//                            @Override
//                            protected void onBindViewHolder(@NonNull final NewsAdapter.ImageViewHolder holder, int position, @NonNull NewsItem model) {
//                                final String usersIDs = getRef(position).getKey();
//                                Log.d("Neews", usersIDs);
//                                final String[] retImage = {"default_image"};
//                                ref.child(usersIDs).addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.exists()) {
//                                            if (dataSnapshot.hasChild("LastMessage"))
//
//                                                Picasso.get().load(model.getImageUrl()).into(holder.newsImage);
//                                               holder.description.setText(model.getItem_description());
//                                               Log.d("Neews", model.getItem_description());
//                                               holder.like_count.setText(model.getLikes_count());
//                                               holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View view) {
//                                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
//                                                    chatIntent.putExtra("curUser", nick);
//                                                    chatIntent.putExtra("othUser", usersIDs);
//                                                    chatIntent.putExtra("visit_image", retImage[0]);
//                                                    startActivity(chatIntent);
//                                                }
//                                            });
//                                        }
//                                    }
//
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//                            }
//
//
//                            @NonNull
//                            @Override
//                            public NewsAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news_image, viewGroup, false);
//                                return new NewsAdapter.ImageViewHolder(view);
//                            }
//                        };
//
//                viewPager2.setAdapter(adapter);
//                adapter.startListening();
//            }
//        });
//    }
}