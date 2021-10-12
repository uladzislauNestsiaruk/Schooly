package com.egormoroz.schooly.ui.chat;

public class Chat {
    private String name = "";
    private int time = 1;
    private String lastMessage = "";

    public Chat (String name, int time, String lastMessage){
        this.name = name;
        this.time = time;
        this.lastMessage = lastMessage;
    }

    public Chat() {

    }

    public String getName () {return  this.name;}

    public void setName (String name) {this.name = name;}

    public String getLastMessage () {return  this.lastMessage;}

    public void setLastMessage (String lastMessage) {this.lastMessage = lastMessage;}

    public int getTime () {return  this.time;}

    public void setTime (int time) {this.time = time;}
}
