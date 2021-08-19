package com.egormoroz.schooly.ui.chat.holders;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.main.MessageActivity;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.io.IOException;

public class OutcomingVoiceMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    private MediaPlayer   player = null;
    private ImageView play;
    private TextView tvDuration;
    private TextView tvTime;
    private String TAG = "TAP";
    private boolean start = true;

    public OutcomingVoiceMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvDuration = itemView.findViewById(R.id.durationVoice);
        tvTime = itemView.findViewById(R.id.time);
        play = itemView.findViewById(R.id.play);
    }

    public void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(MessageActivity.fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    public void stopPlaying() {
        player.stop();
        player.release();
        player = null;
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying();
            play.setImageResource(R.drawable.ic_stop);
        } else {
            stopPlaying();
            play.setImageResource(R.drawable.ic_play);
        }
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        tvDuration.setText(
                FormatUtils.getDurationString(
                        message.getVoice().getDuration()));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(start);
                start = !start;
                Log.d(TAG, "Tap");
            }
        });
        play.setImageResource(R.drawable.ic_play);
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}
