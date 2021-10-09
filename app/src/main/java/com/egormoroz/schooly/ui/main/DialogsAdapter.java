package com.egormoroz.schooly.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Mining.WeakMinersAdapter;
import com.google.firebase.database.core.view.View;

import java.util.ArrayList;

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ChatsViewHolder> {
    ArrayList<String> usersNicks;
    private WeakMinersAdapter.ItemClickListener clickListener;
    FirebaseModel firebaseModel = new FirebaseModel();

    public DialogsAdapter(ArrayList<String>  usersNicks) {
        this.usersNicks = usersNicks;
    }


    @NonNull
    @java.lang.Override
    public ChatsViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_display_layout, parent, false);
        ChatsViewHolder chatsViewHolder=new ChatsViewHolder(v);
        return chatsViewHolder;
    }

    @java.lang.Override
    public void onBindViewHolder(@NonNull DialogsAdapter.ChatsViewHolder holder, int position) {
        firebaseModel.initAll();
        holder.userName.setText("usersNicks");
        Log.d("###############", "array "+usersNicks);
    }

    @java.lang.Override
    public int getItemCount() {
        return 0;
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView userStatus, userName;
        ChatsViewHolder(RelativeLayout itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_viewer);
            //   userStatus = itemView.findViewById(R.id.user_status);
            userName = itemView.findViewById(R.id.otherusernick);
        }
    }
}
