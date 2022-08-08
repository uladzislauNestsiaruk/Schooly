package com.egormoroz.schooly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.PixelCopy;
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

import com.egormoroz.schooly.ui.main.MyClothes.ViewingMyClothes;
import com.egormoroz.schooly.ui.main.Shop.Clothes;
import com.egormoroz.schooly.ui.main.UserInformation;
import com.egormoroz.schooly.ui.news.NewsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class InstagramShareFragment extends Fragment {

    Fragment fragment;
    Clothes clothes;
    UserInformation userInformation;
    Bundle bundle;
    RelativeLayout relativeLayout;
    ImageView clothesImage,back;
    TextView clothesTitle,clothesCreator,share;
    FirebaseModel firebaseModel=new FirebaseModel();

    public InstagramShareFragment(Fragment fragment,UserInformation userInformation,Bundle bundle,Clothes clothes) {
        this.fragment = fragment;
        this.userInformation=userInformation;
        this.bundle=bundle;
        this.clothes=clothes;
    }

    public static InstagramShareFragment newInstance(Fragment fragment, UserInformation userInformation, Bundle bundle,Clothes clothes) {
        return new InstagramShareFragment(fragment,userInformation,bundle,clothes);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_instagram_share, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bottomNavigationView);
        bnv.setVisibility(bnv.GONE);
        firebaseModel.initAll();
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back=view.findViewById(R.id.back);
        relativeLayout=view.findViewById(R.id.relative);
        clothesCreator=view.findViewById(R.id.clothesCreator);
        clothesImage=view.findViewById(R.id.clothesImage);
        clothesTitle=view.findViewById(R.id.clothesTitle);
        share=view.findViewById(R.id.share);
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

        Picasso.get().load(clothes.getClothesImage()).into(clothesImage);
        clothesTitle.setText(clothes.getClothesTitle());
        clothesCreator.setText(clothes.getCreator());

        share.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getBitmapFormView(relativeLayout, getActivity(), new NewsAdapter.Callback<Bitmap>() {
                    @Override
                    public void onResult1(Bitmap bitmap) {
                        Uri backgroundAssetUri = getImageUri(getActivity(), bitmap);
                        Uri stickerAssetUri = getImageUri(getActivity(), bitmap);
                        String sourceApplication = "com.egormoroz.schooly";

                        Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
                        intent.putExtra("source_application", sourceApplication);

                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(backgroundAssetUri, "image/*");
                        intent.putExtra("interactive_asset_uri", stickerAssetUri);


                        Activity activity = getActivity();
//                        activity.grantUriPermission(
//                                "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
                            activity.startActivityForResult(intent, 0);
                        }
                    }
                });
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getBitmapFormView(View view, Activity activity, NewsAdapter.Callback<Bitmap> callback) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        int[] locations = new int[2];
        view.getLocationInWindow(locations);
        Rect rect = new Rect(locations[0], locations[1], locations[0] + view.getWidth(), locations[1] + view.getHeight());


        PixelCopy.request(activity.getWindow(), rect, bitmap, copyResult -> {
            if (copyResult == PixelCopy.SUCCESS) {
                callback.onResult1(bitmap);
            }
        }, new Handler(Looper.getMainLooper()));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
