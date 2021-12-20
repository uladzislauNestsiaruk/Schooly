package com.egormoroz.schooly.ui.profile;

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

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>  {

    ArrayList<Subscriber> listAdapter;
    private FriendsAdapter.ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    long friendsCount;

    public  FriendsAdapter(ArrayList<Subscriber> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitemfriends, viewGroup, false);
        FriendsAdapter.ViewHolder viewHolder=new FriendsAdapter.ViewHolder(v);
        firebaseModel.initAll();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick)
                        .child("friendsCount");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        friendsCount=snapshot.getValue(Long.class);
                        Log.d("####", "1   "+friendsCount);
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
        holder.deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Log.d("####", "daa"+subscriber.getSub());
                        firebaseModel.getReference().child("users").child(nick).child("friends")
                                .child(subscriber.getSub()).removeValue();
                        firebaseModel.getReference().child("users")
                                .child(nick).child("subscriders")
                                .child(subscriber.getSub()).setValue(subscriber.getSub());
                        if (friendsCount!=-1) {
                            friendsCount = friendsCount - 1;
                            firebaseModel.getUsersReference().child(nick)
                                    .child("subscribersCount").setValue(friendsCount - 1);
                        }
                        holder.deleteFriend.setText("Добавлен");
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
        final TextView otherUserNick,deleteFriend;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            deleteFriend=itemView.findViewById(R.id.deleteFriend);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Subscriber getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(FriendsAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
