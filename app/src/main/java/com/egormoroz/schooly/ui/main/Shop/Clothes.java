package com.egormoroz.schooly.ui.main.Shop;

import java.util.ArrayList;

public class Clothes {
    private String type;
    private String clothesImage;
    private long clothesPrice=1;

    public Clothes(String type, String  clothesImage, long clothesPrice){
        this.type=type;
        this.clothesImage=clothesImage;
        this.clothesPrice=clothesPrice;
    }
    public Clothes(){
    }

    public String  getClothesType(){
        return this.type;
    }

    public void setClothesType(String  type){
        this.type=type;
    }

    public String getClothesImage(){
        return this.clothesImage;
    }

    public void setClothesImage(String clothesImage){
        this.clothesImage=clothesImage;
    }

    public long getClothesPrice(){
        return this.clothesPrice;
    }

    public void setClothesPrice(long clothesPrice){
        this.clothesPrice=clothesPrice;
    }

}
