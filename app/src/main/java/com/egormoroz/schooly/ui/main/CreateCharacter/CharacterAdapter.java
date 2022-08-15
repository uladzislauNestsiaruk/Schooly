package com.egormoroz.schooly.ui.main.CreateCharacter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.FacePart;
import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.Person;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.main.Shop.BasketAdapter;
import com.egormoroz.schooly.ui.main.Shop.Clothes;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder>{

    ArrayList<FacePart> bodyPartArrayList;
    ItemClickListener onItemClick;
    FilamentModel filamentModel=new FilamentModel();
    ArrayList<FacePart> activeFaceParts;
    String type;

    public CharacterAdapter(ArrayList<FacePart> bodyPartArrayList, ItemClickListener onItemClick,ArrayList<FacePart> activeFaceParts,String type) {
        this.bodyPartArrayList= bodyPartArrayList;
        this.onItemClick= onItemClick;
        this.activeFaceParts=activeFaceParts;
        this.type=type;
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
        FacePart facePart=bodyPartArrayList.get(position);
        filamentModel.postFrameCallback();
        for(int i=0;i<activeFaceParts.size();i++){
            FacePart facePartActive=activeFaceParts.get(i);
            if(!facePartActive.getPartType().equals(type)){
                if(i==0){
                    Log.d("#####", "aafefvecc     "+facePart.getPartType());
                    filamentModel.initFilamentForPersonCustom(holder.surfaceView, facePartActive.getBuffer());
                }else{
                    Log.d("#####", "bbdgtr   "+facePartActive.getPartType());
                    filamentModel.populateSceneFacePart(facePartActive.getBuffer());
                }
            }
            if(i==activeFaceParts.size()-1){
                Log.d("#####", "vvvvdvd");
                filamentModel.populateSceneFacePart(facePart.getBuffer());
            }
        }
        holder.surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(facePart);
            }
        });

    }

    public interface ItemClickListener {
        void onItemClick( FacePart facePart);
    }

    @Override
    public int getItemCount() {
        return bodyPartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SurfaceView surfaceView;
        ViewHolder(View itemView) {
            super(itemView);
            surfaceView=itemView.findViewById(R.id.surfaceView);
        }


    }
}
