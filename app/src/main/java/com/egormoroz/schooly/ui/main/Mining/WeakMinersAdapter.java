package com.egormoroz.schooly.ui.main.Mining;

import android.graphics.Rect;
import android.util.TypedValue;
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
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WeakMinersAdapter extends RecyclerView.Adapter<WeakMinersAdapter.ViewHolder> {


    ArrayList<Miner> listAdapterMiner;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    ItemClickListener itemClickListener;

    public WeakMinersAdapter(ArrayList<Miner> listAdapter, ItemClickListener itemClickListener) {
        this.listAdapterMiner = listAdapter;
        this.itemClickListener= itemClickListener;
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
        holder.minerImage.setVisibility(View.VISIBLE);
        Picasso.get().load(miner.getMinerImage()).into(holder.minerImage);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("miners")
                                .child(String.valueOf(holder.getAdapterPosition())+"weak");
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
                                            if (money!=-1){
                                                itemClickListener.onItemClick(pos,miner,"weak",money);
                                            }
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView minerPrice,buy;
        ImageView minerImage;
        ViewHolder(View itemView) {
            super(itemView);
            minerPrice=itemView.findViewById(R.id.minerprice);
            buy=itemView.findViewById(R.id.buy);
            minerImage=itemView.findViewById(R.id.minerImage);
        }
    }

    Miner getItem(int id) {
        return listAdapterMiner.get(id);
    }


    public interface ItemClickListener {
        void onItemClick(int position,Miner miner,String type,long money);
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