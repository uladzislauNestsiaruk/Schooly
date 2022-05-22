package com.egormoroz.schooly.ui.profile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.main.MyClothes.MyClothesAdapter;
import com.egormoroz.schooly.ui.main.MyClothes.PresentClothesFragment;
import com.egormoroz.schooly.ui.main.MyClothes.ViewingMyClothes;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.NewClothesAdapter;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.profile.Wardrobe.WardrobeAccessories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClothesViewingProfile extends Fragment {

    Fragment fragment;
    String type,nick;
    UserInformation userInformation;
    Bundle bundle;


    public ClothesViewingProfile(String type,Fragment fragment,UserInformation userInformation,Bundle bundle) {
        this.fragment = fragment;
        this.type = type;
        this.userInformation=userInformation;
        this.bundle=bundle;
    }

    public static ClothesViewingProfile newInstance(String type,Fragment fragment,UserInformation userInformation,Bundle bundle) {
        return new ClothesViewingProfile(type,fragment,userInformation,bundle);

    }


    TextView clothesTitleCV, description, noDescription,purchaseToday,purchaseAll,profitToday,profitAll
            ,perSentToday,perSentAll,clothesPrice;
    ImageView clothesImageCV, backToShop, coinsImage,coinsImageAll,coinsImagePurple,send;
    RelativeLayout presentClothes,resaleClothes;
    RecyclerView recyclerView;
    ArrayList<Subscriber> userFromBase;
    SendLookAdapter.ItemClickListener itemClickListener;
    LinearLayout linearElse,linearTelegram,linearInstagram;
    TextView emptyList;
    EditText editText,messageEdit;
    String userName,otherUserNickString;
    Clothes clothesViewing;
    private FirebaseModel firebaseModel = new FirebaseModel();
    double perCent;
    String clothesPriceString,purchaseTodayString,profitTodayString,profitAllString;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewingmyclothes, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;

    }

    @Override
    public void onViewCreated(@Nullable View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nick=userInformation.getNick();
        clothesImageCV = view.findViewById(R.id.clothesImagecv);
        coinsImage = view.findViewById(R.id.coinsImage);
        coinsImageAll = view.findViewById(R.id.coinsImageAll);
        noDescription = view.findViewById(R.id.noDescription);
        clothesTitleCV = view.findViewById(R.id.clothesTitlecv);
        description = view.findViewById(R.id.description);
        backToShop = view.findViewById(R.id.back_toshop);
        purchaseToday=view.findViewById(R.id.purchasesToday);
        coinsImagePurple=view.findViewById(R.id.coinImagePrice);
        clothesPrice=view.findViewById(R.id.clothesPricecv);
        resaleClothes=view.findViewById(R.id.resaleClothes);
        resaleClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        send=view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        purchaseAll=view.findViewById(R.id.purchasesAll);
        profitToday=view.findViewById(R.id.profit);
        profitAll=view.findViewById(R.id.profitAll);
        presentClothes=view.findViewById(R.id.presentClothes);
        perSentToday=view.findViewById(R.id.perSentPurchase);
        perSentAll=view.findViewById(R.id.perSentPurchaseAll);
        backToShop.setOnClickListener(new View.OnClickListener() {
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

        ClothesAdapter.singeClothesInfo(new ClothesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                clothesViewing = clothes;
                presentClothes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.setCurrentFragment(PresentClothesFragment.newInstance(clothesViewing,ClothesViewingProfile.newInstance(type,fragment,userInformation,bundle),userInformation,bundle), getActivity());
                    }
                });
                clothesTitleCV.setText(clothes.getClothesTitle());
                purchaseTodayString=String.valueOf(clothesViewing.getPurchaseToday());
                if(clothes.getPurchaseToday()<1000){
                    purchaseToday.setText(String.valueOf(clothes.getPurchaseToday()));
                }else if(clothes.getPurchaseToday()>1000 && clothes.getPurchaseToday()<10000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 1)+"."+purchaseTodayString.substring(1, 2)+"K");
                }
                else if(clothes.getPurchaseToday()>10000 && clothes.getPurchaseToday()<100000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 2)+"."+purchaseTodayString.substring(2,3)+"K");
                }
                else if(clothes.getPurchaseToday()>10000 && clothes.getPurchaseToday()<100000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 2)+"."+purchaseTodayString.substring(2,3)+"K");
                }else if(clothes.getPurchaseToday()>100000 && clothes.getPurchaseToday()<1000000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 3)+"K");
                }
                else if(clothes.getPurchaseToday()>1000000 && clothes.getPurchaseToday()<10000000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 1)+"KK");
                }
                else if(clothes.getPurchaseToday()>10000000 && clothes.getPurchaseToday()<100000000){
                    purchaseToday.setText(purchaseTodayString.substring(0, 2)+"KK");
                }
                clothesPriceString=String.valueOf(clothes.getPurchaseNumber());
                if(clothes.getPurchaseNumber()<1000){
                    purchaseAll.setText(String.valueOf(clothes.getPurchaseNumber()));
                }else if(clothes.getPurchaseNumber()>1000 && clothes.getPurchaseNumber()<10000){
                    purchaseAll.setText(clothesPriceString.substring(0, 1)+"."+clothesPriceString.substring(1, 2)+"K");
                }
                else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
                    purchaseAll.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
                }
                else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
                    purchaseAll.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
                }else if(clothes.getPurchaseNumber()>100000 && clothes.getPurchaseNumber()<1000000){
                    purchaseAll.setText(clothesPriceString.substring(0, 3)+"K");
                }
                else if(clothes.getPurchaseNumber()>1000000 && clothes.getPurchaseNumber()<10000000){
                    purchaseAll.setText(clothesPriceString.substring(0, 1)+"KK");
                }
                else if(clothes.getPurchaseNumber()>10000000 && clothes.getPurchaseNumber()<100000000){
                    purchaseAll.setText(clothesPriceString.substring(0, 2)+"KK");
                }
                profitTodayString=String.valueOf(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday());
                profitAllString=String.valueOf(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice());
                if (clothesViewing.getCurrencyType().equals("dollar")){
                    clothesPrice.setText("$"+String.valueOf(clothes.getClothesPrice()));
                    coinsImagePurple.setVisibility(View.GONE);
                    coinsImage.setVisibility(View.GONE);
                    coinsImageAll.setVisibility(View.GONE);
                    ////
                    if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000){
                        profitToday.setText("+"+profitTodayString+"$");
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000
                            && clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"."+profitTodayString.substring(1, 2)+"K"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K"+"$");
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>100000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 3)+"K"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"KK"+"$");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"KK"+"$");
                    }
                    ////////
                    if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000){
                        profitAll.setText("+"+profitAllString+"$");
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000
                            && clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"."+profitAllString.substring(1, 2)+"K"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K"+"$");
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>100000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000000){
                        profitAll.setText("+"+profitAllString.substring(0, 3)+"K"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"KK"+"$");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"KK"+"$");
                    }
                }else {
                    clothesPrice.setText(String.valueOf(clothes.getClothesPrice()));
                    if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000){
                        profitToday.setText("+"+profitTodayString);
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000
                            && clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"."+profitTodayString.substring(1, 2)+"K");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"."+profitTodayString.substring(2,3)+"K");
                    }else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>100000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<1000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 3)+"K");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>1000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<10000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 1)+"KK");
                    }
                    else if(clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()>10000000 &&
                            clothesViewing.getClothesPrice()*clothesViewing.getPurchaseToday()<100000000){
                        profitToday.setText("+"+profitTodayString.substring(0, 2)+"KK");
                    }
                    ////////
                    if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000){
                        profitAll.setText("+"+profitAllString);
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000
                            && clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"."+profitAllString.substring(1, 2)+"K");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"."+profitAllString.substring(2,3)+"K");
                    }else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>100000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<1000000){
                        profitAll.setText("+"+profitAllString.substring(0, 3)+"K");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>1000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<10000000){
                        profitAll.setText("+"+profitAllString.substring(0, 1)+"KK");
                    }
                    else if(clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()>10000000 &&
                            clothesViewing.getPurchaseNumber()*clothesViewing.getClothesPrice()<100000000){
                        profitAll.setText("+"+profitAllString.substring(0, 2)+"KK");
                    }
                }
                if (clothesViewing.getPurchaseNumber()==0){
                    perCent=0;
                }else {
                    perCent=clothesViewing.getPurchaseToday()*100/clothesViewing.getPurchaseNumber();
                }
                perSentToday.setText("("+String.valueOf(perCent)+"%)");
                Picasso.get().load(clothesViewing.getClothesImage()).into(clothesImageCV);
                if (clothesViewing.getDescription().trim().length() == 0) {
                    noDescription.setVisibility(View.VISIBLE);
                    description.setVisibility(View.GONE);
                } else {
                    description.setText(clothesViewing.getDescription());
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

    public void showDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_layout_sell_clothes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout sellRelative=dialog.findViewById(R.id.sellRelative);


        sellRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}