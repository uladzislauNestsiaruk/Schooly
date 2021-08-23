package com.egormoroz.schooly.ui.chat.holders;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.main.MessageActivity;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

public class OutImageHolder extends MessageHolders.IncomingTextMessageViewHolder<Message>
{
    private ImageView imageView;
    private String imageUrl;
    private TextView tvTime;
    private String TAG = "Image";

    public OutImageHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = itemView.findViewById(R.id.time);
        imageView = itemView.findViewById(R.id.image_viewer);
        imageUrl = MessageActivity.fileName;
    }
    @Override
    public void onBind(Message message) {
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        Picasso.get().load(imageUrl).into(imageView);
        Log.d(TAG,"SEND");
    }
}
