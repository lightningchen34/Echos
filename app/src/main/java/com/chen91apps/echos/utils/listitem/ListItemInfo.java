package com.chen91apps.echos.utils.listitem;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.widget.ImageView;

public class ListItemInfo {
    private String title;
    private String subtitle;
    private Bitmap image;

    public ListItemInfo(String title, String subtitle, Bitmap image)
    {
        this.title = title;
        this.subtitle = subtitle;
        Matrix matrix = new Matrix();
        matrix.postScale(0.1f, 0.1f);
        this.image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }
}
