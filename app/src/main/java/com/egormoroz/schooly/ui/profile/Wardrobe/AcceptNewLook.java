package com.egormoroz.schooly.ui.profile.Wardrobe;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.LockableNestedScrollView;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.Shop.ViewingClothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.news.ViewingClothesNews;
import com.egormoroz.schooly.ui.profile.Look;
import com.egormoroz.schooly.ui.profile.LooksAdapter;
import com.egormoroz.schooly.ui.profile.LooksFragmentProfileOther;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AcceptNewLook extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    RelativeLayout publish;
    TextView lookPrice,lookPriceDollar,constituentsText;
    EditText descriptionLook;
    ImageView schoolyCoin;
    RecyclerView recyclerView;
    long lookPriceLong,lookPriceDollarLong;
    String lookPriceString,lookPriceDollarString,nick,model,type;
    ConstituentsAdapter.ItemClickListener itemClickListener;
    Fragment fragment;
    UserInformation userInformation;
    Bundle bundle;
    String lookType;
    SurfaceView surfaceView;
    static byte[] buffer;
    static URI uri;
    static Future<Buffer> future;
    ArrayList<Buffer> buffers;
    static Buffer buffer1,bufferToFilament,b;
    static FilamentModel filamentModel=new FilamentModel();
    static ArrayList<Clothes> clothesList=new ArrayList<>();
    static ArrayList<String> clothesUid=new ArrayList<>();
    LockableNestedScrollView lockableNestedScrollView;

    public AcceptNewLook(String model,String type,Fragment fragment,UserInformation userInformation,Bundle bundle,String lookType,ArrayList<String> clothesUid,
                         ArrayList<Clothes> clothesList) {
        this.model = model;
        this.type = type;
        this.fragment=fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.lookType=lookType;
        AcceptNewLook.clothesUid =clothesUid;
        AcceptNewLook.clothesList=clothesList;
    }

    public static AcceptNewLook newInstance(String model,String type,Fragment fragment,UserInformation userInformation,Bundle bundle,String lookType
            ,ArrayList<String> clothesUid, ArrayList<Clothes> clothesList) {
        return new AcceptNewLook(model,type,fragment,userInformation,bundle,lookType,clothesUid,clothesList);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_acceptnewlook, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(descriptionLook.getText().toString().length()>0){
            bundle.putString("EDIT_DESCRIPTION_LOOK",descriptionLook.getText().toString().trim());
        }
        bundle.putSerializable("CLOTHESUID", clothesUid);
        bundle.putSerializable("ALLLOADCLOTHESLIST", clothesList);
    }


    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        nick=userInformation.getNick();
        publish=view.findViewById(R.id.publish);
        lookPrice=view.findViewById(R.id.lookPrice);
        lookPriceDollar=view.findViewById(R.id.lookPriceDollar);
        descriptionLook=view.findViewById(R.id.addDescriptionEdit);
        surfaceView=view.findViewById(R.id.surfaceView);
        recyclerView=view.findViewById(R.id.constituentsRecycler);
        schoolyCoin=view.findViewById(R.id.schoolyCoin);
        constituentsText=view.findViewById(R.id.lookConstituentsText);
        itemClickListener=new ConstituentsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Clothes clothes) {
                RecentMethods.setCurrentFragment(ViewingClothesNews.newInstance(AcceptNewLook.newInstance(model,type,fragment, userInformation,bundle,lookType,clothesUid,clothesList),userInformation,bundle), getActivity());
            }
        };

        ImageView backfromwardrobe=view.findViewById(R.id.back_toprofile);
        backfromwardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(type,fragment,userInformation,bundle,lookType), getActivity());
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                RecentMethods.setCurrentFragment(CreateLookFragment.newInstance(type,fragment,userInformation,bundle,lookType), getActivity());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        lockableNestedScrollView=view.findViewById(R.id.lockableNestedScrollView);
        loadPerson(userInformation, lockableNestedScrollView);
        if(bundle!=null){
            if(bundle.getString("EDIT_DESCRIPTION_LOOK")!=null){
                String editDescriptionText=bundle.getString("EDIT_DESCRIPTION_LOOK");
                descriptionLook.setText(editDescriptionText);
            }
        }
        loadLookClothes();
        getLookClothes();
    }

    public void getLookClothes(){
        if(userInformation.getLookClothes().size()!=0){
            for (int i=0;i<userInformation.getLookClothes().size();i++){
                Clothes clothes=userInformation.getLookClothes().get(i);
                if(clothes.getCurrencyType().equals("dollar")){
                    lookPriceDollarLong+=clothes.getClothesPrice();
                }else {
                    lookPriceLong+=clothes.getClothesPrice();
                }
            }
            if(lookPriceDollarLong>0 || lookPriceLong>0){
                setTextInLookPrice();
            }
            FirebaseModel newsModel = new FirebaseModel();
            newsModel.initNewsDatabase();
            ConstituentsAdapter constituentsAdapter=new ConstituentsAdapter(userInformation.getLookClothes(), itemClickListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(constituentsAdapter);
            publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lookId=newsModel.getReference().child(nick).push().getKey();
                    newsModel.getReference().child(nick).child(lookId)
                            .setValue(new NewsItem(model, descriptionLook.getText().toString(), "0", lookId,
                                    "", userInformation.getLookClothes(), 1200, 0,"",nick,0,userInformation.getPerson()));
                    descriptionLook.getText().clear();
                    Toast.makeText(getContext(), getContext().getResources().getText(R.string.lookpublishedsuccessfully), Toast.LENGTH_SHORT).show();
                    RecentMethods.setCurrentFragment(ProfileFragment.newInstance(type, nick, fragment,userInformation,bundle), getActivity());
                }
            });
        }else{
            lookPrice.setText("0");
            constituentsText.setVisibility(View.GONE);
            publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), getContext().getResources().getText(R.string.nolookcomponents), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        filamentModel.postFrameCallback();
    }

    @Override
    public void onPause() {
        super.onPause();

        filamentModel.removeFrameCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        filamentModel.removeFrameCallback();
    }

    public void loadLookClothes(){
        if(clothesList.size()==0) {
            firebaseModel.getUsersReference().child(nick).child("lookClothes")
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Clothes clothes = snap.getValue(Clothes.class);
                            addModelInScene(clothes);
                            Log.d("#####", "d1");
                        }
                    }
                }
            });
        }  else{
            Log.d("#####", "d2");
            for(int i=0;i<clothesList.size();i++ ){
                Clothes clothes=clothesList.get(i);
                if(clothesUid.contains(clothes.getUid())&&clothes.getBuffer()!=null){
                    filamentModel.populateScene(clothes.getBuffer(), clothes);
                } else if(clothesUid.contains(clothes.getUid())&&clothes.getBuffer()==null){
                    addModelInScene(clothes);
                }
            }
        }
    }

    public void setTextInLookPrice(){
        if (lookPriceDollarLong==0 && lookPriceLong>0){
            lookPriceDollar.setVisibility(View.GONE);
        }else{
            lookPriceDollarString=String.valueOf(lookPriceDollarLong);
            if(lookPriceDollarLong<1000){
                lookPriceDollar.setText(" + "+lookPriceDollarString+"$");
            }else if(lookPriceDollarLong>1000 && lookPriceDollarLong<10000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 1)+"."+lookPriceDollarString.substring(1, 2)+"K"+"$");
            }
            else if(lookPriceDollarLong>10000 && lookPriceDollarLong<100000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
            }
            else if(lookPriceDollarLong>10000 && lookPriceDollarLong<100000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"."+lookPriceDollarString.substring(2,3)+"K"+"$");
            }else if(lookPriceDollarLong>100000 && lookPriceDollarLong<1000000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 3)+"K"+"$");
            }
            else if(lookPriceDollarLong>1000000 && lookPriceDollarLong<10000000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 1)+"KK"+"$");
            }
            else if(lookPriceDollarLong>10000000 && lookPriceDollarLong<100000000){
                lookPriceDollar.setText(" + "+lookPriceDollarString.substring(0, 2)+"KK"+"$");
            }
        }
        if(lookPriceLong==0){
            schoolyCoin.setVisibility(View.GONE);
            lookPrice.setVisibility(View.GONE);
            lookPriceDollarString=String.valueOf(lookPriceDollarLong);
            checkCounts(lookPriceDollar, lookPriceDollarLong, lookPriceDollarString);
        }else {
            lookPriceString=String.valueOf(lookPriceLong);
            checkCounts(lookPrice, lookPriceLong, lookPriceString);
        }
    }

    public void checkCounts(TextView textView,Long count,String stringCount){
        if(count<1000){
            textView.setText(String.valueOf(count));
        }else if(count>1000 && count<10000){
            textView.setText(stringCount.substring(0, 1)+"."+stringCount.substring(1, 2)+"K");
        }
        else if(count>10000 && count<100000){
            textView.setText(stringCount.substring(0, 2)+"."+stringCount.substring(2,3)+"K");
        }else if(count>100000 && count<1000000){
            textView.setText(stringCount.substring(0, 3)+"K");
        }
        else if(count>1000000 && count<10000000){
            textView.setText(stringCount.substring(0, 1)+"KK");
        }
        else if(count>10000000 && count<100000000){
            textView.setText(stringCount.substring(0, 2)+"KK");
        }
    }

    public static byte[] getBytes( URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream());
            byte[] byteChunk = new byte[4096];
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            Log.d("####", "Failed while reading bytes from %s: %s"+ url.toExternalForm()+ e.getMessage());
            e.printStackTrace ();
        }
        finally {
            if (is != null) { is.close(); }
        }
        return  baos.toByteArray();
    }

    public static void addModelInScene(Clothes clothes)  {
        loadBuffer(clothes.getModel());
        try {
            bufferToFilament= future.get();
            filamentModel.populateScene(bufferToFilament,clothes);
            clothes.setBuffer(bufferToFilament);
            clothesList.add(clothes);
            clothesUid.add(clothes.getUid());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void loadBuffer(String model){
        ExecutorService executorService= Executors.newCachedThreadPool();
        future = executorService.submit(new Callable(){
            public Buffer call() throws Exception {
                uri = new URI(model);
                buffer = getBytes(uri.toURL());
                buffer1= ByteBuffer.wrap(buffer);
                return buffer1;
            }
        });
    }

    public void loadPerson(UserInformation userInformation,LockableNestedScrollView lockableNestedScrollView){
        try {
            if(bundle.getSerializable("PERSON"+userInformation.getNick())==null){
                uri = new URI(userInformation.getPerson().getBody().getModel());
                buffer = getBytes(uri.toURL());
                bufferToFilament= ByteBuffer.wrap(buffer);
                buffers=new ArrayList<>();
                buffers.add(bufferToFilament);
                bundle.putSerializable("PERSON"+userInformation.getNick(),buffers);
                filamentModel.initFilament(surfaceView,bufferToFilament,true,lockableNestedScrollView
                        ,"regularRender",true);
                loadBodyPart(userInformation.getPerson().getBrows().getModel());
                loadBodyPart(userInformation.getPerson().getEars().getModel());
                loadBodyPart(userInformation.getPerson().getEyes().getModel());
                loadBodyPart(userInformation.getPerson().getHair().getModel());
                loadBodyPart(userInformation.getPerson().getHead().getModel());
                loadBodyPart(userInformation.getPerson().getLips().getModel());
                loadBodyPart(userInformation.getPerson().getNose().getModel());
                loadBodyPart(userInformation.getPerson().getPirsing().getModel());
                loadBodyPart(userInformation.getPerson().getSkinColor().getModel());

            }else{
                ArrayList<Buffer> buffers= (ArrayList<Buffer>) bundle.getSerializable("PERSON"+userInformation.getNick());
                for(int i=0;i<buffers.size();i++){
                    Buffer buffer3=buffers.get(i);
                    if(i==0){
                        filamentModel.initFilament(surfaceView,buffer3 ,true,lockableNestedScrollView
                                ,"regularRender",true);
                    }else{
                        filamentModel.populateSceneFacePart(buffer3);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void loadBodyPart(String string){
        if(string!=null){
            loadBuffer(string);
            try {
                Buffer bufferToFilament= future.get();
                filamentModel.populateSceneFacePart(bufferToFilament);
                buffers.add(bufferToFilament);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}