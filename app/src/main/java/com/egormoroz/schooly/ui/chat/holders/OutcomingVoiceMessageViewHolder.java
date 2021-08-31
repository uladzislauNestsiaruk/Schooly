package com.egormoroz.schooly.ui.chat.holders;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.SystemClock;
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
    private final ImageView play;
    private final TextView tvDuration;
    private final TextView tvTime;
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
            String TAG = "TAP";
            Log.e(TAG, "prepare() failed");
        }
    }

    public void stopPlaying() {
        player.stop();
        player.release();
        player = null;
    }
    public int getDuration(String mUri) {
        int duration;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mUri);
        String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = Integer.parseInt(time);
        mmr.release();
        return duration;
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
        play.setOnClickListener(v -> {
            onPlay(start);
            start = !start;
            new Thread(){
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    while (true){
                        SystemClock.sleep(100);
                        long end = System.currentTimeMillis();
                        if (end - start > getDuration(MessageActivity.fileName)){
                            play.setImageResource(R.drawable.ic_play);
                            break;
                        }
                    }
                }
            }.start();
        });
        play.setImageResource(R.drawable.ic_play);
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }
}
