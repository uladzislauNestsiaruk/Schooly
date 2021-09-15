package com.egormoroz.schooly.ui.main.Mining;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WeakMinersAdapter extends RecyclerView.Adapter<WeakMinersAdapter.ViewHolder> {

    ArrayList<Miner> listAdapterMiner;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();

    public WeakMinersAdapter(ArrayList<Miner> listAdapter) {
        this.listAdapterMiner = listAdapter;
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
        Miner miner=listAdapterMiner.get(position);
        holder.minerPrice.setText(String.valueOf(miner.getMinerPrice()));
        String minerPriceText= (String) holder.minerPrice.getText();
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
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
                                                RecentMethods.buyWeakMiner(String.valueOf(pos), firebaseModel, new Callbacks.buyMiner() {
                                                    @Override
                                                    public void buyMiner(Miner miner) {
                                                        firebaseModel.getReference("users").child(nick)
                                                                                .child("miners").child(String.valueOf(pos)+"weak").setValue(miner);
                                                        Log.d("#########", "miner  " + miner);
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
        return listAdapterMiner.size();
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
        return listAdapterMiner.get(id);
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
        }
    }
}