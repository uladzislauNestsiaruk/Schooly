package com.egormoroz.schooly.ui.main.Shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.ChatsFragment;
import com.egormoroz.schooly.ui.main.RegisrtationstartFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class PopularClothesAdapter extends RecyclerView.Adapter<PopularClothesAdapter.ViewHolder> {

    private ItemClickListener clickListener;
    ArrayList<Clothes> clothesArrayList;
    private FirebaseModel firebaseModel = new FirebaseModel();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference=storage.getReference();
    static Clothes clothes,trueClothes;
    ItemClickListener onClothesClick;
    String clothesPriceString,purchaseNumberString;
    static int pos;

    public PopularClothesAdapter(ArrayList<Clothes> clothesArrayList,ItemClickListener onClothesClick) {
        this.clothesArrayList= clothesArrayList;
        this.onClothesClick= onClothesClick;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitempopular, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        firebaseModel.initAll();
        clothes=clothesArrayList.get(position);
        holder.clothesTitle.setText(clothes.getClothesTitle());
        holder.clothesImage.setVisibility(View.VISIBLE);
        holder.creator.setText(clothes.getCreator());
        clothesPriceString=String.valueOf(clothes.getClothesPrice());
        if(clothes.getClothesPrice()<1000){
            holder.clothesPrice.setText(String.valueOf(clothes.getClothesPrice()));
        }else if(clothes.getClothesPrice()>1000 && clothes.getClothesPrice()<10000){
            holder.clothesPrice.setText(clothesPriceString.substring(0, 1)+"."+clothesPriceString.substring(1, 2)+"K");
        }
        else if(clothes.getClothesPrice()>10000 && clothes.getClothesPrice()<100000){
            holder.clothesPrice.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
        }
        else if(clothes.getClothesPrice()>10000 && clothes.getClothesPrice()<100000){
            holder.clothesPrice.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
        }else if(clothes.getClothesPrice()>100000 && clothes.getClothesPrice()<1000000){
            holder.clothesPrice.setText(clothesPriceString.substring(0, 3)+"K");
        }
        else if(clothes.getClothesPrice()>1000000 && clothes.getClothesPrice()<10000000){
            holder.clothesPrice.setText(clothesPriceString.substring(0, 1)+"KK");
        }
        else if(clothes.getClothesPrice()>10000000 && clothes.getClothesPrice()<100000000){
            holder.clothesPrice.setText(clothesPriceString.substring(0, 2)+"KK");
        }
        purchaseNumberString=String.valueOf(clothes.getPurchaseNumber());
        if(clothes.getPurchaseNumber()<1000){
            holder.purchaseNumber.setText(String.valueOf(clothes.getPurchaseNumber()));
        }else if(clothes.getPurchaseNumber()>1000 && clothes.getPurchaseNumber()<10000){
            holder.purchaseNumber.setText(purchaseNumberString.substring(0, 1)+"."+purchaseNumberString.substring(1, 2)+"K");
        }
        else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
            holder.purchaseNumber.setText(purchaseNumberString.substring(0, 2)+"."+purchaseNumberString.substring(2,3)+"K");
        }
        else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
            holder.purchaseNumber.setText(purchaseNumberString.substring(0, 2)+"."+purchaseNumberString.substring(2,3)+"K");
        }else if(clothes.getPurchaseNumber()>100000 && clothes.getPurchaseNumber()<1000000){
            holder.purchaseNumber.setText(purchaseNumberString.substring(0, 3)+"K");
        }
        else if(clothes.getPurchaseNumber()>1000000 && clothes.getPurchaseNumber()<10000000){
            holder.purchaseNumber.setText(purchaseNumberString.substring(0, 1)+"KK");
        }
        else if(clothes.getPurchaseNumber()>10000000 && clothes.getPurchaseNumber()<100000000){
            holder.purchaseNumber.setText(purchaseNumberString.substring(0, 2)+"KK");
        }
        if (clothes.getCurrencyType().equals("dollar")){
            holder.dollarImage.setVisibility(View.VISIBLE);
            holder.coinsImage.setVisibility(View.GONE);
        }
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Log.d("#####", "whats ");
                Query query=firebaseModel.getUsersReference().child(nick).child("clothes")
                        .child(clothes.getUid());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Log.d("#####", "whats what "+clothes.getUid());
                            holder.ifBuy.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        Picasso.get().load(clothes.getClothesImage()).into(holder.clothesImage);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClothesClick.onItemClick(clothesArrayList.get(holder.getAdapterPosition()));
                trueClothes=clothesArrayList.get(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return clothesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView clothesTitle,clothesPrice,creator,purchaseNumber;
        ImageView clothesImage,ifBuy,dollarImage,coinsImage;
        ViewHolder(View itemView) {
            super(itemView);
            clothesImage=itemView.findViewById(R.id.clothesImage);
            clothesTitle=itemView.findViewById(R.id.clothesTitle);
            clothesPrice=itemView.findViewById(R.id.clothesPrice);
            creator=itemView.findViewById(R.id.creator);
            ifBuy=itemView.findViewById(R.id.ifBuy);
            dollarImage=itemView.findViewById(R.id.dollarImage);
            coinsImage=itemView.findViewById(R.id.coinsImage);
            purchaseNumber=itemView.findViewById(R.id.purchaseNumber);
        }


    }

    public static void singeClothesInfo(ItemClickListener itemClickListener){
        itemClickListener.onItemClick(trueClothes);
    }


    public interface ItemClickListener {
        void onItemClick( Clothes clothes);
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