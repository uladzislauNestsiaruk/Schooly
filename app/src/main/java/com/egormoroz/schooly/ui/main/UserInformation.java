package com.egormoroz.schooly.ui.main;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInformation {
    private String Nick;
    private String phone;
    private String uid;
    private String Avatar = "Seg";
    private String password;
    private String gender = "Helicopter";
    private int age = 404;
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

    public String getAvatar() {return Avatar;}

    public void setAvatar(String Avatar){
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

    public UserInformation(String nick, String phone, String uid, String Avatar, String password,
                           String gender, int age) {
        this.Nick = nick;
        this.uid = uid;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.Avatar = Avatar;
    }
}
