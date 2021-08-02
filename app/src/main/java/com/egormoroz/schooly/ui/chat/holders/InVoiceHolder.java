package com.egormoroz.schooly.ui.chat.holders;

import android.view.View;
import android.widget.TextView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.stfalcon.chatkit.messages.MessageHolders;

import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class InVoiceHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private TextView tvDuration;
    private TextView tvTime;

    public InVoiceHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvDuration = itemView.findViewById(R.id.back_tomain);
        tvTime = itemView.findViewById(R.id.time);
    }

    public void onBind(Message message) {
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}