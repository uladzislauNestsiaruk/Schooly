package com.egormoroz.schooly;

public class Nontification {

    private String nick;
    private String typeDispatch;
    private String typeView;
    private String timestamp;

    public Nontification(String  nick,String typeDispatch,String typeView,String timestamp){
        this.nick=nick;
        this.typeDispatch=typeDispatch;
        this.typeView=typeView;
        this.timestamp=timestamp;
    }
    public Nontification(){
    }

    public String  getNick(){
        return this.nick;
    }

    public void setNick(String  nick){
        this.nick=nick;
    }

    public String  getTypeDispatch(){
        return this.typeDispatch;
    }

    public void setTypeDispatch(String  typeDispatch){
        this.typeDispatch=typeDispatch;
    }

    public String  getTypeView(){
        return this.typeView;
    }

    public void setTypeView(String  typeView){
        this.typeView=typeView;
    }

    public String  getTimestamp(){
        return this.timestamp;
    }

    public void setTimestamp(String  timestamp){
        this.timestamp=timestamp;
    }
}

