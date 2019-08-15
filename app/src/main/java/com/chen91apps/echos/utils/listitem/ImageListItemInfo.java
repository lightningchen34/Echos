package com.chen91apps.echos.utils.listitem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen91apps.echos.R;

public class ImageListItemInfo extends BaseListItemInfo {

    private String title;
    private String imageUrl_1;
    private String imageUrl_2;
    private String imageUrl_3;

    private ViewHolder vh;

    public ImageListItemInfo(String title, String imageUrl_1, String imageUrl_2, String imageUrl_3)
    {
        super(R.layout.mylistview_item_image);
        this.title = title;
        this.imageUrl_1 = imageUrl_1;
        this.imageUrl_2 = imageUrl_2;
        this.imageUrl_3 = imageUrl_3;
    }

    @Override
    public void setup(View view) {
        vh = new ViewHolder();
        vh.title = (TextView) view.findViewById(R.id.mylistview_item_title);
        vh.image_1 = (ImageView) view.findViewById(R.id.mylistview_item_image_1);
        vh.image_2 = (ImageView) view.findViewById(R.id.mylistview_item_image_2);
        vh.image_3 = (ImageView) view.findViewById(R.id.mylistview_item_image_3);
        view.setTag(this);
    }

    @Override
    public void show() {
        vh.title.setText(title);
        com.chen91apps.echos.utils.ImageLoader.load(vh.image_1, "thumbnail", imageUrl_1);
        com.chen91apps.echos.utils.ImageLoader.load(vh.image_2, "thumbnail", imageUrl_2);
        com.chen91apps.echos.utils.ImageLoader.load(vh.image_3, "thumbnail", imageUrl_3);
    }

    private class ViewHolder
    {
        TextView title;
        ImageView image_1;
        ImageView image_2;
        ImageView image_3;
    }
}
