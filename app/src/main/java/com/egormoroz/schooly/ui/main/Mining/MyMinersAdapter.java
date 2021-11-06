package com.egormoroz.schooly.ui.main.Mining;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyMinersAdapter extends RecyclerView.Adapter<MyMinersAdapter.ViewHolder> {

    List<Miner> listAdapter;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();

    public  MyMinersAdapter(ArrayList<Miner> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.myminers_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Miner miner=listAdapter.get(position);
        holder.inHour.setText(String.valueOf(miner.getInHour()));
        holder.minerImage.setVisibility(View.VISIBLE);
        Picasso.get().load(miner.getMinerImage()).into(holder.minerImage);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                    @Override
                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                        Query query=firebaseModel.getUsersReference().child(nick)
                                .child("activeMiners").child(String.valueOf(miner.getMinerPrice()));
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    holder.use.setText("Используется");
                                    holder.use.setBackgroundResource(R.drawable.corners14dpappcolor2dpstroke);
                                    holder.use.setTextColor(Color.parseColor("#F3A2E5"));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if(activeMinersFromBase.size()==5){
                            holder.use.setBackgroundResource(R.drawable.corners14grey);
                        }
                    }
                });
            }
        });
        holder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                            @Override
                            public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                                if(activeMinersFromBase.size()==5){
                                    holder.use.setBackgroundResource(R.drawable.corners14grey);
                                }else {int pos=holder.getAdapterPosition();
                                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel
                                            , new Callbacks.GetUserNickByUid() {
                                                @Override
                                                public void PassUserNick(String nick) {
                                                    firebaseModel.getUsersReference().child(nick)
                                                            .child("activeMiners")
                                                            .child(String.valueOf(miner.getMinerPrice())).setValue(listAdapter.get(pos));
                                                }
                                            });

                                }
                            }
                        });
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
        final TextView use;
        final ImageView minerImage;
        final TextView inHour;
        ViewHolder(View itemView) {
            super(itemView);
            use = itemView.findViewById(R.id.use);
            inHour=itemView.findViewById(R.id.inhour);
            minerImage=itemView.findViewById(R.id.viewrecycler);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Miner getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}