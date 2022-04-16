package com.egormoroz.schooly.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.profile.Wardrobe.CreateLookFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    RecyclerView recyclerView;
    ImageView back;
    TextView emptyList;

    String type;
    Fragment fragment;

    public SavedFragment(String type,Fragment fragment) {
        this.type = type;
        this.fragment=fragment;
    }

    public static SavedFragment newInstance(String type, Fragment fragment) {
        return new SavedFragment(type,fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.blackListRecycler);
        back=view.findViewById(R.id.back_tosettings);
        emptyList=view.findViewById(R.id.emptyBlackList);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(SettingsFragment.newInstance(type,fragment),getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(SettingsFragment.newInstance(type, fragment), getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getSavedLooks(nick, firebaseModel, new Callbacks.getSavedLook() {
                    @Override
                    public void getSavedLook(ArrayList<NewsItem> newsItems) {
                        if(newsItems.size()==0){
                            emptyList.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            LooksAdapter looksAdapter=new LooksAdapter(newsItems, SavedFragment.newInstance(type,fragment));
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            recyclerView.setAdapter(looksAdapter);
                            LooksAdapter.ItemClickListener itemClickListener=new LooksAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(NewsItem newsItem) {
                                    RecentMethods.setCurrentFragment(ViewingLookFragment.newInstance(SavedFragment.newInstance(type,fragment)), getActivity());
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
