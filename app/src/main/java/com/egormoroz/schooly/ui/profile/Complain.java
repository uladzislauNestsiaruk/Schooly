package com.egormoroz.schooly.ui.profile;

import java.util.ArrayList;

public class Complain {
    private String appealedPerson;
    private String sneakPerson;
    private ArrayList<Reason> reasonArrayList;

    public Complain(String sneakPerson,String  appealedPerson,ArrayList<Reason> reasonArrayList){
        this.appealedPerson=appealedPerson;
        this.reasonArrayList=reasonArrayList;
        this.sneakPerson=sneakPerson;
    }
    public Complain(){
    }

    public String  getSneakPerson(){
        return this.sneakPerson;
    }

    public void setSneakPerson(String  sneakPerson){
        this.sneakPerson=sneakPerson;
    }

    public String  getAppealedPerson(){
        return this.appealedPerson;
    }

    public void setAppealedPerson(String  appealedPerson){
        this.appealedPerson=appealedPerson;
    }

    public ArrayList<Reason>  getReasonArrayList(){
        return this.reasonArrayList;
    }

    public void setReasonArrayList(ArrayList<Reason> reasonArrayList){
        this.reasonArrayList=reasonArrayList;
    }
}
