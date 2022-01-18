package com.egormoroz.schooly.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WardrobeAdapter extends RecyclerView.Adapter<WardrobeAdapter.ViewHolder> {

    ArrayList<Clothes> listAdapter;
    private WardrobeAdapter.ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();

    public WardrobeAdapter(ArrayList<Clothes> listAdapter) {
        this.listAdapter = listAdapter;
    }


    @NotNull
    @Override
    public WardrobeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitemwardrobe, viewGroup, false);
        WardrobeAdapter.ViewHolder viewHolder=new WardrobeAdapter.ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WardrobeAdapter.ViewHolder holder, int position) {
        Clothes clothes=listAdapter.get(position);
        holder.clothesTitle.setText(clothes.getClothesTitle());
        Picasso.get().load(clothes.getClothesImage()).into(holder.clothesImage);
    }



    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView clothesImage;
        final TextView clothesTitle;
        ViewHolder(View itemView) {
            super(itemView);
            clothesImage=itemView.findViewById(R.id.clothesImage);
            clothesTitle=itemView.findViewById(R.id.clothesTitle);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Clothes getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(WardrobeAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

