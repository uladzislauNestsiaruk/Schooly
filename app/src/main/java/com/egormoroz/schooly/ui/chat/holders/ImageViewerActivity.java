package com.egormoroz.schooly.ui.chat.holders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.egormoroz.schooly.R;
import com.squareup.picasso.Picasso;

public class ImageViewerActivity extends AppCompatActivity
{
    private ImageView imageView;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_image);

        imageView = findViewById(R.id.image_viewer);
        imageUrl = getIntent().getStringExtra("url");

        Picasso.get().load(imageUrl).into(imageView);
    }
}
