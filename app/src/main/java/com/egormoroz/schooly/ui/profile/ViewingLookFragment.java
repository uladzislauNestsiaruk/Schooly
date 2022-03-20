package com.egormoroz.schooly.ui.profile;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.MyClothes.CreateClothesFragment;
import com.egormoroz.schooly.ui.main.MyClothes.CriteriaFragment;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import java.util.ArrayList;
import java.util.function.Consumer;

public class ViewingLookFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView back,like,comment,send,schoolyCoin,cross;
    TextView nick,description,likesCount,lookPrice,lookPriceDollar,clothesCreator,emptyList;
    SceneView sceneView;
    EditText editText;
    RecyclerView recyclerView;
    String userNameToProfile,userName;
    String likesCountString,lookPriceString,lookPriceDollarString;


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
                likesCountString=String.valueOf(newsItem.getLikes_count());
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialog();
                    }
                });
                long likesCountLong=Long.valueOf(newsItem.getLikes_count());
                if(likesCountLong<1000){
                    likesCount.setText(String.valueOf(likesCountLong));
                }else if(likesCountLong>1000 && likesCountLong<10000){
                    likesCount.setText(likesCountString.substring(0, 1)+"."+likesCountString.substring(1, 2)+"K");
                }
                else if(likesCountLong>10000 && likesCountLong<100000){
                    likesCount.setText(likesCountString.substring(0, 2)+"."+likesCountString.substring(2,3)+"K");
                }
                else if(likesCountLong>10000 && likesCountLong<100000){
                    likesCount.setText(likesCountString.substring(0, 2)+"."+likesCountString.substring(2,3)+"K");
                }else if(likesCountLong>100000 && likesCountLong<1000000){
                    likesCount.setText(likesCountString.substring(0, 3)+"K");
                }
                else if(likesCountLong>1000000 && likesCountLong<10000000){
                    likesCount.setText(likesCountString.substring(0, 1)+"KK");
                }
                else if(likesCountLong>10000000 && likesCountLong<100000000){
                    likesCount.setText(likesCountString.substring(0, 2)+"KK");
                }
                description.setText(newsItem.getItem_description());
                if (newsItem.getLookPrice()==0) {
                    lookPrice.setVisibility(View.GONE);
                    lookPriceDollarString=String.valueOf(newsItem.getLookPriceDollar());
                    if(newsItem.getLookPriceDollar()<1000){
                        lookPriceDollar.setText(lookPriceDollarString+"$");
                    }else if(newsItem.getLookPriceDollar()>1000 && newsItem.getLookPriceDollar()<10000){
                        lookPriceDollar.setText(lookPriceDollarString.substring(0, 1)+"."+lookPriceDollarString.substring(1, 2)+"K"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>10000 && newsItem.getLookPriceDollar()<100000){
                        lookPriceDollar.setText(lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>10000 && newsItem.getLookPriceDollar()<100000){
                        lookPriceDollar.setText(lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
                    }else if(newsItem.getLookPriceDollar()>100000 && newsItem.getLookPriceDollar()<1000000){
                        lookPriceDollar.setText(lookPriceDollarString.substring(0, 3)+"K"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>1000000 && newsItem.getLookPriceDollar()<10000000){
                        lookPriceDollar.setText(lookPriceDollarString.substring(0, 1)+"KK"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>10000000 && newsItem.getLookPriceDollar()<100000000){
                        lookPriceDollar.setText(lookPriceDollarString.substring(0, 2)+"KK"+"$");
                    }
                }else{
                    lookPriceDollarString=String.valueOf(newsItem.getLookPriceDollar());
                    if(newsItem.getLookPriceDollar()<1000){
                        lookPriceDollar.setText(" + "+lookPriceDollarString+"$");
                    }else if(newsItem.getLookPriceDollar()>1000 && newsItem.getLookPriceDollar()<10000){
                        lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 1)+"."+lookPriceDollarString.substring(1, 2)+"K"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>10000 && newsItem.getLookPriceDollar()<100000){
                        lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>10000 && newsItem.getLookPriceDollar()<100000){
                        lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
                    }else if(newsItem.getLookPriceDollar()>100000 && newsItem.getLookPriceDollar()<1000000){
                        lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 3)+"K"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>1000000 && newsItem.getLookPriceDollar()<10000000){
                        lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 1)+"KK"+"$");
                    }
                    else if(newsItem.getLookPriceDollar()>10000000 && newsItem.getLookPriceDollar()<100000000){
                        lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"KK"+"$");
                    }
                }
                if (newsItem.getLookPriceDollar()==0){
                    lookPriceDollar.setVisibility(View.GONE);
                }
                if(newsItem.getLookPrice()==0){
                    schoolyCoin.setVisibility(View.GONE);
                    lookPrice.setVisibility(View.GONE);
                }else {
                    lookPriceString=String.valueOf(newsItem.getLookPrice());
                    if(newsItem.getLookPrice()<1000){
                        lookPrice.setText(String.valueOf(lookPriceString));
                    }else if(newsItem.getLookPrice()>1000 && newsItem.getLookPrice()<10000){
                        lookPrice.setText(lookPriceString.substring(0, 1)+"."+lookPriceString.substring(1, 2)+"K");
                    }
                    else if(newsItem.getLookPrice()>10000 && newsItem.getLookPrice()<100000){
                        lookPrice.setText(lookPriceString.substring(0, 2)+"."+lookPriceString.substring(2,3)+"K");
                    }
                    else if(newsItem.getLookPrice()>10000 && newsItem.getLookPrice()<100000){
                        lookPrice.setText(lookPriceString.substring(0, 2)+"."+lookPriceString.substring(2,3)+"K");
                    }else if(newsItem.getLookPrice()>100000 && newsItem.getLookPrice()<1000000){
                        lookPrice.setText(lookPriceString.substring(0, 3)+"K");
                    }
                    else if(newsItem.getLookPrice()>1000000 && newsItem.getLookPrice()<10000000){
                        lookPrice.setText(lookPriceString.substring(0, 1)+"KK");
                    }
                    else if(newsItem.getLookPrice()>10000000 && newsItem.getLookPrice()<100000000){
                        lookPrice.setText(lookPriceString.substring(0, 2)+"KK");
                    }
                }
            }
        });
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        editText=bottomSheetDialog.findViewById(R.id.searchuser);
        recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cross=bottomSheetDialog.findViewById(R.id.cross);
        emptyList=bottomSheetDialog.findViewById(R.id.emptySubscribersList);

        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.getSubscriptionList(nick, firebaseModel, new Callbacks.getFriendsList() {
                    @Override
                    public void getFriendsList(ArrayList<Subscriber> friends) {
                        if (friends.size()==0){
                            emptyList.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            SendLookAdapter sendLookAdapter = new SendLookAdapter(friends);
                            recyclerView.setAdapter(sendLookAdapter);
                        }
                    }
                });
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        initUserEnter();

        bottomSheetDialog.show();
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

    public void initUserEnter() {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        userName = String.valueOf(editText.getText()).trim();
                        userName = userName.toLowerCase();
                        Query query = firebaseModel.getUsersReference().child(nick).child("subscription");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Subscriber> userFromBase = new ArrayList<>();
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    Subscriber subscriber = new Subscriber();
                                    subscriber.setSub(snap.getValue(String.class));
                                    String nick = subscriber.getSub();
                                    int valueLetters = userName.length();
                                    nick = nick.toLowerCase();
                                    if (nick.length() < valueLetters) {
                                        if (nick.equals(userName))
                                            userFromBase.add(subscriber);
                                    } else {
                                        nick = nick.substring(0, valueLetters);
                                        if (nick.equals(userName))
                                            userFromBase.add(subscriber);
                                    }

                                }
                                SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase);
                                recyclerView.setAdapter(sendLookAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                            }
                        });
                    }
                });
            }
        });
    }
}
