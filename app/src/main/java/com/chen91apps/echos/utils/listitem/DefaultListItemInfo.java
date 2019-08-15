package com.chen91apps.echos.utils.listitem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen91apps.echos.R;

public class DefaultListItemInfo extends BaseListItemInfo {

    private String title;
    private String subtitle;
    private String imageUrl;

    private ViewHolder vh;

    public DefaultListItemInfo(String title, String subtitle, String imageUrl)
    {
        super(R.layout.mylistview_item_default);
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
    }

    @Override
    public void setup(View view) {
        vh = new ViewHolder();
        vh.title = (TextView) view.findViewById(R.id.mylistview_item_title);
        vh.subtitle = (TextView) view.findViewById(R.id.mylistview_item_subtitle);
        vh.image = (ImageView) view.findViewById(R.id.mylistview_item_image);
        view.setTag(this);
    }

    @Override
    public void show() {
        com.chen91apps.echos.utils.ImageLoader.load(vh.image, "thumbnail", imageUrl);
        vh.title.setText(title);
        vh.subtitle.setText(subtitle);
    }

    private class ViewHolder
    {
        TextView title;
        TextView subtitle;
        ImageView image;
    }
}
