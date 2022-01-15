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
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubscribersAdapter extends RecyclerView.Adapter<SubscribersAdapter.ViewHolder>  {

    ArrayList<Subscriber> listAdapter;
    private SubscribersAdapter.ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    long subscriptionsCount,subscribersCount;
    boolean check=false;
    int a=0;

    public  SubscribersAdapter(ArrayList<Subscriber> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public SubscribersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitemsubscribers, viewGroup, false);
        SubscribersAdapter.ViewHolder viewHolder=new SubscribersAdapter.ViewHolder(v);
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
                Log.d("#####", "fhjh");
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
                            holder.addFriend.setText("Отписаться");
                            holder.addFriend.setTextColor(Color.parseColor("#F3A2E5"));
                            holder.addFriend.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Query queryRequest2=firebaseModel.getUsersReference().child(subscriber.getSub())
                        .child("requests").child(nick);
                queryRequest2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.addFriend.setText("Запрошено");
                            holder.addFriend.setTextColor(Color.parseColor("#F3A2E5"));
                            holder.addFriend.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.addFriend.setOnClickListener(new View.OnClickListener() {
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
                        Query queryRequest=firebaseModel.getUsersReference().child(subscriber.getSub())
                                .child("requests").child(nick);
                        queryRequest.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    a=3;
                                    Log.d("#####", "c  "+a);

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
                                Query query1=firebaseModel.getUsersReference().child(subscriber.getSub())
                                        .child("accountType");
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.getValue(String.class).equals("open")){
                                            firebaseModel.getReference().child("users").child(nick).child("subscription")
                                                    .child(subscriber.getSub()).setValue(subscriber.getSub());
                                            firebaseModel.getReference().child("users").child(subscriber.getSub()).child("subscribers")
                                                    .child(nick).setValue(nick);
                                            firebaseModel.getReference().child("users")
                                                    .child(subscriber.getSub()).child("nontifications")
                                                    .child(nick).setValue(new Nontification(nick,"не отправлено","обычный"
                                                    ,ServerValue.TIMESTAMP.toString()));
                                            holder.addFriend.setText("Отписаться");
                                            holder.addFriend.setTextColor(Color.parseColor("#F3A2E5"));
                                            holder.addFriend.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                            a=0;
                                        }else {
                                            firebaseModel.getReference().child("users").child(subscriber.getSub()).child("requests")
                                                    .child(nick).setValue(nick);
                                            firebaseModel.getReference().child("users")
                                                    .child(subscriber.getSub()).child("nontifications")
                                                    .child(nick).setValue(new Nontification(nick,"не отправлено","запрос"
                                                    ,ServerValue.TIMESTAMP.toString()));
                                            holder.addFriend.setText("Запрошено");
                                            holder.addFriend.setTextColor(Color.parseColor("#F3A2E5"));
                                            holder.addFriend.setBackgroundResource(R.drawable.corners10appcolor2dpstroke);
                                            a=0;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            if (a == 1) {
                                Log.d("#####", "one  " + a);
                                firebaseModel.getReference().child("users").child(nick).child("subscription")
                                        .child(subscriber.getSub()).removeValue();
                                firebaseModel.getReference().child("users").child(subscriber.getSub()).child("subscribers")
                                        .child(nick).removeValue();
                                holder.addFriend.setText("Подписаться");
                                holder.addFriend.setTextColor(Color.parseColor("#FFFEFE"));
                                holder.addFriend.setBackgroundResource(R.drawable.corners10dpappcolor);
                                a=0;

                            }
                            if (a == 3) {
                                firebaseModel.getReference().child("users").child(subscriber.getSub()).child("requests")
                                        .child(nick).removeValue();
                                holder.addFriend.setText("Подписаться");
                                holder.addFriend.setTextColor(Color.parseColor("#FFFEFE"));
                                holder.addFriend.setBackgroundResource(R.drawable.corners10dpappcolor);
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
        final TextView otherUserNick,addFriend;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            addFriend=itemView.findViewById(R.id.addFriend);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Subscriber getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(SubscribersAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}