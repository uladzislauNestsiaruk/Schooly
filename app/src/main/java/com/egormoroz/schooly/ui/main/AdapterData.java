package com.egormoroz.schooly.ui.main;

public class AdapterData {
    private String inHour="tyomaa";
    private int minerImage;

    public AdapterData(String inHour, int minerImage){
        this.inHour=inHour;
        this.minerImage=minerImage;
    }

    public String getInHour(){
        return this.inHour;
    }

    public void setInHour(String inHour){
        this.inHour=inHour;
    }

    public int getMinerImage(){
        return this.minerImage;
    }

    public void setMinerImage(int minerImage){
        this.minerImage=minerImage;
    }
}
