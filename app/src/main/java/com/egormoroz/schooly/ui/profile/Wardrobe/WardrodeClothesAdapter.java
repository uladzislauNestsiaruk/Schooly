package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
  static Clothes clothes,trueClothes;
  ItemClickListener onClothesClick;
  static int pos;

  public WardrobeClothesAdapter(ArrayList<Clothes> clothesArrayListWardrobe, ItemClickListener onClothesClick) {
    this.clothesArrayListWardrobe= clothesArrayListWardrobe;
    this.onClothesClick=onClothesClick;
  }




  @NotNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
            inflate(R.layout.rvitemwardrobe, viewGroup, false);
    ViewHolder viewHolder=new ViewHolder(v);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    firebaseModel.initAll();
    clothes=clothesArrayListWardrobe.get(position);
    holder.clothesTitle.setText(clothes.getClothesTitle());
    File file=new File(clothes.getClothesImage());
    storageReference.child("clothes").getFile(file);
    holder.clothesImage.setVisibility(View.VISIBLE);
    holder.creator.setText(clothes.getCreator());
    Picasso.get().load(clothes.getClothesImage()).into(holder.clothesImage);
    holder.fittingClothes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(v.getContext(), "Перевод выполнен", Toast.LENGTH_SHORT).show();
      }
    });
  }



  @Override
  public int getItemCount() {
    return clothesArrayListWardrobe.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView clothesTitle,creator;
    ImageView clothesImage,fittingClothes;
    ViewHolder(View itemView) {
      super(itemView);
      clothesImage=itemView.findViewById(R.id.clothesImage);
      clothesTitle=itemView.findViewById(R.id.clothesTitle);
      creator=itemView.findViewById(R.id.creator);
      fittingClothes=itemView.findViewById(R.id.fittingClothes);
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