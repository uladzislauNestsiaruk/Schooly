package com.egormoroz.schooly.ui.main.Nontifications;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class NontificationAdapter extends RecyclerView.Adapter<NontificationAdapter.ViewHolder>  {

    ArrayList<Nontification> listAdapter;
    private NontificationAdapter.ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    String nick;
    UserInformation userInformation;
    static Nontification sendNont;
    static String type;

    public  NontificationAdapter(ArrayList<Nontification> listAdapter, UserInformation userInformation) {
        this.listAdapter = listAdapter;
        this.userInformation=userInformation;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitem_nontification, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Nontification nontification=listAdapter.get(position);
        nick = userInformation.getNick();
        if (!nontification.getType().equals("запрос")){
            holder.addFriend.setVisibility(View.GONE);
        }
        if(nontification.getType().equals("не просмотрено")){
            firebaseModel.getUsersReference().child(nick).child("nontifications")
                    .child(nontification.getUid()).child("type")
                    .setValue("просмотрено");
        }
        if(nontification.getTypeView().equals("запрос")) {
            holder.otherUserNick.setVisibility(View.VISIBLE);
            holder.userImage.setVisibility(View.VISIBLE);
            holder.otherUserNick.setText(nontification.getNick()+" хочет подписаться на тебя");
            holder.addFriend.setVisibility(View.VISIBLE);
            holder.addFriend.setText("Добавить");
            holder.otherUserNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(listAdapter.get(holder.getAdapterPosition()),"sub");
                    sendNont=listAdapter.get(holder.getAdapterPosition());
                }
            });
            holder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseModel.getReference().child("users")
                            .child(nick).child("subscribers")
                            .child(nontification.getNick()).setValue(nontification.getNick());
                    firebaseModel.getReference().child("users")
                            .child(nontification.getNick()).child("subscription")
                            .child(nick).setValue(nick);
                    if (nontification.getTypeView().equals("запрос")){
                        firebaseModel.getUsersReference().child(nick).child("nontifications")
                                .child(nontification.getUid()).removeValue();
                        Toast.makeText(v.getContext(), "Подписчик добавлен", Toast.LENGTH_SHORT).show();
                    }
                    firebaseModel.getReference().child("users").child(nick).child("requests")
                            .child(nontification.getNick()).removeValue();
                    holder.addFriend.setText("Добавлен");
                }
            });
        }else if(nontification.getTypeView().equals("обычный")) {
            holder.otherUserNick.setVisibility(View.VISIBLE);
            holder.userImage.setVisibility(View.VISIBLE);
            holder.otherUserNick.setText(nontification.getNick()+" подписался на тебя");
            holder.otherUserNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(listAdapter.get(holder.getAdapterPosition()),"sub");
                    sendNont=listAdapter.get(holder.getAdapterPosition());
                }
            });
        }else if(nontification.getTypeView().equals("одежда")) {
            holder.otherUserNick.setVisibility(View.VISIBLE);
            holder.userImage.setVisibility(View.VISIBLE);
            holder.otherUserNick.setText(nontification.getNick()+" купил у тебя "+nontification.getClothesName());
            holder.otherUserNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(listAdapter.get(holder.getAdapterPosition()),"sub");
                    sendNont=listAdapter.get(holder.getAdapterPosition());
                }
            });
        }else if (nontification.getTypeView().equals("перевод")){
            holder.otherUserNick.setVisibility(View.VISIBLE);
            holder.userImage.setVisibility(View.VISIBLE);
            holder.otherUserNick.setText(nontification.getNick()+" перевел тебе "+nontification.getClothesName()+"S коинов");
            holder.otherUserNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(listAdapter.get(holder.getAdapterPosition()),"sub");
                    sendNont=listAdapter.get(holder.getAdapterPosition());
                }
            });
        }else if (nontification.getTypeView().equals("запросодежда")){
            holder.otherUserNick.setVisibility(View.VISIBLE);
            holder.userImage.setVisibility(View.VISIBLE);
            Picasso.get().load(nontification.getClothesImage()).into(holder.userImage);
            holder.otherUserNick.setText("Пришел ответ на заявку "+nontification.getClothesName());
            holder.addFriend.setVisibility(View.VISIBLE);
            holder.addFriend.setText("Перейти");
        }
        else if (nontification.getTypeView().equals("подарок")){
            holder.otherUserNick.setVisibility(View.VISIBLE);
            holder.userImage.setVisibility(View.VISIBLE);
            holder.otherUserNick.setText(nontification.getNick()+" подарил тебе "+nontification.getClothesName()+" !!!");
            holder.addFriend.setVisibility(View.GONE);
            holder.otherUserNick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onItemClick(listAdapter.get(holder.getAdapterPosition()),"sub");
                    sendNont=listAdapter.get(holder.getAdapterPosition());
                }
            });
            holder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        else if (nontification.getTypeView().equals("майнинг")){
            holder.imageCoins.setVisibility(View.VISIBLE);
            holder.fromWho.setVisibility(View.VISIBLE);
            holder.sum.setVisibility(View.VISIBLE);
            holder.type.setVisibility(View.VISIBLE);
            holder.remittanceTime.setVisibility(View.VISIBLE);
            holder.type.setText("Начисление");
            holder.fromWho.setText("Майнинг");
            holder.sum.setText("+"+String.valueOf(nontification.getClothesProfit()));
            holder.otherUserNick.setVisibility(View.GONE);
            holder.addFriend.setVisibility(View.GONE);
            holder.userImage.setVisibility(View.GONE);
        }
        else if (nontification.getTypeView().equals("одеждаприбыль")){
            holder.imageCoins.setVisibility(View.VISIBLE);
            holder.fromWho.setVisibility(View.VISIBLE);
            holder.sum.setVisibility(View.VISIBLE);
            holder.type.setVisibility(View.VISIBLE);
            holder.remittanceTime.setVisibility(View.VISIBLE);
            holder.type.setText("Начисление");
            holder.fromWho.setText("Одежда");
            holder.sum.setText("+"+String.valueOf(nontification.getClothesProfit()));
            holder.otherUserNick.setVisibility(View.GONE);
            holder.addFriend.setVisibility(View.GONE);
            holder.userImage.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView otherUserNick,addFriend,type,fromWho,sum,remittanceTime;
        ImageView imageCoins,userImage;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            addFriend=itemView.findViewById(R.id.addFriend);
            type=itemView.findViewById(R.id.type);
            fromWho=itemView.findViewById(R.id.fromWho);
            sum=itemView.findViewById(R.id.sum);
            imageCoins=itemView.findViewById(R.id.image);
            userImage=itemView.findViewById(R.id.userImage);
            remittanceTime=itemView.findViewById(R.id.remittanceTime);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(sendNont,"ok");
        }
    }

    Nontification getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(NontificationAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public static void singeClothesInfo(NontificationAdapter.ItemClickListener itemClickListener){
        itemClickListener.onItemClick(sendNont,type);
    }

    public interface ItemClickListener {
        void onItemClick( Nontification nontification,String type);
    }
}