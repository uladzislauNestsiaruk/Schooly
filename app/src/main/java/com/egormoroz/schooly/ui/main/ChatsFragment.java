package com.egormoroz.schooly.ui.main;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.Contacts;
import com.egormoroz.schooly.ui.chat.TabsAccessorAdapter;
import com.egormoroz.schooly.ui.main.Mining.MyMinersFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsFragment extends Fragment
{
    private View PrivateChatsView;
    private RecyclerView chatsList;
    FirebaseModel firebaseModel = new FirebaseModel();
    private DatabaseReference ChatsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID="";
    ArrayList<String> usersNicks=new ArrayList<String>();

    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chats, container, false);
        firebaseModel.initAll();
        chatsList = root.findViewById(R.id.chats_list);
        allDialogsFromBase();

        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public  void allDialogsFromBase(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @java.lang.Override
            public void PassUserNick(String nick) {
                RecentMethods.GetUsersNicks(nick, firebaseModel, new Callbacks.GetUserNicks() {
                    @java.lang.Override
                    public void GetUsersNicks(ArrayList<String> userNicks) {
                        userNicks.addAll(userNicks);
                        DialogsAdapter dialogsAdapter=new DialogsAdapter(usersNicks);
                        chatsList.setAdapter(dialogsAdapter);
                        Log.d("c", "chats  "+userNicks);
                    }
                });
            }
        });
    }


}
