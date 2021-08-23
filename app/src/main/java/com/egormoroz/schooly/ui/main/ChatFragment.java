package com.egormoroz.schooly.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.ui.chat.Dialog;
import com.egormoroz.schooly.ui.chat.Message;
import com.egormoroz.schooly.ui.chat.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessagesList;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class ChatFragment extends Fragment implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog>, sendDialogs {
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth authDatabase;
    String userId;
    public static ChatFragment newInstance(){return new ChatFragment();}
    public ChatFragment() {}
    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView back = view.findViewById(R.id.backtomainfromchat);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).setCurrentFragment(MainFragment.newInstance());
            }
        });
    }
    public void onDialogClick(Dialog dialog) {
        open(dialog);
    }

    public void open(Dialog dialog) {
        String dialogId = dialog.getId();
        Intent i = new Intent(getActivity(), MessageActivity.class);
        i.putExtra("dialogId", dialogId);
        startActivity(i);
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }
    DialogsList dialogsList;
    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);


    }


    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                              @Nullable Bundle SavedInstanceState) {
        View root = inflater.inflate(R.layout.activity_default_dialogs, container, false);
        dialogsList = root.findViewById(R.id.dialogsList);
        super.onCreate(SavedInstanceState);
        initFirebase();
        initAdapter();
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        return root;
    }

    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsAdapter.setOnDialogClickListener(getActivity().findViewById(R.id.dialogsList));
        dialogsAdapter.setOnDialogLongClickListener(getActivity().findViewById(R.id.dialogsList));
        dialogsAdapter.setItems(getDialogs());
        dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {
                open(dialog);
            }
        });
        dialogsAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<Dialog>() {
            @Override
            public void onDialogLongClick(Dialog dialog) {
                Toast.makeText(getActivity(), dialog.getDialogName(), Toast.LENGTH_SHORT).show();
            }
        });
        dialogsList.setAdapter(dialogsAdapter);
    }
    @Override
    public void onDialogLongClick(Dialog dialog) {
        //TODO:: CONTEXT MENU
    }
    private  void initFirebase(){
        database  = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        authDatabase = FirebaseAuth.getInstance();
        userId = authDatabase.getCurrentUser().getUid();
        ref = database.getReference("users").child(userId).child("chats");
    }

    @Override
    public void setDialogs(ArrayList<Dialog> dialogs) {
        Log.d("#####", dialogs.size() + " receive");
        dialogsAdapter.setItems(getDialogs());
    }











        private DatabaseReference chatsReference;
//
//        private DialogsFixtures(DatabaseReference ref, String userId) {
//            chatsReference = ref;
//            this.userId = userId;
//            throw new AssertionError();
//        }
static String TAG = "Dialogs";
    public static ArrayList<Dialog> getDialogs() {
        ArrayList<Dialog> chats = new ArrayList<>();
        Log.d(TAG, "created dialogs");
        for (int i = 0; i < 20; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -(i * i));
            calendar.add(Calendar.MINUTE, -(i * i));

            chats.add(getDialog(i, calendar.getTime()));
        }

        return chats;
    }

        private static Dialog getDialog(int i, Date lastMessageCreatedAt) {
            ArrayList<User> users = getUsers();
            Log.d(TAG, "created dialog once");
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
//        private static void uploadDialog(DatabaseReference ref, String userId, Dialog dialog){
//            String id = ref.push().getKey();
//            ref.push().setValue(dialog);
//            dialog.setId(id);
//        }
//        private static void uploadDialogs(DatabaseReference ref, String userId,
//                                          ArrayList<Dialog> dialogs){
//            for(Dialog dialog : dialogs)
//                uploadDialog(ref, userId, dialog);
//        }


    static SecureRandom rnd = new SecureRandom();

    static ArrayList<String> avatars = new ArrayList<String>() {
        {
            add("http://i.imgur.com/pv1tBmT.png");
            add("http://i.imgur.com/R3Jm1CL.png");
            add("http://i.imgur.com/ROz4Jgh.png");
            add("http://i.imgur.com/Qn9UesZ.png");
        }
    };

    static final ArrayList<String> groupChatImages = new ArrayList<String>() {
        {
            add("http://i.imgur.com/hRShCT3.png");
            add("http://i.imgur.com/zgTUcL3.png");
            add("http://i.imgur.com/mRqh5w1.png");
        }
    };

    static final ArrayList<String> groupChatTitles = new ArrayList<String>() {
        {
            add("Samuel, Michelle");
            add("Jordan, Jordan, Zoe");
            add("Julia, Angel, Kyle, Jordan");
        }
    };

    static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("Samuel Reynolds");
            add("Kyle Hardman");
            add("Zoe Milton");
            add("Angel Ogden");
            add("Zoe Milton");
            add("Angelina Mackenzie");
            add("Kyle Oswald");
            add("Abigail Stevenson");
            add("Julia Goldman");
            add("Jordan Gill");
            add("Michelle Macey");
        }
    };

    static final ArrayList<String> messages = new ArrayList<String>() {
        {
            add("Hello!");
            add("This is my phone number - +1 (234) 567-89-01");
            add("Here is my e-mail - myemail@example.com");
            add("Hey! Check out this awesome link! www.github.com");
            add("Hello! No problem. I can today at 2 pm. And after we can go to the office.");
            add("At first, for some time, I was not able to answer him one word");
            add("At length one of them called out in a clear, polite, smooth dialect, not unlike in sound to the Italian");
            add("By the bye, Bob, said Hopkins");
            add("He made his passenger captain of one, with four of the men; and himself, his mate, and five more, went in the other; and they contrived their business very well, for they came up to the ship about midnight.");
            add("So saying he unbuckled his baldric with the bugle");
            add("Just then her head struck against the roof of the hall: in fact she was now more than nine feet high, and she at once took up the little golden key and hurried off to the garden door.");
        }
    };

    static final ArrayList<String> images = new ArrayList<String>() {
        {
            add("https://habrastorage.org/getpro/habr/post_images/e4b/067/b17/e4b067b17a3e414083f7420351db272b.jpg");
            add("https://cdn.pixabay.com/photo/2017/12/25/17/48/waters-3038803_1280.jpg");
        }
    };

    public static String getRandomId() {
        return Long.toString(UUID.randomUUID().getLeastSignificantBits());
    }

    static String getRandomAvatar() {
        return avatars.get(rnd.nextInt(avatars.size()));
    }

    static String getRandomGroupChatImage() {
        return groupChatImages.get(rnd.nextInt(groupChatImages.size()));
    }

    static String getRandomGroupChatTitle() {
        return groupChatTitles.get(rnd.nextInt(groupChatTitles.size()));
    }

    static String getRandomName() {
        return names.get(rnd.nextInt(names.size()));
    }

    static String getRandomMessage() {
        return messages.get(rnd.nextInt(messages.size()));
    }

    static String getRandomImage() {
        return images.get(rnd.nextInt(images.size()));
    }

    static boolean getRandomBoolean() {
        return rnd.nextBoolean();
    }
}