package com.egormoroz.schooly.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public class MiningAdapter extends RecyclerView.Adapter<MiningAdapter.ViewHolder> {

    List<Miner> listAdapterMiner;
    private ItemClickListener clickListener;

    public  MiningAdapter(ArrayList<Miner> listAdapter) {
        this.listAdapterMiner = listAdapter;
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
        Miner miner=listAdapterMiner.get(position);
        holder.minerinhour.setText(String.valueOf(miner.getInHour()));
    }

    @Override
    public int getItemCount() {
        return listAdapterMiner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView minerinhour;
        ViewHolder(View itemView) {
            super(itemView);
            minerinhour=itemView.findViewById(R.id.minerinhour);
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
}