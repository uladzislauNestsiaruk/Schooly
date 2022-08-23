package com.egormoroz.schooly.ui.main.CreateCharacter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.egormoroz.schooly.CONST;
import com.egormoroz.schooly.FacePart;
import com.egormoroz.schooly.FilamentModel;
import com.egormoroz.schooly.FirebaseModel;
import com.egormoroz.schooly.MainActivity;
import com.egormoroz.schooly.Person;
import com.egormoroz.schooly.R;
import com.egormoroz.schooly.RecentMethods;
import com.egormoroz.schooly.TaskRunnerCustom;
import com.egormoroz.schooly.ui.main.GenderFragment;
import com.egormoroz.schooly.ui.main.MainFragment;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsItem;
import com.egormoroz.schooly.ui.people.UserPeopleAdapter;
import com.egormoroz.schooly.ui.profile.ProfileFragment;
import com.egormoroz.schooly.ui.profile.Wardrobe.AcceptNewLook;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class CreateCharacterFragment extends Fragment {

    TabLayout tabLayout;
    TextView ready;
    final String databaseUrl = CONST.RealtimeDatabaseUrl;
    ViewPager2 viewPager;
    FragmentAdapter fragmentAdapter;
    FirebaseAuth authenticationDatabase;
    FirebaseDatabase database;
    private FirebaseModel firebaseModel = new FirebaseModel();
    final int GOOGLE_SIGN_IN = 101;
    FirebaseAuth AuthenticationBase;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    DatabaseReference reference;
    Bundle bundle;
    UserInformation userInformation;
    Fragment fragment;
    SurfaceView surfaceView;
    static ArrayList<Buffer> buffers;
    static byte[] buffer;
    static URI uri;
    static Buffer bufferToFilament,b;
    static FilamentModel filamentModel=new FilamentModel();
    ArrayList<FacePart> facePartArrayList=new ArrayList<>();
    static ArrayList<FacePart> activeFaceParts=new ArrayList<>();
    static ArrayList<String> activeFacePartsString=new ArrayList<>();
    String from;
    public CreateCharacterFragment(UserInformation userInformation,Bundle bundle,Fragment fragment,String from) {
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.fragment=fragment;
        this.from=from;
    }

    public static CreateCharacterFragment newInstance(UserInformation userInformation,Bundle bundle,Fragment fragment,String from) {
        return new CreateCharacterFragment(userInformation,bundle,fragment,from);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_createcharacter, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;
    }

    @Override
    public void onViewCreated(@Nullable View view,@NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        AuthenticationBase = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(getActivity(), gso);
        database = FirebaseDatabase.getInstance(databaseUrl);
        reference = database.getReference("users");
        tabLayout=view.findViewById(R.id.tabsCharacter);
        ready=view.findViewById(R.id.ready);
        surfaceView=view.findViewById(R.id.surfaceViewCreateCharacter);
        viewPager=view.findViewById(R.id.viewPagerCharacter);
        if(from.equals("dd")){
            FacePart body=new FacePart();
            body.setPartType("body");
            body.setUid("fsdbjhbc");
            body.setModel("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2F%D0%BB%D0%B0%D1%81%D1%82%20%D1%87%D0%B5%D0%BB.glb?alt=media&token=ce3a5b3b-6876-443f-91e6-26ffeaf61594");
//            FacePart nose=new FacePart();
//            nose.setModel("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2Fnose123.glb?alt=media&token=e5ef0809-dd60-4b85-8882-7187abcbde8b");
//            nose.setPartType("nose");
//            nose.setUid("dswvbdf");
//            FacePart hair=new FacePart();
//            hair.setModel("https://firebasestorage.googleapis.com/v0/b/schooly-47238.appspot.com/o/3d%20models%2F%D0%BF%D1%80%D0%B8%D1%87%D0%B03.glb?alt=media&token=1fe8e688-7f84-48af-aafd-6a3fefa684fc");
//            hair.setPartType("hair");
//            facePartArrayList.add(hair);
            //facePartArrayList.add(nose);
            TaskRunnerCustom taskRunnerCustom=new TaskRunnerCustom();
            taskRunnerCustom.executeAsync(new LongRunningTask(body), (data) -> {
                filamentModel.initFilamentForPersonCustom(surfaceView,data.getBuffer() );
                loadDefaultParts(facePartArrayList);
            });
        }
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        ImageView leftarrowtoreg =view.findViewById(R.id.leftarrowtoreg);
        leftarrowtoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentFragment(GenderFragment.newInstance(userInformation, bundle, fragment,from));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                ((MainActivity)getActivity()).setCurrentFragment(GenderFragment.newInstance(userInformation, bundle, fragment,from));
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        FragmentManager fm = getChildFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.skincolour)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.hairstyle)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.hairstyleColor)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.brows)));
        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.browsColor)));
        //tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getText(R.string.lips)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }

    public static void loadNewFacePart(FacePart facePart){
        int checkOnMakeInvisible=0;
        FacePart facePartInvisible=new FacePart();
        for(int i=0;i<activeFaceParts.size();i++){
            FacePart facePart1=activeFaceParts.get(i);
            if(facePart1.getUid().equals(facePart.getUid())){
                break;
            }
            if(facePart1.getPartType().equals(facePart.getPartType())){
                checkOnMakeInvisible++;
                facePartInvisible=facePart1;
            }
            if(i==activeFaceParts.size()-1){
                if(checkOnMakeInvisible==1){
                    activeFacePartsString.remove(facePartInvisible.getUid());
                    changeFacePart(facePart, facePartInvisible);
                }else{
                    changeFacePart(facePart, null);
                }
            }
        }
    }

    public static ArrayList<FacePart> sentFaceParts(){
        return activeFaceParts;
    }

    public static void changeFacePart(FacePart newFacePart,FacePart facePartToChange){
        if(facePartToChange!=null){
            filamentModel.setMaskOnFacePart(facePartToChange);
        }
        activeFaceParts.add(newFacePart);
        activeFacePartsString.add(newFacePart.getUid());
        filamentModel.populateSceneFacePart(newFacePart.getBuffer());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
            Log.d("#####", "ggds");
        } catch (ApiException e) {

            // Google Sign In failed, update UI appropriately
        }
    }

    public void showDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.accept_character_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout no=dialog.findViewById(R.id.no);
        RelativeLayout yes=dialog.findViewById(R.id.yes);
        TextView textView=dialog.findViewById(R.id.acceptText);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from.equals("reg")){
//                    Person person=RecentMethods.setAllPerson(activeFaceParts);
//                    firebaseModel.getUsersReference().child("tyomaa6")
//                            .child("person").setValue(person);
                    if(bundle.getString("PHONE")!=null&&bundle.getString("PASSWORD")!=null&bundle.getString("NICK")!=null) {
                        createNewEmailUser(RecentMethods.makeEmail(bundle.getString("PHONE")),
                                bundle.getString("PASSWORD"), bundle.getString("NICK"));
                    }
                }else if(from.equals("nick")){
                    if(bundle.getString("NICKNAMEFRAGMENT")!=null){
                        authenticationDatabase = FirebaseAuth.getInstance();
                        AuthorizationThrowGoogle();
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void createNewEmailUser(String email, String password, String nick) {
        AuthenticationBase.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("###", "createUserWithEmail:success");
                            Person person=RecentMethods.setAllPerson(activeFaceParts,"base");
                            FirebaseUser user = AuthenticationBase.getCurrentUser();
                            getBitmapFormSurfaceView(surfaceView, new Callback<Bitmap>() {
                                @Override
                                public void onResult1(Bitmap bitmap) {
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images")
                                            .child(user.getUid() + ".png");
                                    UploadTask uploadTask = storageReference.putBytes(getImageUri(getContext(), bitmap));
                                    uploadTask.continueWithTask(new Continuation() {
                                        @Override
                                        public Object then(@NonNull Task task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }
                                            return storageReference.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Uri downloadUrl = task.getResult();
                                            UserInformation res = new UserInformation(nick, RecentMethods.getPhone(email), user.getUid(),
                                                    "6", password, "Helicopter", 1000, new ArrayList<>(),new ArrayList<>(),1,100,0
                                                    , new ArrayList<>(), new ArrayList<>(), ""," ","open","open","open","open"
                                                    ,new ArrayList<>(),"regular", new ArrayList<>(),0,new ArrayList<>(),new ArrayList<>()
                                                    ,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<Clothes>(),person,
                                                    new ArrayList<>(),new ArrayList<>(),"",downloadUrl.toString());
                                            reference.child(nick).setValue(res);
                                            FirebaseModel firebaseModel=new FirebaseModel();
                                            firebaseModel.initAll();
                                            firebaseModel.getReference("usersNicks")
                                                    .child(nick).setValue(new UserPeopleAdapter(nick,downloadUrl.toString()," "));
                                            ((MainActivity)getActivity()).IsEntered();
                                            ((MainActivity)getActivity()).checkMining();
                                            RecentMethods.setCurrentFragment(MainFragment.newInstance(res,bundle), getActivity());

                                        }
                                    });
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("###", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), getContext().getResources().getText(R.string.authenticationfailed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getBitmapFormSurfaceView(View view, Callback<Bitmap> callback) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        PixelCopy.request((SurfaceView) view, bitmap, copyResult -> {
            if (copyResult == PixelCopy.SUCCESS) {
                callback.onResult1(bitmap);
            }
        }, new Handler(Looper.getMainLooper()));
    }

    public byte[] getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        byte[] bytesArray=bytes.toByteArray();
        return bytesArray;
    }

    public interface Callback<Bitmap> {
        void onResult1(Bitmap bitmap);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        AuthenticationBase.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    RecentMethods.saveData(reference, authenticationDatabase.getCurrentUser()
                            , bundle.getString("NICKNAMEFRAGMENT"),bundle,getActivity());
                }
            }
        });
    }
    public void AuthorizationThrowGoogle() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    public class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }
        @NonNull
        @Override
        public Fragment createFragment ( int position){


            switch (position) {
                case 1:
                    return new HairstyleFragment();
                case 2:
                    return new HairstyleColorFragment();
                case 3:
                    return new EyebrowsFragment();
                case 4:
                    return new EyebrowsColorFragment();
//                case 5:
//                    return new LipsFragment();
            }
            return new SkinColourFragment();
        }


        @Override
        public int getItemCount() {
            return 6;
        }
    }

    static class LongRunningTask implements Callable<FacePart> {
        private FacePart facePart;

        public LongRunningTask(FacePart facePart) {
            this.facePart = facePart;
        }

        @Override
        public FacePart call() {
            return loadFacePart(facePart);
        }
    }

    public static FacePart loadFacePart(FacePart facePart){
        try {
            uri = new URI(facePart.getModel());
            buffer = RecentMethods.getBytes(uri.toURL());
            bufferToFilament= ByteBuffer.wrap(buffer);
            facePart.setBuffer(bufferToFilament);
            activeFaceParts.add(facePart);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return facePart;
    }

    public void loadDefaultParts(ArrayList<FacePart> facePartArrayList){
        for(int i=0;i<facePartArrayList.size();i++){
            Log.d("#####", "dd ");
            FacePart facePart=facePartArrayList.get(i);
            TaskRunnerCustom taskRunnerCustom=new TaskRunnerCustom();
            taskRunnerCustom.executeAsync(new LongRunningTask(facePart), (data) -> {
                    filamentModel.populateSceneFacePart(data.getBuffer());
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
}
