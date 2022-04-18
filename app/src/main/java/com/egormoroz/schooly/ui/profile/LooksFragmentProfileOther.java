package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class LooksFragmentProfileOther extends Fragment {

  RecyclerView looksRecyclerOther;
  TextView noLooksOther;
  FirebaseModel firebaseModel=new FirebaseModel();
  int looksListSize;
  String otherUserNick;

  public LooksFragmentProfileOther(String otherUserNick) {
    this.otherUserNick = otherUserNick;
  }

  public static LooksFragmentProfileOther newInstance(String otherUserNick) {
    return new LooksFragmentProfileOther(otherUserNick);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.viewpager_profileother, container, false);
    BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
    bnv.setVisibility(bnv.VISIBLE);
    firebaseModel.initAll();
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
    return root;
  }

  @Override
  public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
    super.onViewCreated(view, savedInstanceState);
    noLooksOther=view.findViewById(R.id.noLooks);
    looksRecyclerOther=view.findViewById(R.id.Recycler);
    checkLooksOther();
  }

  public void checkLooksOther(){
    RecentMethods.getLooksList(otherUserNick, firebaseModel, new Callbacks.getLooksList() {
      @Override
      public void getLooksList(ArrayList<NewsItem> look) {
        if (look.size()==0){
          noLooksOther.setVisibility(View.VISIBLE);
          noLooksOther.setText("У "+otherUserNick+" нет образов :(");
          looksRecyclerOther.setVisibility(View.GONE);
        }else{
          Collections.reverse(look);
          LooksAdapter looksAdapter=new LooksAdapter(look,LooksFragmentProfileOther.newInstance(otherUserNick));
          GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(), 3);
          looksRecyclerOther.setLayoutManager(gridLayoutManager);
          looksRecyclerOther.setAdapter(looksAdapter);
          LooksAdapter.ItemClickListener itemClickListener=new LooksAdapter.ItemClickListener() {
            @Override
            public void onItemClick(NewsItem newsItem) {
              RecentMethods.setCurrentFragment(ViewingLookFragment.newInstance(ProfileFragment.
                      newInstance("other", otherUserNick, LooksFragmentProfileOther.newInstance(otherUserNick))), getActivity());
            }
          };
          looksAdapter.setClickListener(itemClickListener);
        }
      }
    });
  }
}