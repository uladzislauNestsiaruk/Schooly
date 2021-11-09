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
import com.egormoroz.schooly.ui.chat.MessageAdapter;
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

//
public class ChatsFragment extends Fragment
{
    private ArrayAdapter<String> arrayAdapter;
    private FirebaseModel firebaseModel = new FirebaseModel();
    public static String currentUserName="", receiverUserName;
    private ArrayList<String> list_of_groups=new ArrayList<String>();
    private ListView list_view;
    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chats, container, false);
        firebaseModel.initAll();
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, list_of_groups);
        RetrieveAndDisplayGroups();
        list_view = root.findViewById(R.id.list_view);
        list_view.setAdapter(arrayAdapter);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String name = adapterView.getItemAtPosition(position).toString();
                        Bundle nicknames = new Bundle();
                        Intent ChatIntent = new Intent(getContext(), ChatActivity.class);
                        nicknames.putString("othUser", name);
                        nicknames.putString("curUser", nick);
                        ChatIntent.putExtras(nicknames);
                        startActivity(ChatIntent);
                    }
                });
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    private void RetrieveAndDisplayGroups()
    {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
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
                        Log.d("Chat", currentUserName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
