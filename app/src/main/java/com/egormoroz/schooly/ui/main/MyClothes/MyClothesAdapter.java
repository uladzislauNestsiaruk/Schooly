package com.egormoroz.schooly.ui.main.MyClothes;

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
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MyClothesAdapter extends RecyclerView.Adapter<MyClothesAdapter.ViewHolder> {

    FirebaseModel firebaseModel=new FirebaseModel();
    ArrayList<Clothes> clothesArrayList;
    static Clothes clothes,trueClothes;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference=storage.getReference();
    MyClothesAdapter.ItemClickListener itemClickListener;
    String clothesPriceString,profitTodayString,purchaseTodayString;
    UserInformation userInformation;
    double perCent;

    public MyClothesAdapter(ArrayList<Clothes> clothesArrayList, MyClothesAdapter.ItemClickListener itemClickListener, UserInformation userInformation) {
        this.clothesArrayList= clothesArrayList;
        this.itemClickListener= itemClickListener;
        this.userInformation=userInformation;
    }

    public static void singeClothesInfo(MyClothesAdapter.ItemClickListener itemClickListener){
        itemClickListener.onItemClick(trueClothes);
    }

    public interface ItemClickListener {
        void onItemClick( Clothes clothes);
    }

    @NonNull
    @Override
    public MyClothesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitem_myclothesfrag, viewGroup, false);
        MyClothesAdapter.ViewHolder viewHolder=new MyClothesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyClothesAdapter.ViewHolder holder, int position) {
        firebaseModel.initAll();
        clothes=clothesArrayList.get(position);
        holder.clothesTitle.setText(clothes.getClothesTitle());
        File file=new File(clothes.getClothesImage());
        storageReference.child("clothes").getFile(file);
        holder.clothesImage.setVisibility(View.VISIBLE);
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
        profitTodayString=String.valueOf(clothes.getClothesPrice()*clothes.getPurchaseToday());
        if (clothes.getCurrencyType().equals("dollar")){
            holder.coinsImage.setVisibility(View.GONE);
            holder.profit.setText("+"+String.valueOf(clothes.getClothesPrice()*clothes.getPurchaseToday())+"$");
            if(clothes.getClothesPrice()*clothes.getPurchaseToday()<1000){
                holder.profit.setText("+"+profitTodayString+"$");
            }else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>1000
                    && clothes.getClothesPrice()*clothes.getPurchaseToday()<10000){
                holder.profit.setText("+"+profitTodayString.substring(0, 1)+"."+profitTodayString.substring(1, 2)+"K"+"$");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>10000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<100000){
                holder.profit.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K"+"$");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>10000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<100000){
                holder.profit.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K"+"$");
            }else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>100000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<1000000){
                holder.profit.setText("+"+profitTodayString.substring(0, 3)+"K"+"$");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>1000000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<10000000){
                holder.profit.setText("+"+profitTodayString.substring(0, 1)+"KK"+"$");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>10000000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<100000000){
                holder.profit.setText("+"+profitTodayString.substring(0, 2)+"KK"+"$");
            }
        }else {
            holder.profit.setText("+"+String.valueOf(clothes.getClothesPrice()*clothes.getPurchaseToday()));
            if(clothes.getClothesPrice()*clothes.getPurchaseToday()<1000){
                holder.profit.setText("+"+profitTodayString);
            }else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>1000
                    && clothes.getClothesPrice()*clothes.getPurchaseToday()<10000){
                holder.profit.setText("+"+profitTodayString.substring(0, 1)+"."+profitTodayString.substring(1, 2)+"K");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>10000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<100000){
                holder.profit.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>10000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<100000){
                holder.profit.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K");
            }else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>100000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<1000000){
                holder.profit.setText("+"+profitTodayString.substring(0, 3)+"K");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>1000000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<10000000){
                holder.profit.setText("+"+profitTodayString.substring(0, 1)+"KK");
            }
            else if(clothes.getClothesPrice()*clothes.getPurchaseToday()>10000000 &&
                    clothes.getClothesPrice()*clothes.getPurchaseToday()<100000000){
                holder.profit.setText("+"+profitTodayString.substring(0, 2)+"KK");
            }
        }
        purchaseTodayString=String.valueOf(clothes.getPurchaseToday());
        if(clothes.getPurchaseToday()<1000){
            holder.purchasesToday.setText(String.valueOf(clothes.getPurchaseToday()));
        }else if(clothes.getPurchaseToday()>1000 && clothes.getPurchaseToday()<10000){
            holder.purchasesToday.setText(purchaseTodayString.substring(0, 1)+"."+purchaseTodayString.substring(1, 2)+"K");
        }
        else if(clothes.getPurchaseToday()>10000 && clothes.getPurchaseToday()<100000){
            holder.purchasesToday.setText(purchaseTodayString.substring(0, 2)+"."+purchaseTodayString.substring(2,3)+"K");
        }
        else if(clothes.getPurchaseToday()>10000 && clothes.getPurchaseToday()<100000){
            holder.purchasesToday.setText(purchaseTodayString.substring(0, 2)+"."+purchaseTodayString.substring(2,3)+"K");
        }else if(clothes.getPurchaseToday()>100000 && clothes.getPurchaseToday()<1000000){
            holder.purchasesToday.setText(purchaseTodayString.substring(0, 3)+"K");
        }
        else if(clothes.getPurchaseToday()>1000000 && clothes.getPurchaseToday()<10000000){
            holder.purchasesToday.setText(purchaseTodayString.substring(0, 1)+"KK");
        }
        else if(clothes.getPurchaseToday()>10000000 && clothes.getPurchaseToday()<100000000){
            holder.purchasesToday.setText(purchaseTodayString.substring(0, 2)+"KK");
        }
        if (clothes.getPurchaseNumber()==0){
            perCent=0;
        }else {
            perCent=clothes.getPurchaseToday()*100/clothes.getPurchaseNumber();
        }
        holder.perSentPurchase.setText("("+String.valueOf(perCent)+"%)");
//        if (clothes.getCurrencyType().equals("dollar")){
//            holder.dollarImage.setVisibility(View.VISIBLE);
//            holder.coinsImage.setVisibility(View.GONE);
//        }
        Picasso.get().load(clothes.getClothesImage()).into(holder.clothesImage);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                itemClickListener.onItemClick(clothesArrayList.get(holder.getAdapterPosition()));
                trueClothes=clothesArrayList.get(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return clothesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView purchasesToday,clothesTitle,purchaseNumber,perSentPurchase,profit;
        ImageView clothesImage,coinsImage;
        ViewHolder(View itemView) {
            super(itemView);
            profit=itemView.findViewById(R.id.profit);
            perSentPurchase=itemView.findViewById(R.id.perSentPurchase);
            purchasesToday=itemView.findViewById(R.id.purchasesToday);
            clothesImage=itemView.findViewById(R.id.clothesImage);
            clothesTitle=itemView.findViewById(R.id.clothesTitle);
            coinsImage=itemView.findViewById(R.id.coinsImage);
            purchaseNumber=itemView.findViewById(R.id.purchaseNumber);
        }


    }
}
