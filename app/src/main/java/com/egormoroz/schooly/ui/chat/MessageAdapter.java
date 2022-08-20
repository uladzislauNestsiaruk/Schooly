package com.egormoroz.schooly.ui.chat;


import static com.google.android.material.transition.MaterialSharedAxis.X;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.holders.ImageViewerActivity;
import com.egormoroz.schooly.ui.chat.holders.VoicePlayer;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> userMessagesList;
    private DatabaseReference usersRef;
    private String messageSenderNick = "", messageReceiverNick = "";
    private String fromUserID;
    private DatabaseReference reference;
    static Clothes trueClothes;
    MessageAdapter.ItemClickListener itemClickListener;




    public MessageAdapter(List<Message> userMessagesList, String messageSenderId, String messageReceiverId,ItemClickListener itemClickListener ) {
        this.userMessagesList = userMessagesList;
        this.messageSenderNick = messageSenderId;
        this.messageReceiverNick = messageReceiverId;
        this.itemClickListener=itemClickListener;
    }

//    public MessageAdapter(List<Message> userMessagesList, String messageSenderId) {
//        this.userMessagesList = userMessagesList;
//        this.messageSenderNick = messageSenderId;
//    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView senderMessageText, receiverMessageText, senderMessageTime, receiverMessageTime, senderTimeVoice
                ,clothesTitleAndCreator,senderTimeClothes,senderMessageTextClothes
        ,clothesTitleAndCreatorOther,receiverTimeClothesOther,receiverMessageTextClothesOther
                ,lookFrom,watchLook,senderTimeLook,senderMessageTextLook
                ,lookFromOther,watchLookOther,receiverTimeLook,receiverMessageTextLook;
        //public ImageView receiverProfileImage;
        public RelativeLayout outMessage, inMessage, outVoice, inVoice,inClothes,outClothes,inLook,outLook;
        public ImageView messageSenderPicture, senderPlay, senderPause,clothesImage,clothesImageOther;
        public ImageView messageReceiverPicture;
        public SeekBar  senderSeekBar;



        private void handleShowView(View view) {
            if (getAdapterPosition() > X - 1) {
                view.setVisibility(View.GONE);
                return;
            }
            view.setVisibility(View.VISIBLE);
        }

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            handleShowView(itemView);
            outMessage = itemView.findViewById(R.id.textMessageOutcoming);
            inMessage = itemView.findViewById(R.id.textMessageIncoming);
            inVoice = itemView.findViewById(R.id.incomingVoice);
            receiverMessageTime = itemView.findViewById(R.id.receiver_time);
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
            inClothes=itemView.findViewById(R.id.clothesFrom);
            clothesTitleAndCreator=itemView.findViewById(R.id.clothesTitleAndCreator);
            clothesImage=itemView.findViewById(R.id.clothesImage);
            senderTimeClothes=itemView.findViewById(R.id.sender_time_clothes);
            senderMessageTextClothes=itemView.findViewById(R.id.sender_message_text_clothes);
            outClothes=itemView.findViewById(R.id.clothesFromOther);
            clothesTitleAndCreatorOther=itemView.findViewById(R.id.clothesTitleAndCreatorOther);
            clothesImageOther=itemView.findViewById(R.id.clothesImageOther);
            receiverTimeClothesOther=itemView.findViewById(R.id.receiver_time_clothes);
            receiverMessageTextClothesOther=itemView.findViewById(R.id.receiver_message_text_clothes_other);
            inLook=itemView.findViewById(R.id.relativeLook);
            lookFrom=itemView.findViewById(R.id.lookFrom);
            watchLook=itemView.findViewById(R.id.watchLook);
            senderTimeLook=itemView.findViewById(R.id.sender_time_look);
            senderMessageTextLook=itemView.findViewById(R.id.sender_message_text_look);
            outLook=itemView.findViewById(R.id.relativeLookOther);
            lookFromOther=itemView.findViewById(R.id.lookFromOther);
            watchLookOther=itemView.findViewById(R.id.watchLookOther);
            receiverTimeLook=itemView.findViewById(R.id.receiver_time_look_other);
            receiverMessageTextLook=itemView.findViewById(R.id.receiver_message_text_look_other);
        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.messages_types, viewGroup, false);
        reference = FirebaseDatabase.getInstance().getReference("users").child(messageSenderNick).child("Chats").child(messageReceiverNick).child("Messages");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, @SuppressLint("RecyclerView") final int position) {


        Message messages = userMessagesList.get(position);
        fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();
        VoicePlayer voicePlayer = new VoicePlayer(messageViewHolder.itemView.getContext());
        voicePlayer.setMediaPlayerListener(new VoicePlayer.MediaPlayerListener() {
            @Override
            public void isPlaying(int currentDuration) {

            }

            @Override
            public void onPause() {
                voicePlayer.release();
                messageViewHolder.senderPlay.setVisibility(View.VISIBLE);
                messageViewHolder.senderPause.setVisibility(View.GONE);
            }

            @Override
            public void onStart() {
                messageViewHolder.senderPlay.setVisibility(View.GONE);
                messageViewHolder.senderPause.setVisibility(View.VISIBLE);
            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    //Picasso.get().load(receiverImage).placeholder(R.drawable.ic_image).into(messageViewHolder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageViewHolder.outMessage.setVisibility(View.GONE);
        messageViewHolder.inMessage.setVisibility(View.GONE);
        messageViewHolder.outVoice.setVisibility(View.GONE);
        messageViewHolder.inVoice.setVisibility(View.GONE);
        messageViewHolder.messageSenderPicture.setVisibility(View.GONE);
        messageViewHolder.messageReceiverPicture.setVisibility(View.GONE);
        messageViewHolder.inLook.setVisibility(View.GONE);
        messageViewHolder.outLook.setVisibility(View.GONE);
        messageViewHolder.inClothes.setVisibility(View.GONE);
        messageViewHolder.outClothes.setVisibility(View.GONE);


        switch (fromMessageType) {
            case "text":
                if (fromUserID.equals(messageSenderNick)) {
                    messageViewHolder.outMessage.setVisibility(View.VISIBLE);
                    messageViewHolder.senderMessageText.setText(messages.getMessage());
                    messageViewHolder.senderMessageTime.setText(messages.getTime());
                } else {
                //messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                    messageViewHolder.inMessage.setVisibility(View.VISIBLE);
                    messageViewHolder.receiverMessageText.setText(messages.getMessage());
                    messageViewHolder.receiverMessageTime.setText(messages.getTime());
                }
                break;
            case "image":
                if (fromUserID.equals(messageSenderNick)) {
                    messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                    Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageSenderPicture);
                    messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                            intent.putExtra("url", userMessagesList.get(position).getMessage());
                            messageViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });

                } else {
                    //messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                    messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
                    Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageReceiverPicture);
                    messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                            intent.putExtra("url", userMessagesList.get(position).getMessage());
                            messageViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });
                }
                break;
            case "pdf":

            case "docx":
                if (fromUserID.equals(messageSenderNick)) {
                    messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                    messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                            messageViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    //messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                    messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
                    messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                            messageViewHolder.itemView.getContext().startActivity(intent);
                            notifyDataSetChanged();
                        }
                    });
                }
                break;
            case "voice":
                if (fromUserID.equals(messageSenderNick)) {
                    messageViewHolder.outVoice.setVisibility(View.VISIBLE);
                    messageViewHolder.senderPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            voicePlayer.setUrlSource(messages.getMessage());
                            if (voicePlayer.isPlaying()){
                                messageViewHolder.senderPlay.setVisibility(View.VISIBLE);
                                messageViewHolder.senderPause.setVisibility(View.GONE);
                                voicePlayer.pause();
                            } else {
                                messageViewHolder.senderPlay.setVisibility(View.GONE);
                                messageViewHolder.senderPause.setVisibility(View.VISIBLE);
                                voicePlayer.start();
                            }
                        }
                    });

                }
                else {
                    messageViewHolder.inVoice.setVisibility(View.VISIBLE);
//                    messageViewHolder.senderPlay.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Log.d("Voice", messages.getMessage());
//                            voicePlayer.setUrlSource(messages.getMessage());
//                            if (voicePlayer.isPlaying()){
//                                voicePlayer.pause();
//                            } else {
//                                voicePlayer.start();
//                            }
//                        }
//                    });
                }
                break;
            case "clothes":
                if (fromUserID.equals(messageSenderNick)) {
                    messageViewHolder.inClothes.setVisibility(View.VISIBLE);
                    messageViewHolder.clothesTitleAndCreator.setText(messages.getClothes().getClothesTitle()+" "+
                            messageViewHolder.clothesTitleAndCreator.getContext().getResources().getString(R.string.by)+" "
                    +messages.getClothes().getCreator());
                    Picasso.get().load(messages.getClothes().getClothesImage()).into(messageViewHolder.clothesImage);
                    messageViewHolder.senderMessageTextClothes.setText(messages.getMessage());
                    messageViewHolder.senderTimeClothes.setText(messages.getTime());
                    messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClickListener.onItemClick(userMessagesList.get(messageViewHolder.getAdapterPosition()).getClothes());
                            trueClothes=userMessagesList.get(messageViewHolder.getAdapterPosition()).getClothes();
                        }
                    });

                } else {
                    messageViewHolder.outClothes.setVisibility(View.VISIBLE);
                    messageViewHolder.clothesTitleAndCreatorOther.setText(messages.getClothes().getClothesTitle()+" "+
                            messageViewHolder.clothesTitleAndCreator.getContext().getResources().getString(R.string.by)+" "
                            +messages.getClothes().getCreator());

                    Picasso.get().load(messages.getClothes().getClothesImage()).into(messageViewHolder.clothesImageOther);
                    messageViewHolder.receiverMessageTextClothesOther.setText(messages.getMessage());
                    messageViewHolder.receiverTimeClothesOther.setText(messages.getTime());
                    messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClickListener.onItemClick(userMessagesList.get(messageViewHolder.getAdapterPosition()).getClothes());
                            trueClothes=userMessagesList.get(messageViewHolder.getAdapterPosition()).getClothes();
                        }
                    });
                }
                break;
            case "look":
                if (fromUserID.equals(messageSenderNick)) {
                    messageViewHolder.inLook.setVisibility(View.VISIBLE);
                    messageViewHolder.lookFrom.setText(
                            messageViewHolder.lookFrom.getContext().getResources().getString(R.string.lookby)+" "
                            +messages.getNewsItem().getNick());

                    messageViewHolder.senderMessageTextLook.setText(messages.getMessage());
                    messageViewHolder.senderTimeLook.setText(messages.getTime());

                } else {
                    messageViewHolder.outLook.setVisibility(View.VISIBLE);
                    messageViewHolder.lookFromOther.setText(
                            messageViewHolder.clothesTitleAndCreator.getContext().getResources().getString(R.string.lookby)+" "
                            +messages.getNewsItem().getNick());

                    
                    messageViewHolder.receiverMessageTextLook.setText(messages.getMessage());
                    messageViewHolder.receiverTimeLook.setText(messages.getTime());
                }
                break;

        }
        
//        messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (fromUserID.equals(messageSenderNick)) {
//                    FirebaseDatabase.getInstance().getReference("users")
//                            .child(messageSenderNick)
//                            .child("Chats").child(messageReceiverNick).child("Messages")
//                            .child(userMessagesList.get(position).getMessageID()).removeValue();
//                    FirebaseDatabase.getInstance().getReference("users")
//                            .child(messageReceiverNick)
//                            .child("Chats").child(messageSenderNick).child("Messages")
//                            .child(userMessagesList.get(position).getMessageID()).removeValue();
//                    delete(position);
//                }
//            }
//        });




    }




    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


    public void delete(int position){
        userMessagesList.remove(position);
        notifyItemRemoved(position);
    }

    public interface ItemClickListener {
        void onItemClick( Clothes clothes);
    }

    public static void singeClothesInfo(NewClothesAdapter.ItemClickListener itemClickListener){
        itemClickListener.onItemClick(trueClothes);
    }

}