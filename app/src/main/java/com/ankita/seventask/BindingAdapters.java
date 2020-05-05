package com.ankita.seventask;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingAdapters {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView,String url){
        Glide.with(imageView.getContext()).load(url).into(imageView);

    }
}
