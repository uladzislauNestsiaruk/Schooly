package com.egormoroz.schooly.ui.chat.holders;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.main.MessageActivity;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.io.IOException;

public class InImageHolder
        extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private ImageView image;
    private String imageUrl;
    private TextView tvTime;
    private String TAG = "Image";
    private boolean start = true;


    public InImageHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = itemView.findViewById(R.id.time);
        image = itemView.findViewById(R.id.image_viewer);
        imageUrl = MessageActivity.fileName;
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        image.setImageURI(MessageActivity.fileUri);
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        Picasso.get().load(imageUrl).into(image);
        Log.d(TAG,"SEND");
    }
}
