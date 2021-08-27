package com.egormoroz.schooly.ui.chat.holders;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.main.MessageActivity;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;


public class OutVidHold extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private MediaPlayer player = null;
    private ImageButton play;
    private TextView tvDuration;
    private TextView tvTime;
    private String TAG = "TAP";
    private VideoView video;
    private String Url;
    private boolean start = true;

    public OutVidHold(View itemView, Object payload) {
        super(itemView, payload);
        tvDuration = itemView.findViewById(R.id.durationVoice);
        tvTime = itemView.findViewById(R.id.time);
        play = itemView.findViewById(R.id.play);
        video = itemView.findViewById(R.id.video_viewer);
        Url = MessageActivity.fileName;
    }
    public void play(View view){
        video.start();
    }
    public void pause(View view){
        video.pause();
    }
    public void stop(View view){
        video.stopPlayback();
        video.resume();
    }
    @Override
    public void onBind(Message message) {
        super.onBind(message);
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(video);
            }
        });
        video.setVideoPath(Url);

        Log.d(TAG,"SEND");
    }
}
