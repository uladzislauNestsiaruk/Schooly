package com.egormoroz.schooly.ui.main;

import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class UserInformation {
    private String Nick;
    private String chatsNontsType;
    private String groupChatsNontsType;
    private String profileNontsType;
    private String phone;
    private String uid;
    private String Avatar;
    private String password;
    private String gender = "Helicopter";
    private long age = 404;
    private ArrayList<Miner> activeMinerArrayList;
    private ArrayList<Miner> minerArrayList;
    private long level=1;
    private long money=100;
    private double todayMining=0;
    private ArrayList<Subscriber> subscription;
    private ArrayList<Subscriber> subscribers;
    private ArrayList<Clothes> myClothes;
    private ArrayList<Clothes> lookClothes;
    private ArrayList<Clothes> clothes;
    private ArrayList<Clothes> clothesBasket;
    private ArrayList<Nontification> notifications;
    private String queue;
    private String bio;
    private ArrayList<NewsItem> looks;
    private String accountType;
    private String version;
    private long miningPremium;
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

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public ArrayList<Miner> getMiners(){
        return activeMinerArrayList;
    }

    public void setMiners(ArrayList<Miner> activeMinerArrayList){
        this.activeMinerArrayList=activeMinerArrayList;
    }

    public ArrayList<Miner> getMyMiners(){
        return minerArrayList;
    }

    public void setMyMiners(ArrayList<Miner> minerArrayList){
        this.minerArrayList=minerArrayList;
    }

    public ArrayList<NewsItem> getLooks(){
        return looks;
    }

    public void setLooks(ArrayList<NewsItem> looks){
        this.looks=looks;
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

    public ArrayList<Subscriber> getSubscription(){
        return subscription;
    }

    public void setSubscription(ArrayList subscription){
        this.subscription = subscription;
    }

    public String getChatsNontsType(){
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

    public ArrayList<Subscriber> getSubscribers(){
        return subscribers;
    }

    public void setSubscribers(ArrayList<Subscriber> subscribers){
        this.subscribers = subscribers;
    }
    public String getQueue(){
        return this.queue;
    }
    public void setQueue(String queue){
        this.queue = queue;
    }

    public ArrayList<Clothes> getMyClothes(){
        return this.myClothes;
    }
    public void setMyClothes(ArrayList<Clothes> myClothes){
        this.myClothes = myClothes;
    }

    public ArrayList<Clothes> getLookClothes(){
        return this.lookClothes;
    }
    public void setLookClothes(ArrayList<Clothes> lookClothes){
        this.lookClothes = lookClothes;
    }

    public ArrayList<Clothes> getClothes(){
        return this.clothes;
    }
    public void setClothes(ArrayList<Clothes> clothes){
        this.clothes = clothes;
    }

    public ArrayList<Clothes> getClothesBasket(){
        return this.clothesBasket;
    }
    public void setClothesBasket(ArrayList<Clothes> clothesBasket){
        this.clothesBasket = clothesBasket;
    }

    public String getBio(){
        return this.bio;
    }

    public void setBio(String bio){
        this.bio = bio;
    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getMiningPremium() {
        return miningPremium;
    }

    public void setMiningPremium(long miningPremium) {
        this.miningPremium = miningPremium;
    }

    public ArrayList<Nontification> getNontifications(){
        return notifications;
    }

    public void setNotifications(ArrayList<Nontification> notifications) {
        this.notifications = notifications;
    }

    public UserInformation(String nick, String phone, String uid, String Avatar, String password,
                           String gender, long age, ArrayList<Miner> activeMinerArrayList,ArrayList<Miner> minerArrayList, long level, long money, double todayMining,
                           ArrayList<Subscriber> subscription, ArrayList<Subscriber> subscribers, String queue, String bio,
                           String accountType, String chatsNontsType,
                           String groupChatsNontsType, String profileNontsType, ArrayList<Clothes> myClothes, String version,
                           ArrayList<NewsItem> looks,long miningPremium,ArrayList<Clothes> lookClothes,ArrayList<Clothes> clothes,ArrayList<Nontification> notifications) {
        this.Nick = nick;
        this.uid = uid;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.Avatar = Avatar;
        this.activeMinerArrayList=activeMinerArrayList;
        this.minerArrayList=minerArrayList;
        this.level=level;
        this.money=money;
        this.todayMining=todayMining;
        this.subscription = subscription;
        this.subscribers = subscribers;
        this.queue = queue;
        this.bio=bio;
        this.accountType=accountType;
        this.chatsNontsType=chatsNontsType;
        this.groupChatsNontsType=groupChatsNontsType;
        this.profileNontsType=profileNontsType;
        this.myClothes=myClothes;
        this.version=version;
        this.looks=looks;
        this.miningPremium=miningPremium;
        this.lookClothes=lookClothes;
        this.clothes=clothes;
        this.notifications=notifications;
    }

}