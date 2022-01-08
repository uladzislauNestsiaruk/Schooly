package com.egormoroz.schooly.ui.profile;

import android.graphics.Color;
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
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubscribersAdapterOther extends RecyclerView.Adapter<SubscribersAdapterOther.ViewHolder>  {

    ArrayList<Subscriber> listAdapter;
    private SubscribersAdapterOther.ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    long subscriptionsCount,subscribersCount;
    int a;

    public  SubscribersAdapterOther(ArrayList<Subscriber> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public SubscribersAdapterOther.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitemfriendsother, viewGroup, false);
        SubscribersAdapterOther.ViewHolder viewHolder=new SubscribersAdapterOther.ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscriber subscriber=listAdapter.get(position);
        holder.otherUserNick.setText(subscriber.getSub());
        holder.otherUserNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) clickListener.onItemClick(view, position);
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick)
                        .child("subscription").child(subscriber.getSub());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.newSubscribe.setText("Отписаться");
                            holder.newSubscribe.setTextColor(Color.parseColor("#F3A2E5"));
                            holder.newSubscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.newSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick)
                                .child("subscription").child(subscriber.getSub());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    a=1;
                                    Log.d("#####", "c  "+a);

                                }else{
                                    a=2;

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.d("#####", "ff  "+a);
                        if(a!=0) {
                            if (a == 2) {
                                Log.d("#####", "ab  " + a);
                                firebaseModel.getReference().child("users").child(nick).child("subscription")
                                        .child(subscriber.getSub()).setValue(subscriber.getSub());
                                firebaseModel.getReference().child("users").child(subscriber.getSub()).child("subscribers")
                                        .child(nick).setValue(nick);
                                firebaseModel.getReference().child("users")
                                        .child(subscriber.getSub()).child("nontifications")
                                        .child(nick).setValue(new Nontification(nick,"не отправлено","не просмотрено"
                                ,String.valueOf(ServerValue.TIMESTAMP)));
                                holder.newSubscribe.setText("Отписаться");
                                holder.newSubscribe.setTextColor(Color.parseColor("#F3A2E5"));
                                holder.newSubscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                a=0;
                            }
                            if (a == 1) {
                                Log.d("#####", "one  " + a);
                                firebaseModel.getReference().child("users").child(nick).child("subscription")
                                        .child(subscriber.getSub()).removeValue();
                                firebaseModel.getReference().child("users").child(subscriber.getSub()).child("subscribers")
                                        .child(nick).removeValue();
                                holder.newSubscribe.setText("Подписаться");
                                holder.newSubscribe.setTextColor(Color.parseColor("#FFFEFE"));
                                holder.newSubscribe.setBackgroundResource(R.drawable.corners10dpappcolor);
                                a=0;

                            }
                        }
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
        final TextView otherUserNick,newSubscribe;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            newSubscribe=itemView.findViewById(R.id.subscribe);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Subscriber getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(SubscribersAdapterOther.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}