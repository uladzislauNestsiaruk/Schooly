package com.egormoroz.schooly.ui.chat;

//TODO:Online display

public class User  {

    private String id;
    private String name;
    private String avatar;
    private boolean online;

    public User(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
