package com.egormoroz.schooly;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
public class FirebaseModel {
    public FirebaseAuth AuthenticationBase;
    public FirebaseDatabase Database;
    public DatabaseReference reference;
    public void initAll(){
        AuthenticationBase = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        reference = Database.getReference();
    }
    public void setReference(ArrayList<String> path){
        reference = Database.getReference();
        for(String cur : path)
            reference = reference.child(cur);
    }
    public DatabaseReference getReference(){
        return reference;
    }
    public DatabaseReference getUsersReference(){
        return getReference().child("users");
    }
    public FirebaseUser getUser(){
        return AuthenticationBase.getCurrentUser();
    }
}
