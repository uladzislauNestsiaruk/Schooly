package com.egormoroz.schooly.ui.chat.holders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.DialogsFragment;
import com.egormoroz.schooly.ui.main.UserInformation;

public class CreateGroupFragment extends Fragment {

    UserInformation userInformation;
    Bundle bundle;
    Fragment fragment;

    public CreateGroupFragment(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
    }

    public static CreateGroupFragment newInstance(UserInformation userInformation,Bundle bundle,Fragment fragment) {
        return new CreateGroupFragment(userInformation,bundle,fragment);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_create_group, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
