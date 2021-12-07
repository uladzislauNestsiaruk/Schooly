package com.egormoroz.schooly.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.egormoroz.schooly.ui.chat.holders.ImageViewerActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import  com.egormoroz.schooly.ui.news.NewsAdapter;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    NewsAdapter newsAdapter;
    ViewPager2 viewPager2;
    TextView newsText;
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
        newsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(AddNewsFragment.newInstance(), getActivity());
            }
        });
//        newsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }


    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        NewsItem newsItem = new NewsItem();
//        newsItem.ImageUrl = "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/Voice%2F-Mo4AAb6qt_XSJhVX8gm.3gp?alt=media&token=8ccb4de4-daf4-4ed6-bbfa-7af291ea5dbe";
//        newsItem.item_description = "hello";
//        newsItem.likes_count = "48";
//        newsItems.add(newsItem);
//
//
//        NewsItem newsItemTwo = new NewsItem();
//        newsItemTwo.ImageUrl = "https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/Images%2F-Mq6JKadvAhJ4jCGZk5C.jpg?alt=media&token=41b94f7a-542d-4f18-956b-60bcc1ab5722";
//        newsItemTwo.item_description = "odimn";
//        newsItemTwo.likes_count = "52";
//        newsItems.add(newsItemTwo);

     //   viewPager2.setAdapter(new NewsAdapter(newsItems));
//        newsText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RecentMethods.setCurrentFragment(AddNewsFragment.newInstance(), getActivity());
//            }
//        });
//        FirebaseRecyclerOptions<NewsItem> options =
//                new FirebaseRecyclerOptions.Builder<NewsItem>()
//                .setQuery(FirebaseDatabase.getInstance().getReference().child("news").orderByChild("TimeMill")  , NewsItem.class)
//                .build();
//
//        newsAdapter = new NewsAdapter(options);
//        viewPager2.setAdapter(newsAdapter);


    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<NewsItem> options =
//                new FirebaseRecyclerOptions.Builder<NewsItem>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("news").child("spaccacrani").child("-MqBYkfzvTksyVbuA2EG") , NewsItem.class)
//                        .build();
//
//        Log.d("Neews", String.valueOf(options));
//        newsAdapter = new NewsAdapter(options);
//        Log.d("Neews", String.valueOf(options.getSnapshots()));
//        viewPager2.setAdapter(newsAdapter);
//        newsAdapter.startListening();
//    }
    @Override
    public void onStart(){
        super.onStart();

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                FirebaseRecyclerOptions<NewsItem> options =
                        new FirebaseRecyclerOptions.Builder<NewsItem>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("news").orderByChild("TimeMill"), NewsItem.class)
                                .build();


                FirebaseRecyclerAdapter<NewsItem, ImageViewHolder> adapter =
                        new FirebaseRecyclerAdapter<NewsItem, ImageViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int pos, @NonNull NewsItem news) {
                                String subNick = getRef(pos).getKey();
                                FirebaseDatabase.getInstance().getReference().child("News").child(subNick).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        imageViewHolder.like_count.setText(news.getLikes_count());
                                        //imageViewHolder.user_nick.setText(news.getUser_nick());
                                        imageViewHolder.description.setText(news.getItem_description());
                                        imageViewHolder.like.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });
                                        Picasso.get().load(news.getImageUrl()).into(imageViewHolder.newsImage);
                                        imageViewHolder.comment.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });

                                        imageViewHolder.newsImage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(), ImageViewerActivity.class);
                                                intent.putExtra("url", news.getImageUrl());
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view  =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_image,
                                        parent,
                                        false);
                                return new NewsFragment.ImageViewHolder(view);
                            }
                        };
                viewPager2.setAdapter(adapter);
                adapter.startListening();
            }
        });
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImage, like, comment;
        TextView description, like_count;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImage = itemView.findViewById(R.id.image_view);
            like = itemView.findViewById(R.id.news_like);
            description = itemView.findViewById(R.id.imageDescription);
            like_count = itemView.findViewById(R.id.likes_count);
            comment = itemView.findViewById(R.id.news_comment);
        }


        void setImageData(NewsItem newsItem) {
            description.setText(newsItem.getItem_description());
            Picasso.get().load(newsItem.getImageUrl()).into(newsImage);
            like_count.setText(newsItem.getLikes_count());
            like.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}