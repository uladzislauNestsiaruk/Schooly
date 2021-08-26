package com.egormoroz.schooly.ui.main;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInformation {
    private String Nick;
    private String phone;
    private String uid;
    private long Avatar = 6;
    private String password;
    private String gender = "Helicopter";
    private long age = 404;
    private String miners="Start mining stupid dog!";
    private long level=1;
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

    public void setUid(String uid) {this.uid = uid; }

    public long getAvatar() {return Avatar;}

    public void setAvatar(long Avatar){
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

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getMiners(){
        return miners;
    }

    public void setMiners(String miners){
        this.miners=miners;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level =level;
    }

    public UserInformation(String nick, String phone, String uid, long Avatar, String password,
                           String gender, long age,String miners,long level) {
        this.Nick = nick;
        this.uid = uid;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.Avatar = Avatar;
        this.miners=miners;
        this.level=level;
    }

}
