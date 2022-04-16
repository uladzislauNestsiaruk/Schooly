package com.egormoroz.schooly.ui.main.Nontifications;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.ClothesRequest;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.MyClothes.CreateClothesFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.FittingFragment;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.function.Consumer;

public class ClothesRequestFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView back;
    TextView clothesTitleCV,clothesPrice,clothesType,result,reason,addReason,reasonText,addReasonText;
    SceneView sceneView;
    ImageView clothesImageCV, coinsImage;

    Fragment fragment;
    String clothesUid;

    public ClothesRequestFragment(Fragment fragment,String clothesUid) {
        this.fragment = fragment;
        this.clothesUid=clothesUid;
    }

    public static ClothesRequestFragment newInstance(Fragment fragment,String clothesUid) {
        return new ClothesRequestFragment(fragment,clothesUid);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_clothesrequest, container, false);
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

        sceneView=view.findViewById(R.id.sceneView);
        clothesType=view.findViewById(R.id.clothesType);
        reason=view.findViewById(R.id.reason);
        result=view.findViewById(R.id.result);
        addReason=view.findViewById(R.id.add);
        reasonText=view.findViewById(R.id.reasonText);
        addReasonText=view.findViewById(R.id.addText);
        clothesImageCV = view.findViewById(R.id.clothesImagecv);
        coinsImage = view.findViewById(R.id.coinsImage);
        clothesTitleCV = view.findViewById(R.id.clothesTitlecv);
        clothesPrice=view.findViewById(R.id.clothesPricecv);
        back=view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick)
                        .child("clothesRequest").child(clothesUid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ClothesRequest clothesRequest=new ClothesRequest();
                        clothesRequest.setClothesImage(snapshot.child("clothesImage").getValue(String.class));
                        clothesRequest.setClothesPrice(snapshot.child("clothesPrice").getValue(Long.class));
                        clothesRequest.setClothesType(snapshot.child("clothesType").getValue(String.class));
                        clothesRequest.setClothesTitle(snapshot.child("clothesTitle").getValue(String.class));
                        clothesRequest.setCreator(snapshot.child("creator").getValue(String.class));
                        clothesRequest.setCurrencyType(snapshot.child("currencyType").getValue(String.class));
                        clothesRequest.setDescription(snapshot.child("description").getValue(String.class));
                        clothesRequest.setModel(snapshot.child("model").getValue(String.class));
                        clothesRequest.setBodyType(snapshot.child("bodyType").getValue(String.class));
                        clothesRequest.setReason(snapshot.child("reason").getValue(String.class));
                        clothesRequest.setResult(snapshot.child("result").getValue(String.class));
                        clothesRequest.setReasonDescription(snapshot.child("reasonDescription").getValue(String.class));
                        clothesRequest.setUid(snapshot.child("uid").getValue(String.class));
                        clothesTitleCV.setText(clothesRequest.getClothesTitle());
                        Picasso.get().load(clothesRequest.getClothesImage()).into(clothesImageCV);
                        clothesPrice.setText(String.valueOf(clothesRequest.getClothesPrice()));
                        clothesType.setText(clothesRequest.getClothesType());
                        loadModels(Uri.parse(clothesRequest.getModel()), sceneView, ClothesRequestFragment.this, 0.25f);
                        if(clothesRequest.getResult().equals("okey")){
                            result.setTextColor(Color.parseColor("#53B35C"));
                            result.setText("Модель добавлена в магазин одежды Schooly");
                        }else {
                            result.setTextColor(Color.parseColor("#EA4646"));
                            result.setText("Запрос на добавление отказан");
                            reason.setVisibility(View.VISIBLE);
                            reasonText.setVisibility(View.VISIBLE);
                            addReason.setVisibility(View.VISIBLE);
                            addReasonText.setVisibility(View.VISIBLE);
                            reason.setText(clothesRequest.getReason());
                            addReason.setText(clothesRequest.getReasonDescription());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
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
