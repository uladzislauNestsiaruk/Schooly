package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.util.Log;
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

public class LooksFragmentProfile extends Fragment {

    RecyclerView looksRecycler;
    TextView createNewLookText,createNewLook;
    FirebaseModel firebaseModel=new FirebaseModel();
    int looksListSize;

    String type;
    Fragment fragment;

    public LooksFragmentProfile(String type,Fragment fragment) {
        this.type = type;
        this.fragment=fragment;
    }

    public static LooksFragmentProfile newInstance(String type,Fragment fragment) {
        return new LooksFragmentProfile(type,fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.viewpager_profile, container, false);
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


        createNewLook=view.findViewById(R.id.CreateYourLook);
        createNewLookText=view.findViewById(R.id.textCreateYourLook);
        looksRecycler=view.findViewById(R.id.Recycler);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getLooksList(nick, firebaseModel, new Callbacks.getLooksList() {
                    @Override
                    public void getLooksList(ArrayList<NewsItem> look) {
                        looksListSize=look.size();
                        if (looksListSize==0){
                            createNewLookText.setVisibility(View.VISIBLE);
                            createNewLookText.setText("Создай свой первый образ!");
                            createNewLook.setVisibility(View.VISIBLE);
                            looksRecycler.setVisibility(View.GONE);
                            createNewLook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(type,fragment), getActivity());
                                }
                            });
                        }else {
                            Collections.reverse(look);
                            LooksAdapter looksAdapter=new LooksAdapter(look,LooksFragmentProfile.newInstance(type,fragment),looksRecycler);
                            GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(), 3);
                            looksAdapter.setHasStableIds(true);
                            looksRecycler.setHasFixedSize(true);
                            looksRecycler.setItemViewCacheSize(20);
                            looksRecycler.setLayoutManager(gridLayoutManager);
                            looksRecycler.setAdapter(looksAdapter);
                            LooksAdapter.ItemClickListener itemClickListener=new LooksAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(NewsItem newsItem) {
                                    RecentMethods.setCurrentFragment(ViewingLookFragment.newInstance(ProfileFragment.
                                            newInstance(type, nick, fragment)), getActivity());
                                }
                            };
                            looksAdapter.setClickListener(itemClickListener);
                        }
                    }
                });
            }
        });

    }
}