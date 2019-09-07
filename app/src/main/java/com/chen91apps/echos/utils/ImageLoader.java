package com.chen91apps.echos.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chen91apps.echos.MainActivity;
import com.chen91apps.echos.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {
    public static ImageLoader imageLoader = new ImageLoader();

    private ImageView imageView;
    private String type;
    private String url;

    private Context context;

    private ImageLoader()
    {
        // TODO
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public void load(String url, ImageView imageView)
    {
        Glide.with(context).load(url).error(R.drawable.ic_broken_image).placeholder(R.drawable.ic_refresh).into(imageView);
    }

    public static void loadImage(String url, ImageView imageView)
    {
        imageLoader.load(url, imageView);
    }
}
