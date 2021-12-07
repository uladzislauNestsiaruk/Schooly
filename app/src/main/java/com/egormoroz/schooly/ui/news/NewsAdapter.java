package com.egormoroz.schooly.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ImageViewHolder> {

    private List<NewsItem> newsList;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_image,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.setImageData(newsList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

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
            description.setText(newsItem.item_description);
            Picasso.get().load(newsItem.ImageUrl).into(newsImage);
            like_count.setText(newsItem.likes_count);

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
