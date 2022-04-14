package com.egormoroz.schooly.ui.main.Shop;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.Nontification;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.Subscriber;
import com.egormoroz.schooly.ui.coins.CoinsFragmentSecond;
import com.egormoroz.schooly.ui.coins.CoinsMainFragment;
import com.egormoroz.schooly.ui.main.Mining.MiningFragment;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.egormoroz.schooly.ui.profile.SendLookAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ViewingClothesBasket extends Fragment {
    public static ViewingClothesBasket newInstance() {
        return new ViewingClothesBasket();

    }

    TextView clothesPriceCV,clothesTitleCV,schoolyCoinCV,buyClothesBottom,purchaseNumber
            ,creator,description,noDescription,fittingClothes;
    ImageView clothesImageCV,backToShop,coinsImage,dollarImage,inBasket,notInBasket,send;
    long schoolyCoins,clothesPrise;
    RelativeLayout checkBasket;
    Clothes clothesViewing;
    int a=0;
    private FirebaseModel firebaseModel = new FirebaseModel();
    LinearLayout coinsLinear;
    String clothesPriceString;
    RecyclerView recyclerView;
    SendLookAdapter.ItemClickListener itemClickListenerSendClothes;
    TextView emptyList;
    LinearLayout linearElse,linearTelegram,linearInstagram;
    EditText editText,messageEdit;
    String userName,otherUserNickString;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_viewingclothes, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;

    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getCoins();
        checkClothesOnBuy();
        schoolyCoinCV=view.findViewById(R.id.schoolycoincvfrag);
        clothesImageCV=view.findViewById(R.id.clothesImagecv);
        inBasket=view.findViewById(R.id.inBasketClothes);
        coinsImage=view.findViewById(R.id.coinsImage);
        notInBasket=view.findViewById(R.id.notInBasketClothes);
        send=view.findViewById(R.id.send);
        noDescription=view.findViewById(R.id.noDescription);
        dollarImage=view.findViewById(R.id.dollarImage);
        clothesTitleCV=view.findViewById(R.id.clothesTitlecv);
        description=view.findViewById(R.id.description);
        creator=view.findViewById(R.id.creator);
        checkBasket=view.findViewById(R.id.checkBasket);
        clothesPriceCV=view.findViewById(R.id.clothesPricecv);
        backToShop=view.findViewById(R.id.back_toshop);
        buyClothesBottom=view.findViewById(R.id.buyClothesBottom);
        purchaseNumber=view.findViewById(R.id.purchaseNumberViewing);
        coinsLinear=view.findViewById(R.id.linearCoins);
        fittingClothes=view.findViewById(R.id.fittingClothes);
        fittingClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(FittingFragment.newInstance(ViewingClothesBasket.newInstance()), getActivity());
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
        coinsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CoinsFragmentSecond.newInstance(ViewingClothesBasket.newInstance()), getActivity());
            }
        });
        backToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(BasketFragment.newInstance(), getActivity());
            }
        });
        BasketAdapter.singeClothesInfo(new BasketAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                clothesViewing=clothes;
                clothesPriceCV.setText(String.valueOf(clothes.getClothesPrice()));
                clothesTitleCV.setText(clothes.getClothesTitle());
                clothesPrise=clothes.getClothesPrice();
                creator.setText(clothesViewing.getCreator());
                creator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                if (clothesViewing.getCreator().equals(nick)) {
                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("userback", nick, ViewingClothesBasket.newInstance()), getActivity());
                                }else {
                                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance("other", clothesViewing.getCreator(), ViewingClothesBasket.newInstance()), getActivity());
                                }
                            }
                        });
                    }
                });
                if (clothesViewing.getDescription().length()==0){
                    noDescription.setVisibility(View.VISIBLE);
                    description.setVisibility(View.GONE);
                }else {
                    description.setText(clothesViewing.getDescription());
                }
                clothesPriceString=String.valueOf(clothes.getPurchaseNumber());
                if(clothes.getPurchaseNumber()<1000){
                    purchaseNumber.setText(String.valueOf(clothes.getPurchaseNumber()));
                }else if(clothes.getPurchaseNumber()>1000 && clothes.getPurchaseNumber()<10000){
                    purchaseNumber.setText(clothesPriceString.substring(0, 1)+"."+clothesPriceString.substring(1, 2)+"K");
                }
                else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
                    purchaseNumber.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
                }
                else if(clothes.getPurchaseNumber()>10000 && clothes.getPurchaseNumber()<100000){
                    purchaseNumber.setText(clothesPriceString.substring(0, 2)+"."+clothesPriceString.substring(2,3)+"K");
                }else if(clothes.getPurchaseNumber()>100000 && clothes.getPurchaseNumber()<1000000){
                    purchaseNumber.setText(clothesPriceString.substring(0, 3)+"K");
                }
                else if(clothes.getPurchaseNumber()>1000000 && clothes.getPurchaseNumber()<10000000){
                    purchaseNumber.setText(clothesPriceString.substring(0, 1)+"KK");
                }
                else if(clothes.getPurchaseNumber()>10000000 && clothes.getPurchaseNumber()<100000000){
                    purchaseNumber.setText(clothesPriceString.substring(0, 2)+"KK");
                }
                Picasso.get().load(clothes.getClothesImage()).into(clothesImageCV);
                if (clothesViewing.getCurrencyType().equals("dollar")){
                    dollarImage.setVisibility(View.VISIBLE);
                    coinsImage.setVisibility(View.GONE);
                }
            }
        });
        checkClothes();
        if (a==2 || a==0){
            checkIfBuy();
        }
        if (a!=3 && a!=0){
            checkClothes();
        }
        buyClothes();
        putInBasket();
    }

    public void getCoins(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                    @Override
                    public void GetMoneyFromBase(long money) {
                        schoolyCoins=money;
                        schoolyCoinCV.setText(String.valueOf(money));
                    }
                });
            }
        });
    }

    public void checkIfBuy(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query2=firebaseModel.getUsersReference().child(nick).child("clothes")
                        .child(clothesViewing.getUid());
                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            a=3;
                        }else {}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void buyClothes(){
        buyClothesBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a==3){
                    Log.d("######", " cvvv  "+a);
                    showDialogAlreadyBuy("Предмет куплен");
                }else {
                    showDialog();
                }
            }
        });
    }

    public void putInBasket(){
        checkBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        Query query=firebaseModel.getUsersReference().child(nick).child("clothes")
                                .child(clothesViewing.getUid());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    a=3;
                                    Toast.makeText(getContext(), "Предмет уже куплен", Toast.LENGTH_SHORT).show();
                                }else {}
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        if(a!=0 && a!=3){
                            if(a==1){
                                firebaseModel.getUsersReference().child(nick).child("basket")
                                        .child(clothesViewing.getUid()).removeValue();
                            }else if (a==2){
                                firebaseModel.getUsersReference().child(nick).child("basket")
                                        .child(clothesViewing.getUid()).setValue(clothesViewing);
                            }
                        }
                    }
                });
            }
        });
    }

    public void checkClothes(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("basket").
                        child(String.valueOf(clothesViewing.getUid()));
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            a=1;
                            inBasket.setVisibility(View.VISIBLE);
                            notInBasket.setVisibility(View.GONE);
                        }else {
                            a=2;
                            inBasket.setVisibility(View.GONE);
                            notInBasket.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void checkClothesOnBuy(){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick).child("clothes").
                        child(String.valueOf(clothesViewing.getUid()));
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            buyClothesBottom.setText("Куплено");
                        }else {
                            buyClothesBottom.setText("Купить");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                itemClickListenerSendClothes=new SendLookAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(String otherUserNick, String type) {
                        if(type.equals("send")){
                            String messageText = messageEdit.getText().toString();

                            String messageSenderRef = otherUserNick + "/Chats/" + nick + "/Messages";
                            String messageReceiverRef = nick + "/Chats/" + otherUserNick+ "/Messages";
                            otherUserNickString=otherUserNick;

                            DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child(nick).child("Chats").child(otherUserNick).child("Messages").push();
                            String messagePushID = userMessageKeyRef.getKey();

                            Map<String, String> messageTextBody = new HashMap<>();
                            messageTextBody.put("message", messageText);
                            messageTextBody.put("type", "text");
                            messageTextBody.put("from", nick);
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
            }
        });

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
                            SendLookAdapter sendLookAdapter = new SendLookAdapter(friends,itemClickListenerSendClothes);
                            recyclerView.setAdapter(sendLookAdapter);
                        }
                    }
                });
            }
        });

        initUserEnter();

        bottomSheetDialog.show();
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
                                SendLookAdapter sendLookAdapter = new SendLookAdapter(userFromBase,itemClickListenerSendClothes);
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

    private void addLastMessage(String type, String Message){
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
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
        });
    }

    public void addType(String type) {
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
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
        });
    }

    public void showDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_buying);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text=dialog.findViewById(R.id.acceptText);

        RelativeLayout no=dialog.findViewById(R.id.no);
        RelativeLayout yes=dialog.findViewById(R.id.yes);


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAAAA", "omg  "+getContext());
                if (clothesViewing.getCurrencyType().equals("dollar")){

                }else {
                    if(schoolyCoins>=clothesPrise){
                        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                            @Override
                            public void PassUserNick(String nick) {
                                Query query=firebaseModel.getUsersReference().child(nick).child("clothes")
                                        .child(String.valueOf(clothesViewing.getUid()));
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Toast.makeText(getContext(), "Предмет куплен", Toast.LENGTH_SHORT).show();
                                        }else {
                                            firebaseModel.getUsersReference().child(nick).child("clothes")
                                                    .child(clothesViewing.getUid()).setValue(clothesViewing);
                                            firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes")
                                                    .child(clothesViewing.getUid()).child("purchaseNumber")
                                                    .setValue(clothesViewing.getPurchaseNumber()+1);
                                            firebaseModel.getReference().child(clothesViewing.getCreator()).child("myClothes").
                                                    child(clothesViewing.getUid()).child("purchaseNumber")
                                                    .setValue(clothesViewing.getPurchaseNumber()+1);
                                            firebaseModel.getReference().child("AppData").child("Clothes").child("AllClothes")
                                                    .child(clothesViewing.getUid()).child("purchaseToday")
                                                    .setValue(clothesViewing.getPurchaseToday()+1);
                                            firebaseModel.getReference().child(clothesViewing.getCreator()).child("myClothes").
                                                    child(clothesViewing.getUid()).child("purchaseToday")
                                                    .setValue(clothesViewing.getPurchaseToday()+1);
                                            if(clothesViewing.getCreator().equals("Schooly")){

                                            }else {
                                                Random random = new Random();
                                                int num1 =random.nextInt(1000000000);
                                                int num2 =random.nextInt(1000000000);
                                                String numToBase=String.valueOf(num1+num2);
                                                Date date = new Date();
                                                SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd hh:mm a");
                                                String dateAndTime = formatter.format(date);
                                                firebaseModel.getReference().child("users")
                                                        .child(clothesViewing.getCreator()).child("nontifications")
                                                        .child(numToBase).setValue(new Nontification(nick,"не отправлено","одежда"
                                                        , "",clothesViewing.getUid(),clothesViewing.getClothesImage(),"не просмотрено",numToBase,0));
                                            }
                                            Query query=firebaseModel.getUsersReference().child(nick).child("basket").
                                                    child(clothesViewing.getUid());
                                            query.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        firebaseModel.getUsersReference().child(nick).child("basket")
                                                                .child(clothesViewing.getUid()).removeValue();
                                                    }else{
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            schoolyCoins=schoolyCoins-clothesPrise;
                                            firebaseModel.getUsersReference().child(nick).child("money").setValue(schoolyCoins);
                                            RecentMethods.GetMoneyFromBase(nick, firebaseModel, new Callbacks.MoneyFromBase() {
                                                @Override
                                                public void GetMoneyFromBase(long money) {
                                                    schoolyCoins=money;
                                                    schoolyCoinCV.setText(String.valueOf(money));
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                        a=3;
                    }else{
                        Toast.makeText(getContext(), "Не хватает коинов", Toast.LENGTH_SHORT).show();
                        RecentMethods.setCurrentFragment(CoinsFragmentSecond.newInstance(ViewingClothesBasket.newInstance()),getActivity());
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showDialogAlreadyBuy(String textInDialog){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text=dialog.findViewById(R.id.Text);
        text.setText(textInDialog);
        RelativeLayout relative=dialog.findViewById(R.id.Relative);


        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}