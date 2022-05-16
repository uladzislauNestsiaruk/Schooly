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
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

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
    private ImageView image, imageload;
    private Uri fileUri;
    private StorageTask uploadTask;
    private EditText text;

    UserInformation userInformation;
    Bundle bundle;

    public AddNewsFragment(UserInformation userInformation,Bundle bundle) {
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static AddNewsFragment newInstance(UserInformation userInformation, Bundle bundle) {
        return new AddNewsFragment(userInformation,bundle);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_news, container, false);
        firebaseModel.initAll();
        firebaseDatabase = FirebaseDatabase.getInstance(CONST.RealtimeDatabaseUrl);
        reference = firebaseDatabase.getReference().child("news");
        sendNews = root.findViewById(R.id.sendButton);
        imageload = root.findViewById(R.id.add_image);
        text = root.findViewById(R.id.news_enter);
        back = root.findViewById(R.id.back);
        image = root.findViewById(R.id.news_image);
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
                        DatabaseReference NewsPush = reference.push();
                        String newsPushID = NewsPush.getKey();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images");
                        Calendar calendar = Calendar.getInstance();
                        final StorageReference filePath = storageReference.child(newsPushID + "." + "jpg");
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
                                newsBody.put("imageUrl", myUrl);
                                newsBody.put("itemDescription", text.getText().toString());
                                newsBody.put("date", RecentMethods.getCurrentTime());
                                newsBody.put("from", nick);
                                newsBody.put("newsID", newsPushID);
                                newsBody.put("likesCount", "0");


                                Map<String, Object> newsBodyDetails = new HashMap<String, Object>();
                                newsBodyDetails.put(newsPushID, newsBody);
                                reference.updateChildren(newsBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        text.setText("");
                                        sendNews.setVisibility(View.GONE);
                                    }
                                });
                                firebaseDatabase.getReference("news").child(newsPushID).child("TimeMill").setValue(calendar.getTimeInMillis() * -1);
                            }
                        });
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentMethods.setCurrentFragment(NewsFragment.newInstance(userInformation,bundle), getActivity());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 443 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    fileUri = data.getData();
                    Picasso.get().load(fileUri).into(image);
                }
    }
}
