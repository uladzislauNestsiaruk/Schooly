package com.egormoroz.schooly.ui.people;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {
    ArrayList<UserInformation> listAdapterPeople=new ArrayList<UserInformation>();
    RecyclerView peopleRecyclerView;
    EditText searchUser;
    String userName,userNameToProfile;


    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }
    FirebaseModel firebaseModel=new FirebaseModel();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        peopleRecyclerView=view.findViewById(R.id.peoplerecycler);
        searchUser=view.findViewById(R.id.searchuser);
        firebaseModel.initAll();
        initUserEnter();
        setPeopleData();
    }

    public void setPeopleData(){
        listAdapterPeople.add(new UserInformation("nick", "fidjfif", "gk",
                6, "password", "Helicopter", 1000, "Miners",1,100,0, "", "", ""," ",0,0,0));
    }
    public void initUserEnter(){
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userName = String.valueOf(searchUser.getText()).trim();
                userName=userName.toLowerCase();
                Query query=firebaseModel.getReference("usersNicks");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<UserPeopleAdapter> userFromBase=new ArrayList<>();
                        Log.d("####","un "+userName);
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            UserPeopleAdapter upa=new UserPeopleAdapter();
                            upa.setNick(snap.child("nick").getValue(String.class));
                            upa.setBio(snap.child("bio").getValue(String.class));
                            upa.setAvatar(snap.child("avatar").getValue(Long.class));
                            String nickName=upa.getNick();
                            String nick=nickName;
                            int valueLetters=userName.length();
                            Log.d("####","un "+userName);
                            nick=nick.toLowerCase();
                            if(nick.length()<valueLetters){
                                if(nick.equals(userName))
                                    userFromBase.add(upa);
                                Log.d("####","nb "+nick);
                            }else{
                                nick=nick.substring(0, valueLetters);
                                if(nick.equals(userName))
                                    userFromBase.add(upa);
                                Log.d("####","nb "+nick);
                            }
                            Log.d("####", "cc " +userFromBase);

                        }
                        PeopleAdapter peopleAdapter = new PeopleAdapter(userFromBase);
                        peopleRecyclerView.setAdapter(peopleAdapter);
                        PeopleAdapter.ItemClickListener clickListener =
                                new PeopleAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        UserPeopleAdapter user = peopleAdapter.getItem(position);
                                        userNameToProfile=user.getNick();
                                        Log.d("###","n "+userNameToProfile);
                                        Query query1=firebaseModel.getReference().child("users").child(userNameToProfile);
                                        query1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                UserInformation userData=new UserInformation();
                                                userData.setAge(snapshot.child("age").getValue(Long.class));
                                                userData.setAvatar(snapshot.child("avatar").getValue(Long.class));
                                                userData.setGender(snapshot.child("gender").getValue(String.class));
                                                //////////////////userData.setMiners();
                                                userData.setNick(snapshot.child("nick").getValue(String.class));
                                                userData.setPassword(snapshot.child("password").getValue(String.class));
                                                userData.setPhone(snapshot.child("phone").getValue(String.class));
                                                userData.setUid(snapshot.child("uid").getValue(String.class));
                                                userData.setQueue(snapshot.child("queue").getValue(String.class));
//                    userData.setSubscribers(snapshot.child("subscribers").getValue(String.class));
//                                                userData.setFriends(snapshot.child("friends").getValue(String.class));
                                                RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userData),
                                                        getActivity());
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                };
                        peopleAdapter.setClickListener(clickListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                    }
                });
            }
        });
    }
}