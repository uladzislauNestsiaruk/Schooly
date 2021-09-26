package com.egormoroz.schooly.ui.chat;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;

@IgnoreExtraProperties
public abstract class Dialog implements IDialog<Message> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<User> users;
    private Message lastMessage;
    private int unreadCount;

    public Dialog(){

    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    public void setDialogPhoto(String dialogPhoto){
        this.dialogPhoto = dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    public void setDialogName(String dialogName){
        this.dialogName = dialogName;
    }
    @Exclude
    @Override
    public ArrayList<User> getUsers() {
        return users;
    }
    @Exclude
    public void setUsers(ArrayList<User> users){
        this.users = users;
    }
@Exclude
    @Override
    public Message getLastMessage() {
        return lastMessage;
    }
@Exclude
    @Override
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount){
        this.unreadCount = unreadCount;
    }

    public Dialog(String id, String name, String photo,
                  ArrayList<User> users, Message lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

}
