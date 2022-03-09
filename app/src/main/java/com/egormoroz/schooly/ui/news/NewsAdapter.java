package com.egormoroz.schooly.ui.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ImageViewHolder> {

    private List<NewsItem> newsList;
    public static String newsId, likeWord;
    FirebaseModel firebaseModel = new FirebaseModel();
    long value;
    Activity activity;


    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseModel.initAll();
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_image,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);
        holder.setIsRecyclable(false);
        holder.like_count.setText(newsItem.getLikes_count());
        Picasso.get().load(newsItem.getImageUrl()).into(holder.newsImage);
        holder.description.setText(newsItem.getItem_description());
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query likeref = firebaseModel.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId());
                likeref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.like.setImageResource(R.drawable.ic_pressedheart40dp);
                        }
                        else {
                            holder.like.setImageResource(R.drawable.ic_heart40dp);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        value = Long.parseLong(holder.like_count.getText().toString());
                        Query likeref = firebaseModel.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId());
                        likeref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    value -= 1;
                                    holder.like.setImageResource(R.drawable.ic_heart40dp);
                                    firebaseModel.getReference("users").child(nick).child("likedNews").child(newsItem.getNewsId()).removeValue();
                                }
                                else {
                                    value += 1;
                                    holder.like.setImageResource(R.drawable.ic_pressedheart40dp);
                                    firebaseModel.getReference("users").child(nick).child("likedNews").child(newsItem.getNewsId()).setValue("liked");
                                }
                                firebaseModel.getReference("news").child(newsItem.getNewsId()).child("likesCount").setValue(String.valueOf(value));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });


                holder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecentMethods.setCurrentFragment(NewsFragment.newInstance(), NewsFragment.newInstance().getActivity());
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImage, like, comment;
        TextView description, like_count;
        private DatabaseReference ref;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImage = itemView.findViewById(R.id.image_view);
            like = itemView.findViewById(R.id.news_like);
            description = itemView.findViewById(R.id.imageDescription);
            like_count = itemView.findViewById(R.id.likes_count);
            comment = itemView.findViewById(R.id.news_comment);
        }
    }

}


