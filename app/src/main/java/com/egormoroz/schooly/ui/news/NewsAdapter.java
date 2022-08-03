package com.egormoroz.schooly.ui.news;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ImageViewHolder> {

    private List<NewsItem> newsList;
    FirebaseModel firebaseModel = new FirebaseModel();
    long value;
    UserInformation userInformation;
    Bundle bundle;
    EditText editText,messageEdit,addDescriptionEdit;
    RecyclerView clothesCreatorsRecycler,complainRecycler;
    RecyclerView recyclerView;
    ArrayList<Subscriber> userFromBase;
    TextView nickView,description,likesCount,lookPrice,lookPriceDollar,clothesCreator
            ,emptyList,comments,sendComment,noComment,save,complain,complainOtherUserText
            ,reasonText;
    String likesCountString,lookPriceString,lookPriceDollarString,reasonTextString,descriptionText
            ,userName,otherUserNickString,editGetText,nick;
    SendLookAdapter.ItemClickListener itemClickListener;
    ConstituentsAdapter.ItemClickListener itemClickListenerClothes;
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


    public NewsAdapter(List<NewsItem> newsList,UserInformation userInformation,Bundle bundle,Fragment fragment) {
        this.newsList = newsList;
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseModel.initAll();
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
                //filamentModel.initFilament(holder.surfaceView,bufferToFilament,true,null,"regularRender",true);
            }else{
                ArrayList<Buffer> buffers= (ArrayList<Buffer>) bundle.getSerializable("CHARACTERMODEL");
                Buffer buffer3=buffers.get(0);
                //filamentModel.initFilament(holder.surfaceView,buffer3,true,null,"regularRender",true);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
       }
        holder.setIsRecyclable(false);
        holder.like_count.setText(newsItem.getLikes_count());
        holder.description.setText(newsItem.getItem_description());
        Query likeref = firebaseModel.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId());
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
                Query likeref = firebaseModel.getUsersReference().child(nick).child("likedNews").child(newsItem.getNewsId());
                likeref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            value -= 1;
                            holder.like.setImageResource(R.drawable.ic_heart40dp);
                            firebaseModel.getReference("users").child(nick).child("likedNews").child(newsItem.getNewsId()).removeValue();
                        }
                        else {
                            value += 1;
                            holder.like.setImageResource(R.drawable.ic_pressedheart40dp);
                            firebaseModel.getReference("users").child(nick).child("likedNews").child(newsItem.getNewsId()).setValue("liked");
                        }
                        firebaseModel.getReference("news").child(newsItem.getNewsId()).child("likesCount").setValue(String.valueOf(value));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

        ImageView newsImage, like, comment;
        TextView description, like_count;
        //SurfaceView surfaceView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            //surfaceView=itemView.findViewById(R.id.surfaceView);
            like = itemView.findViewById(R.id.like);
            description = itemView.findViewById(R.id.description);
            like_count = itemView.findViewById(R.id.likesCount);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    private void showBottomSheetDialogComments(NewsItem newsItem,View v) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_comment);

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
                            String commentId=firebaseModel.getUsersReference().child(newsItem.getNick()).child("looks")
                                    .child(newsItem.getNewsId()).child("comments").push().getKey();
                            firebaseModel.getUsersReference().child(newsItem.getNick()).child("looks")
                                    .child(newsItem.getNewsId()).child("comments").child(commentId)
                                    .setValue(new Comment(editText.getText().toString(), "0", commentId,RecentMethods.getCurrentTime(),nick,"image","comment"));
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
        RecentMethods.getCommentsList(newsItem.getNick(), newsItem.getNewsId(), firebaseModel, new Callbacks.getCommentsList() {
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
            RecentMethods.getLookClothes(newsItem.getNick(), newsItem.getNewsId(), firebaseModel, new Callbacks.getLookClothes() {
                @Override
                public void getLookClothes(ArrayList<Clothes> clothesArrayList) {
                    lookClothesArrayList=clothesArrayList;
                    for(int i=0;i<clothesArrayList.size();i++){
                        Clothes clothes=clothesArrayList.get(i);
//                        TaskRunner taskRunner=new TaskRunner();
//                        taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
//                            filamentModel.populateScene(data.getBuffer(), data);
//                        });
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
            RecentMethods.getLookClothes(newsItem.getNick(), newsItem.getNewsId(), firebaseModel, new Callbacks.getLookClothes() {
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

}


