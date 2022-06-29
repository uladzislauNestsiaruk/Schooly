package com.egormoroz.schooly;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.ui.main.Shop.FittingFragment;
import com.google.android.filament.Camera;
import com.google.android.filament.Colors;
import com.google.android.filament.Engine;
import com.google.android.filament.EntityManager;
import com.google.android.filament.Fence;
import com.google.android.filament.Filament;
import com.google.android.filament.LightManager;
import com.google.android.filament.Renderer;
import com.google.android.filament.Scene;
import com.google.android.filament.Skybox;
import com.google.android.filament.SwapChain;
import com.google.android.filament.View;
import com.google.android.filament.android.DisplayHelper;
import com.google.android.filament.android.UiHelper;
import com.google.android.filament.gltfio.AssetLoader;
import com.google.android.filament.gltfio.FilamentAsset;
import com.google.android.filament.gltfio.Gltfio;
import com.google.android.filament.gltfio.MaterialProvider;
import com.google.android.filament.gltfio.ResourceLoader;
import com.google.android.filament.gltfio.UbershaderLoader;
import com.google.android.filament.utils.AutomationEngine;
import com.google.android.filament.utils.Float3;
import com.google.android.filament.utils.GestureDetector;
import com.google.android.filament.utils.Manipulator;
import com.google.android.filament.utils.ModelViewer;
import com.google.android.filament.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class FilamentModel {

    UiHelper uiHelper;
    Engine engine;
    Manipulator cameraManipulator;
    ModelViewer modelViewer;
    Choreographer choreographer=Choreographer.getInstance();
    GestureDetector doubleTapDetector;
    AutomationEngine.ViewerContent viewerContent=new AutomationEngine.ViewerContent();
    Float3 float3=new Float3(0.0f, 0.0f, -2.2f);
    long loadStartTime;
    Fence loadStartFence;
    byte[] buffer;
    URI uri;
    Buffer buffer1,bufferToFilament;

    public void initFilament(SurfaceView surfaceView,Buffer buffer,boolean onTouch) throws IOException, URISyntaxException {
        Filament.init();
        Gltfio.init();
        Utils.INSTANCE.init();
        cameraManipulator=new Manipulator.Builder()
                .targetPosition(0.0f, 0.0f, -2.2f)
                .viewport(surfaceView.getWidth(), surfaceView.getHeight())
                .build(Manipulator.Mode.ORBIT);
        doubleTapDetector=new GestureDetector(surfaceView, cameraManipulator);
        uiHelper=new UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK);
        engine=Engine.create();
        modelViewer=new ModelViewer(surfaceView, engine,uiHelper,cameraManipulator);
        setupFilament();
        surfaceView.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, MotionEvent event) {
                modelViewer.onTouchEvent(event);
                doubleTapDetector.onTouchEvent(event);
                return onTouch;
            }
        });
        loadGlb(buffer);
        Skybox skybox=new Skybox.Builder().build(modelViewer.getEngine());
        modelViewer.getScene().setSkybox(skybox);

    }

    Choreographer.FrameCallback frameCallback=new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            choreographer.postFrameCallback(frameCallback);
            Log.d("####", "####1");
            if(modelViewer!=null){
                modelViewer.render(frameTimeNanos);
            }
        }
    };

    public void loadGlb(Buffer buffer){
        modelViewer.destroyModel();
        modelViewer.loadModelGlb(buffer);
        modelViewer.transformToUnitCube(float3);
        loadStartTime=System.nanoTime();
        loadStartFence=modelViewer.getEngine().createFence();
    }

    public void postFrameCallback(){
        choreographer.postFrameCallback(frameCallback);
    }

    public void removeFrameCallback(){
        choreographer.removeFrameCallback(frameCallback);
    }

    public void setupFilament(){
        viewerContent.view=modelViewer.getView();
        viewerContent.sunlight=modelViewer.getLight();
        viewerContent.lightManager=modelViewer.getEngine().getLightManager();
        viewerContent.scene=modelViewer.getScene();
        viewerContent.renderer=modelViewer.getRenderer();

    }

    public void executeTask(String url,SurfaceView surfaceView,boolean onTouch,Buffer buffer) throws ExecutionException, InterruptedException, IOException, URISyntaxException {
        MyAsyncTask myAsyncTask=new MyAsyncTask();
        if(buffer==null){
            myAsyncTask.execute(url);
            bufferToFilament = myAsyncTask.get();
            initFilament(surfaceView,bufferToFilament,onTouch);
        }else{
            initFilament(surfaceView,buffer,onTouch);
        }
    }

    public byte[] getBytes( URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream ());
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

    public class MyAsyncTask extends AsyncTask<String, Integer, Buffer> {
        @Override
        protected Buffer doInBackground(String... parameter) {
            try {
                uri = new URI(parameter[0]);
                buffer = getBytes(uri.toURL());
                buffer1= ByteBuffer.wrap(buffer);
            } catch (URISyntaxException | MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

    }

}
