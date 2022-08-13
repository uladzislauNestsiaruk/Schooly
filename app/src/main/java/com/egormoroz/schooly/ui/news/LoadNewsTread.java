package com.egormoroz.schooly.ui.news;

import android.util.Log;

import androidx.annotation.NonNull;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.TaskRunner;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

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
import java.util.concurrent.Callable;

public class LoadNewsTread {
    UserInformation userInformation;
    FirebaseModel newsModel;

    public LoadNewsTread(UserInformation userInformation, Callbacks.loadNewsTread loadNewsTread){
        newsModel=new FirebaseModel();
        newsModel.initNewsDatabase();
        this.userInformation=userInformation;
        getNews(loadNewsTread);
    }

    static byte[] buffer;
    static URI uri;
    static Buffer bufferToFilament,b;
    static FilamentModel filamentModel;
    ArrayList<NewsItem> newsItemArrayListToAdapter=new ArrayList<>();

    public void getNews(Callbacks.loadNewsTread loadNewsTread){
        newsModel.getReference().child("tyomaa6").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    ArrayList<NewsItem> newsItemArrayList=new ArrayList<>();
                    for(DataSnapshot snap:snapshot.getChildren()){
                        NewsItem newsItem=new NewsItem();
                        newsItem.setImageUrl(snap.child("imageUrl").getValue(String.class));
                        newsItem.setLookPrice(snap.child("lookPrice").getValue(Long.class));
                        newsItem.setItem_description(snap.child("item_description").getValue(String.class));
                        newsItem.setNewsId(snap.child("newsId").getValue(String.class));
                        newsItem.setLikesCount(snap.child("likes_count").getValue(String.class));
                        newsItem.setViewCount(snap.child("viewCount").getValue(Long.class));
                        newsItem.setPostTime(snap.child("postTime").getValue(String.class));
                        newsItem.setNick(snap.child("nick").getValue(String.class));
                        newsItem.setLookPriceDollar(snap.child("lookPriceDollar").getValue(Long.class));
                        newsItemArrayList.add(newsItem);
                    }
                    loadLooksClothes(newsItemArrayList,loadNewsTread);
                }
            }
        });
    }

    public void loadLooksClothes(ArrayList<NewsItem> newsItemArrayList,Callbacks.loadNewsTread loadNewsTread){
        for(int i=0;i<newsItemArrayList.size();i++){
            NewsItem newsItem=newsItemArrayList.get(i);
            newsModel.getReference().child(newsItem.getNick()).child(newsItem.getNewsId()).child("clothesCreators").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot= task.getResult();
                        ArrayList<Clothes> clothesArrayList=new ArrayList<>();
                        for(DataSnapshot snap:snapshot.getChildren()){
                            Clothes clothes = new Clothes();
                            clothes.setClothesImage(snap.child("clothesImage").getValue(String.class));
                            clothes.setClothesPrice(snap.child("clothesPrice").getValue(Long.class));
                            clothes.setPurchaseNumber(snap.child("purchaseNumber").getValue(Long.class));
                            clothes.setClothesType(snap.child("clothesType").getValue(String.class));
                            clothes.setClothesTitle(snap.child("clothesTitle").getValue(String.class));
                            clothes.setCreator(snap.child("creator").getValue(String.class));
                            clothes.setCurrencyType(snap.child("currencyType").getValue(String.class));
                            clothes.setDescription(snap.child("description").getValue(String.class));
                            clothes.setPurchaseToday(snap.child("purchaseToday").getValue(Long.class));
                            clothes.setModel(snap.child("model").getValue(String.class));
                            clothes.setBodyType(snap.child("bodyType").getValue(String.class));
                            clothes.setUid(snap.child("uid").getValue(String.class));
                            clothes.setExclusive(snap.child("exclusive").getValue(String.class));
                            clothes.setX(snap.child("x").getValue(Float.class));
                            clothes.setY(snap.child("y").getValue(Float.class));
                            clothes.setZ(snap.child("z").getValue(Float.class));
                            clothes.setTransformRatio(snap.child("transformRatio").getValue(Float.class));
                            clothesArrayList.add(clothes);
                        }
                        newsItem.setClothesCreators(clothesArrayList);
                        loadClothesBuffer(loadNewsTread,newsItem);
                    }
                }
            });
        }
    }

    public void loadClothesBuffer(Callbacks.loadNewsTread loadNewsTread,NewsItem newsItem){
        ArrayList<Clothes> clothesArrayListWithBuffers=new ArrayList<>();
        for(int i=0;i<newsItem.getClothesCreators().size();i++){
            Clothes clothes=newsItem.getClothesCreators().get(i);
            TaskRunner taskRunner=new TaskRunner();
            int finalI = i;
            taskRunner.executeAsync(new LongRunningTask(clothes), (data) -> {
                clothesArrayListWithBuffers.add(data);
                if(finalI ==newsItem.getClothesCreators().size()-1){
                    newsItem.setClothesCreators(clothesArrayListWithBuffers);
                    newsItemArrayListToAdapter.add(newsItem);
                    loadNewsTread.LoadNews(newsItemArrayListToAdapter);
                }
            });
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return clothes;
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
