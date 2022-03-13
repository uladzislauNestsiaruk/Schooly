package com.egormoroz.schooly.ui.profile;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MyClothes.CreateClothesFragment;
import com.egormoroz.schooly.ui.main.MyClothes.CriteriaFragment;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.function.Consumer;

public class ViewingLookFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView back,like,comment,send,schoolyCoin;
    TextView nick,description,likesCount,lookPrice,lookPriceDollar,clothesCreator;
    SceneView sceneView;


    Fragment fragment;

    public ViewingLookFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public static ViewingLookFragment newInstance(Fragment fragment) {
        return new ViewingLookFragment(fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewinglook, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
//        AppBarLayout abl = getActivity().findViewById(R.id.AppBarLayout);
//        abl.setVisibility(abl.GONE);
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        back=view.findViewById(R.id.back);
        like=view.findViewById(R.id.like);
        comment=view.findViewById(R.id.comment);
        description=view.findViewById(R.id.description);
        schoolyCoin=view.findViewById(R.id.schoolyCoin);
        clothesCreator=view.findViewById(R.id.clothesCreator);
        clothesCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        likesCount=view.findViewById(R.id.likesCount);
        sceneView=view.findViewById(R.id.sceneView);
        lookPrice=view.findViewById(R.id.lookPrice);
        lookPriceDollar=view.findViewById(R.id.lookPriceDollar);
        nick=view.findViewById(R.id.nick);
        send=view.findViewById(R.id.send);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment,getActivity());
            }
        });
        LooksAdapter.lookInfo(new LooksAdapter.ItemClickListener() {
            @Override
            public void onItemClick(NewsItem newsItem) {
                loadModels(Uri.parse(newsItem.getImageUrl()), sceneView, ViewingLookFragment.this, 0.25f);
                nick.setText(newsItem.getNick());
                likesCount.setText(newsItem.getLikes_count());
                description.setText(newsItem.getItem_description());
                if (newsItem.getLookPriceDollar()==0){
                    lookPriceDollar.setVisibility(View.GONE);
                }else{
                    lookPriceDollar.setText(" + "+String.valueOf(newsItem.getLookPriceDollar())+"$");
                }
                if(newsItem.getLookPrice()==0){
                    schoolyCoin.setVisibility(View.GONE);
                    lookPrice.setVisibility(View.GONE);
                }else {
                    lookPrice.setText(String.valueOf(newsItem.getLookPrice()));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadModels(Uri url, SceneView sceneView, Fragment fragment, float scale) {
        ModelRenderable.builder()
                .setSource(
                        fragment.getContext(), new RenderableSource.Builder().setSource(
                                fragment.getContext(),
                                url,
                                RenderableSource.SourceType.GLB
                        ).setScale(scale)
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
//        modelNode1.setLocalRotation(Quaternion.multiply(
//                Quaternion.axisAngle(new Vector3(1f, 0f, 0f), 45),
//                Quaternion.axisAngle(new Vector3(0f, 1f, 0f), 75)));
        modelNode1.setLocalPosition(new Vector3(0f, 0f, -0.9f));
        sceneView.getScene().addChild(modelNode1);
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
