package com.egormoroz.schooly.ui.news;

import android.os.Bundle;
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
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.people.PeopleAdapter;
import com.egormoroz.schooly.ui.profile.ComplainAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Bundle bundle;
    String newsId, nick, likeWord;
    UserInformation userInformation;
    long value;
    public CommentAdapter(List<Comment> commentAdapterList, String nick, Bundle bundle, String newsId, UserInformation userInformation) {
        this.commentAdapterList = commentAdapterList;
        this.nick=nick;
        this.bundle=bundle;
        this.userInformation = userInformation;
        this.newsId = newsId;
    }
    private List<Comment> commentAdapterList;
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
        holder.likesCount.setText(String.valueOf(comment.getLikes_count()));
        holder.nick.setText(comment.getNick());
        Picasso.get().load(comment.getImage()).into(holder.image);
        holder.comment.setText(comment.getText());
        holder.postTime.setText(comment.getPostTime());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = Long.parseLong(holder.likesCount.getText().toString());
                Query likeref = firebaseModel.getUsersReference().child(userInformation.getNick()).child("likedComm").child(newsId);
                likeref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            value -= 1;
                            holder.like.setImageResource(R.drawable.ic_heart40dp);
                            holder.likesCount.setText(String.valueOf(value));
                            firebaseModel.getReference("users").child(nick).child("likedComm").child(newsId).removeValue();
                        }
                        else {
                            value += 1;
                            holder.likesCount.setText(String.valueOf(value));
                            holder.like.setImageResource(R.drawable.ic_pressedheart40dp);
                            firebaseModel.getReference("users").child(nick).child("likedComm").child(newsId).setValue("liked");
                        }
                        firebaseModel.getUsersReference().child("looks").child(newsId).child("comments").child(comment.getCommentId()).child("likes_count").setValue(String.valueOf(value));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

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
