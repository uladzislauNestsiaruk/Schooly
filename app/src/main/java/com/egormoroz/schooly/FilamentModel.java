package com.egormoroz.schooly;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class FilamentModel {

    UiHelper uiHelper;
    Engine engine;
    Manipulator cameraManipulator;
    ModelViewer modelViewer;
    Choreographer choreographer;
    GestureDetector doubleTapDetector;
    AutomationEngine.ViewerContent viewerContent=new AutomationEngine.ViewerContent();
    Float3 float3=new Float3(0.0f, 0.0f, -4.0f);
    long loadStartTime;
    Fence loadStartFence;

    public void initFilament(SurfaceView surfaceView,Buffer buffer) throws IOException, URISyntaxException {
        Filament.init();
        Gltfio.init();
        Utils.INSTANCE.init();
        choreographer=Choreographer.getInstance();
        cameraManipulator=new Manipulator.Builder()
                .targetPosition(0.0f, 0.0f, -4.0f)
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
                return true;
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
            modelViewer.render(frameTimeNanos);
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

}
