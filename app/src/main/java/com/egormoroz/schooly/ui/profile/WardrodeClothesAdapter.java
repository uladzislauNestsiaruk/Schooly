package com.egormoroz.schooly.ui.profile;

import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
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
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

class WardrobeClothesAdapter extends RecyclerView.Adapter<WardrobeClothesAdapter.ViewHolder>  {
    private NewClothesAdapter.ItemClickListener clickListener;
    ArrayList<Clothes> clothesArrayListWardrobe;
    private FirebaseModel firebaseModel = new FirebaseModel();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference=storage.getReference();
    static Clothes clothes;
    NewClothesAdapter.ItemClickListener onClothesClick;
    static int pos;

    public WardrobeClothesAdapter(ArrayList<Clothes> clothesArrayListWardrobe) {
        this.clothesArrayListWardrobe= clothesArrayListWardrobe;
    }


    @NotNull
    @Override
    public WardrobeClothesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.new_clothes_rvitem, viewGroup, false);
        WardrobeClothesAdapter.ViewHolder viewHolder=new WardrobeClothesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseModel.initAll();
        pos=position;
        clothes=clothesArrayListWardrobe.get(position);
        holder.clothesTitle.setText(clothes.getClothesTitle());
        Log.d("#####", "ggxadwd  "+holder.clothesImage);
        holder.clothesPrise.setText(String.valueOf(clothes.getClothesPrice()));
        File file=new File(clothes.getClothesImage());
        storageReference.child("clothes").getFile(file);
        Intent intent=new Intent();
        holder.clothesImage.setVisibility(View.VISIBLE);
        Picasso.get().load(clothes.getClothesImage()).into(holder.clothesImage);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClothesClick.onItemClick(clothes,position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return clothesArrayListWardrobe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView clothesPrise,clothesTitle;
        ImageView clothesImage;
        ViewHolder(View itemView) {
            super(itemView);
            clothesPrise=itemView.findViewById(R.id.clothesPrice);
            clothesImage=itemView.findViewById(R.id.clothesImage);
            clothesTitle=itemView.findViewById(R.id.clothesTitle);
        }


    }


    public interface ItemClickListener {
        void onItemClick( Clothes clothes,int position);
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
