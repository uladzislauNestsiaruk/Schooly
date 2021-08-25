package com.egormoroz.schooly.ui.main.Mining;

import java.util.ArrayList;

public class Miner {
    private int inHour;
    private int minerImage;
    private int minerPrice;

    public Miner(int inHour, int minerImage, int minerPrice){
        this.inHour=inHour;
        this.minerImage=minerImage;
        this.minerPrice=minerPrice;
    }

    public int getInHour(){
        return this.inHour;
    }

    public void setInHour(int inHour){
        this.inHour=inHour;
    }

    public int getMinerImage(){
        return this.minerImage;
    }

    public void setMinerImage(int minerImage){
        this.minerImage=minerImage;
    }

    public int getMinerPrice(){
        return this.minerPrice;
    }

    public void setMinerPrice(int minerPrice){
        this.minerPrice=minerPrice;
    }
}
