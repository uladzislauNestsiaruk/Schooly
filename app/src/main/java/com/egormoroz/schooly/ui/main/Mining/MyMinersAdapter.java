package com.egormoroz.schooly.ui.main.Mining;

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
public class MyMinersAdapter extends RecyclerView.Adapter<MyMinersAdapter.ViewHolder> {

    List<Miner> listAdapter;
    private ItemClickListener clickListener;

    public  MyMinersAdapter(ArrayList<Miner> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public MyMinersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.myminers_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Miner miner=listAdapter.get(position);
        holder.inHour.setText(String.valueOf(miner.getInHour()));
    }

    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView use;
        final ImageView recycleviewImageView;
        final TextView inHour;
        ViewHolder(View itemView) {
            super(itemView);
            use = itemView.findViewById(R.id.use);
            inHour=itemView.findViewById(R.id.inhour);
            recycleviewImageView=itemView.findViewById(R.id.viewrecycler);
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