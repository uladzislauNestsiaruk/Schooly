package com.egormoroz.schooly.ui.main;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInformation {
    private String Nick;
    private String phone;
    private String uid;
    private int Avatar = 6;
    private String password;
    private String gender = "Helicopter";
    private int age = 404;
    private String miners="Start mining stupid dog!";
    public UserInformation(){

    }

    public String getNick() {
        return Nick;
    }

    public void setNick(String nick) {
        Nick = nick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() { return uid;}

    public void setUid() {this.uid = uid; }

    public int getAvatar() {return Avatar;}

    public void setAvatar(int Avatar){
        this.Avatar = Avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMiners(){
        return miners;
    }

    public void setMiners(String miners){
        this.miners=miners;
    }

    public UserInformation(String nick, String phone, String uid, int Avatar, String password,
                           String gender, int age,String miners) {
        this.Nick = nick;
        this.uid = uid;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.Avatar = Avatar;
        this.miners=miners;
    }
}
