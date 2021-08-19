package com.egormoroz.schooly.ui.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.UserInformation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    List<UserInformation> listAdapterPeople;
    private ItemClickListener clickListener;

    public  PeopleAdapter(ArrayList<UserInformation> listAdapter) {
        this.listAdapterPeople = listAdapter;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.peoplerecyclerview_item, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserInformation userInformation=listAdapterPeople.get(position);
        holder.usernickname.setText(userInformation.getNick());
        holder.userAvatar.setImageResource(getItemViewType(userInformation.getAvatar()));
    }

    @Override
    public int getItemCount() {
        return listAdapterPeople.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView usernickname,userLevel;
        ImageView userAvatar;
        ViewHolder(View itemView) {
            super(itemView);
            usernickname=itemView.findViewById(R.id.usernickname);
            userLevel=itemView.findViewById(R.id.userlevel);
            userAvatar=itemView.findViewById(R.id.useravatar);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    UserInformation getItem(int id) {
        return listAdapterPeople.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}