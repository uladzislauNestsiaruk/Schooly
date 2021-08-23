package com.egormoroz.schooly.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
public class MainFragment extends Fragment{
    protected DialogsListAdapter<IDialog> dialogsAdapter;
    protected ImageLoader imageLoader;
    private MainViewModel mainViewModel;
    private FirebaseModel firebaseModel = new FirebaseModel();
    private UserInformation userData = new UserInformation();
    public static MainFragment newInstance() {
        return new MainFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(View.VISIBLE);
        firebaseModel.initAll();
        LoadUserData();
        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }
    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ImageView chat=view.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(ChatFragment.newInstance());
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                startActivity(intent);
            }
        });
        ImageView nontifications=view.findViewById(R.id.nontification);
        nontifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(GenderFragment.newInstance());
//
            }
        });
        TextView shop=view.findViewById(R.id.shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment((ShopFragment.newInstance()));
            }
        });
        TextView mining=view.findViewById(R.id.mining);
        mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(MiningFragment.newInstanse());
            }
        });
    }
    public void LoadUserData(){
        FirebaseUser user = firebaseModel.getUser();
        Query query = firebaseModel.getUsersReference().
                orderByChild("uid").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotParent) {
                for (DataSnapshot snapshot : snapshotParent.getChildren()) {
                    //userData.setAge(snapshot.child("age").getValue(Integer.class));
                    //userData.setAvatar(snapshot.child("avatar").getValue(Integer.class));
                    userData.setGender(snapshot.child("gender").getValue(String.class));
                    //////////////////userData.setMiners();
                    userData.setNick(snapshot.child("nick").getValue(String.class));
                    userData.setPassword(snapshot.child("password").getValue(String.class));
                    userData.setPhone(snapshot.child("phone").getValue(String.class));
                    userData.setUid(snapshot.child("uid").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}