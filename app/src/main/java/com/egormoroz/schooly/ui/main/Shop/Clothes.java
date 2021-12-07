package com.egormoroz.schooly.ui.main.Shop;

import java.util.ArrayList;

public class Clothes {
    private String type;
    private String clothesImage;
    private long clothesPrice=1;
    private long purchaseNumber;
    private String clothesTitle;
    private long timesTamp;
    private String creator;


    public Clothes(String type, String  clothesImage, long clothesPrice,String clothesTitle
            ,long purchaseNumber,long timesTamp,String creator){
        this.type=type;
        this.clothesImage=clothesImage;
        this.clothesPrice=clothesPrice;
        this.clothesTitle=clothesTitle;
        this.purchaseNumber=purchaseNumber;
        this.timesTamp=timesTamp;
        this.creator=creator;
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

    public String getClothesTitle(){
        return this.clothesTitle;
    }

    public void setClothesTitle(String clothesTitle){
        this.clothesTitle=clothesTitle;
    }

    public long getPurchaseNumber(){
        return this.purchaseNumber;
    }

    public void setPurchaseNumber(long purchaseNumber){
        this.purchaseNumber=purchaseNumber;
    }

    public String getCreator(){
        return this.creator;
    }

    public void setCreator(String creator){
        this.creator=creator;
    }

    public long getTimesTamp(){
        return this.timesTamp;
    }

    public void setTimesTamp(long timesTamp){
        this.timesTamp=timesTamp;
    }

}