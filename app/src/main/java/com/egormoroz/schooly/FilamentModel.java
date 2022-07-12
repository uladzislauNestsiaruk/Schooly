package com.egormoroz.schooly;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.SurfaceView;


import androidx.core.view.MotionEventCompat;

import com.google.android.filament.Colors;
import com.google.android.filament.Engine;
import com.google.android.filament.EntityInstance;
import com.google.android.filament.EntityManager;
import com.google.android.filament.Fence;
import com.google.android.filament.Filament;
import com.google.android.filament.LightManager;
import com.google.android.filament.Skybox;
import com.google.android.filament.TransformManager;
import com.google.android.filament.android.UiHelper;
import com.google.android.filament.gltfio.Animator;
import com.google.android.filament.gltfio.AssetLoader;
import com.google.android.filament.gltfio.FilamentAsset;
import com.google.android.filament.gltfio.FilamentInstance;
import com.google.android.filament.gltfio.Gltfio;
import com.google.android.filament.gltfio.MaterialProvider;
import com.google.android.filament.gltfio.ResourceLoader;
import com.google.android.filament.gltfio.UbershaderLoader;
import com.google.android.filament.utils.AutomationEngine;
import com.google.android.filament.utils.Float3;
import com.google.android.filament.utils.GestureDetector;
import com.google.android.filament.utils.Manipulator;
import com.google.android.filament.utils.Utils;
import com.egormoroz.schooly.ModelViewer;

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
    Float3 float3=new Float3(0.0f, 10.0f, 0.0f);
    Float3  float31=new Float3(-0.1f, 0.1f, 0.0f);
    long loadStartTime;
    Fence loadStartFence;
    byte[] buffer;
    FilamentAsset filamentAsset1;
    URI uri;
    TransformManager transformManager;
    boolean normalizeSkinningWeights = true;
    boolean recomputeBoundingBoxes = false;
    boolean ignoreBindTransform = false;
    Buffer buffer1,bufferToFilament;
    FilamentAsset filamentAsset;
    int a;
    int b=0;

    public void initFilament(SurfaceView surfaceView,Buffer buffer,Buffer buffer1,boolean onTouch
            ,LockableNestedScrollView lockableNestedScrollView,String type
    ,boolean transform) throws IOException, URISyntaxException {
        Filament.init();
        Gltfio.init();
        Utils.INSTANCE.init();
        cameraManipulator=new Manipulator.Builder()
                .targetPosition(0.0f, 10.0f, 0.0f)
                .orbitHomePosition(0.0f, 10.0f, 28.0f)
                .viewport(surfaceView.getWidth(), surfaceView.getHeight())
                .zoomSpeed(0.07f)
                .build(Manipulator.Mode.ORBIT);
        doubleTapDetector=new GestureDetector(surfaceView, cameraManipulator);
        uiHelper=new UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK);
        engine=Engine.create();
        modelViewer=new ModelViewer(surfaceView, engine, uiHelper, cameraManipulator);
        setupFilament();
        surfaceView.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, MotionEvent event) {
                modelViewer.onTouchEvent(event);
                doubleTapDetector.onTouchEvent(event);
                if(lockableNestedScrollView!=null){
                    int action = MotionEventCompat.getActionMasked(event);
                    switch(action) {
                        case (MotionEvent.ACTION_DOWN) :
                            lockableNestedScrollView.setScrollingEnabled(false);
                            return true;
                        case (MotionEvent.ACTION_MOVE) :
                            lockableNestedScrollView.setScrollingEnabled(false);
                            return true;
                        case (MotionEvent.ACTION_UP) :
                            lockableNestedScrollView.setScrollingEnabled(true);
                            return true;
                        case (MotionEvent.ACTION_CANCEL) :
                            lockableNestedScrollView.setScrollingEnabled(true);
                            return true;
                        case (MotionEvent.ACTION_OUTSIDE) :
                            lockableNestedScrollView.setScrollingEnabled(true);
                            return true;
                        case (MotionEvent.ACTION_SCROLL) :
                            lockableNestedScrollView.setScrollingEnabled(true);
                            return true;
                    }
                }
                return onTouch;
            }
        });
        loadGlb(buffer,buffer1,true);
        Skybox skybox=new Skybox.Builder()
                .color(0.255f, 0.124f, 0.232f, 1.0f)
                .build(modelViewer.getEngine());
        if(type.equals("regularRender")){
            loadPointLights();
        }else if(type.equals("looksRecycler")){
            loadDefaultLight();
        }
        modelViewer.getScene().setSkybox(skybox);

    }

    public void loadDefaultLight(){
        int light=EntityManager.get().create();
        float[] float1 = Colors.cct(5_500.0f);
        new LightManager.Builder(LightManager.Type.DIRECTIONAL)
                .color(float1[0], float1[1], float1[2])
                .intensity(120_000.0f)
                .direction(0.0f, -0.5f, -1.0f)
                .castShadows(true)
                .build(engine, light);
        modelViewer.getScene().addEntity(light);
    }

    public void loadPointLights(){
        int light=EntityManager.get().create();
        float[] float1 = Colors.cct(5_500.0f);
        new LightManager.Builder(LightManager.Type.DIRECTIONAL)
                .color(float1[0], float1[1], float1[2])
                .intensity(100_000.0f)
                .direction(0.0f, 0.0f, -1.0f)
                .castShadows(true)
                .build(engine, light);
        int light1=EntityManager.get().create();
        new LightManager.Builder(LightManager.Type.POINT)
                .color(float1[0], float1[1], float1[2])
                .intensity(19_000_000.0f)
                .falloff(12)
                .position(-4.0f, 0.0f, -5.0f)
                .castShadows(true)
                .build(engine, light1);
        int light2=EntityManager.get().create();
        new LightManager.Builder(LightManager.Type.POINT)
                .color(float1[0], float1[1], float1[2])
                .intensity(55_000_000.0f)
                .falloff(14)
                .position(4.0f, -0.7f, -8.0f)
                .castShadows(true)
                .build(engine, light2);
        int light3=EntityManager.get().create();
        new LightManager.Builder(LightManager.Type.POINT)
                .color(float1[0], float1[1], float1[2])
                .intensity(7_000_000.0f)
                .falloff(5)
                .position(0.0f, 4.0f, -3.0f)
                .castShadows(true)
                .build(engine, light3);
        int[] lights={light,light1,light2,light3};
        modelViewer.getScene().addEntities(lights);
    }

    Choreographer.FrameCallback frameCallback=new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            choreographer.postFrameCallback(frameCallback);
            if(modelViewer!=null){
                modelViewer.render(frameTimeNanos,filamentAsset);
            }
        }
    };

    public void loadGlb(Buffer buffer,Buffer buffer1,boolean transform){
        MaterialProvider materialProvider=new UbershaderLoader(engine);
        AssetLoader assetLoader=new AssetLoader(engine,materialProvider,EntityManager.get());
        filamentAsset=assetLoader.createAssetFromBinary(buffer);
//        FilamentInstance[] instances={assetLoader.createInstance(filamentAsset)};
//        filamentAsset=assetLoader.createInstancedAsset(buffer1, instances[0]);
        ResourceLoader resourceLoader=new ResourceLoader(engine, normalizeSkinningWeights, recomputeBoundingBoxes, ignoreBindTransform);
        resourceLoader.asyncBeginLoad(filamentAsset);
        Animator animator= filamentAsset.getAnimator();
        filamentAsset.releaseSourceData();
        //modelViewer.transformToUnitCube(float3,filamentAsset);
        modelViewer.getScene().addEntities(filamentAsset.getEntities());
        loadStartTime=System.nanoTime();
        loadStartFence=modelViewer.getEngine().createFence();
    }

    public void populateScene(Buffer buffer){
        FilamentAsset filamentAsset1=filamentAsset;
        MaterialProvider materialProvider=new UbershaderLoader(engine);
        AssetLoader assetLoader=new AssetLoader(engine,materialProvider,EntityManager.get());
        FilamentAsset filamentAsset=assetLoader.createAssetFromBinary(buffer);
        ResourceLoader resourceLoader=new ResourceLoader(engine, normalizeSkinningWeights, recomputeBoundingBoxes, ignoreBindTransform);
        resourceLoader.asyncBeginLoad(filamentAsset);
        filamentAsset.releaseSourceData();
        resourceLoader.asyncUpdateLoad();
        modelViewer.populateScene(filamentAsset);
        int[] entities=new int[1];
        entities=filamentAsset.getEntities();
        Log.d("###", "gg "+filamentAsset.getEntities());
        TransformManager tm=engine.getTransformManager();
        tm.create(entities[0]);
        //float[] f={-9.0f,0.0f,0.0f,-9.0f,0.0f,0.0f,-9.0f,0.0f,0.0f,-9.0f,0.0f,0.0f,0.0f,0.0f,-9.0f,0.0f,0.0f};
        //tm.setTransform(tm.getInstance(filamentAsset.getRoot()), f);

//        tm.create(entities[0],parent, new float[1] );
        modelViewer.transformToUnitCube(float31,filamentAsset);
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

    public void executeTask(String url, SurfaceView surfaceView, boolean onTouch, Buffer buffer,Buffer buffer1,LockableNestedScrollView lockableNestedScrollView
                            , String type, boolean transform
    ) throws ExecutionException, InterruptedException, IOException, URISyntaxException {
        MyAsyncTask myAsyncTask=new MyAsyncTask();
        if(buffer==null){
            myAsyncTask.execute(url);
            bufferToFilament = myAsyncTask.get();
            initFilament(surfaceView,bufferToFilament,buffer1,onTouch,lockableNestedScrollView,type,transform);
        }else{
            initFilament(surfaceView,buffer,buffer1,onTouch,lockableNestedScrollView,type,transform);
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
