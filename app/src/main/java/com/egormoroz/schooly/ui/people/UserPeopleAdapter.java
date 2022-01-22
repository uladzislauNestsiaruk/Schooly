package com.egormoroz.schooly.ui.people;

public class UserPeopleAdapter {

    private String Nick;
    private String Avatar ;
    private String bio;
    public UserPeopleAdapter(){
    }

    public String getNick() {
        return Nick;
    }

    public void setNick(String nick) {
        Nick = nick;
    }

    public String getAvatar() {return Avatar;}

    public void setAvatar(String Avatar){
        this.Avatar = Avatar;
    }



    public String getBio(){
        return this.bio;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public UserPeopleAdapter(String nick,  String Avatar, String bio) {
        this.Nick = nick;
        this.Avatar = Avatar;
        this.bio=bio;
    }

}
