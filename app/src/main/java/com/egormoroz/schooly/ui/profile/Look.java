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
    //
}
