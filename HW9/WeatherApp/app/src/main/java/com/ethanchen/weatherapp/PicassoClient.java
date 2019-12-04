package com.ethanchen.weatherapp;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {

    public  static void downloadImage(Context c, String url, ImageView img) {
        //Picasso.with(c).load(url).fit().centerInside().into(img);
        Picasso.with(c).load(url).fit().centerCrop().into(img);
    }
}
