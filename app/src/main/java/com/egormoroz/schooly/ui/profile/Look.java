package com.egormoroz.schooly.ui.profile;

public class Look {
    private String nick;
    private String lookImage;
    private String postTime;
    private String lookID;

    public Look(String  nick){
        this.nick=nick;
    }
    public Look(){
    }

    public String  getNick(){
        return this.nick;
    }

    public void setNick(String  nick){
        this.nick=nick;
    }

    public String  getLookImage(){
        return this.lookImage;
    }

    public void setLookImage(String  lookImage){
        this.lookImage=lookImage;
    }

    public String  getPostTime(){
        return this.postTime;
    }

    public void setPostTime(String  postTime){
        this.postTime=postTime;
    }

    public String  getLookID(){
        return this.lookID;
    }

    public void setLookID(String  lookID){
        this.lookID=lookID;
    }
}
