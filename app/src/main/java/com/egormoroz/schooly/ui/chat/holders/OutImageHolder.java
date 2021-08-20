package com.egormoroz.schooly.ui.chat.holders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

public class OutImageHolder extends MessageHolders.IncomingTextMessageViewHolder<Message>
{
    private ImageView imageView;
    private String imageUrl;
    private TextView tvDuration;
    private TextView tvTime;

    public OutImageHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = itemView.findViewById(R.id.time);
        imageView = itemView.findViewById(R.id.image_viewer);
    }
    @Override
    public void onBind(Message message) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imageView = findViewById(R.id.image_viewer);
        imageUrl = getIntent().getStringExtra("url");
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        Picasso.get().load(imageUrl).into(imageView);
    }
}
