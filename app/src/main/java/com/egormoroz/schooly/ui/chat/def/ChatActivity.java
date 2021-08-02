package com.egormoroz.schooly.ui.chat.def;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.DemoDialogsActivity;
import com.egormoroz.schooly.ui.chat.Dialog;
import com.egormoroz.schooly.ui.main.ChatFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.egormoroz.schooly.ui.chat.fixtures.DialogsFixtures;

public class ChatActivity extends DemoDialogsActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth authDatabase;
    String userId;
    public static void open(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    private DialogsList dialogsList;

    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_dialogs);
        dialogsList = findViewById(R.id.dialogsList);
        initFirebase();
        initAdapter();
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        ChatActivity.open(this);
    }

    private void initAdapter() {
        super.dialogsAdapter = new DialogsListAdapter<>(super.imageLoader);
        super.dialogsAdapter.setItems(DialogsFixtures.getDialogs(ref, userId));

        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(super.dialogsAdapter);
    }

    //for example
    private void onNewMessage(String dialogId, Message message) {
        boolean isUpdated = dialogsAdapter.updateDialogWithMessage(dialogId, message);
        if (!isUpdated) {
//            adapter.updateItemById(DIALOG item);
        }
    }

    //for example
    private void onNewDialog(Dialog dialog) {
        dialogsAdapter.addItem(dialog);
    }
    private  void initFirebase(){
        database  = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        authDatabase = FirebaseAuth.getInstance();
        userId = authDatabase.getCurrentUser().getUid();
        ref = database.getReference("users").child(userId).child("chats");
    }
}
