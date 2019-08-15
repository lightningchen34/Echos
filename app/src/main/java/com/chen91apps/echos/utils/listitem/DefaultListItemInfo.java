package com.chen91apps.echos.utils.listitem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen91apps.echos.R;

public class DefaultListItemInfo extends ListItemInfo {

    private String title;
    private String subtitle;
    private String imageUrl;

    public DefaultListItemInfo(String title, String subtitle, String imageUrl)
    {
        super(R.layout.mylistview_item_default);
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
    }

    @Override
    protected ListItemInfo.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    private class ViewHolder extends ListItemInfo.ViewHolder
    {
        TextView titleView;
        TextView subtitleView;
        ImageView imageView;

        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.mylistview_item_title);
            subtitleView = (TextView) view.findViewById(R.id.mylistview_item_subtitle);
            imageView = (ImageView) view.findViewById(R.id.mylistview_item_image);
            view.setTag(this);
        }

        @Override
        public void show() {
            titleView.setText(title);
            subtitleView.setText(subtitle);
            com.chen91apps.echos.utils.ImageLoader.load(imageView, "thumbnail", imageUrl);
        }
    }
}
