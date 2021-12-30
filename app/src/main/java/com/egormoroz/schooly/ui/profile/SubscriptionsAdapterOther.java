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
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubscriptionsAdapterOther extends RecyclerView.Adapter<SubscriptionsAdapterOther.ViewHolder>  {

    ArrayList<Subscriber> listAdapter;
    private SubscriptionsAdapterOther.ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    long subscriptionsCount,subscribersCount;
    int a;

    public SubscriptionsAdapterOther(ArrayList<Subscriber> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public SubscriptionsAdapterOther.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitemfriendsother, viewGroup, false);
        SubscriptionsAdapterOther.ViewHolder viewHolder=new SubscriptionsAdapterOther.ViewHolder(v);
        firebaseModel.initAll();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick)
                        .child("subscriptionCount");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subscriptionsCount=snapshot.getValue(Long.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscriber subscriber=listAdapter.get(position);
        holder.otherUserNick.setText(subscriber.getSub());
        Query query=firebaseModel.getUsersReference().child(subscriber.getSub())
                .child("subscribersCount");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subscribersCount=snapshot.getValue(Long.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick)
                        .child("subscription").child(subscriber.getSub());
                Log.d("######", "sub  "+subscriber.getSub());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.subscribe.setText("Отписаться");
                            holder.subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                            holder.subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.subscribe.setOnClickListener(new View.OnClickListener() {
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
                                holder.subscribe.setText("Отписаться");
                                holder.subscribe.setTextColor(Color.parseColor("#F3A2E5"));
                                holder.subscribe.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                a=0;
                                if (subscribersCount!=-1){
                                    subscribersCount=subscribersCount+1;
                                    Log.d("#####","subsCount  "+subscribersCount);
                                    firebaseModel.getUsersReference().child(subscriber.getSub())
                                            .child("subscribersCount").setValue(subscribersCount);
                                }
                                if (subscriptionsCount!=-1) {
                                    subscriptionsCount = subscriptionsCount + 1;
                                    Log.d("#####", "subsCount  " + subscriptionsCount);
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("subscriptionCount").setValue(subscriptionsCount);
                                }
                            }
                            if (a == 1) {
                                Log.d("#####", "one  " + a);
                                firebaseModel.getReference().child("users").child(nick).child("subscription")
                                        .child(subscriber.getSub()).removeValue();
                                firebaseModel.getReference().child("users").child(subscriber.getSub()).child("subscribers")
                                        .child(nick).removeValue();
                                holder.subscribe.setText("Подписаться");
                                holder.subscribe.setTextColor(Color.parseColor("#FFFEFE"));
                                holder.subscribe.setBackgroundResource(R.drawable.corners10dpappcolor);
                                a=0;
                                if (subscribersCount!=-1){
                                    subscribersCount=subscribersCount-1;
                                    Log.d("#####","subsCount  "+subscribersCount);
                                    firebaseModel.getUsersReference().child(subscriber.getSub())
                                            .child("subscribersCount").setValue(subscribersCount);
                                }
                                if (subscriptionsCount!=-1){
                                    subscriptionsCount=subscriptionsCount-1;
                                    Log.d("#####","subsCount  "+subscriptionsCount);
                                    firebaseModel.getUsersReference().child(nick)
                                            .child("subscriptionCount").setValue(subscriptionsCount);
                                }

                            }
                        }
//                        if (subsCount!=-1){
//                        subsCount=subsCount-1;
//                        Log.d("#####","subsCount  "+subsCount);
//                        firebaseModel.getUsersReference().child(nick)
//                                .child("subscribersCount").setValue(subsCount);
//                        }
//                        holder.addFriend.setText("Добавлен");
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
        final TextView otherUserNick,subscribe;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            subscribe=itemView.findViewById(R.id.subscribe);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Subscriber getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(SubscriptionsAdapterOther.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}