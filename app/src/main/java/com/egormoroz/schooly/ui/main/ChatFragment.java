package com.egormoroz.schooly.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Dialog;
import com.egormoroz.schooly.ui.chat.fixtures.DialogsFixtures;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ChatFragment extends Fragment implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog>, sendDialogs {
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth authDatabase;
    String userId;
    public static ChatFragment newInstance(){return new ChatFragment();}

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView baktomainfromchat = view.findViewById(R.id.backtomainfromchat);
        baktomainfromchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).setCurrentFragment(MainFragment.newInstance());
            }
        });
    }
    public void onDialogClick(Dialog dialog) {
        open(dialog);
    }
    public void open(Dialog dialog) {
        String dialogId = dialog.getId();
        Intent i = new Intent(getActivity(), MessageActivity.class);
        i.putExtra("dialogId", dialogId);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }
    DialogsList dialogsList;
    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);
    }
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                              @Nullable Bundle SavedInstanceState) {
        View root = inflater.inflate(R.layout.activity_default_dialogs, container, false);
        dialogsList = root.findViewById(R.id.dialogsList);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        super.onCreate(SavedInstanceState);
        initFirebase();
        initAdapter();
        return root;
    }
    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsAdapter.setOnDialogClickListener(getActivity().findViewById(R.id.dialogsList));
        dialogsAdapter.setOnDialogLongClickListener(getActivity().findViewById(R.id.dialogsList));
        dialogsList.setAdapter(dialogsAdapter);
        dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {
                open(dialog);
            }
        });
        dialogsAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<Dialog>() {
            @Override
            public void onDialogLongClick(Dialog dialog) {
                Toast.makeText(getActivity(), dialog.getDialogName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDialogLongClick(Dialog dialog) {
        //TODO:: CONTEXT MENU
    }
    private  void initFirebase(){
        database  = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        authDatabase = FirebaseAuth.getInstance();
        userId = authDatabase.getCurrentUser().getUid();
        ref = database.getReference("users").child(userId).child("chats");
    }

    @Override
    public void setDialogs(ArrayList<Dialog> dialogs) {
        Log.d("#####", dialogs.size() + " receive");
        dialogsAdapter.setItems(DialogsFixtures.getDialogs(dialogs));
    }
}