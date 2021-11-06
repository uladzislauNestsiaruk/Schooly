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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActiveMinersAdapter extends RecyclerView.Adapter<ActiveMinersAdapter.ViewHolder> {

    ArrayList<Miner> listAdapterActivaMiner;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();

    public ActiveMinersAdapter(ArrayList<Miner> listAdapter) {
        this.listAdapterActivaMiner = listAdapter;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.horizontalrecyclerview_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        firebaseModel.initAll();
        Miner miner=listAdapterActivaMiner.get(holder.getAdapterPosition());
        holder.inHour.setText(String.valueOf(miner.getInHour()));
        holder.minerImage.setVisibility(View.VISIBLE);
        Picasso.get().load(miner.getMinerImage()).into(holder.minerImage);
        holder.putAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        firebaseModel.getUsersReference().child(nick)
                                .child("activeMiners").child(String.valueOf(miner.getMinerPrice())).removeValue();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAdapterActivaMiner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView inHour,putAway;
        ImageView minerImage;
        ViewHolder(View itemView) {
            super(itemView);
            inHour=itemView.findViewById(R.id.minerinhour);
            putAway=itemView.findViewById(R.id.putaway);
            minerImage=itemView.findViewById(R.id.activeminerimage);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Miner getItem(int id) {
        return listAdapterActivaMiner.get(id);
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