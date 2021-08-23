package com.egormoroz.schooly.ui.people;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {
    ArrayList<UserInformation> listAdapterPeople=new ArrayList<UserInformation>();
    RecyclerView peopleRecyclerView;
    EditText searchUser;


    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PeopleAdapter peopleAdapter= new PeopleAdapter(listAdapterPeople);
        peopleRecyclerView=view.findViewById(R.id.peoplerecycler);
        peopleRecyclerView.setAdapter(peopleAdapter);
        searchUser=view.findViewById(R.id.searchuser);
        initUserEnter();
        setPeopleData();
    }

    public void setPeopleData(){
        listAdapterPeople.add(new UserInformation("nick", "fidjfif", "gk",
                6, "password", "Helicopter", 1000, "Miners",1));
    }
    public void initUserEnter(){
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userName = String.valueOf(searchUser.getText()).trim();
                ArrayList<UserInformation> users = RecentMethods.findUsers(userName);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}