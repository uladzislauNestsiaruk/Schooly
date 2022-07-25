package com.egormoroz.schooly;

public class Person {
    private String hair;
    private String brows;
    private String eyes;
    private String nose;
    private String lips;
    private String skinColor;
    private String pirsing;
    private String body;
    private String ears;
    private String head;

    public Person(String hair,String brows,String eyes,String nose,String lips,
                  String skinColor,String pirsing,String body,String ears,String head){
        this.hair=hair;
        this.brows=brows;
        this.eyes=eyes;
        this.nose=nose;
        this.lips=lips;
        this.skinColor=skinColor;
        this.pirsing=pirsing;
        this.body=body;
        this.ears=ears;
        this.head=head;
    }

    public Person(){

    }

    public String  getHair(){
        return this.hair;
    }

    public void setHair(String  hair){
        this.hair=hair;
    }

    public String getBrows(){
        return this.brows;
    }

    public void setBrows(String brows){
        this.brows=brows;
    }

    public String  getEyes(){
        return this.eyes;
    }

    public void setEyes(String  eyes){
        this.eyes=eyes;
    }

    public String getNose(){
        return this.nose;
    }

    public void setNose(String nose){
        this.nose=nose;
    }

    public String  getLips(){
        return this.lips;
    }

    public void setLips(String  lips){
        this.lips=lips;
    }

    public String getSkinColor(){
        return this.skinColor;
    }

    public void setSkinColor(String skinColor){
        this.skinColor=skinColor;
    }

    public String  getPirsing(){
        return this.pirsing;
    }

    public void setPirsing(String  pirsing){
        this.pirsing=pirsing;
    }

    public String getBody(){
        return this.body;
    }

    public void setBody(String body){
        this.body=body;
    }

    public String  getEars(){
        return this.ears;
    }

    public void setEars(String  ears){
        this.ears=ears;
    }

    public String getHead(){
        return this.head;
    }

    public void setHead(String head){
        this.head=head;
    }
}
