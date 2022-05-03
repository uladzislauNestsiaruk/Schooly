package com.egormoroz.schooly.ui.main.Mining;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    UserInformation userInformation;
    String nick;

    public  MyMinersAdapter(ArrayList<Miner> listAdapter,UserInformation userInformation) {
        this.listAdapter = listAdapter;
        this.userInformation=userInformation;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.myminers_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        firebaseModel.initAll();
        nick=userInformation.getNick();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Miner miner=listAdapter.get(position);
        holder.inHour.setText("+"+String.valueOf(miner.getInHour())+"S");
        holder.minerImage.setVisibility(View.VISIBLE);
        Picasso.get().load(miner.getMinerImage()).into(holder.minerImage);
        RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
            @Override
            public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                firebaseModel.getUsersReference().child(nick)
                        .child("activeMiners").child(String.valueOf(miner.getMinerPrice()))
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            DataSnapshot snapshot= task.getResult();
                            if(snapshot.exists()){
                                holder.use.setText("Используется");
                                holder.use.setBackgroundResource(R.drawable.corners14dpappcolor2dpstroke);
                                holder.use.setTextColor(Color.parseColor("#F3A2E5"));
                            }
                        }
                    }
                });
                if(activeMinersFromBase.size()==5){
                    holder.use.setBackgroundResource(R.drawable.corners14grey);
                }
            }
        });
        holder.use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                    @Override
                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                        if(holder.use.getText().toString().equals("Используется")){

                        }else{
                            if(activeMinersFromBase.size()==5){
                                holder.use.setBackgroundResource(R.drawable.corners14grey);
                                Toast.makeText(v.getContext(), "Пять майнеров уже активны",Toast.LENGTH_SHORT).show();
                            }else {int pos=holder.getAdapterPosition();
                                holder.use.setText("Используется");
                                holder.use.setBackgroundResource(R.drawable.corners14dpappcolor2dpstroke);
                                holder.use.setTextColor(Color.parseColor("#F3A2E5"));
                                firebaseModel.getUsersReference().child(nick)
                                        .child("activeMiners")
                                        .child(String.valueOf(miner.getMinerPrice())).setValue(listAdapter.get(pos));
                                RecentMethods.GetActiveMiner(nick, firebaseModel, new Callbacks.GetActiveMiners() {
                                    @Override
                                    public void GetActiveMiners(ArrayList<Miner> activeMinersFromBase) {
                                        userInformation.setMiners(activeMinersFromBase);
                                    }
                                });

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