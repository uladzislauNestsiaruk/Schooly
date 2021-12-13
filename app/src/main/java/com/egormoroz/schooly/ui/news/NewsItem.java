package com.egormoroz.schooly.ui.news;

import android.widget.ImageView;

public class NewsItem {
    public String ImageUrl, item_description, likes_count, newsId;

    public NewsItem(String ImageUrl, String item_description, String likes_count, String newsId){
        this.ImageUrl = ImageUrl;
        this.likes_count = likes_count;
        this.item_description = item_description;
        this.newsId = newsId;
    }



    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getImageUrl (){
        return  ImageUrl;
    }
    public String getItem_description (){
        return  item_description;
    }
    public String getLikes_count (){
        return  likes_count;
    }
    public String getNewsId() {return newsId;}
}
