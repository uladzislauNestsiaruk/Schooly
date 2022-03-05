package com.egormoroz.schooly.ui.main.MyClothes;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.egormoroz.schooly.Callbacks;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.sceneform.SceneView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateClothesFragment extends Fragment {

    FirebaseModel firebaseModel=new FirebaseModel();
    EditText editTextClothes,editClothesPrice,addDescriptionEdit;
    ImageView addModelFile,addModelImage,modelPhoto;
    TextView modelWay,before,criteria
            ,noTitle,noModel,noPhoto,noSum;
    RelativeLayout publish;
    RadioGroup radioGroup,radioGroupCurrency;
    private String checker = "", myUrl = "";
    private Uri fileUri;
    SceneView modelScene;
    private StorageTask uploadTask;
    RadioButton radioButton1,radioButton2,radioButton3,radioButton4
            ,radioButton5,radioButton6,radioButton7,radioButton8
            ,radioButton9,radioButton10,radioButton11
            ,radioButton12,radioButton13,radioButtonCoin,radioButtonDollar;

    Fragment fragment;
    String premiumType;

    public CreateClothesFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public static CreateClothesFragment newInstance(Fragment fragment) {
        return new CreateClothesFragment(fragment);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_createclothes, container, false);
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

        editClothesPrice=view.findViewById(R.id.editClothesPrice);
        editTextClothes=view.findViewById(R.id.editTextClothes);
        addDescriptionEdit=view.findViewById(R.id.addDescriptionEdit);
        radioGroupCurrency=view.findViewById(R.id.radioGroupCurrency);
        addModelFile=view.findViewById(R.id.addModelFile);
        addModelImage=view.findViewById(R.id.addModelImage);
        modelPhoto=view.findViewById(R.id.modelPhoto);
        modelScene=view.findViewById(R.id.modelFile);
        before=view.findViewById(R.id.before);
        criteria=view.findViewById(R.id.criteria);
        noPhoto=view.findViewById(R.id.noPhoto);
        noModel=view.findViewById(R.id.noFile);
        noSum=view.findViewById(R.id.noSum);
        noTitle=view.findViewById(R.id.noEnterTitleText);
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(FragmentCriteria.newInstance(), getActivity());
            }
        });
        criteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.setCurrentFragment(FragmentCriteria.newInstance(), getActivity());
            }
        });
        publish=view.findViewById(R.id.publish);
        radioGroup=view.findViewById(R.id.radioGroup);
        radioButton1=view.findViewById(R.id.radio_button_1);
        radioButton2=view.findViewById(R.id.radio_button_2);
        radioButton3=view.findViewById(R.id.radio_button_3);
        radioButton4=view.findViewById(R.id.radio_button_4);
        radioButton5=view.findViewById(R.id.radio_button_5);
        radioButton6=view.findViewById(R.id.radio_button_6);
        radioButton7=view.findViewById(R.id.radio_button_7);
        radioButton8=view.findViewById(R.id.radio_button_8);
        radioButton9=view.findViewById(R.id.radio_button_9);
        radioButton10=view.findViewById(R.id.radio_button_10);
        radioButton11=view.findViewById(R.id.radio_button_11);
        radioButton12=view.findViewById(R.id.radio_button_12);
        radioButton13=view.findViewById(R.id.radio_button_13);
        radioButtonCoin=view.findViewById(R.id.schoolyCoinRadio);
        radioButtonDollar=view.findViewById(R.id.dollarRadio);
        addModelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "image";
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 443);
            }
        });
        addModelFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "image";
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("glb/*");
                startActivityForResult(intent, 443);
            }
        });

        ImageView backtoMyClothes=view.findViewById(R.id.back_tomyclothes);
        backtoMyClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        RecentMethods.setCurrentFragment(fragment, getActivity());
                    }
                });
            }
        });
        RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
            @Override
            public void PassUserNick(String nick) {
                Query query=firebaseModel.getUsersReference().child(nick)
                        .child("version");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        premiumType=snapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        radioButtonDollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(premiumType.equals("premium")){
                    radioButtonDollar.setChecked(true);
                }else {
                    radioButtonDollar.setChecked(false);
                    radioButtonCoin.setChecked(true);
                    Toast.makeText(getContext(), "К сожалению, вы не подписаны на Schooly Premium", Toast.LENGTH_SHORT).show();
                }
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextClothes.getText().toString().length()==0){
                    noTitle.setVisibility(View.VISIBLE);
                }else {
                    noTitle.setVisibility(View.GONE);
                }
                if(editClothesPrice.getText().toString().length()==0 ||
                        editClothesPrice.getText().toString().equals("0") ){
                        noSum.setVisibility(View.VISIBLE);
                }else {
                    noSum.setVisibility(View.GONE);
                }if(modelPhoto.getVisibility()==View.GONE){
                    noPhoto.setVisibility(View.VISIBLE);
                }else {
                    noPhoto.setVisibility(View.GONE);
                }
                if(modelScene.getVisibility()==View.GONE){
                    noModel.setVisibility(View.VISIBLE);
                }else {
                    noModel.setVisibility(View.GONE);
                }if(editTextClothes.getText().toString().length()>0 && editClothesPrice.getText().toString().length()>0 &&
                        !editClothesPrice.getText().toString().equals("0")&& modelPhoto.getVisibility()==View.VISIBLE
                ){
                    RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                        @Override
                        public void PassUserNick(String nick) {
                            Random random = new Random();
                            int num =random.nextInt(1000000000);
                            firebaseModel.getReference().child("clothesReqests").child(String.valueOf(num))
                                    .setValue(new ClothesRequest("a", "d", Long.valueOf(editClothesPrice.getText().toString()), editTextClothes.getText().toString()
                                            , 111, nick, "coin",addDescriptionEdit.getText().toString() ,"ff" , "ff"));
                            Toast.makeText(getContext(), "Заявка отправлена", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 443 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            if (checker.equals("image")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images");

                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child(nick).child("imageApplication").push();
                        final String messagePushID = userMessageKeyRef.getKey();

                        final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");
                        uploadTask = filePath.putFile(fileUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                firebaseModel.getUsersReference().child(nick).child("imageApplication").setValue(myUrl);

                                Query query=firebaseModel.getUsersReference().child(nick).child("imageApplication");
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        modelPhoto.setVisibility(View.VISIBLE);
                                        Picasso.get().load(snapshot.getValue(String.class)).into(modelPhoto);
                                        noPhoto.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                });

            }
            else if (checker.equals("model")){
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("model");

                RecentMethods.UserNickByUid(firebaseModel.getUser().getUid(), firebaseModel, new Callbacks.GetUserNickByUid() {
                    @Override
                    public void PassUserNick(String nick) {
                        DatabaseReference userMessageKeyRef = firebaseModel.getUsersReference().child(nick).child("modelApplication").push();
                        final String messagePushID = userMessageKeyRef.getKey();

                        final StorageReference filePath = storageReference.child(messagePushID + "." + "glb");
                        uploadTask = filePath.putFile(fileUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                firebaseModel.getUsersReference().child(nick).child("modelApplication").setValue(myUrl);

                                Query query=firebaseModel.getUsersReference().child(nick).child("modelApplication");
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

}
