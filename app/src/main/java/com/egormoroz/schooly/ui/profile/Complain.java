package com.egormoroz.schooly.ui.profile;

import java.util.ArrayList;

public class Complain {
    private String appealedPerson;
    private ArrayList<Reason> reasonArrayList;

    public Complain(String  appealedPerson,ArrayList<Reason> reasonArrayList){
        this.appealedPerson=appealedPerson;
        this.reasonArrayList=reasonArrayList;
    }
    public Complain(){
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
