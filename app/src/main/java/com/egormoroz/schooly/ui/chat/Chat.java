package com.egormoroz.schooly.ui.chat;

import com.egormoroz.schooly.ui.people.UserPeopleAdapter;

import java.util.ArrayList;

public class Chat {
    private String name = "";
    private String LastTime = "";
    private String LastMessage = "";
    private String type = "";
    private long unreadMessages;
    ArrayList<UserPeopleAdapter> members;

    public Chat (String name, String LastTime, String LastMessage, String type,long unreadMessages,
                 ArrayList<UserPeopleAdapter> members){
        this.name = name;
        this.LastTime = LastTime;
        this.LastMessage = LastMessage;
        this.type = type;
        this.unreadMessages=unreadMessages;
        this.members=members;
    }

    public Chat() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName () {return  this.name;}

    public void setName (String name) {this.name = name;}

    public String getLastMessage () {return  this.LastMessage;}

    public void setLastMessage (String Messages) {this.LastMessage = Messages;}

    public String getLastTime () {return  this.LastTime;}

    public void setLastTime (String time) {this.LastTime = time;}

    public long getUnreadMessages () {return  this.unreadMessages;}

    public void setUnreadMessages (long unreadMessages) {this.unreadMessages = unreadMessages;}

    public ArrayList<UserPeopleAdapter> getMembers () {return  this.members;}

    public void setMembers (ArrayList<UserPeopleAdapter> members) {this.members = members;}

}
