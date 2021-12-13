package com.egormoroz.schooly.ui.news;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ImageViewHolder> {

    private List<NewsItem> newsList;
    public static String newsId;
    FirebaseModel firebaseModel=new FirebaseModel();
    long value;


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
        holder.setImageData(newsList.get(position));
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = firebaseModel.getReference().child("news").child(newsId).child("likesCount");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("######", "dd  ");
                        value = Long.valueOf(dataSnapshot.getValue(String.class));
                        value = value + 1;
                        Log.d("news", "addLike");
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });
                firebaseModel.getReference("news").child(newsId).child("likesCount").setValue(String.valueOf(value));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

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


        void setImageData(NewsItem newsItem) {
            ref = FirebaseDatabase.getInstance().getReference().child("news").child(newsItem.item_description);
            description.setText(newsItem.item_description);
            Picasso.get().load(newsItem.ImageUrl).into(newsImage);
            like_count.setText(newsItem.likes_count);
            newsId = newsItem.newsId;
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final long[] value = new long[1];
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("news").child("likesCount");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            value[0] = Long.parseLong((String) dataSnapshot.getValue());
                            value[0] = value[0] + 1;
                            dataSnapshot.getRef().setValue(String.valueOf(value[0]));
                            Log.d("news", "addLike");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }


                    });
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
