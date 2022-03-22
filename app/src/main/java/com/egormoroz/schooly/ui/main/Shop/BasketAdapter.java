package com.egormoroz.schooly.ui.main.Shop;

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

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder>{

  private NewClothesAdapter.ItemClickListener clickListener;
  ArrayList<Clothes> clothesArrayList;
  private FirebaseModel firebaseModel = new FirebaseModel();
  FirebaseStorage storage = FirebaseStorage.getInstance();
  StorageReference storageReference=storage.getReference();
  static Clothes clothes,trueClothes;
  ItemClickListener onClothesClick;
  static int pos;

  public BasketAdapter(ArrayList<Clothes> clothesArrayList, ItemClickListener onClothesClick) {
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
    holder.clothesPrice.setText(String.valueOf(clothes.getClothesPrice()));
    File file=new File(clothes.getClothesImage());
    storageReference.child("clothes").getFile(file);
    holder.clothesImage.setVisibility(View.VISIBLE);
    holder.creator.setText(clothes.getCreator());
    Picasso.get().load(clothes.getClothesImage()).into(holder.clothesImage);
    Query query=firebaseModel.getReference().child("AppData").child("Clothes")
            .child("AllClothes").child(clothes.getUid());
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
          @Override
          public void PassUserNick(String nick) {
            Clothes clothesAll = new Clothes();
            clothesAll.setClothesImage(snapshot.child("clothesImage").getValue(String.class));
            clothesAll.setClothesPrice(snapshot.child("clothesPrice").getValue(Long.class));
            clothesAll.setPurchaseNumber(snapshot.child("purchaseNumber").getValue(Long.class));
            clothesAll.setClothesType(snapshot.child("clothesType").getValue(String.class));
            clothesAll.setClothesTitle(snapshot.child("clothesTitle").getValue(String.class));
            clothesAll.setCurrencyType(snapshot.child("currencyType").getValue(String.class));
            clothesAll.setCreator(snapshot.child("creator").getValue(String.class));
            clothesAll.setUid(snapshot.child("uid").getValue(String.class));
            if(clothesAll.getUid().equals(clothes.getUid())){
              holder.purchaseNumber.setText(String.valueOf(clothesAll.getPurchaseNumber()));
            }

          }
        });
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    if (clothes.getCurrencyType().equals("dollar")){
      holder.dollarImage.setVisibility(View.VISIBLE);
      holder.coinsImage.setVisibility(View.GONE);
    }
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