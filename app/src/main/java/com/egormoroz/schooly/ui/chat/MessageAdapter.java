package com.egormoroz.schooly.ui.chat;


import static com.google.android.material.transition.MaterialSharedAxis.X;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.chat.holders.ImageViewerActivity;
import com.egormoroz.schooly.ui.chat.holders.VoicePlayer;
import com.egormoroz.schooly.ui.main.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> userMessagesList;
    private DatabaseReference usersRef;
    private String messageSenderId = "", messageReceiverId = "";
    private String fromUserID;
    private DatabaseReference reference;
    private String messageID;



    public MessageAdapter(List<Message> userMessagesList, String messageSenderId, String messageReceiverId ) {
        this.userMessagesList = userMessagesList;
        this.messageSenderId = messageSenderId;
        this.messageReceiverId = messageReceiverId;
    }

    public MessageAdapter(List<Message> userMessagesList, String messageSenderId) {
        this.userMessagesList = userMessagesList;
        this.messageSenderId = messageSenderId;
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView senderMessageText, receiverMessageText, senderMessageTime, receiverMessageTime;
        public CircleImageView receiverProfileImage;
        public ImageView messageSenderPicture;
        public ImageView messageReceiverPicture;
        public static VoicePlayerView voicePlayerView;
        public static VoicePlayerView voicePlayerViewReceiver;

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
            receiverMessageTime = itemView.findViewById(R.id.receiver_time);
            senderMessageTime = itemView.findViewById(R.id.sender_time);
            senderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = (TextView) itemView.findViewById(R.id.receiver_message_text);
         //   receiverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
         //   voicePlayerViewReceiver = itemView.findViewById(R.id.voicePlayerViewReceiver);

        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_messages_layout, viewGroup, false);
        reference = FirebaseDatabase.getInstance().getReference("users").child(messageSenderId).child("Chats").child(messageReceiverId).child("Messages");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, @SuppressLint("RecyclerView") final int position) {


        Message messages = userMessagesList.get(position);
        fromUserID = messages.getFrom();
        String fromMessageType = messages.getType();



        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.ic_image).into(messageViewHolder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//

        messageViewHolder.receiverMessageText.setVisibility(View.GONE);
//        messageViewHolder.receiverProfileImage.setVisibility(View.GONE);
        messageViewHolder.senderMessageText.setVisibility(View.GONE);
        messageViewHolder.messageSenderPicture.setVisibility(View.GONE);
        messageViewHolder.messageReceiverPicture.setVisibility(View.GONE);
//        messageViewHolder.voicePlayerView.setVisibility(View.GONE);
//        messageViewHolder.voicePlayerViewReceiver.setVisibility(View.GONE);

        if (fromMessageType.equals("text")) {
            if (fromUserID.equals(messageSenderId)) {
                messageViewHolder.senderMessageText.setVisibility(View.VISIBLE);
                messageViewHolder.senderMessageText.setText(messages.getMessage());
                messageViewHolder.senderMessageTime.setText(messages.getTime());
            } else {
//                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.receiverMessageText.setVisibility(View.VISIBLE);
                messageViewHolder.receiverMessageText.setText(messages.getMessage());
                messageViewHolder.receiverMessageTime.setText(messages.getTime());
            }
        } else if (fromMessageType.equals("image")) {
            if (fromUserID.equals(messageSenderId)) {
                messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageSenderPicture);

            } else {
//                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(messageViewHolder.messageReceiverPicture);
            }
        } else if (fromMessageType.equals("pdf") || fromMessageType.equals("docx")) {
            if (fromUserID.equals(messageSenderId)) {
                messageViewHolder.messageSenderPicture.setVisibility(View.VISIBLE);
                messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        messageViewHolder.itemView.getContext().startActivity(intent);
                    }
                });

            } else {
                messageViewHolder.receiverProfileImage.setVisibility(View.VISIBLE);
                messageViewHolder.messageReceiverPicture.setVisibility(View.VISIBLE);
            }
        } else if (fromMessageType.equals("voice")) {
            if (fromUserID.equals(messageSenderId)) {
//                VoicePlayer.getInstance(ChatActivity.getContext()).init(messages.getMessage(),messageViewHolder.);

//                MessageViewHolder.voicePlayerView.setAudio(messages.getMessage());
//                MessageViewHolder.voicePlayerView.setVisibility(View.VISIBLE);
            }
        } else {
//            MessageViewHolder.voicePlayerViewReceiver.setAudio(messages.getMessage());
//            MessageViewHolder.voicePlayerViewReceiver.setVisibility(View.VISIBLE);
        }

        if (fromUserID.equals(messageSenderId)) {

            messageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Map<String, String> messageTextBody = new HashMap<>();
                    messageTextBody.put("message", null);
                    messageTextBody.put("type", null);
                    messageTextBody.put("from", null);
                    messageTextBody.put("to", null);
                    messageTextBody.put("time", null);
                    messageTextBody.put("messageID", null);


                    Map<String, Object> messageBodyDetails = new HashMap<String, Object>();
                    messageBodyDetails.put(reference + "/" + userMessagesList.get(position).getMessageID(), messageTextBody);
                    reference.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                                delete(position);
                        }
                    });
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }


    public void delete(int position){
        userMessagesList.remove(position);
        notifyItemRemoved(position);
    }

}