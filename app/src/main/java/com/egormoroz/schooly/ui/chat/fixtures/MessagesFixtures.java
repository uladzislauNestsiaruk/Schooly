package com.egormoroz.schooly.ui.chat.fixtures;

import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.User;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public final class MessagesFixtures extends FixturesData {
    private DatabaseReference messagesRef;
    private MessagesFixtures(DatabaseReference ref) {
        messagesRef = ref.child("messages");
        throw new AssertionError();
    }
    private static void uploadMessage(DatabaseReference ref, Message message){
        ref.push().setValue(message);
    }
    private static void uploadMessages(DatabaseReference ref, ArrayList<Message> messages){
        for(Message message : messages)
            uploadMessage(ref, message);
    }
    public static Message getImageMessage() {
        Message message = new Message(getRandomId(), getUser(), null);
        message.setImage(new Message.Image(getRandomImage()));
        return message;}

        //useful//

    public static Message getVoiceMessage(String FileName, int Duration) {
        Message message = new Message(getRandomId(), getUser(), null);
        message.setVoice(new Message.Voice(FileName, Duration));
        return message;
    }
        //////////
    public static Message getTextMessage() {
        return getTextMessage(getRandomName());
    }

    public static Message getTextMessage(String text) {
        return new Message(getRandomId(), getUser(), text);
    }

    public static ArrayList<Message> getMessages(Date startDate, DatabaseReference ref) {
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10/*days count*/; i++) {
            int countPerDay = rnd.nextInt(5) + 1;

            for (int j = 0; j < countPerDay; j++) {
                Message message;
                if (i % 2 == 0 && j % 3 == 0) {
                    message = getImageMessage();
                } else {
                    message = getTextMessage();
                }

                Calendar calendar = Calendar.getInstance();
                if (startDate != null) calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, -(i * i + 1));

                message.setCreatedAt(calendar.getTime());
                messages.add(message);
            }
        }
        uploadMessages(ref, messages);
        return messages;
    }

    public static User getUser() {
        boolean even = rnd.nextBoolean();
        return new User(
                even ? "0" : "1",
                even ? names.get(0) : names.get(1),
                even ? avatars.get(0) : avatars.get(1),
                true);
    }
}
