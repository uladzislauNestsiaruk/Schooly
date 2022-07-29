package com.egormoroz.schooly.ui.chat;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;

import java.util.ArrayList;


public class ChatsFragment extends Fragment
{
    View ChatsView;
    RecyclerView chatsList;
    TextView noChats;
    FirebaseModel firebaseModel = new FirebaseModel();

    UserInformation userInformation;
    Bundle bundle;
    Fragment fragment;

    public ChatsFragment(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
    }

    public static ChatsFragment newInstance(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        return new ChatsFragment(userInformation,bundle,fragment);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ChatsView = inflater.inflate(R.layout.fragment_chats, container, false);
        firebaseModel.initAll();
        return ChatsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noChats=view.findViewById(R.id.noChats);
        chatsList = (RecyclerView) view.findViewById(R.id.chats_list);
        loadChats();
    }

    public  void loadChats(){
        if(userInformation.getChats()==null){
            RecentMethods.getDialogs(userInformation.getNick(), firebaseModel, new Callbacks.loadDialogs() {
                @Override
                public void LoadData(ArrayList<Chat> dialogs,ArrayList<Chat> talksArrayList) {
                    if(dialogs.size()==0){
                        noChats.setVisibility(View.VISIBLE);
                        chatsList.setVisibility(View.GONE);
                    }   else {
                        DialogAdapter dialogAdapter=new DialogAdapter(dialogs);
                        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
                        chatsList.setAdapter(dialogAdapter);
                        DialogAdapter.ItemClickListener itemClickListener=new DialogAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(Chat chat) {
                                RecentMethods.setCurrentFragment(MessageFragment.newInstance(userInformation, bundle, DialogsFragment.newInstance(userInformation, bundle,fragment),chat), getActivity());
                            }
                        };
                        dialogAdapter.setClickListener(itemClickListener);
                    }
                }
            });
        }else{
            if(userInformation.getChats().size()==0){
                noChats.setVisibility(View.VISIBLE);
                chatsList.setVisibility(View.GONE);
            }else {
                DialogAdapter dialogAdapter=new DialogAdapter(userInformation.getChats());
                chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
                chatsList.setAdapter(dialogAdapter);
                DialogAdapter.ItemClickListener itemClickListener=new DialogAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Chat chat) {
                        RecentMethods.setCurrentFragment(MessageFragment.newInstance(userInformation, bundle, DialogsFragment.newInstance(userInformation, bundle,fragment),chat), getActivity());
                    }
                };
                dialogAdapter.setClickListener(itemClickListener);
            }
        }
    }
}
