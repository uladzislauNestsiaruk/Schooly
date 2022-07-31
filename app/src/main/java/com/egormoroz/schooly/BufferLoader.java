package com.egormoroz.schooly;

import android.os.Bundle;
import android.util.Log;

import com.egormoroz.schooly.ui.main.Shop.Clothes;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BufferLoader {

    static byte[] buffer;
    static URI uri;
    static Future<Buffer> future;
    static Buffer buffer1,bufferToFilament,b;
    static ArrayList<Clothes> clothesArrayListWithBuffers=new ArrayList<>();
    static ArrayList<String> clothesLoadBuffersUid=new ArrayList<>();
    static int a=0;
    static Bundle bundle;

    public static void loadBuffers(ArrayList<Clothes> clothesArrayList,Bundle bundle){
        if(bun)
        Log.d("##", "x "+clothesArrayList.size());
        for(int i=0;i<clothesArrayList.size();i++){
            Clothes clothes=clothesArrayList.get(i);
            if(!clothesLoadBuffersUid.contains(clothes.getUid()))
            addBufferToClothes(clothes);
        }
    }

    public static ArrayList<Clothes> getLoadBuffersList(){
        return clothesArrayListWithBuffers;
    }

    public static void loadBuffer(String model){
        ExecutorService executorService= Executors.newCachedThreadPool();
        future = executorService.submit(new Callable(){
            public Buffer call() throws Exception {
                a++;
                Log.d("###", model+"   "+a);
                uri = new URI(model);
                buffer = getBytes(uri.toURL());
                buffer1= ByteBuffer.wrap(buffer);
                return buffer1;
            }
        });
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

    public static void addBufferToClothes(Clothes clothes)  {
        loadBuffer(clothes.getModel());
        try {
            bufferToFilament= future.get();
            clothes.setBuffer(bufferToFilament);
            clothesArrayListWithBuffers.add(clothes);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
