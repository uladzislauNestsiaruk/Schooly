package com.egormoroz.schooly.ui.main.Mining;

import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StrongMinersAdapter extends RecyclerView.Adapter<StrongMinersAdapter.ViewHolder>{

    ArrayList<Miner> listAdapterStrongMiner;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();

    public StrongMinersAdapter(ArrayList<Miner> listAdapter) {
        this.listAdapterStrongMiner = listAdapter;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.allminersrecyclerview_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        firebaseModel.initAll();
        Miner miner=listAdapterStrongMiner.get(position);
        holder.minerPrice.setText(String.valueOf(miner.getMinerPrice()));
        String minerPriceText= (String) holder.minerPrice.getText();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("miners")
                                .child(String.valueOf(holder.getAdapterPosition())+"strong");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    holder.buy.setText("Куплено");
                                    holder.buy.setBackgroundResource(R.drawable.corners14grey);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if (money<Long.valueOf(minerPriceText)){
                            holder.buy.setBackgroundResource(R.drawable.corners14grey);
                        }else {
                            holder.buy.setBackgroundResource(R.drawable.corners14appcolor);
                            holder.buy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int pos=holder.getAdapterPosition();
                                    RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                                        @Override
                                        public void GetMoneyFromBase(long money) {
                                            RecentMethods.buyStrongMiner(String.valueOf(pos), firebaseModel, new Callbacks.buyMiner() {
                                                @Override
                                                public void buyMiner(Miner miner) {
                                                    firebaseModel.getReference("users").child(nick)
                                                            .child("miners").child(String.valueOf(pos)+"strong").setValue(miner);
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAdapterStrongMiner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView minerPrice,buy;
        ViewHolder(View itemView) {
            super(itemView);
            minerPrice=itemView.findViewById(R.id.minerprice);
            buy=itemView.findViewById(R.id.buy);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Miner getItem(int id) {
        return listAdapterStrongMiner.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int margin = 16;
            int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, view.getResources().getDisplayMetrics());
            if(parent.getChildAdapterPosition(view) == 0){
                outRect.left = space;
                outRect.bottom = 0;
            }
            if(parent.getChildAdapterPosition(view) == 4){
                outRect.right = space;
                outRect.bottom = 0;
            }
        }
    }
}