package com.egormoroz.schooly.ui.main;

import com.egormoroz.schooly.ui.main.Mining.Miner;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
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
    private String subscription;
    private String subscribers;
    private ArrayList<Clothes> myClothes;
    private ArrayList<Clothes> lookClothes;
    private ArrayList<Clothes> clothes;
    private ArrayList<Clothes> clothesBasket;
    private String queue;
    private String bio;
    private String looks;
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

    public String getLooks(){
        return looks;
    }

    public void setLooks(String looks){
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

    public String getSubscription(){
        return subscription;
    }

    public void setSubscription(String subscription){
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

    public UserInformation(String nick, String phone, String uid, String Avatar, String password,
                           String gender, long age, ArrayList<Miner> activeMinerArrayList,ArrayList<Miner> minerArrayList, long level, long money, double todayMining,
                           String subscription, String subscribers, String queue, String bio,
                           String accountType, String chatsNontsType,
                           String groupChatsNontsType, String profileNontsType, ArrayList<Clothes> myClothes, String version,
                           String looks,long miningPremium,ArrayList<Clothes> lookClothes,ArrayList<Clothes> clothes) {
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
    }

}