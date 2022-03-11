package com.egormoroz.schooly.ui.news;

import android.widget.ImageView;

public class NewsItem {
    private String ImageUrl, item_description, likes_count, newsId,postTime,
            comments,clothesCreators;
    private   long lookPrice,viewCount;
    public NewsItem(){}

    public NewsItem(String ImageUrl, String item_description, String likes_count, String newsId,
    String comments,String clothesCreators,long lookPrice,long viewCount,String postTime){
        this.ImageUrl = ImageUrl;
        this.likes_count = likes_count;
        this.item_description = item_description;
        this.newsId = newsId;
        this.comments=comments;
        this.clothesCreators=clothesCreators;
        this.lookPrice=lookPrice;
        this.viewCount=viewCount;
        this.postTime=postTime;
    }

    public String getImageUrl (){
        return  ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        ImageUrl = ImageUrl;
    }

    public String getItem_description (){
        return  item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getLikes_count (){
        return  likes_count;
    }


    public void setLikesCount(String likes_count) {
        likes_count = likes_count;
    }

    public String getNewsId() {return newsId;}

    public void setNewsId(String newsId) {
        newsId = newsId;
    }

    public String getComments (){
        return  comments;
    }

    public void setComments(String comments) {
        comments = comments;
    }

    public String getClothesCreators() {return clothesCreators;}

    public void setClothesCreators(String clothesCreators) {
        clothesCreators = clothesCreators;
    }

    public long getLookPrice (){
        return  lookPrice;
    }

    public void setLookPrice(long lookPrice) {
        lookPrice = lookPrice;
    }

    public long getViewCount() {return viewCount;}

    public void setViewCount(long viewCount) {
        viewCount = viewCount;
    }

    public String getPostTime() {return postTime;}

    public void setPostTime(String postTime) {
        postTime = postTime;
    }
}
