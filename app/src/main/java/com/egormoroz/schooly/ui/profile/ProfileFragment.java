package com.egormoroz.schooly.ui.profile;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.Dialog;
import com.egormoroz.schooly.ui.main.ChatFragment;
import com.egormoroz.schooly.ui.main.MessageActivity;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class ProfileFragment extends Fragment {
    FirebaseModel firebaseModel = new FirebaseModel();
    String type;
    UserInformation info;
    TextView nickname;
    TextView message;
    public String nickother;
    public ProfileFragment(String type, UserInformation info){
        this.type = type;
        this.info = info;
    }
    public static ProfileFragment newInstance(String type, UserInformation info) {
        return new ProfileFragment(type, info);
    }


    public void onAttach(FragmentActivity activity) {
        super.onAttach(activity);
    }
    public void open() {
        Dialog dialog = new Dialog();
        String dialogId = dialog.getId();
        Intent i = new Intent(getActivity(), MessageActivity.class);
        i.putExtra("dialogId", dialogId);
        i.putExtra("nick", nickother);

        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = type.equals("user") ? inflater.inflate(R.layout.fragment_profile, container, false) :
                inflater.inflate(R.layout.fragment_otheruser, container, false);
//        AppBarLayout abl=getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.VISIBLE);
        nickname = type.equals("user") ? root.findViewById(R.id.usernick) :
                    root.findViewById(R.id.otherusernick);
        message = type.equals("user") ? null :
                root.findViewById(R.id.message);
        if (message !=null){
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open();
                }
            });
        }
        return root;
    }
    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseModel.initAll();

        switch (type) {
            case "user":
            ///////////////////////// set nickname /////////////////////
            RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(),
                    firebaseModel,
                    new Callbacks.GetUserNickByUid() {
                        @Override
                        public void PassUserNick(String nick) {
                            nickname.setText(nick);
                            nickother = nick;
                        }
                    });
            //////////////////////////////////////////////////
            ImageView imageView = view.findViewById(R.id.settingsIcon);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).setCurrentFragment(SettingsFragment.newInstance());
                }
            });
            ///////// I want GM on CF
            ImageView arrowtowardrobe = view.findViewById(R.id.arrowtowardrobe);
            arrowtowardrobe.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
                }
            });
            TextView editing = view.findViewById(R.id.redact);
            editing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).setCurrentFragment(EditingFragment.newInstance());
                }
            });
            TextView texttowardrobe = view.findViewById(R.id.shielf);
            texttowardrobe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).setCurrentFragment(WardrobeFragment.newInstance());
                }
            });

            break;
            case "other":
                nickname.setText(info.getNick());
                nickother = nickname.getText().toString();
                break;
        }
    }

}