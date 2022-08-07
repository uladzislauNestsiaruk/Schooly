package com.egormoroz.schooly.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.Comment;
import com.egormoroz.schooly.ui.news.CommentAdapter;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.news.ViewingClothesNews;
import com.egormoroz.schooly.ui.people.PeopleFragment;
import com.egormoroz.schooly.ui.profile.Wardrobe.AcceptNewLook;
import com.egormoroz.schooly.ui.profile.Wardrobe.ConstituentsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import javax.security.auth.callback.Callback;

public class ViewingLookFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    ImageView back,like,comment,send,schoolyCoin,options;
    TextView nickView,description,likesCount,lookPrice,lookPriceDollar,clothesCreator
            ,emptyList,comments,sendComment,noComment,save,complain,complainOtherUserText
            ,reasonText;
    //SceneView sceneView;
    LinearLayout linearElse,linearTelegram,linearInstagram;
    EditText editText,messageEdit,addDescriptionEdit;
    RecyclerView clothesCreatorsRecycler,complainRecycler;
    RecyclerView recyclerView;
    ArrayList<Subscriber> userFromBase;
    String likesCountString,lookPriceString,lookPriceDollarString,reasonTextString,descriptionText
            ,userName,otherUserNickString,editGetText,nick;
    SendLookAdapter.ItemClickListener itemClickListener;
    ConstituentsAdapter.ItemClickListener itemClickListenerClothes;
    ComplainAdapter.ItemClickListener itemClickListenerComplain;
    RelativeLayout sendReason;
    UserInformation userInformation;
    Fragment fragment;
    Bundle bundle;

    public ViewingLookFragment(Fragment fragment,UserInformation userInformation,Bundle bundle) {
        this.fragment = fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static ViewingLookFragment newInstance(Fragment fragment,UserInformation userInformation,Bundle bundle) {
        return new ViewingLookFragment(fragment,userInformation,bundle);

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
        nick=userInformation.getNick();
        back=view.findViewById(R.id.back);
        like=view.findViewById(R.id.like);
        comment=view.findViewById(R.id.comment);
        description=view.findViewById(R.id.description);
        schoolyCoin=view.findViewById(R.id.schoolyCoin);
        clothesCreator=view.findViewById(R.id.clothesCreator);
        likesCount=view.findViewById(R.id.likesCount);
        lookPrice=view.findViewById(R.id.lookPrice);
        options=view.findViewById(R.id.options);
        lookPriceDollar=view.findViewById(R.id.lookPriceDollar);
        nickView=view.findViewById(R.id.nick);
        send=view.findViewById(R.id.send);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(fragment,getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                RecentMethods.setCurrentFragment(fragment, getActivity());
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        LooksAdapter.lookInfo(new LooksAdapter.ItemClickListener() {
            @Override
            public void onItemClick(NewsItem newsItem) {
                nickView.setText(newsItem.getNick());
                nickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userNameToProfile=nickView.getText().toString();
                        firebaseModel.getUsersReference().child(userNameToProfile).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.exists()){
                                    Toast.makeText(getContext(), R.string.usernotfound, Toast.LENGTH_SHORT).show();
                                }else {
                                    if(userNameToProfile.equals(nick)){
                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback",nick,ViewingLookFragment.newInstance(fragment, userInformation,bundle),userInformation,bundle),getActivity());
                                    }else {
                                        RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", userNameToProfile,ViewingLookFragment.newInstance(fragment, userInformation,bundle),userInformation,bundle
                                        ), getActivity());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                likesCountString=String.valueOf(newsItem.getLikes_count());
                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialogComments(newsItem);
                    }
                });
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialog();
                    }
                });
                options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialogLookOptions(newsItem);
                    }
                });
                clothesCreator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomSheetDialogClothesCreators(newsItem);
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

    private void showBottomSheetDialogComments(NewsItem newsItem) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_comment);

        editText=bottomSheetDialog.findViewById(R.id.commentEdit);
        recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyList=bottomSheetDialog.findViewById(R.id.emptyCommentsList);
        comments=bottomSheetDialog.findViewById(R.id.comments);
        comments.setText(R.string.comments);
        noComment=bottomSheetDialog.findViewById(R.id.noComment);
        sendComment=bottomSheetDialog.findViewById(R.id.send);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        loadComments(newsItem);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editGetText=editText.getText().toString();
                if (editGetText.length()==0){
                    sendComment.setVisibility(View.GONE);
                }else {
                    sendComment.setVisibility(View.VISIBLE);
                    sendComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String commentId=firebaseModel.getUsersReference().child(newsItem.getNick()).child("looks")
                                    .child(newsItem.getNewsId()).child("comments").push().getKey();
                            firebaseModel.getUsersReference().child(newsItem.getNick()).child("looks")
                                    .child(newsItem.getNewsId()).child("comments").child(commentId)
                                    .setValue(new Comment(editText.getText().toString(), "0", commentId,RecentMethods.getCurrentTime(),nick,"image","comment"));
                            editText.getText().clear();
                            loadComments(newsItem);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bottomSheetDialog.show();
    }

    private void showBottomSheetDialogComplain(NewsItem newsItem) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_complain);

        complainOtherUserText=bottomSheetDialog.findViewById(R.id.complainOtherUserText);
        complainRecycler=bottomSheetDialog.findViewById(R.id.reasonsRecycler);

        complainOtherUserText.setText(newsItem.getNick());

        itemClickListenerComplain=new ComplainAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Reason reason) {
                showBottomSheetDialogComplainToBase(newsItem,reason);
                bottomSheetDialog.dismiss();
            }
        };
        ArrayList<Reason> arrayListReason=new ArrayList<>();
        arrayListReason.add(new Reason(getContext().getResources().getString(R.string.fraud)));
        arrayListReason.add(new Reason(getContext().getResources().getString(R.string.violenceordangerousorganizations)));
        arrayListReason.add(new Reason(getContext().getResources().getString(R.string.hostilesayingsorsymbols)));
        arrayListReason.add(new Reason(getContext().getResources().getString(R.string.saleofillegalgoods)));
        arrayListReason.add(new Reason(getContext().getResources().getString(R.string.violationofintellectualpropertyrights)));
        ComplainAdapter complainAdapter=new ComplainAdapter(arrayListReason,itemClickListenerComplain);
        complainRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        complainRecycler.setAdapter(complainAdapter);

        bottomSheetDialog.show();
    }

    private void showBottomSheetDialogComplainToBase(NewsItem newsItem,Reason reason) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_complaintobase);

        reasonText=bottomSheetDialog.findViewById(R.id.reasonText);
        sendReason=bottomSheetDialog.findViewById(R.id.sendReasons);
        addDescriptionEdit=bottomSheetDialog.findViewById(R.id.addDescriptionEdit);

        reasonTextString=reason.getReason();
        reasonText.setText(reasonTextString);
        sendReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionText=addDescriptionEdit.getText().toString();
                String uid=firebaseModel.getReference().child("AppData").child("complains").push().getKey();
                firebaseModel.getReference().child("AppData").child("complains").child(uid)
                        .setValue(new Complain(newsItem.getNick(),nick, reasonTextString,descriptionText,uid,newsItem));
                Toast.makeText(getContext(), getContext().getResources().getText(R.string.complaintsent), Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }

    public void loadComments(NewsItem newsItem){
        RecentMethods.getCommentsList(newsItem.getNick(), newsItem.getNewsId(), firebaseModel, new Callbacks.getCommentsList() {
            @Override
            public void getCommentsList(ArrayList<Comment> comment) {
                if(comment.size()==0){
                    noComment.setVisibility(View.VISIBLE);
                }else {
                    noComment.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    CommentAdapter commentAdapter = new CommentAdapter(comment, newsItem.getNick(), bundle, newsItem.getNewsId(), userInformation);
                    recyclerView.setAdapter(commentAdapter);
                }
            }
        });
    }

    private void showBottomSheetDialogClothesCreators(NewsItem newsItem) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_clothescreators);
        clothesCreatorsRecycler=bottomSheetDialog.findViewById(R.id.recyclerView);
        RecentMethods.getLookClothes(newsItem.getNick(), newsItem.getNewsId(), firebaseModel, new Callbacks.getLookClothes() {
            @Override
            public void getLookClothes(ArrayList<Clothes> clothesArrayList) {
                ConstituentsAdapter constituentsAdapter=new ConstituentsAdapter(clothesArrayList,itemClickListenerClothes);
                clothesCreatorsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                clothesCreatorsRecycler.setAdapter(constituentsAdapter);
            }
        });
        itemClickListenerClothes=new ConstituentsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                bottomSheetDialog.dismiss();
                RecentMethods.setCurrentFragment(ViewingClothesNews.newInstance(ViewingLookFragment.newInstance(fragment, userInformation,bundle),userInformation,bundle), getActivity());
            }
        };

        bottomSheetDialog.show();
    }

    private void showBottomSheetDialogLookOptions(NewsItem newsItem) {
        if(!newsItem.getNick().equals(nick)){
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_lookoptions);

            save=bottomSheetDialog.findViewById(R.id.save);
            complain=bottomSheetDialog.findViewById(R.id.complain);

            if(userInformation.getSavedLooks().size()>0){
                for(int i=0;i<userInformation.getSavedLooks().size();i++){
                    NewsItem newsItem1=userInformation.getSavedLooks().get(i);
                    if(newsItem1.getNewsId().equals(newsItem.getNewsId())) {
                        save.setText(R.string.dontsave);
                    }
                }
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    DataSnapshot snapshot=task.getResult();
                                    if(snapshot.exists()){
                                        save.setText(R.string.save);
                                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                                .removeValue();
                                        Toast.makeText(getContext(), v.getContext().getResources().getText(R.string.lookwasdeletedfromsaved), Toast.LENGTH_SHORT).show();
                                    }else {
                                        save.setText(R.string.dontsave);
                                        Log.d("####", "####11"+newsItem.getNewsId()+"  "+newsItem.getNewsId());
                                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                                .setValue(newsItem);
                                        Toast.makeText(getContext(), v.getContext().getResources().getText(R.string.looksaved), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                });
            }else {
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                .setValue(newsItem);
                        Toast.makeText(getContext(), v.getContext().getResources().getText(R.string.looksaved), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            complain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialogComplain(newsItem);
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.show();
        }else{
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_lookoptions);

            save=bottomSheetDialog.findViewById(R.id.save);
            complain=bottomSheetDialog.findViewById(R.id.complain);
            complain.setText(R.string.deletelook);

            if(userInformation.getSavedLooks().size()>0){
                for(int i=0;i<userInformation.getSavedLooks().size();i++){
                    NewsItem newsItem1=userInformation.getSavedLooks().get(i);
                    if(newsItem1.getNewsId().equals(newsItem.getNewsId())) {
                        save.setText(R.string.dontsave);
                    }
                }
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    DataSnapshot snapshot=task.getResult();
                                    if(snapshot.exists()){
                                        save.setText(R.string.save);
                                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                                .removeValue();
                                        Toast.makeText(getContext(), v.getContext().getResources().getText(R.string.lookwasdeletedfromsaved), Toast.LENGTH_SHORT).show();
                                    }else {
                                        save.setText(R.string.dontsave);
                                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                                .setValue(newsItem);
                                        Toast.makeText(getContext(), v.getContext().getResources().getText(R.string.looksaved), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                });
            }else {
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseModel.getUsersReference().child(nick).child("saved").child(newsItem.getNewsId())
                                .setValue(newsItem);
                        Toast.makeText(getContext(), v.getContext().getResources().getText(R.string.looksaved), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            complain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseModel.getUsersReference().child(nick).child("looks")
                            .child(newsItem.getNewsId()).removeValue();
                    Toast.makeText(getContext(), R.string.lookwasdeleted, Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.show();
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        editText=bottomSheetDialog.findViewById(R.id.searchuser);
        recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyList=bottomSheetDialog.findViewById(R.id.emptySubscribersList);
        linearElse=bottomSheetDialog.findViewById(R.id.linearElse);
        linearTelegram=bottomSheetDialog.findViewById(R.id.linearTelegram);
        linearInstagram=bottomSheetDialog.findViewById(R.id.linearInstagram);
        messageEdit=bottomSheetDialog.findViewById(R.id.message);

        linearElse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linearTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getBitmapFormView();
                Uri backgroundAssetUri = Uri.parse("your-image-asset-uri-goes-here");
                String sourceApplication = "com.egormoroz.schooly";


                Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");

                intent.setDataAndType(backgroundAssetUri, "image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                Activity activity = getActivity();
                if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
                    activity.startActivityForResult(intent, 0);
                }
            }
        });
        itemClickListener=new SendLookAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String otherUserNick, String type) {
                if(type.equals("send")){
                    String messageText = messageEdit.getText().toString();

                    String messageSenderRef = otherUserNick + "/Chats/" + userInformation.getNick() + "/Messages";
                    String messageReceiverRef = userInformation.getNick()  + "/Chats/" + otherUserNick+ "/Messages";
                    otherUserNickString=otherUserNick;

                    DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child(userInformation.getNick() ).child("Chats").child(otherUserNick).child("Messages").push();
                    String messagePushID = userMessageKeyRef.getKey();

                    Map<String, String> messageTextBody = new HashMap<>();
                    messageTextBody.put("message", messageText);
                    messageTextBody.put("type", "text");
                    messageTextBody.put("from", userInformation.getNick() );
                    messageTextBody.put("to", otherUserNick);
                    messageTextBody.put("time", RecentMethods.getCurrentTime());
                    messageTextBody.put("messageID", messagePushID);
                    addLastMessage("text", messageText);

                    Map<String, Object> messageBodyDetails = new HashMap<String, Object>();
                    messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                    messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);
                }else {
                    Log.d("####", type);
                }
            }
        };
        if(userInformation.getSubscription()==null){
            RecentMethods.getSubscriptionList(userInformation.getNick(), firebaseModel, new Callbacks.getFriendsList() {
                @Override
                public void getFriendsList(ArrayList<Subscriber> friends) {
                    if (friends.size()==0){
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        SendLookAdapter sendLookAdapter = new SendLookAdapter(friends,itemClickListener);
                        recyclerView.setAdapter(sendLookAdapter);
                    }
                }
            });
        }else {
            if (userInformation.getSubscription().size()==0){
                emptyList.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                SendLookAdapter sendLookAdapter = new SendLookAdapter(userInformation.getSubscription(),itemClickListener);
                recyclerView.setAdapter(sendLookAdapter);
            }
        }

        initUserEnter();

        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getBitmapFormView(View view, Activity activity, Callback callback) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        int[] locations = new int[2];
        view.getLocationInWindow(locations);
        Rect rect = new Rect(locations[0], locations[1], locations[0] + view.getWidth(), locations[1] + view.getHeight());


        PixelCopy.request(activity.getWindow(), rect, bitmap, copyResult -> {
            if (copyResult == PixelCopy.SUCCESS) {
                callback.onResult(bitmap);
            }
        }, new Handler(Looper.getMainLooper()));
    }

    public interface Callback<Bitmap> {
        void onResult(Bitmap bitmap);
    }

    public void initUserEnter() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userName = String.valueOf(editText.getText()).trim();
                userName = userName.toLowerCase();
                if(userInformation.getSubscription()==null){
                    Query query = firebaseModel.getUsersReference().child(userInformation.getNick()).child("subscription");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userFromBase = new ArrayList<>();
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
                            if(userFromBase.size()==0){
                                emptyList.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }else {
                                emptyList.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListener);
                                recyclerView.setAdapter(sendLookAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    userFromBase=new ArrayList<>();
                    for (int s=0;s<userInformation.getSubscription().size();s++) {
                        Subscriber subscriber = userInformation.getSubscription().get(s);
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
                    if(userFromBase.size()==0){
                        emptyList.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        emptyList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListener);
                        recyclerView.setAdapter(sendLookAdapter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void addLastMessage(String type, String Message){
        switch (type) {
            case "text":
                addType("text");
                firebaseModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastMessage").setValue(Message);
                firebaseModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastMessage").setValue(Message);
                break;
            case "voice":
                addType("voice");
                firebaseModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastMessage").setValue("Голосовое сообщение");
                firebaseModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastMessage").setValue("Голосовое сообщение");
                break;
            case "image":
                firebaseModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastMessage").setValue("Фотография");
                firebaseModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastMessage").setValue("Фотография");
                addType("image");
                break;
        }
        Calendar calendar = Calendar.getInstance();
        firebaseModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("LastTime").setValue(RecentMethods.getCurrentTime());
        firebaseModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("LastTime").setValue(RecentMethods.getCurrentTime());
        firebaseModel.getUsersReference().child(nick).child("Chats").child(otherUserNickString).child("TimeMill").setValue(calendar.getTimeInMillis() * -1);
        firebaseModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child("TimeMill").setValue(calendar.getTimeInMillis() * -1);
    }

    public void addType(String type) {
        final long[] value = new long[1];
        DatabaseReference ref = firebaseModel.getUsersReference().child(otherUserNickString).child("Chats").child(nick).child(type);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    value[0] = (long) dataSnapshot.getValue();
                    value[0] = value[0] + 1;
                    dataSnapshot.getRef().setValue(value[0]);}
                else dataSnapshot.getRef().setValue(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

}
