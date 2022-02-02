package com.egormoroz.schooly;

public class Nontification {

    private String nick;
    private String typeDispatch;
    private String typeView;
    private String timestamp;
    private String clothesName;
    private String clothesImage;
    private String type;

    public Nontification(String  nick,String typeDispatch,String typeView,String timestamp
    ,String clothesName,String clothesImage,String type){
        this.nick=nick;
        this.typeDispatch=typeDispatch;
        this.typeView=typeView;
        this.timestamp=timestamp;
        this.clothesImage=clothesImage;
        this.clothesName=clothesName;
        this.type=type;
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

    public String  getClothesImage(){
        return this.clothesImage;
    }

    public void setClothesImage(String  clothesImage){
        this.clothesImage=clothesImage;
    }

    public String  getClothesName(){
        return this.clothesName;
    }

    public void setClothesName(String  clothesName){
        this.clothesName=clothesName;
    }

    public String  getType(){
        return this.type;
    }

    public void setType(String  type){
        this.type=type;
    }
}

