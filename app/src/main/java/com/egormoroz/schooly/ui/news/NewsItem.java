package com.egormoroz.schooly.ui.news;

import android.widget.ImageView;

public class NewsItem {
    public String item_description, likes_count, newsId,postTime,nick,
            comments,clothesCreators,ImageUrl;
    private   long lookPrice,viewCount;

    public NewsItem(){

    }

    public NewsItem(String ImageUrl, String item_description, String likes_count, String newsId,
    String comments,String clothesCreators,long lookPrice,long viewCount,String postTime,String nick){
        this.ImageUrl = ImageUrl;
        this.likes_count = likes_count;
        this.item_description = item_description;
        this.newsId = newsId;
        this.comments=comments;
        this.clothesCreators=clothesCreators;
        this.lookPrice=lookPrice;
        this.viewCount=viewCount;
        this.postTime=postTime;
        this.nick=nick;
    }

    public String getImageUrl (){
        return  this.ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getItem_description (){
        return  this.item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getLikes_count (){
        return  this.likes_count;
    }


    public void setLikesCount(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getNewsId() {return this.newsId;}

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getComments (){
        return  this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getClothesCreators() {return this.clothesCreators;}

    public void setClothesCreators(String clothesCreators) {
        this.clothesCreators = clothesCreators;
    }

    public long getLookPrice (){
        return  this.lookPrice;
    }

    public void setLookPrice(long lookPrice) {
        this.lookPrice = lookPrice;
    }

    public long getViewCount() {return this.viewCount;}

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public String getPostTime() {return this.postTime;}

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getNick() {return this.nick;}

    public void setNick(String nick) {
        this.nick = nick;
    }
}
