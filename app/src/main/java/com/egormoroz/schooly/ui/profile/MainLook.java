package com.egormoroz.schooly.ui.profile;


import java.nio.Buffer;

public class MainLook {

    String buffer;

    public MainLook(String buffer){
        this.buffer=buffer;
    }
    public MainLook(){
    }

    public String  getBuffer(){
        return this.buffer;
    }

    public void setBuffer(String  buffer){
        this.buffer=buffer;
    }
}
