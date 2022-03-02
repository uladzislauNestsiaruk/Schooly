package com.egormoroz.schooly.ui.main.Nontifications;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NontificationAdapter extends RecyclerView.Adapter<NontificationAdapter.ViewHolder>  {

    ArrayList<Nontification> listAdapter;
    private ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    String accountType;

    public  NontificationAdapter(ArrayList<Nontification> listAdapter) {
        this.listAdapter = listAdapter;
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
        if (!nontification.getType().equals("запрос")){
            holder.addFriend.setVisibility(View.GONE);
        }
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getNontificationsList(nick, firebaseModel, new Callbacks.getNontificationsList() {
                    @Override
                    public void getNontificationsList(ArrayList<Nontification> nontifications) {
                        for (int i=0;i<nontifications.size();i++){
                            Nontification nontification=nontifications.get(i);
                            if(nontification.getType().equals("не просмотрено")){
                                firebaseModel.getUsersReference().child(nick).child("nontifications")
                                        .child(nontification.getUid()).child("type")
                                        .setValue("просмотрено");
                            }
                        }
                    }
                });
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                if(nontification.getTypeView().equals("запрос")) {
                    holder.otherUserNick.setText(nontification.getNick()+" хочет подписаться на тебя");
                    holder.addFriend.setVisibility(View.VISIBLE);
                    holder.addFriend.setText("Добавить");
                    holder.addFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                                @Override
                                public void PassUserNick(String nick) {
                                    firebaseModel.getReference().child("users")
                                            .child(nick).child("subscribers")
                                            .child(nontification.getNick()).setValue(nontification.getNick());
                                    firebaseModel.getReference().child("users")
                                            .child(nontification.getNick()).child("subscription")
                                            .child(nick).setValue(nick);
                                    Query query=firebaseModel.getReference().child("users").child(nick).child("nontifications");
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot snap:snapshot.getChildren()){
                                                Nontification nontification=new Nontification();
                                                nontification.setNick(snap.child("nick").getValue(String.class));
                                                nontification.setTypeDispatch(snap.child("typeDispatch").getValue(String.class));
                                                nontification.setTypeView(snap.child("typeView").getValue(String.class));
                                                nontification.setTimestamp(snap.child("timestamp").getValue(String.class));
                                                nontification.setClothesName(snap.child("clothesName").getValue(String.class));
                                                nontification.setClothesImage(snap.child("clothesImage").getValue(String.class));
                                                nontification.setType(snap.child("type").getValue(String.class));
                                                nontification.setUid(snap.child("uid").getValue(String.class));
                                                if (nontification.getTypeView().equals("запрос")){
                                                    firebaseModel.getUsersReference().child(nick).child("nontifications")
                                                            .child(nontification.getUid()).removeValue();
                                                    Toast.makeText(v.getContext(), "Подписчик добавлен", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    firebaseModel.getReference().child("users").child(nick).child("requests")
                                            .child(nontification.getNick()).removeValue();
                                    holder.addFriend.setText("Добавлен");
                                }
                            });
                        }
                    });
                }else if(nontification.getTypeView().equals("обычный")) {
                    holder.otherUserNick.setText(nontification.getNick()+" подписался на тебя");
                }else if(nontification.getTypeView().equals("одежда")) {
                    holder.otherUserNick.setText(nontification.getNick()+" купил у тебя "+nontification.getClothesName());
                }else if (nontification.getTypeView().equals("перевод")){
                    holder.otherUserNick.setText(nontification.getNick()+" перевел тебе "+nontification.getUid()+"коина");
                }else if (nontification.getTypeView().equals("запросодежда")){
                    holder.otherUserNick.setText("Пришел ответ на публикацию "+nontification.getClothesName());
                    holder.addFriend.setVisibility(View.VISIBLE);
                    holder.addFriend.setText("Перейти");
                    holder.addFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView otherUserNick,addFriend;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            addFriend=itemView.findViewById(R.id.addFriend);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Nontification getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}