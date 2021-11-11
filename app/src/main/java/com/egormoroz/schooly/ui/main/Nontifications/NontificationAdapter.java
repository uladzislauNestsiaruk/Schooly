package com.egormoroz.schooly.ui.main.Nontifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NontificationAdapter extends RecyclerView.Adapter<NontificationAdapter.ViewHolder>  {

    ArrayList<Subscriber> listAdapter;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();

    public  NontificationAdapter(ArrayList<Subscriber> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitem_nontification, viewGroup, false);
       ViewHolder viewHolder=new ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscriber subscriber=listAdapter.get(position);
        Log.d("####", "suuck"+listAdapter);
        holder.otherUserNick.setText(subscriber.getSub()+" хочет добавить тебя в друзья");
        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.getFriendssList(nick, firebaseModel, new Callbacks.getSubscribersList() {
                            @Override
                            public void getSubscribersList(ArrayList<Subscriber> subscribers) {
                                subscribers.add(new Subscriber(subscriber.getSub()));
                                firebaseModel.getReference().child("users").child(nick).child("subscribers").child(subscriber.getSub()).removeValue();
                                firebaseModel.getReference().child("users")
                                        .child(nick).child("friends")
                                        .setValue(subscribers);
                                holder.addFriend.setBackgroundResource(R.drawable.corners14dpappcolor2dpstroke);
                                holder.addFriend.setText("Добавлен");
                            }
                        });
                    }
                });
            }
        });
        holder.rejectFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        holder.addFriend.setText("Отклонен");
                        firebaseModel.getReference().child("users").child(nick).child("subscribers").child(subscriber.getSub()).removeValue();
                        firebaseModel.getReference().child("users")
                                .child(nick).child("subscriders").child(subscriber.getSub()).removeValue();
                        holder.rejectFriend.setText("Отклонен");
                    }
                });
            }
        });
    }



    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView otherUserNick,addFriend,rejectFriend;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            addFriend=itemView.findViewById(R.id.addFriend);
            rejectFriend=itemView.findViewById(R.id.rejectFriend);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Subscriber getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
