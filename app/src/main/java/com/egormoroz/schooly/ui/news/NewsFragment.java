package com.egormoroz.schooly.ui.news;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.chat.holders.ImageViewerActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        firebaseModel.initAll();

        return root;
    }

    @Override
    public void onStart(){
        super.onStart();

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                FirebaseRecyclerOptions<News> options =
                        new FirebaseRecyclerOptions.Builder<News>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("news").orderByChild("TimeMill"), News.class)
                                .build();


                FirebaseRecyclerAdapter<News, NewsViewHolder> adapter =
                        new FirebaseRecyclerAdapter<News, NewsViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int pos, @NonNull News news) {
                                String subNick = getRef(pos).getKey();
                                FirebaseDatabase.getInstance().getReference().child("News").child(subNick).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        newsViewHolder.likes_count.setText(news.getLike_count());
                                        newsViewHolder.user_nick.setText(news.getUser_nick());
                                        newsViewHolder.date.setText(news.getDate());
                                        newsViewHolder.like.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });
                                        Picasso.get().load(news.getImage()).into(newsViewHolder.news_image);
                                        newsViewHolder.comment.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        });

                                        newsViewHolder.news_image.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(), ImageViewerActivity.class);
                                                intent.putExtra("url", news.getImage());
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
                            public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                return null;
                            }
                        };
            }
        });
    }


    public static class  NewsViewHolder extends RecyclerView.ViewHolder
    {


        ImageView news_image, like, comment;
        TextView news_text, date, user_nick, likes_count;


        public NewsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            like = itemView.findViewById(R.id.otheUserImage);
            comment = itemView.findViewById(R.id.time);
            news_image = itemView.findViewById(R.id.otherUserNick);
            news_text = itemView.findViewById(R.id.lastMessage);
            date = itemView.findViewById(R.id.lastMessage);
            user_nick = itemView.findViewById(R.id.lastMessage);
            likes_count = itemView.findViewById(R.id.lastMessage);
        }
    }

}