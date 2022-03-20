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
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.coins.TransferMoneyAdapter;

import java.util.ArrayList;

public class SendLookAdapter extends RecyclerView.Adapter<SendLookAdapter.ViewHolder> {
    ArrayList<Subscriber> listAdapter;
    private SendLookAdapter.ItemClickListener clickListener;
    private FirebaseModel firebaseModel = new FirebaseModel();
    long subscriptionsCount,subscribersCount;
    boolean check=false;
    int a=0;

    public  SendLookAdapter(ArrayList<Subscriber> listAdapter) {
        this.listAdapter = listAdapter;
    }

    @NonNull
    @Override
    public SendLookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rv_item_bottom_sheet_look, viewGroup, false);
        ViewHolder viewHolder=new ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SendLookAdapter.ViewHolder holder, int position) {
        Subscriber subscriber=listAdapter.get(position);
        holder.otherUserNick.setText(subscriber.getSub());
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView otherUserNick,send;
        ImageView userImage;
        ViewHolder(View itemView) {
            super(itemView);
            otherUserNick = itemView.findViewById(R.id.otherUserNick);
            send=itemView.findViewById(R.id.send);
            userImage=itemView.findViewById(R.id.userImage);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Subscriber getItem(int id) {
        return listAdapter.get(id);
    }

    void setClickListener(SendLookAdapter.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
