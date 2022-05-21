package com.egormoroz.schooly.ui.news;

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
import com.egormoroz.schooly.ui.people.PeopleAdapter;
import com.egormoroz.schooly.ui.profile.ComplainAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.a3dexample.jgltf_model.impl.v1.Image;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public CommentAdapter(List<Comment> commentAdapterList) {
        this.commentAdapterList = commentAdapterList;
    }
    private List<Comment> commentAdapterList;
    public static String newsId, likeWord;
    FirebaseModel firebaseModel = new FirebaseModel();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitem_comment, viewGroup, false);
        CommentAdapter.ViewHolder viewHolder=new CommentAdapter.ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment=commentAdapterList.get(position);
        holder.likesCount.setText(String.valueOf(558));
        holder.nick.setText(comment.getNick());
        Picasso.get().load(comment.getImage()).into(holder.image);
        holder.comment.setText(comment.getText());
        holder.postTime.setText("12:54");
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView nick,comment,answer,likesCount,postTime;
        ImageView image,like;
        ViewHolder(View itemView) {
            super(itemView);
            nick=itemView.findViewById(R.id.nick);
            comment=itemView.findViewById(R.id.comment);
            postTime=itemView.findViewById(R.id.postTime);
            answer=itemView.findViewById(R.id.answer);
            likesCount=itemView.findViewById(R.id.likesCount);
            image=itemView.findViewById(R.id.image);
            like=itemView.findViewById(R.id.like);

        }

    }

    @Override
    public int getItemCount() {
        return commentAdapterList.size();
    }
}
