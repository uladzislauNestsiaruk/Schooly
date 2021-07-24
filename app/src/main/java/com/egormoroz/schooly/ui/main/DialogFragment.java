package com.egormoroz.schooly.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.fixtures.MessagesFixtures;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class DialogFragment extends Fragment
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener {

    public static void open(FragmentActivity context) {
        context.startActivity(new Intent(context, DialogFragment.class));
    }

    private MessagesList messagesList;
    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter messagesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_default_messages, container, false);

        messagesList = root.findViewById(R.id.messagesList);
        initAdapter();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout input = view.findViewById(R.id.input);
        ImageView sendfile = view.findViewById(R.id.sendimage);
        ImageView sendvoice = view.findViewById(R.id.voicemessage);
        ImageView sendmessage = view.findViewById(R.id.sendmessage);
        EditText editmessage = view.findViewById(R.id.editmessage);
        if (editmessage.isInEditMode()) {
            sendmessage.setVisibility(View.VISIBLE);
            sendfile.setVisibility(View.GONE);
            sendvoice.setVisibility(View.GONE);
        } else {
            sendfile.setVisibility(View.VISIBLE);
            sendvoice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        messagesAdapter.addToStart(
                MessagesFixtures.getTextMessage(input.toString()), true);
        return true;
    }

    @Override
    public void onAddAttachments() {
        messagesAdapter.addToStart(
                MessagesFixtures.getImageMessage(), true);
    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>( senderId, imageLoader);
        messagesAdapter.enableSelectionMode((MessagesListAdapter.SelectionListener) this);
        messagesAdapter.setLoadMoreListener((MessagesListAdapter.OnLoadMoreListener) this);
//        super.messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
//                (view, message) -> AppUtils.showToast(MessageActivity.this,
//                        message.getUser().getName() + " avatar click",
//                        false));
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    public void onStartTyping() {
        Log.v("Typing listener", getString(R.string.start_typing_status));
    }

    @Override
    public void onStopTyping() {
        Log.v("Typing listener", getString(R.string.stop_typing_status));
    }
}
