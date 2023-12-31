package com.egormoroz.schooly.ui.chat;

import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.news.NewsItem;

import java.util.Date;

public class Message {
    private String from, message, type, to, messageID, time, date, name;
    Clothes clothes;
    NewsItem newsItem;


    public Message(String from, String message, String type, String to, String messageID, String time,
                   String date, String name,Clothes clothes,NewsItem newsItem) {
        this.from = from;
        this.message = message;
        this.type = type;
        this.to = to;
        this.messageID = messageID;
        this.time = time;
        this.date = date;
        this.name = name;
        this.clothes=clothes;
        this.newsItem=newsItem;
    }

    public  Message()
    {

    }

    public String getId() {
        return messageID;
    }

    public String getText() {
        return null;
    }



    public Date getCreatedAt() {
        return null;
    }


    public static class Video {

        private String url;

        public Video() {

        }
        public String getUrl(){
            return url;
        }
        public Video(String url) {
            this.url = url;
        }
    }
    public static class Voice {

        private String url;
        private int duration;

        public Voice() {

        }

        public String getUrl() {
            return url;
        }

        public int getDuration() {
            return duration;
        }

        public Voice(String url, int duration) {
            this.url = url;
            this.duration = duration;

        }
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Clothes getClothes () {return  this.clothes;}

    public void setClothes (Clothes clothes) {this.clothes = clothes;}

    public NewsItem getNewsItem () {return  this.newsItem;}

    public void setNewsItem (NewsItem newsItem) {this.newsItem = newsItem;}
}

