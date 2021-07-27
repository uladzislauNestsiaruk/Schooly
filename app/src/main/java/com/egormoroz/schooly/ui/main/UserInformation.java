package com.egormoroz.schooly.ui.main;

public class UserInformation {
    String Nick;
    String phone;
    int uid;
    String Avatar;
    String password;
    String gender = "Helicopter";
    int age = 404;
    public UserInformation(){

    }
    public UserInformation(String nick, String phone, String password) {
        Nick = nick;
        this.uid = uid;
        this.phone = phone;
        this.password = password;
    }

    public UserInformation(String nick, String phone, String password, String gender, int age) {
        Nick = nick;
        this.uid = uid;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    public String getNick() {
        return Nick;
    }
    public String getAvatar() {return Avatar;}
    public int getUid() { return uid;}

    public void setUid() {this.uid = uid; }

    public void setNick(String nick) {
        Nick = nick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}
