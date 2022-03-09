package com.egormoroz.schooly.ui.profile;

import android.util.Log;
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
import com.egormoroz.schooly.ui.main.MyClothes.MyClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ViewHolder> {

    ArrayList<Clothes> listAdapter;
    private FirebaseModel firebaseModel = new FirebaseModel();
    String clothesPriceString;
    static Clothes clothes,trueClothes;
    ClothesAdapter.ItemClickListener itemClickListener;

    public ClothesAdapter(ArrayList<Clothes> listAdapter,ClothesAdapter.ItemClickListener itemClickListener) {
        this.listAdapter = listAdapter;
        this.itemClickListener=itemClickListener;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitem_clothesprofile, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clothes clothes=listAdapter.get(position);
        holder.clothesTitle.setText(clothes.getClothesTitle());
        holder.clothesPrise.setText(String.valueOf(clothes.getClothesPrice()));
        clothesPriceString=String.valueOf(clothes.getPurchaseNumber());
        if(clothes.getPurchaseNumber()<1000){
            holder.purchaseNumber.setText(String.valueOf(clothes.getPurchaseNumber()));
        }else if(clothes.getPurchaseNumber()>1000 && clothes.getPurchaseNumber()<10000){
            holder.purchaseNumber.setText(clothesPriceString.substring(0, 1)+"."+clothesPriceString.substring(1, 2)+"K");
        }
        else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
            holder.purchaseNumber.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
        }
        else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
            holder.purchaseNumber.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
        }else if(clothes.getPurchaseNumber()>100000 && clothes.getPurchaseNumber()<1000000){
            holder.purchaseNumber.setText(clothesPriceString.substring(0, 3)+"K");
        }
        else if(clothes.getPurchaseNumber()>1000000 && clothes.getPurchaseNumber()<10000000){
            holder.purchaseNumber.setText(clothesPriceString.substring(0, 1)+"KK");
        }
        else if(clothes.getPurchaseNumber()>10000000 && clothes.getPurchaseNumber()<100000000){
            holder.purchaseNumber.setText(clothesPriceString.substring(0, 2)+"KK");
        }
        if (clothes.getCurrencyType().equals("dollar")){
            holder.dollarImage.setVisibility(View.VISIBLE);
            holder.coinsImage.setVisibility(View.GONE);
        }
        Picasso.get().load(clothes.getClothesImage()).into(holder.clothesImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(listAdapter.get(holder.getAdapterPosition()));
                trueClothes=listAdapter.get(holder.getAdapterPosition());
            }
        });
    }



    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView clothesImage,dollarImage,coinsImage;
        TextView clothesPrise,clothesTitle,purchaseNumber;
        ViewHolder(View itemView) {
            super(itemView);
            clothesImage=itemView.findViewById(R.id.clothesImage);
            clothesPrise=itemView.findViewById(R.id.clothesPrice);
            clothesImage=itemView.findViewById(R.id.clothesImage);
            clothesTitle=itemView.findViewById(R.id.clothesTitle);
            coinsImage=itemView.findViewById(R.id.coinsImage);
            dollarImage=itemView.findViewById(R.id.dollarImage);
            purchaseNumber=itemView.findViewById(R.id.purchaseNumber);

        }
    }

    Clothes getItem(int id) {
        return listAdapter.get(id);
    }

    public static void singeClothesInfo(ClothesAdapter.ItemClickListener itemClickListener){
        itemClickListener.onItemClick(trueClothes);
    }

    public interface ItemClickListener {
        void onItemClick( Clothes clothes);
    }
}
