package com.egormoroz.schooly.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.people.PeopleAdapter;
import com.egormoroz.schooly.ui.profile.ComplainAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                inflate(R.layout.rvitem_reason, viewGroup, false);
        CommentAdapter.ViewHolder viewHolder=new CommentAdapter.ViewHolder(v);
        firebaseModel.initAll();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        //
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return commentAdapterList.size();
    }
}
