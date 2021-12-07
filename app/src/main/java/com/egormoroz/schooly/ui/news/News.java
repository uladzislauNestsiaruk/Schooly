package com.egormoroz.schooly.ui.news;

public class News {
    private String user_nick = "";
    private String date = "";
    private String like_count = "";
    private String text  = "";
    private String image;

    private News(String user_nick, String date, String like_count, String text, String image){
        this.user_nick = user_nick;
        this.date = date;
        this.like_count = like_count;
        this.text = text;
        this.image = image;
    }

    public News(){

    }


    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }
}
