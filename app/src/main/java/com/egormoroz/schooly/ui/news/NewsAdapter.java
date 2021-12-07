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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends FirebaseRecyclerAdapter<NewsItem, NewsAdapter.ImageViewHolder> {

    private List<NewsItem> newsList;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NewsAdapter(@NonNull FirebaseRecyclerOptions<NewsItem> options) {
        super(options);
    }

//    public NewsAdapter(List<NewsItem> newsList) {
//        super(newsList);
//        this.newsList = newsList;


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_image,
        parent,
        false);
        return new ImageViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i, @NonNull NewsItem newsItem) {
        imageViewHolder.setImageData(newsItem);
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
