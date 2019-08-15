package com.chen91apps.echos.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

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

public class ImageLoader extends AsyncTask<Object, Void, Bitmap> {
    private ImageView imageView;
    private String type;
    private String url;

    public static void load(ImageView imageView, String type, String url)
    {
        Bitmap bitmap = MainActivity.acache.getAsBitmap(type + ":::" + url);
        if (bitmap == null)
        {
            imageView.setImageResource(R.drawable.ic_broken_image);

            ImageLoader loader = new ImageLoader();
            loader.execute(imageView, type, url);
        } else
        {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(Object... objects) {
        imageView = (ImageView)  objects[0];
        type = (String) objects[1];
        url = (String) objects[2];
        Bitmap bitmap = Loader.loadImage(url);
        if (type == "thumbnail")
        {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            float scale;
            if (height > width)
                scale = 500.0f / width;
            else
                scale = 500.0f / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);// 使用后乘
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        }
        MainActivity.acache.put(type + ":::" + url, bitmap);
        return bitmap;
    }

    static class Loader
    {
        public static Bitmap loadImage(String url)
        {
            Bitmap bitmap = null;
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            InputStream inputStream = null;
            try
            {
                response = client.execute(new HttpGet(url));
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (ClientProtocolException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}
