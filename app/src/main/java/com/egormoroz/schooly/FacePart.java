package com.egormoroz.schooly;

import java.nio.Buffer;

public class FacePart {
    private String type;
    private String partTitle;
    String model;
    String uid;
    Buffer buffer;


    public FacePart(String type, String  partTitle,
                   String model,String uid,Buffer buffer
    ){
        this.type=type;
        this.partTitle=partTitle;
        this.model=model;
        this.uid=uid;
        this.buffer=buffer;
    }
    public FacePart(){
    }

    public String  getPartType(){
        return this.type;
    }

    public void setPartType(String  type){
        this.type=type;
    }

    public String getPartTitle(){
        return this.partTitle;
    }

    public void setPartTitle(String partTitle){
        this.partTitle=partTitle;
    }

    public String getModel(){
        return this.model;
    }

    public void setModel(String model){
        this.model=model;
    }

    public String getUid(){
        return this.uid;
    }

    public void setUid(String uid){
        this.uid=uid;
    }

    public Buffer getBuffer(){
        return this.buffer;
    }

    public void setBuffer(Buffer buffer){
        this.buffer=buffer;
    }


}
