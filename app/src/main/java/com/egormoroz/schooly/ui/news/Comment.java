package com.egormoroz.schooly.ui.news;

public class Comment {
    private String text, likes_count, commentId,postTime,nick,
            comments,clothesCreators,image;
    private   long lookPrice,lookPriceDollar,viewCount;

    public Comment(){

    }

    public Comment( String text, String likes_count, String commentId,
                    String postTime,String nick,String image){
        this.image = image;
        this.likes_count = likes_count;
        this.text = text;
        this.commentId = commentId;
        this.postTime=postTime;
        this.nick=nick;
    }

    public String getImage (){
        return  this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText (){
        return  this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLikes_count (){
        return  this.likes_count;
    }

    public void setLikesCount(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getCommentId() {return this.commentId;}

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComments (){
        return  this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPostTime() {return this.postTime;}

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getNick() {return this.nick;}

    public void setNick(String nick) {
        this.nick = nick;
    }

    public long getLookPriceDollar (){
        return  this.lookPriceDollar;
    }

    public void setLookPriceDollar(long lookPriceDollar) {
        this.lookPriceDollar = lookPriceDollar;
    }
}
