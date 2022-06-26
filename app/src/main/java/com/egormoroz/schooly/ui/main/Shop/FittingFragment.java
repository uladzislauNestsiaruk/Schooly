package com.egormoroz.schooly.ui.main.Shop;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.google.android.filament.Box;
import com.google.android.filament.Camera;
import com.google.android.filament.Engine;
import com.google.android.filament.EntityManager;
import com.google.android.filament.Filament;
import com.google.android.filament.IndexBuffer;
import com.google.android.filament.LightManager;
import com.google.android.filament.Material;
import com.google.android.filament.MaterialInstance;
import com.google.android.filament.RenderableManager;
import com.google.android.filament.Renderer;
import com.google.android.filament.Scene;
import com.google.android.filament.SwapChain;
import com.google.android.filament.VertexBuffer;
import com.google.android.filament.android.UiHelper;
import com.google.android.filament.gltfio.AssetLoader;
import com.google.android.filament.gltfio.ResourceLoader;
import com.google.android.filament.gltfio.FilamentAsset;
import com.google.android.filament.gltfio.Gltfio;
import com.google.android.filament.gltfio.MaterialProvider;
import com.google.android.filament.gltfio.UbershaderLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;

public class FittingFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView back;
    UserInformation userInformation;
    Bundle bundle;
    Fragment fragment;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    AssetManager assetManager;
    FilamentAsset filamentAsset;
    InputStream inputStream;
    IndexBuffer indexBuffer;
    VertexBuffer vertexBuffer;
    Material material;
    MaterialInstance materialInstance;
    byte[] buffer;

    public FittingFragment(Fragment fragment,UserInformation userInformation,Bundle bundle) {
        this.fragment = fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static FittingFragment newInstance(Fragment fragment, UserInformation userInformation,Bundle bundle) {
        return new FittingFragment(fragment,userInformation,bundle);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fitting, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        back=view.findViewById(R.id.backToViewingClothes);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        surfaceView=view.findViewById(R.id.surfaceView);
        FilamentModel filamentModel=new FilamentModel();
        try {
            filamentModel.initFilament(surfaceView,getActivity());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
//        try {
//            Engine engine=Engine.create();
//            MaterialProvider materialProvider=new UbershaderLoader(engine);
//            surfaceHolder=surfaceView.getHolder();
//            Surface surface=surfaceHolder.getSurface();
//            SwapChain swapChain= engine.createSwapChain(surface);
////            Renderer renderer=engine.createRenderer();
//            inputStream = getActivity().getContentResolver().openInputStream(Uri.parse("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Funtitled.glb?alt=media&token=657b45d7-a84b-4f2a-89f4-a699029401f7"));
//            buffer = getBytes(inputStream);
//            Buffer buffer1=ByteBuffer.wrap(buffer);
//            Scene scene=engine.createScene();
////            indexBuffer.setBuffer(engine, buffer1);
////            vertexBuffer.setBufferAt(engine, buffer1.position(), buffer1);
////            material=new Material.Builder().build(engine);
////            materialInstance=material.createInstance();
////            new RenderableManager.Builder(1)
////                    .boundingBox(new Box(0.0f, 0.0f, 0.0f, 9000.0f, 9000.0f, 9000.0f))
////                    .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vertexBuffer, indexBuffer,0,6*6)
////                    .material(0, materialInstance)
////                    .build(engine,renderable);
//            int sun=EntityManager.get().create();
//            new LightManager.Builder(LightManager.Type.SUN)
//                    .castShadows(true)
//                    .build(engine,sun);
//            scene.addEntity(sun);
//            Camera camera=engine.createCamera(EntityManager.get().create());
//            camera.setProjection(45, 16.0/9.0, 0.1, 1.0, Camera.Fov.VERTICAL);
//            camera.lookAt(0, 1.60, 1, 0, 0, 0, 0, 1, 0);
//            com.google.android.filament.View view1=engine.createView();
//            view1.setScene(scene);
//            if (renderer.beginFrame(swapChain,10000000)){}{
//                renderer.render(view1);
//                renderer.endFrame();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
