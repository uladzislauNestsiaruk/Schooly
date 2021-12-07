package com.egormoroz.schooly.ui.main;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import com.egormoroz.schooly.ui.chat.Chat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class ChatsFragment extends Fragment
{
    private View ChatsView;
    private RecyclerView chatsList;

    private FirebaseModel firebaseModel = new FirebaseModel();

    public ChatsFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ChatsView = inflater.inflate(R.layout.fragment_chats, container, false);

        firebaseModel.initAll();

        chatsList = (RecyclerView) ChatsView.findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return ChatsView;
    }
    @Override
    public void onStart() {
        super.onStart();

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                FirebaseRecyclerOptions<Chat> options =
                        new FirebaseRecyclerOptions.Builder<Chat>()
                                .setQuery(firebaseModel.getUsersReference().child(nick).child("Chats").orderByChild("TimeMill"), Chat.class)
                                .build();

                Log.d("Neews", String.valueOf(options));
                FirebaseRecyclerAdapter<Chat, ChatsViewHolder> adapter =
                        new FirebaseRecyclerAdapter<Chat, ChatsViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Chat model) {
                                final String usersIDs = getRef(position).getKey();
                                final String[] retImage = {"default_image"};
                                firebaseModel.getUsersReference().child(nick).child("Chats").child(usersIDs).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            if (dataSnapshot.hasChild("LastMessage"))

                                            holder.lastMeassage.setText(model.getLastMessage());
                                            holder.userName.setText(usersIDs);
                                            holder.lastTime.setText(model.getLastTime());
                                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                    chatIntent.putExtra("curUser", nick);
                                                    chatIntent.putExtra("othUser", usersIDs);
                                                    chatIntent.putExtra("visit_image", retImage[0]);
                                                    startActivity(chatIntent);
                                                }
                                            });
                                        }
                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }


                            @NonNull
                            @Override
                            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dialog_item_layout, viewGroup, false);
                                return new ChatsViewHolder(view);
                            }
                        };

                chatsList.setAdapter(adapter);
                adapter.startListening();
            }
        });
    }




    public static class  ChatsViewHolder extends RecyclerView.ViewHolder
    {
        ImageView profileImage;
        TextView lastTime, userName, lastMeassage;

        public ChatsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            profileImage = itemView.findViewById(R.id.otheUserImage);
            lastTime = itemView.findViewById(R.id.time);
            userName = itemView.findViewById(R.id.otherUserNick);
            lastMeassage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
