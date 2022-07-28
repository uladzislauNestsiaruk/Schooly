package com.egormoroz.schooly.ui.chat;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.AccessoriesFragment;
import com.egormoroz.schooly.ui.main.Shop.ClothesFragment;
import com.egormoroz.schooly.ui.main.Shop.ExclusiveFragment;
import com.egormoroz.schooly.ui.main.Shop.HatsFragment;
import com.egormoroz.schooly.ui.main.Shop.PopularFragment;
import com.egormoroz.schooly.ui.main.Shop.ShoesFargment;
import com.egormoroz.schooly.ui.main.Shop.ShopFragment;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DialogsFragment extends Fragment {

    ImageView backToMainFromChat;
    String getEditText;
    EditText editText;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    FirebaseModel firebaseModel=new FirebaseModel();
    TextView noChats;
    private ViewPager2 viewPager;

    UserInformation userInformation;
    FragmentAdapter fragmentAdapter;
    Bundle bundle;
    int tabLayoutPosition;
    Fragment fragment;

    public DialogsFragment(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
    }

    public static DialogsFragment newInstance(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        return new DialogsFragment(userInformation,bundle,fragment);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bundle.putString("EDIT_DIALOGS_TAG",editText.getText().toString().trim());
        bundle.putInt("TAB_INT_DIALOGS", tabLayoutPosition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_default_dialogs, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        firebaseModel.initAll();

        editText=view.findViewById(R.id.editText);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                RecentMethods.setCurrentFragment(fragment,getActivity());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        if (bundle!=null){
            tabLayoutPosition=bundle.getInt("TAB_INT_DIALOGS");
            if(bundle.getString("EDIT_DIALOGS_TAG")!=null){
                String bundleEditText=bundle.getString("EDIT_DIALOGS_TAG").trim();
                if(bundleEditText.length()!=0){
                    editText.setText(bundleEditText);
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    //loadChat();
                }
            }
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getEditText=editText.getText().toString().toLowerCase();
                if (getEditText.length()>0){
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    recyclerView=view.findViewById(R.id.recyclerView);
                    noChats=view.findViewById(R.id.noChats);
                    noChats.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    loadChat(getEditText);

                }else if(getEditText.length()==0){
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    FragmentManager fm = getChildFragmentManager();
                    fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
                    viewPager.setAdapter(fragmentAdapter);
                    viewPager.setCurrentItem(tabLayoutPosition, false);

                    tabLayout = (TabLayout) view.findViewById(R.id.tabschat);
                    tabLayout.addTab(tabLayout.newTab().setText(R.string.personal));
                    tabLayout.addTab(tabLayout.newTab().setText(R.string.talks));
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            tabLayoutPosition=tab.getPosition();
                            viewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                    tabLayout.selectTab(tabLayout.getTabAt(tabLayoutPosition));
                    viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            tabLayoutPosition=position;
                            tabLayout.selectTab(tabLayout.getTabAt(position));
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        viewPager=view.findViewById(R.id.frcont);

        FragmentManager fm = getChildFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(tabLayoutPosition, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabschat);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.personal));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.talks));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayoutPosition=tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.selectTab(tabLayout.getTabAt(tabLayoutPosition));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayoutPosition=position;
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        backToMainFromChat=(ImageView) view.findViewById(R.id.backtomainfromchat);
        backToMainFromChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(MainFragment.newInstance(userInformation, bundle), getActivity());
            }
        });
    }

    public void loadChat(String editText){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("Chats");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment ( int position){
            switch (position){
                case 0:
                    return new ChatsFragment(userInformation,bundle);
                case 1:
                    return new GroupsFragment(userInformation,bundle);
            }
            return null;
        }


        @Override
        public int getItemCount() {
            return 2;
        }

    }
}

