package com.egormoroz.schooly.ui.main.Nontifications;

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
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NontificationAdapter extends RecyclerView.Adapter<NontificationAdapter.ViewHolder>  {

    ArrayList<Nontification> listAdapter;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();

    public  NontificationAdapter(ArrayList<Nontification> listAdapter) {
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
        Nontification nontification=listAdapter.get(position);
        holder.otherUserNick.setText(nontification.getNick());
        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
//                        firebaseModel.getReference().child("users").child(nick).child("nontifications")
//                                .child(nontification.getNick()).removeValue();
                        firebaseModel.getReference().child("users").child(nick).child("subscribers")
                                .child(nontification.getNick()).removeValue();
                        firebaseModel.getReference().child("users")
                                .child(nick).child("friends")
                                .child(nontification.getNick()).setValue(nontification.getNick());
                        Query query=firebaseModel.getUsersReference().child(nick)
                                .child("subscribersCount");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long subsCount=snapshot.getValue(Long.class);
                                firebaseModel.getUsersReference().child(nick)
                                        .child("subscribersCount").setValue(subsCount-1);
                                Log.d("####", "1   "+subsCount);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        holder.addFriend.setText("Добавлен");
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
                        firebaseModel.getReference().child("users").child(nick).child("nontifications")
                                .child(nontification.getNick()).removeValue();
                        holder.rejectFriend.setText("Отклонен");
                        holder.addFriend.setBackgroundResource(R.drawable.corners14grey);
                        holder.rejectFriend.setTextColor(Color.parseColor("#FFFFFF"));
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

    Nontification getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
