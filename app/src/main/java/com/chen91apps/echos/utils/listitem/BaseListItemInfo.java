package com.chen91apps.echos.utils.listitem;

import android.view.View;
import android.widget.ImageView;

public abstract class BaseListItemInfo {
    private int layoutId;

    public BaseListItemInfo(int layoutId)
    {
        this.layoutId = layoutId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public abstract void setup(View view);
    public abstract void show();

    /*
    public BaseListItemInfo(String title, String subtitle, String imageURL)
    {
        this.title = title;
        this.subtitle = subtitle;
        this.imageURL = imageURL;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }
    */

}
