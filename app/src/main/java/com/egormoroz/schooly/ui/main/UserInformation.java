package com.egormoroz.schooly.ui.main;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserInformation {
    private String Nick;
    private String chatsNontsType;
    private String groupChatsNontsType;
    private String profileNontsType;
    private String phone;
    private String uid;
    private long Avatar = 6;
    private String password;
    private String gender = "Helicopter";
    private long age = 404;
    private String miners="Start mining stupid dog!";
    private long level=1;
    private long subscribersCount;
    private long looksCount;
    private long subscriptionCount;
    private long money=100;
    private double todayMining=0;
    private String subscription;
    private String subscribers;
    private String queue;
    private String bio;
    private String accountType;
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

    public long getmoney() {return money;}

    public void setmoney(long money){
        this.money = money;
    }

    public double getTodayMining() {return todayMining;}

    public void setTodayMining(double todayMining){
        this.todayMining = todayMining;
    }

    public String getSubscription(){
        return subscription;
    }

    public void setSubscription(String subscription){
        this.subscription = subscription;
    }

    public String getCheckNontsType(){
        return chatsNontsType;
    }

    public void setChatsNontsType(String chatsNontsType){
        this.chatsNontsType = chatsNontsType;
    }

    public String getProfileNontsType(){
        return profileNontsType;
    }

    public void setProfileNontsType(String profileNontsType){
        this.profileNontsType = profileNontsType;
    }

    public String getGroupChatsNontsType(){
        return groupChatsNontsType;
    }

    public void setGroupChatsNontsType(String groupChatsNontsType){
        this.groupChatsNontsType = groupChatsNontsType;
    }

    public String getSubscribers(){
        return subscribers;
    }

    public void setSubscribers(String subscribers){
        this.subscribers = subscribers;
    }
    public String getQueue(){
        return this.queue;
    }
    public void setQueue(String queue){
        this.queue = queue;
    }

    public String getBio(){
        return this.bio;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public long getSubscribersCount() {return subscribersCount;}

    public void setSubscribersCount(long subscribersCount) {
        this.subscribersCount =subscribersCount;
    }

    public long getLooksCount() {return looksCount;}

    public void setLooksCount(long looksCount) {
        this.looksCount =looksCount;
    }

    public long getSubscriptionCount() {return subscriptionCount;}

    public void setSubscriptionCount(long subscriptionCount) {
        this.subscriptionCount =subscriptionCount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public UserInformation(String nick, String phone, String uid, long Avatar, String password,
                           String gender, long age,String miners,long level,long money,double todayMining,
                           String subscription, String subscribers, String queue,String bio,long subscribersCount,
                           long looksCount,long subscriptionCount,String accountType,String chatsNontsType,
                           String groupChatsNontsType,String profileNontsType) {
        this.Nick = nick;
        this.uid = uid;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.Avatar = Avatar;
        this.miners=miners;
        this.level=level;
        this.money=money;
        this.todayMining=todayMining;
        this.subscription = subscription;
        this.subscribers = subscribers;
        this.queue = queue;
        this.bio=bio;
        this.subscribersCount=subscribersCount;
        this.looksCount=looksCount;
        this.subscriptionCount=subscriptionCount;
        this.accountType=accountType;
        this.chatsNontsType=chatsNontsType;
        this.groupChatsNontsType=groupChatsNontsType;
        this.profileNontsType=profileNontsType;
    }

}