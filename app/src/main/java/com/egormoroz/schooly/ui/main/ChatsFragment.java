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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatsFragment extends Fragment
{
    private View PrivateChatsView;
    private ArrayAdapter<String> arrayAdapter;
    private FirebaseModel firebaseModel = new FirebaseModel();
    private DatabaseReference ChatsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID="";
    private ArrayList<String> list_of_groups=new ArrayList<String>();
    private ListView list_view;
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
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list_of_groups);
//        DialogsAdapter dialogsAdapter = new DialogsAdapter(usersNicks);
//        Log.d("one", String.valueOf(usersNicks));
        RetrieveAndDisplayGroups();
        list_view = root.findViewById(R.id.list_view);
        list_view.setAdapter(arrayAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String name = adapterView.getItemAtPosition(position).toString();
                Intent groupChatIntent = new Intent(getContext(), ChatActivity.class);
                groupChatIntent.putExtra("name" , name);
                startActivity(groupChatIntent);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

//    public  void allDialogsFromBase(){
//
//        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
//            @java.lang.Override
//            public void PassUserNick(String nick) {
//                RecentMethods.GetUsersNicks(nick, firebaseModel, new Callbacks.GetUserNicks() {
//                    @java.lang.Override
//                    public void GetUsersNicks(ArrayList<String> userNicks) {
//                        userNicks.clear();
//                        userNicks.addAll(userNicks);
//                        DialogsAdapter dialogsAdapter=new DialogsAdapter(usersNicks);
//                        chatsList.notifyDataSetChanged();
//                        Log.d("c", "chats  "+userNicks);
//                    }
//                });
//            }
//        });
//    }

    private void RetrieveAndDisplayGroups()
    {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                i.putExtra("cname", nick);
                firebaseModel.getUsersReference().child(nick).child("Chats").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Set<String> set = new HashSet<>();
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext())
                        {
                            set.add(((DataSnapshot)iterator.next()).getKey());
                            Log.d("Chat", String.valueOf(set));
                        }

                        list_of_groups.clear();
                        list_of_groups.addAll(set);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
