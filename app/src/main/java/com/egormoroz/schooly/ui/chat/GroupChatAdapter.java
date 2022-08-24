package com.egormoroz.schooly.ui.chat;


import static com.google.android.material.transition.MaterialSharedAxis.X;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.holders.ImageViewerActivity;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.GroupChatViewHolder> {
    private List<Message> userMessagesList;
    private DatabaseReference usersRef;
    private String messageSenderNick = "", messageReceiverNick = "";
    private String fromUserID;
    private DatabaseReference reference;
    static Clothes trueClothes;
    private MessageFragment messageFragment;
    private GroupChatFragment groupChatFragment;
    static  NewsItem newsItemToViewing;
    private boolean isMpPlaying = false;
    private CurrentVoice currentVoice = new CurrentVoice();
    GroupChatAdapter.ItemClickListener itemClickListener;


    public GroupChatAdapter(List<Message> userMessagesList, String messageSenderId, String messageReceiverId, ItemClickListener itemClickListener, GroupChatFragment groupChatFragment) {
        this.userMessagesList = userMessagesList;
        this.messageSenderNick = messageSenderId;
        this.messageReceiverNick = messageReceiverId;
        this.itemClickListener = itemClickListener;
        this.groupChatFragment = groupChatFragment;
    }

    public GroupChatAdapter(List<Message> userMessagesList, String messageSenderId, String messageReceiverId, ItemClickListener itemClickListener) {
        this.userMessagesList = userMessagesList;
        this.messageSenderNick = messageSenderId;
        this.messageReceiverNick = messageReceiverId;
        this.itemClickListener = itemClickListener;

    }


//    public MessageAdapter(List<Message> userMessagesList, String messageSenderId) {
//        this.userMessagesList = userMessagesList;
//        this.messageSenderNick = messageSenderId;
//    }


    public static class GroupChatViewHolder extends RecyclerView.ViewHolder {
        public TextView senderMessageText, receiverMessageText, senderMessageTime, receiverTimePost,
                receiverMessageTime, senderTimeVoice, senderTimePost, clothesTitleAndCreator, senderTimeClothes,
                senderMessageTextClothes, clothesTitleAndCreatorOther, receiverTimeClothesOther,
                receiverMessageTextClothesOther, lookFrom, senderTimeLook, senderMessageTextLook,
                lookFromOther, receiverTimeLook, receiverMessageTextLook, receiveTimeVoice;
        //public ImageView receiverProfileImage;
        public RelativeLayout outMessage, inMessage, outVoice, inVoice, inClothes, outClothes, inLook, outLook,userIcon;
        public ImageView messageSenderPicture, senderPlay, senderPause, clothesImage, clothesImageOther;
        public ImageView messageReceiverPicture, receivePlay, receivePause, watchLookOther, watchLook;
        public SeekBar senderSeekBar, receiveSeekBar;

        private void handleShowView(View view) {
            if (getAdapterPosition() > X - 1) {
                view.setVisibility(View.GONE);
                return;
            }
            view.setVisibility(View.VISIBLE);
        }

        public GroupChatViewHolder(@NonNull View itemView) {
            super(itemView);
            handleShowView(itemView);
            outMessage = itemView.findViewById(R.id.textMessageOutcoming);
            inMessage = itemView.findViewById(R.id.textMessageIncoming);
            inVoice = itemView.findViewById(R.id.incomingVoice);
            receiverMessageTime = itemView.findViewById(R.id.receiver_time);
            receivePlay = itemView.findViewById(R.id.receive_imgPlay);
            receivePause = itemView.findViewById(R.id.receive_imgPause);
            senderMessageTime = itemView.findViewById(R.id.sender_time);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
            //receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
            senderPlay = itemView.findViewById(R.id.imgPlay);
            senderPause = itemView.findViewById(R.id.imgPause);
            senderSeekBar = itemView.findViewById(R.id.seekBar);
            senderTimeVoice = itemView.findViewById(R.id.txtTime);
            outVoice = itemView.findViewById(R.id.outcomingVoice);
            inClothes = itemView.findViewById(R.id.clothesFrom);
            clothesTitleAndCreator = itemView.findViewById(R.id.clothesTitleAndCreator);
            clothesImage = itemView.findViewById(R.id.clothesImage);
            senderTimeClothes = itemView.findViewById(R.id.sender_time_clothes);
            senderMessageTextClothes = itemView.findViewById(R.id.sender_message_text_clothes);
            outClothes = itemView.findViewById(R.id.clothesFromOther);
            clothesTitleAndCreatorOther = itemView.findViewById(R.id.clothesTitleAndCreatorOther);
            clothesImageOther = itemView.findViewById(R.id.clothesImageOther);
            receiverTimeClothesOther = itemView.findViewById(R.id.receiver_time_clothes);
            receiverMessageTextClothesOther = itemView.findViewById(R.id.receiver_message_text_clothes_other);
            inLook = itemView.findViewById(R.id.relativeLook);
            lookFrom = itemView.findViewById(R.id.lookFrom);
            watchLook = itemView.findViewById(R.id.watchLook);
            senderTimeLook = itemView.findViewById(R.id.sender_time_look);
            senderMessageTextLook = itemView.findViewById(R.id.sender_message_text_look);
            outLook = itemView.findViewById(R.id.relativeLookOther);
            lookFromOther = itemView.findViewById(R.id.lookFromOther);
            watchLookOther = itemView.findViewById(R.id.watchLookOther);
            receiverTimeLook = itemView.findViewById(R.id.receiver_time_look_other);
            receiverMessageTextLook = itemView.findViewById(R.id.receiver_message_text_look_other);
            receiverTimePost = itemView.findViewById(R.id.receive_time_voice);
            senderTimePost = itemView.findViewById(R.id.sender_time_voice);
            receiveSeekBar = itemView.findViewById(R.id.receive_seekBar);
            receiveTimeVoice = itemView.findViewById(R.id.receive_txtTime);
        }
    }


    @NonNull
    @Override
    public GroupChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.messages_types, viewGroup, false);
        reference = FirebaseDatabase.getInstance().getReference("users").child(messageSenderNick).child("Chats").child(messageReceiverNick).child("Messages");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        return new GroupChatViewHolder(view);
    }

    class CurrentVoice {
        MediaPlayer mediaPlayer;
        ImageView play, pause;

        public CurrentVoice(MediaPlayer mp, ImageView play, ImageView pause) {
            this.play = play;
            this.pause = pause;
            mediaPlayer = mp;
        }

        public CurrentVoice() {

        }

        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
        }

        public void setMediaPlayer(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        public ImageView getPlay() {
            return play;
        }

        public void setPlay(ImageView play) {
            this.play = play;
        }

        public ImageView getPause() {
            return pause;
        }

        public void setPause(ImageView pause) {
            this.pause = pause;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final GroupChatViewHolder groupChatViewHolder, @SuppressLint("RecyclerView") final int position) {
        Message messages = userMessagesList.get(position);
        fromUserID = messages.getFrom();
        MediaPlayer mediaplayer = MediaPlayer.create(groupChatViewHolder.itemView.getContext(), Uri.parse(messages.getMessage()));
        String fromMessageType = messages.getType();
        groupChatViewHolder.receiverTimePost.setText(messages.getTime());
        groupChatViewHolder.senderTimePost.setText(messages.getTime());
        int duration;
        final long[] timeBefore = new long[1];

        groupChatViewHolder.inMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:

                        timeBefore[0] = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_UP:
                        long timeAfter = System.currentTimeMillis();
                        if ((timeAfter - timeBefore[0]) > 500) {
                            groupChatFragment.showChatFunc(messages);
                        }
                        break;
                }
                return true;
            }
        });
        groupChatViewHolder.outMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        timeBefore[0] = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_UP:
                        long timeAfter = System.currentTimeMillis();
                        Log.d("###", "time: " + (timeAfter - timeBefore[0]));
                        if ((timeAfter - timeBefore[0]) > 500) {
                            try {
                                groupChatFragment.showChatFunc(messages);
                            } catch (Exception e) {

                            }

                        }
                        break;
                }
                return true;
            }
        });
        groupChatViewHolder.inVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        timeBefore[0] = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_UP:
                        long timeAfter = System.currentTimeMillis();
                        Log.d("###", "time: " + (timeAfter - timeBefore[0]));
                        if ((timeAfter - timeBefore[0]) > 500) {
                            try {
                                groupChatFragment.showChatFunc(messages);
                            } catch (Exception e) {

                            }

                        }
                        break;
                }
                return true;

            }
        });
        groupChatViewHolder.outVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        timeBefore[0] = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_UP:
                        long timeAfter = System.currentTimeMillis();
                        Log.d("###", "time: " + (timeAfter - timeBefore[0]));
                        if ((timeAfter - timeBefore[0]) > 500) {
                            try {
                                groupChatFragment.showChatFunc(messages);
                            } catch (Exception e) {

                            }

                        }
                        break;
                }
                return true;

            }
        });


        //VoicePlayer voicePlayer = new VoicePlayer(groupChatViewHolder.itemView.getContext());
        double voiceDuration = 1;
        groupChatViewHolder.senderSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaplayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        groupChatViewHolder.receiveSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaplayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
        try {
            voiceDuration = Double.valueOf(mediaplayer.getDuration()) / 1000;
            Date date = new Date();
            date.setTime(mediaplayer.getDuration());
            SimpleDateFormat formatDate = new SimpleDateFormat("mm:ss");
            groupChatViewHolder.receiveTimeVoice.setText(formatDate.format(date));
            groupChatViewHolder.senderTimeVoice.setText(formatDate.format(date));
            groupChatViewHolder.senderSeekBar.setMin(0);
            groupChatViewHolder.senderSeekBar.setMax((int) voiceDuration);
        } catch (Exception e) {
        }

        /*voicePlayer.setMediaPlayerListener(new VoicePlayer.MediaPlayerListener() {
            @Override
            public void isPlaying(int currentDuration) {

            }

            @Override
            public void onPause() {
                groupChatViewHolder.senderPlay.setVisibility(View.VISIBLE);
                groupChatViewHolder.senderPause.setVisibility(View.GONE);

            }

            @Override
            public void onStart() {

            }
        });*/

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    //Picasso.get().load(receiverImage).placeholder(R.drawable.ic_image).into(groupChatViewHolder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupChatViewHolder.outMessage.setVisibility(View.GONE);
        groupChatViewHolder.inMessage.setVisibility(View.GONE);
        groupChatViewHolder.outVoice.setVisibility(View.GONE);
        groupChatViewHolder.inVoice.setVisibility(View.GONE);
        groupChatViewHolder.messageSenderPicture.setVisibility(View.GONE);
        groupChatViewHolder.messageReceiverPicture.setVisibility(View.GONE);
        groupChatViewHolder.inLook.setVisibility(View.GONE);
        groupChatViewHolder.outLook.setVisibility(View.GONE);
        groupChatViewHolder.inClothes.setVisibility(View.GONE);
        groupChatViewHolder.outClothes.setVisibility(View.GONE);


        switch (fromMessageType) {
            case "text":
                if (fromUserID.equals(messageSenderNick)) {
                    groupChatViewHolder.outMessage.setVisibility(View.VISIBLE);
                    groupChatViewHolder.senderMessageText.setText(messages.getMessage());
                    groupChatViewHolder.senderMessageTime.setText(messages.getTime());
                } else {
                    //groupChatViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                    groupChatViewHolder.inMessage.setVisibility(View.VISIBLE);
                    groupChatViewHolder.receiverMessageText.setText(messages.getMessage());
                    groupChatViewHolder.receiverMessageTime.setText(messages.getTime());
                }
                break;
            case "image":
                if (fromUserID.equals(messageSenderNick)) {
                    groupChatViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                    Picasso.get().load(messages.getMessage()).into(groupChatViewHolder.messageSenderPicture);
                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(groupChatViewHolder.itemView.getContext(), ImageViewerActivity.class);
                            intent.putExtra("url", userMessagesList.get(position).getMessage());
                            groupChatViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });

                } else {
                    //groupChatViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                    groupChatViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
                    Picasso.get().load(messages.getMessage()).into(groupChatViewHolder.messageReceiverPicture);
                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(groupChatViewHolder.itemView.getContext(), ImageViewerActivity.class);
                            intent.putExtra("url", userMessagesList.get(position).getMessage());
                            groupChatViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });
                }
                break;
            case "pdf":

            case "docx":
                if (fromUserID.equals(messageSenderNick)) {
                    groupChatViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                            groupChatViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    //groupChatViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                    groupChatViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                            groupChatViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });
                }
                break;
            case "voice":
                if (fromUserID.equals(messageSenderNick)) {
                    groupChatViewHolder.outVoice.setVisibility(View.VISIBLE);
                    groupChatViewHolder.senderPause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            groupChatViewHolder.senderPlay.setVisibility(View.VISIBLE);
                            groupChatViewHolder.senderPause.setVisibility(View.GONE);
                            mediaplayer.pause();
                            isMpPlaying = false;
                        }
                    });
                    groupChatViewHolder.senderPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isMpPlaying) {
                                currentVoice.getMediaPlayer().pause();
                                currentVoice.getPause().setVisibility(View.GONE);
                                currentVoice.getPlay().setVisibility(View.VISIBLE);
                            }
                            groupChatViewHolder.senderPlay.setVisibility(View.GONE);
                            groupChatViewHolder.senderPause.setVisibility(View.VISIBLE);
                            currentVoice.setMediaPlayer(mediaplayer);
                            currentVoice.setPause(groupChatViewHolder.senderPause);
                            currentVoice.setPlay(groupChatViewHolder.senderPlay);
                            mediaplayer.start();
                            isMpPlaying = true;
                            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    groupChatViewHolder.senderPlay.setVisibility(View.VISIBLE);
                                    groupChatViewHolder.senderPause.setVisibility(View.GONE);
                                    isMpPlaying = false;
                                }
                            });

                        }
                    });
                } else {
                    groupChatViewHolder.inVoice.setVisibility(View.VISIBLE);
                    groupChatViewHolder.receivePause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            groupChatViewHolder.receivePlay.setVisibility(View.VISIBLE);
                            groupChatViewHolder.receivePause.setVisibility(View.GONE);
                            currentVoice.setMediaPlayer(mediaplayer);
                            currentVoice.setPause(groupChatViewHolder.receivePause);
                            currentVoice.setPlay(groupChatViewHolder.receivePlay);
                            mediaplayer.pause();
                            isMpPlaying = false;
                        }
                    });
                    groupChatViewHolder.receivePlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isMpPlaying) {
                                currentVoice.getMediaPlayer().pause();
                                currentVoice.getPause().setVisibility(View.GONE);
                                currentVoice.getPlay().setVisibility(View.VISIBLE);
                            }
                            groupChatViewHolder.receivePlay.setVisibility(View.GONE);
                            groupChatViewHolder.receivePause.setVisibility(View.VISIBLE);
                            mediaplayer.start();
                            isMpPlaying = true;
                            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    groupChatViewHolder.receivePlay.setVisibility(View.VISIBLE);
                                    groupChatViewHolder.receivePause.setVisibility(View.GONE);
                                    isMpPlaying = false;
                                }
                            });
                        }
                    });
                }


                break;
            case "clothes":
                if (fromUserID.equals(messageSenderNick)) {
                    groupChatViewHolder.inClothes.setVisibility(View.VISIBLE);
                    groupChatViewHolder.clothesTitleAndCreator.setText(messages.getClothes().getClothesTitle() + " " +
                            groupChatViewHolder.clothesTitleAndCreator.getContext().getResources().getString(R.string.by) + " "
                            + messages.getClothes().getCreator());
                    Picasso.get().load(messages.getClothes().getClothesImage()).into(groupChatViewHolder.clothesImage);
                    groupChatViewHolder.senderMessageTextClothes.setText(messages.getMessage());
                    groupChatViewHolder.senderTimeClothes.setText(messages.getTime());
                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClickListener.onItemClick(userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getClothes(), null);
                            trueClothes = userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getClothes();
                        }
                    });

                } else {
                    groupChatViewHolder.outClothes.setVisibility(View.VISIBLE);
                    groupChatViewHolder.clothesTitleAndCreatorOther.setText(messages.getClothes().getClothesTitle() + " " +
                            groupChatViewHolder.clothesTitleAndCreator.getContext().getResources().getString(R.string.by) + " "
                            + messages.getClothes().getCreator());

                    Picasso.get().load(messages.getClothes().getClothesImage()).into(groupChatViewHolder.clothesImageOther);
                    groupChatViewHolder.receiverMessageTextClothesOther.setText(messages.getMessage());
                    groupChatViewHolder.receiverTimeClothesOther.setText(messages.getTime());
                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClickListener.onItemClick(userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getClothes(), null);
                            trueClothes = userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getClothes();
                        }
                    });
                }
                break;
            case "look":
                if (fromUserID.equals(messageSenderNick)) {
                    Log.d("#####", "AA   " + messages.getNewsItem());
                    groupChatViewHolder.inLook.setVisibility(View.VISIBLE);
                    groupChatViewHolder.lookFrom.setText(
                            groupChatViewHolder.lookFrom.getContext().getResources().getString(R.string.lookby) + " "
                                    + messages.getNewsItem().getNick());

                    groupChatViewHolder.senderMessageTextLook.setText(messages.getMessage());
                    groupChatViewHolder.senderTimeLook.setText(messages.getTime());
                    Picasso.get().load(messages.getNewsItem().getImageUrl()).into(groupChatViewHolder.watchLook);
                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClickListener.onItemClick(null, userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getNewsItem());
                            newsItemToViewing = userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getNewsItem();
                        }
                    });
                } else {
                    groupChatViewHolder.outLook.setVisibility(View.VISIBLE);
                    groupChatViewHolder.lookFromOther.setText(
                            groupChatViewHolder.clothesTitleAndCreator.getContext().getResources().getString(R.string.lookby) + " "
                                    + messages.getNewsItem().getNick());


                    groupChatViewHolder.receiverMessageTextLook.setText(messages.getMessage());
                    groupChatViewHolder.receiverTimeLook.setText(messages.getTime());
                    Picasso.get().load(messages.getNewsItem().getImageUrl()).into(groupChatViewHolder.watchLook);

                    groupChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClickListener.onItemClick(null, userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getNewsItem());
                            newsItemToViewing = userMessagesList.get(groupChatViewHolder.getAdapterPosition()).getNewsItem();
                        }
                    });
                }
                break;

        }


    }


    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


    public void delete(int position) {
        userMessagesList.remove(position);
        notifyItemRemoved(position);
    }

    public static void lookInfo(ItemClickListener itemClickListener){
        itemClickListener.onItemClick(null, newsItemToViewing);
    }

    public interface ItemClickListener {
        void onItemClick(Clothes clothes, NewsItem newsItem);
    }

    public static void singeClothesInfo(NewClothesAdapter.ItemClickListener itemClickListener) {
        itemClickListener.onItemClick(trueClothes);
    }


}