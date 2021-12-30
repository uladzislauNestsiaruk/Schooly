package com.egormoroz.schooly.ui.chat;

public class Chat {
    private String name = "";
    private String LastTime = "";
    private String LastMessage = "";

    public Chat (String name, String LastTime, String LastMessage){
        this.name = name;
        this.LastTime = LastTime;
        this.LastMessage = LastMessage;
    }

    public Chat() {

    }

    public String getName () {return  this.name;}

    public void setName (String name) {this.name = name;}

    public String getLastMessage () {return  this.LastMessage;}

    public void setLastMessage (String Messages) {this.LastMessage = Messages;}

    public String getLastTime () {return  this.LastTime;}

    public void setLastTime (String time) {this.LastTime = time;}
}
