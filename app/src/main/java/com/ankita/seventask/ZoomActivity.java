package com.ankita.seventask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ZoomActivity extends AppCompatActivity {

    ImageView myImage;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        url = getIntent().getStringExtra("image_url");

        myImage = findViewById(R.id.myImage);
        Glide.with(this).load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(myImage);
    }
}
