package com.egormoroz.schooly.ui.Model;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.egormoroz.schooly.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class SceneViewModelActivity extends AppCompatActivity {
    private Uri loadUrl;
    private SceneView backgroundSceneView;
    private SceneView transparentSceneView;

    public SceneViewModelActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_view_model);
        loadUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2FSciFiHelmet.gltf?alt=media&token=a82512c1-14bf-4faf-8f67-abeb70da7697");
        loadModels(loadUrl, backgroundSceneView);
        backgroundSceneView = findViewById(R.id.backgroundSceneView);
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSceneView.pause();

    }
    @Override
    public void onResume() {
        super.onResume();
        try {
          //  loadModels(loadUrl, backgroundSceneView);
            backgroundSceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadModels(Uri url, SceneView sceneView) {
        ModelRenderable modelRenderable = null;

        modelRenderable.builder()
                .setSource(
                        this, new RenderableSource.Builder().setSource(
                                this,
                                url,
                                RenderableSource.SourceType.GLTF2
                        ).setScale(0.5f)
                                .setRecenterMode(RenderableSource.RecenterMode.CENTER)
                                .build()
                )
                .setRegistryId(url)
                .build()
                .thenAccept(new Consumer<ModelRenderable>() {
                    @Override
                    public void accept(ModelRenderable modelRenderable) {
                        addNode(modelRenderable, sceneView);
                    }
                });
    }

    public void addNode(ModelRenderable modelRenderable, SceneView sceneView) {
        Node modelNode1 = new Node();
        modelNode1.setRenderable(modelRenderable);
        modelNode1.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));
        modelNode1.setLocalRotation(Quaternion.multiply(
                Quaternion.axisAngle(new Vector3(1f, 0f, 0f), 45),
                Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 75)));
        modelNode1.setLocalPosition(new Vector3(0f, 0f, -1.0f));
        sceneView.getScene().addChild(modelNode1);
    }


}