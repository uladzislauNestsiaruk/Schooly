package com.egormoroz.schooly.ui.chat.fixtures;



import androidx.annotation.NonNull;

import com.egormoroz.schooly.ui.chat.Dialog;
import com.egormoroz.schooly.ui.chat.User;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public final class DialogsFixtures extends FixturesData {
    private DatabaseReference chatsReference;
    private String userId;
    private DialogsFixtures(DatabaseReference ref, String userId) {
        chatsReference = ref;
        this.userId = userId;
        throw new AssertionError();
    }

    public static ArrayList<Dialog> getDialogs(DatabaseReference ref, String userId) {

        ArrayList<Dialog> chats = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -(i * i));
            calendar.add(Calendar.MINUTE, -(i * i));

            chats.add(getDialog(i, calendar.getTime()));
        }
        uploadDialogs(ref, userId, chats);
        return chats;
    }

    private static Dialog getDialog(int i, Date lastMessageCreatedAt) {
        ArrayList<User> users = getUsers();
        return new Dialog(
                getRandomId(),
                users.size() > 1 ? groupChatTitles.get(users.size() - 2) : users.get(0).getName(),
                users.size() > 1 ? groupChatImages.get(users.size() - 2) : getRandomAvatar(),
                users,
                getMessage(lastMessageCreatedAt),
                i < 3 ? 3 - i : 0);
    }

    private static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        int usersCount = 1 + rnd.nextInt(4);

        for (int i = 0; i < usersCount; i++) {
            users.add(getUser());
        }

        return users;
    }

    private static User getUser() {
        return new User(
                getRandomId(),
                getRandomName(),
                getRandomAvatar(),
                getRandomBoolean());
    }

    private static Message getMessage(final Date date) {
        return new Message(
                getRandomId(),
                getUser(),
                getRandomMessage(),
                date);
    }
    private static void uploadDialog(DatabaseReference ref, String userId, Dialog dialog){
        String id = ref.push().getKey();
        ref.push().setValue(dialog);
        dialog.setId(id);
    }
    private static void uploadDialogs(DatabaseReference ref, String userId,
                                      ArrayList<Dialog> dialogs){
        for(Dialog dialog : dialogs)
            uploadDialog(ref, userId, dialog);
    }
    public ArrayList<Dialog> getDialogs(DatabaseReference ref){
        ArrayList<Dialog> res = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return res;
    }
}
