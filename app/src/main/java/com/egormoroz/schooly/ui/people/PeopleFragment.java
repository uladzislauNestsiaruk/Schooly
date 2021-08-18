package com.egormoroz.schooly.ui.people;

import androidx.lifecycle.ViewModelProvider;

import android.app.MediaRouteButton;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {
    ArrayList<UserInformation> listAdapterPeople=new ArrayList<UserInformation>();
    RecyclerView peopleRecyclerView;


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
        setPeopleData();
    }

    public void setPeopleData(){
        listAdapterPeople.add(new UserInformation("nick", "fidjfif", "gk",
                6, "password", "Helicopter", 1000, "Miners"));
    }
}