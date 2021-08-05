package com.egormoroz.schooly.ui.chat;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.egormoroz.schooly.ui.main.MessageActivity;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public abstract class DemoMessagesActivity extends AppCompatActivity
        implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener {

    private static final int TOTAL_MESSAGES_COUNT = 100;
    private String TAG = "##########";
    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private String dialogId;
    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;
    private DatabaseReference ref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }


    protected void loadMessages() {
        //imitation of internet connection
        new Handler().postDelayed(() -> {
            ArrayList<Message> messages = MessageActivity.getMessages(lastLoadedDate, ref);
            lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
            messagesAdapter.addToEnd(messages, false);
        }, 1000);
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return message -> {
            String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                    .format(message.getCreatedAt());

            String text = message.getText();
            if (text == null) text = "[attachment]";

            return String.format(Locale.getDefault(), "%s: %s (%s)",
                    message.getUser().getName(), text, createdAt);
        };
    }
    public void getReference(DatabaseReference ref){
        this.ref = ref;
    }

    public abstract boolean hasContentFor(Message message, byte type);
}
