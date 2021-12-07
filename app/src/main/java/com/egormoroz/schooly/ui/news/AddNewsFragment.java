package com.egormoroz.schooly.ui.news;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddNewsFragment extends Fragment {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd hh:mm");;
    private FirebaseModel firebaseModel = new FirebaseModel();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private String date, news_text, senderNick, myUrl;
    private ImageView sendNews, back;
    private TextView imageload;
    private Uri fileUri;
    private StorageTask uploadTask;
    private EditText text;

    public static AddNewsFragment newInstance() {
        return new AddNewsFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_news, container, false);
        firebaseModel.initAll();
        firebaseDatabase = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        reference = firebaseDatabase.getReference().child("news");
        sendNews = root.findViewById(R.id.sendButton);
        imageload = root.findViewById(R.id.newscreate);
        text = root.findViewById(R.id.news_enter);
        back = root.findViewById(R.id.back);
        return root;
    }

    public void onStart(){
        super.onStart();

    }
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        imageload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 443);
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {

            @Override
            public void PassUserNick(String nick) {
                sendNews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        DatabaseReference NewsPush = reference.child(nick).push();
                        String newsPushID = NewsPush.getKey();

                        Calendar calendar = Calendar.getInstance();


                        Map<String, String> newsBody = new HashMap<String, String>();
                        newsBody.put("news", myUrl);
                        newsBody.put("description", text.getText().toString());
                        newsBody.put("date", RecentMethods.getCurrentTime());
                        newsBody.put("newsID", newsPushID);
                        newsBody.put("TimeMill", String.valueOf(calendar.getTimeInMillis() * -1));


                        Map<String, Object> newsBodyDetails = new HashMap<String, Object>();
                        newsBodyDetails.put(nick + "/" + newsPushID, newsBody);
                        reference.updateChildren(newsBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                text.setText("");
                            }
                        });
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(NewsFragment.newInstance(), getActivity());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {

            @Override
            public void PassUserNick(String nick) {
                DatabaseReference NewsPush = reference.child(nick).push();
                String newsPushID = NewsPush.getKey();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images");

                Calendar calendar = Calendar.getInstance();
                final StorageReference filePath = storageReference.child(newsPushID + "." + "jpg");
                if (requestCode == 443 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    fileUri = data.getData();
                    uploadTask = filePath.putFile(fileUri);

                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();


                            Map<String, String> newsBody = new HashMap<String, String>();
                            newsBody.put("ImageUrl", myUrl);
                            newsBody.put("item_description", text.getText().toString());
                            newsBody.put("date", RecentMethods.getCurrentTime());
                            newsBody.put("newsID", newsPushID);
                            newsBody.put("likes_count", "0");
                            newsBody.put("TimeMill", String.valueOf(calendar.getTimeInMillis() * -1));


                            Map<String, Object> newsBodyDetails = new HashMap<String, Object>();
                            newsBodyDetails.put(nick + "/" + newsPushID, newsBody);
                            reference.updateChildren(newsBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    text.setText("");
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
