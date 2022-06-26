package com.egormoroz.schooly;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.Choreographer;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.google.android.filament.Camera;
import com.google.android.filament.Colors;
import com.google.android.filament.Engine;
import com.google.android.filament.EntityManager;
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

    Choreographer choreographer;
    DisplayHelper displayHelper;
    UiHelper uiHelper;
    Engine engine;
    Renderer renderer;
    Scene scene;
    View view;
    Camera camera;
    int light;
    float[] color;
    InputStream inputStream;
    byte[] buffer;
    FilamentAsset filamentAsset;
    SurfaceHolder surfaceHolder;
    SwapChain swapChain;

    public void initFilament(SurfaceView surfaceView,Activity activity) throws IOException, URISyntaxException {
        Filament.init();
        Gltfio.init();
        choreographer=Choreographer.getInstance();
        displayHelper=new DisplayHelper(surfaceView.getContext());
        setupSurfaceView(surfaceView);
        setupFilament(surfaceView);
        setupView();
        setupScene();
    }

    public void setupSurfaceView(SurfaceView surfaceView){
        uiHelper=new UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK);
        uiHelper.setRenderCallback(new UiHelper.RendererCallback() {
            @Override
            public void onNativeWindowChanged(Surface surface) {

            }

            @Override
            public void onDetachedFromSurface() {

            }

            @Override
            public void onResized(int i, int i1) {

            }
        });
        uiHelper.attachTo(surfaceView);
    }

    public void setupFilament(SurfaceView surfaceView){
        engine=Engine.create();
        renderer=engine.createRenderer();
        scene=engine.createScene();
        view=engine.createView();
        camera=engine.createCamera(EntityManager.get().create());
        surfaceHolder=surfaceView.getHolder();
        Surface surface=surfaceHolder.getSurface();
        swapChain= engine.createSwapChain(surface);
    }

    public void setupView(){
        scene.setSkybox(new Skybox.Builder().color(0.035f, 0.035f, 0.035f, 1.0f).build(engine));
        view.setCamera(camera);
        view.setScene(scene);
    }

    public void setupScene() throws IOException, URISyntaxException {
        Log.d("####", "init  "+view+"   "+scene+"   "+camera+"   "+scene.getSkybox());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("3d models");
        StorageReference islandRef = storageReference.child("untitled.glb");
        islandRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri uri1=task.getResult();
                    Log.d("####", "fgg "+uri1);
                    URI uri= null;
                    try {
                        uri = new URI(uri1.toString());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    try {
                        buffer = getBytes(uri.toURL());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Buffer buffer1= ByteBuffer.wrap(buffer);
                    MaterialProvider materialProvider=new UbershaderLoader(engine);
                    AssetLoader assetLoader=new AssetLoader(engine, materialProvider, EntityManager.get());
                    ResourceLoader resourceLoader=new ResourceLoader(engine);
                    filamentAsset=assetLoader.createAssetFromBinary(buffer1);
                    resourceLoader.addResourceData("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7", buffer1);
                    resourceLoader.loadResources(filamentAsset);
                    resourceLoader.destroy();
                    filamentAsset.releaseSourceData();
                    scene.addEntities(filamentAsset.getEntities());
                    light=EntityManager.get().create();
                    color=Colors.cct(5_500.0f);
                    new LightManager.Builder(LightManager.Type.DIRECTIONAL)
                            .color(color[0],color[1],color[2])
                            .intensity(110_000.0f)
                            .direction(0.0f, -0.5f, -1.0f)
                            .castShadows(true)
                            .build(engine, light);
                    scene.addEntity(light);
                    camera.setExposure(16.0f, 1.0f / 125.0f, 100.0f);
                    camera.lookAt(0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
                    if(renderer.beginFrame(swapChain,1000)) {
                        renderer.render(view);
                        renderer.endFrame();
                    }
                }
            }
        });
    }

    public byte[] getBytes( URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream ());
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace ();
            // Perform any other exception handling that's appropriate.
        }
        finally {
            if (is != null) { is.close(); }
        }
        return  baos.toByteArray();
    }
}
