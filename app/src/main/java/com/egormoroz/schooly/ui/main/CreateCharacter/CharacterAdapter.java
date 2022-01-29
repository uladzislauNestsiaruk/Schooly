package com.egormoroz.schooly.ui.main.CreateCharacter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.Shop.BasketAdapter;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.google.ar.sceneform.SceneView;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder>{

    ArrayList<String> bodyPartArrayList;
    ItemClickListener onItemClick;

    public CharacterAdapter(ArrayList<String> bodyPartArrayList, ItemClickListener onItemClick) {
        this.bodyPartArrayList= bodyPartArrayList;
        this.onItemClick= onItemClick;
    }


    @NonNull
    @Override
    public CharacterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.rvitemcharacter, viewGroup, false);
        CharacterAdapter.ViewHolder viewHolder=new CharacterAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.ViewHolder holder, int position) {

    }

    public interface ItemClickListener {
        void onItemClick( String modelName);
    }

    @Override
    public int getItemCount() {
        return bodyPartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SceneView sceneView;
        ViewHolder(View itemView) {
            super(itemView);
            sceneView=itemView.findViewById(R.id.sceneView);

        }


    }
}
