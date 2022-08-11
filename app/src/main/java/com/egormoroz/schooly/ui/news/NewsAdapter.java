package com.egormoroz.schooly.ui.news;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.TaskRunner;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.ComplainAdapter;
import com.egormoroz.schooly.ui.profile.SendLookAdapter;
import com.egormoroz.schooly.ui.profile.ViewingLookFragment;
import com.egormoroz.schooly.ui.profile.Wardrobe.ConstituentsAdapter;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ImageViewHolder> {

    private List<NewsItem> newsList;
    static FirebaseModel firebaseNewsModel = new FirebaseModel();
    FirebaseModel DefaultDatabase = new FirebaseModel();
    long value;
    static UserInformation userInformation;
    Bundle bundle;
    static EditText editText,messageEdit,addDescriptionEdit;
    RecyclerView clothesCreatorsRecycler,complainRecycler;
    static RecyclerView recyclerView;
    static ArrayList<Subscriber> userFromBase;
    static TextView nickView,description,likesCount,lookPrice,lookPriceDollar,clothesCreator
            ,emptyList,comments,sendComment,noComment,save,complain,complainOtherUserText
            ,reasonText;
    static String likesCountString,lookPriceString,lookPriceDollarString,reasonTextString,descriptionText
            ,userName,otherUserNickString,editGetText,nick;
    static SendLookAdapter.ItemClickListener itemClickListener;
    ConstituentsAdapter.ItemClickListener itemClickListenerClothes;
    LinearLayout linearElse,linearTelegram,linearInstagram;
    ComplainAdapter.ItemClickListener itemClickListenerComplain;
    RelativeLayout sendReason;
    ArrayList<Clothes> lookClothesArrayList;
    static byte[] buffer;
    static URI uri;
    static Future<Buffer> future;
    static Buffer buffer1,bufferToFilament,b;
    static FilamentModel filamentModel=new FilamentModel();
    static ArrayList<Clothes> clothesList=new ArrayList<>();
    static ArrayList<String> clothesUid=new ArrayList<>();
    Fragment fragment;
    Activity activity;


    public NewsAdapter(List<NewsItem> newsList,UserInformation userInformation,Bundle bundle,Fragment fragment,Activity activity) {
        this.newsList = newsList;
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
        this.activity=activity;
        firebaseNewsModel.initNewsDatabase();
        DefaultDatabase.initAll();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_image,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);
        loadLookClothes(newsItem);
        nick=userInformation.getNick();
        filamentModel.postFrameCallback();
        try {
            if(bundle.getSerializable("CHARACTERMODEL")==null){
                loadBuffer(userInformation.getPerson().getBody());
                bufferToFilament=future.get();
                ArrayList<Buffer> buffers=new ArrayList<>();
                buffers.add(bufferToFilament);
                bundle.putSerializable("CHARACTERMODEL",buffers);
                filamentModel.initFilament(holder.surfaceView,bufferToFilament,true,null,"regularRender",true);
            }else{
                ArrayList<Buffer> buffers= (ArrayList<Buffer>) bundle.getSerializable("CHARACTERMODEL");
                Buffer buffer3=buffers.get(0);
                filamentModel.initFilament(holder.surfaceView,buffer3,true,null,"regularRender",true);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
       } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        holder.setIsRecyclable(false);
        holder.like_count.setText(newsItem.getLikes_count());
        holder.description.setText(newsItem.getItem_description());
        firebaseNewsModel.initNewsDatabase();
        Log.d("#####", "Database url" + firebaseNewsModel.getReference());
        Query likeref = DefaultDatabase.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId());
        likeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.like.setImageResource(R.drawable.ic_pressedheart40dp);
                }
                else {
                    holder.like.setImageResource(R.drawable.ic_heart40dp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = Long.parseLong(holder.like_count.getText().toString());
                Log.d("#####", "Firebase : " + firebaseNewsModel.getReference() + "   Likes before " + value);
                Query likeref = DefaultDatabase.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId());
                likeref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            value -= 1;
                            holder.like.setImageResource(R.drawable.ic_heart40dp);
                            DefaultDatabase.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId()).removeValue();
                        }
                        else{
                            value += 1;
                            holder.like.setImageResource(R.drawable.ic_pressedheart40dp);
                            DefaultDatabase.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId()).setValue("liked");
                        }
                        Log.d("#####", "Firebase : " + firebaseNewsModel.getReference() + "   Likes " + value);
                        holder.like_count.setText(String.valueOf(value));
                        firebaseNewsModel.getReference().child(nick).child(newsItem.getNewsId()).child("likes_count").setValue(String.valueOf(value));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(holder.itemView,holder.surfaceView);
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogComments(newsItem, holder.comment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImage, like, comment,send;
        TextView description, like_count;
        SurfaceView surfaceView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            surfaceView=itemView.findViewById(R.id.surfaceView);
            send=itemView.findViewById(R.id.send);
            like = itemView.findViewById(R.id.like);
            description = itemView.findViewById(R.id.description);
            like_count = itemView.findViewById(R.id.likesCount);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    private void showBottomSheetDialogComments(NewsItem newsItem,View v) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_comment);
        FirebaseModel newsModel = new FirebaseModel();
        newsModel.initNewsDatabase();
        editText=bottomSheetDialog.findViewById(R.id.commentEdit);
        recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        emptyList=bottomSheetDialog.findViewById(R.id.emptyCommentsList);
        comments=bottomSheetDialog.findViewById(R.id.comments);
        comments.setText(R.string.comments);
        noComment=bottomSheetDialog.findViewById(R.id.noComment);
        sendComment=bottomSheetDialog.findViewById(R.id.send);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        loadComments(newsItem,v);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editGetText=editText.getText().toString();
                if (editGetText.length()==0){
                    sendComment.setVisibility(View.GONE);
                }else {
                    sendComment.setVisibility(View.VISIBLE);
                    sendComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String commentId=newsModel.getReference().child(newsItem.getNick())
                                    .child(newsItem.getNewsId()).child("comments").push().getKey();
                            newsModel.getReference().child(newsItem.getNick())
                                    .child(newsItem.getNewsId()).child("comments").child(commentId)
                                    .setValue(new Comment(editText.getText().toString(), 0, commentId,RecentMethods.getCurrentTime(),nick,"image","comment"));
                            editText.getText().clear();
                            loadComments(newsItem,v);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bottomSheetDialog.show();
    }

    public void loadComments(NewsItem newsItem,View v){
        RecentMethods.getCommentsList(newsItem.getNick(), newsItem.getNewsId(), firebaseNewsModel, new Callbacks.getCommentsList() {
            @Override
            public void getCommentsList(ArrayList<Comment> comment) {
                if(comment.size()==0){
                    noComment.setVisibility(View.VISIBLE);
                }else {
                    noComment.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
                    CommentAdapter commentAdapter = new CommentAdapter(comment, newsItem.getNick(), bundle, newsItem.getNewsId(), userInformation);
                    recyclerView.setAdapter(commentAdapter);
                }
            }
        });
    }

    public void loadLookClothes(NewsItem newsItem){
        if(clothesUid.size()==0) {
            RecentMethods.getLookClothes(newsItem.getNick(), newsItem.getNewsId(), firebaseNewsModel, new Callbacks.getLookClothes() {
                @Override
                public void getLookClothes(ArrayList<Clothes> clothesArrayList) {
                    lookClothesArrayList=clothesArrayList;
                    for(int i=0;i<clothesArrayList.size();i++){
                        Clothes clothes=clothesArrayList.get(i);
                        TaskRunner taskRunner=new TaskRunner();
                        taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
                            filamentModel.populateScene(data.getBuffer(), data);
                        });
                    }
                }
            });
        }  else{
            Log.d("####", "ccc  "+clothesUid.size());
            for(int i=0;i<clothesList.size();i++ ){
                Clothes clothes=clothesList.get(i);
                if(clothesUid.contains(clothes.getUid())&&clothes.getBuffer()!=null){
                    //filamentModel.populateScene(clothes.getBuffer(), clothes);
                } else if(clothesUid.contains(clothes.getUid())&&clothes.getBuffer()==null){
//                    TaskRunner taskRunner=new TaskRunner();
//                    taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
//                        filamentModel.populateScene(data.getBuffer(), data);
//                    });
                }
            }
        }
    }

    public static byte[] getBytes( URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream());
            byte[] byteChunk = new byte[4096];
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            Log.d("####", "Failed while reading bytes from %s: %s"+ url.toExternalForm()+ e.getMessage());
            e.printStackTrace ();
        }
        finally {
            if (is != null) { is.close(); }
        }
        return  baos.toByteArray();
    }

    public static Clothes addModelInScene(Clothes clothes)  {
        try {
            uri = new URI(clothes.getModel());
            buffer = getBytes(uri.toURL());
            bufferToFilament= ByteBuffer.wrap(buffer);
            clothes.setBuffer(bufferToFilament);
            clothesList.add(clothes);
            clothesUid.add(clothes.getUid());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return clothes;
    }

    public static void loadBuffer(String model){
        ExecutorService executorService= Executors.newCachedThreadPool();
        future = executorService.submit(new Callable(){
            public Buffer call() throws Exception {
                uri = new URI(model);
                buffer = getBytes(uri.toURL());
                buffer1= ByteBuffer.wrap(buffer);
                return buffer1;
            }
        });
    }

    private void showBottomSheetDialogClothesCreators(NewsItem newsItem,View v) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_clothescreators);
        clothesCreatorsRecycler=bottomSheetDialog.findViewById(R.id.recyclerView);
        if(lookClothesArrayList.size()==0){
            RecentMethods.getLookClothes(newsItem.getNick(), newsItem.getNewsId(), firebaseNewsModel, new Callbacks.getLookClothes() {
                @Override
                public void getLookClothes(ArrayList<Clothes> clothesArrayList) {
                    ConstituentsAdapter constituentsAdapter=new ConstituentsAdapter(clothesArrayList,itemClickListenerClothes);
                    clothesCreatorsRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
                    clothesCreatorsRecycler.setAdapter(constituentsAdapter);
                }
            });
        }else{
            ConstituentsAdapter constituentsAdapter=new ConstituentsAdapter(lookClothesArrayList,itemClickListenerClothes);
            clothesCreatorsRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
            clothesCreatorsRecycler.setAdapter(constituentsAdapter);
        }
        itemClickListenerClothes=new ConstituentsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                bottomSheetDialog.dismiss();
                RecentMethods.setCurrentFragment(ViewingClothesNews.newInstance(ViewingLookFragment.newInstance(fragment, userInformation,bundle),userInformation,bundle), (Activity) v.getContext());
            }
        };

        bottomSheetDialog.show();
    }

    private void showBottomSheetDialog(View view,SurfaceView surfaceView) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        editText=bottomSheetDialog.findViewById(R.id.searchuser);
        recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        emptyList=bottomSheetDialog.findViewById(R.id.emptySubscribersList);
        linearElse=bottomSheetDialog.findViewById(R.id.linearElse);
        linearTelegram=bottomSheetDialog.findViewById(R.id.linearTelegram);
        linearInstagram=bottomSheetDialog.findViewById(R.id.linearInstagram);
        messageEdit=bottomSheetDialog.findViewById(R.id.message);

        linearElse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linearTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearInstagram.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getBitmapFormView(surfaceView, activity, new Callback<Bitmap>() {
                            @Override
                            public void onResult1(Bitmap bitmap) {
                                Uri backgroundAssetUri = getImageUri(activity, bitmap);
                                Uri stickerAssetUri = getImageUri(activity, bitmap);
                                String sourceApplication = "com.egormoroz.schooly";

                                Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
                                intent.putExtra("source_application", sourceApplication);

                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(backgroundAssetUri, "image/*");
                                intent.putExtra("interactive_asset_uri", stickerAssetUri);


                                if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
                                    activity.startActivityForResult(intent, 0);
                                }
                            }
                        });
            }
        });
        itemClickListener=new SendLookAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String otherUserNick, String type) {
                if(type.equals("send")){
                    String messageText = messageEdit.getText().toString();

                    String messageSenderRef = otherUserNick + "/Chats/" + userInformation.getNick() + "/Messages";
                    String messageReceiverRef = userInformation.getNick()  + "/Chats/" + otherUserNick+ "/Messages";
                    otherUserNickString=otherUserNick;

                    DatabaseReference userMessageKeyRef = firebaseNewsModel.getUsersReference().child(userInformation.getNick() ).child("Chats").child(otherUserNick).child("Messages").push();
                    String messagePushID = userMessageKeyRef.getKey();

                    Map<String, String> messageTextBody = new HashMap<>();
                    messageTextBody.put("message", messageText);
                    messageTextBody.put("type", "text");
                    messageTextBody.put("from", userInformation.getNick() );
                    messageTextBody.put("to", otherUserNick);
                    messageTextBody.put("time", RecentMethods.getCurrentTime());
                    messageTextBody.put("messageID", messagePushID);
                    addLastMessage("text", messageText);

                    Map<String, Object> messageBodyDetails = new HashMap<String, Object>();
                    messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                    messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);
                }else {
                    Log.d("####", type);
                }
            }
        };
        if(userInformation.getSubscription()==null){
            RecentMethods.getSubscriptionList(userInformation.getNick(), firebaseNewsModel, new Callbacks.getFriendsList() {
                @Override
                public void getFriendsList(ArrayList<Subscriber> friends) {
                    if (friends.size()==0){
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        SendLookAdapter sendLookAdapter = new SendLookAdapter(friends,itemClickListener);
                        recyclerView.setAdapter(sendLookAdapter);
                    }
                }
            });
        }else {
            if (userInformation.getSubscription().size()==0){
                emptyList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                SendLookAdapter sendLookAdapter = new SendLookAdapter(userInformation.getSubscription(),itemClickListener);
                recyclerView.setAdapter(sendLookAdapter);
            }
        }

        initUserEnter();

        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getBitmapFormView(View view, Activity activity, Callback<Bitmap> callback) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        PixelCopy.request((SurfaceView) view, bitmap, copyResult -> {
            if (copyResult == PixelCopy.SUCCESS) {
                callback.onResult1(bitmap);
            }
        }, new Handler(Looper.getMainLooper()));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public interface Callback<Bitmap> {
        void onResult1(Bitmap bitmap);
    }

    public void initUserEnter() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userName = String.valueOf(editText.getText()).trim();
                userName = userName.toLowerCase();
                if(userInformation.getSubscription()==null){
                    Query query = firebaseNewsModel.getUsersReference().child(userInformation.getNick()).child("subscription");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userFromBase = new ArrayList<>();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Subscriber subscriber = new Subscriber();
                                subscriber.setSub(snap.getValue(String.class));
                                String nick = subscriber.getSub();
                                int valueLetters = userName.length();
                                nick = nick.toLowerCase();
                                if (nick.length() < valueLetters) {
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                } else {
                                    nick = nick.substring(0, valueLetters);
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                }

                            }
                            if(userFromBase.size()==0){
                                emptyList.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else {
                                emptyList.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListener);
                                recyclerView.setAdapter(sendLookAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    userFromBase=new ArrayList<>();
                    for (int s=0;s<userInformation.getSubscription().size();s++) {
                        Subscriber subscriber = userInformation.getSubscription().get(s);
                        String nick = subscriber.getSub();
                        int valueLetters = userName.length();
                        nick = nick.toLowerCase();
                        if (nick.length() < valueLetters) {
                            if (nick.equals(userName))
                                userFromBase.add(subscriber);
                        } else {
                            nick = nick.substring(0, valueLetters);
                            if (nick.equals(userName))
                                userFromBase.add(subscriber);
                        }

                    }
                    if(userFromBase.size()==0){
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        emptyList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListener);
                        recyclerView.setAdapter(sendLookAdapter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void addLastMessage(String type, String Message){
        switch (type) {
            case "text":
                addType("text");
                firebaseNewsModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastMessage").setValue(Message);
                firebaseNewsModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastMessage").setValue(Message);
                break;
            case "voice":
                addType("voice");
                firebaseNewsModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastMessage").setValue("Голосовое сообщение");
                firebaseNewsModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastMessage").setValue("Голосовое сообщение");
                break;
            case "image":
                firebaseNewsModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastMessage").setValue("Фотография");
                firebaseNewsModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastMessage").setValue("Фотография");
                addType("image");
                break;
        }
        Calendar calendar = Calendar.getInstance();
        firebaseNewsModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastTime").setValue(RecentMethods.getCurrentTime());
        firebaseNewsModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastTime").setValue(RecentMethods.getCurrentTime());
        firebaseNewsModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("TimeMill").setValue(calendar.getTimeInMillis() * -1);
        firebaseNewsModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("TimeMill").setValue(calendar.getTimeInMillis() * -1);
    }

    public void addType(String type) {
        final long[] value = new long[1];
        DatabaseReference ref = firebaseNewsModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child(type);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    value[0] = (long) dataSnapshot.getValue();
                    value[0] = value[0] + 1;
                    dataSnapshot.getRef().setValue(value[0]);}
                else dataSnapshot.getRef().setValue(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    static class LongRunningTask implements Callable<Clothes> {
        private Clothes clothes;

        public LongRunningTask(Clothes clothes) {
            this.clothes = clothes;
        }

        @Override
        public Clothes call() {
            return addModelInScene(clothes);
        }
    }
    public static void CommentReply(String commentId, String name){
        editText.setHint("You replying to " + name + "\n");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userName = String.valueOf(editText.getText()).trim();
                userName = userName.toLowerCase();
                if(userInformation.getSubscription()==null){
                    Query query = firebaseNewsModel.getUsersReference().child(userInformation.getNick()).child("subscription");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userFromBase = new ArrayList<>();
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Subscriber subscriber = new Subscriber();
                                subscriber.setSub(snap.getValue(String.class));
                                String nick = subscriber.getSub();
                                int valueLetters = userName.length();
                                nick = nick.toLowerCase();
                                if (nick.length() < valueLetters) {
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                } else {
                                    nick = nick.substring(0, valueLetters);
                                    if (nick.equals(userName))
                                        userFromBase.add(subscriber);
                                }

                            }
                            if(userFromBase.size()==0){
                                emptyList.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else {
                                emptyList.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListener);
                                recyclerView.setAdapter(sendLookAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    userFromBase=new ArrayList<>();
                    for (int s=0;s<userInformation.getSubscription().size();s++) {
                        Subscriber subscriber = userInformation.getSubscription().get(s);
                        String nick = subscriber.getSub();
                        int valueLetters = userName.length();
                        nick = nick.toLowerCase();
                        if (nick.length() < valueLetters) {
                            if (nick.equals(userName))
                                userFromBase.add(subscriber);
                        } else {
                            nick = nick.substring(0, valueLetters);
                            if (nick.equals(userName))
                                userFromBase.add(subscriber);
                        }

                    }
                    if(userFromBase.size()==0){
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        emptyList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListener);
                        recyclerView.setAdapter(sendLookAdapter);
                    }
                }
               /* editGetText=editText.getText().toString();
                if (editGetText.length()==0){
                    sendComment.setVisibility(View.GONE);
                }else {
                    sendComment.setVisibility(View.VISIBLE);
                    sendComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String commentId=firebaseNewsModel.getReference().child(newsItem.getNick())
                                    .child(newsItem.getNewsId()).child("comments").push().getKey();
                            firebaseNewsModel.getReference().child(newsItem.getNick())
                                    .child(newsItem.getNewsId()).child("comments").child(commentId)
                                    .setValue(new Comment(editText.getText().toString(), 0, commentId,RecentMethods.getCurrentTime(),nick,"image","comment"));
                            editText.getText().clear();
                            loadComments(newsItem,v);
                        }
                    });
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


}


