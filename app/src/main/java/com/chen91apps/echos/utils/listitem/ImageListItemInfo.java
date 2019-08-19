package com.chen91apps.echos.utils.listitem;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen91apps.echos.R;

public class ImageListItemInfo extends ListItemInfo {

    private String title;
    private String imageUrl_1;
    private String imageUrl_2;
    private String imageUrl_3;

    public ImageListItemInfo(String title, String imageUrl_1, String imageUrl_2, String imageUrl_3)
    {
        super(R.layout.mylistview_item_image);
        this.title = title;
        this.imageUrl_1 = imageUrl_1;
        this.imageUrl_2 = imageUrl_2;
        this.imageUrl_3 = imageUrl_3;
    }

    @Override
    protected ListItemInfo.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    private class ViewHolder extends ListItemInfo.ViewHolder
    {
        TextView titleView;
        ImageView imageView_1;
        ImageView imageView_2;
        ImageView imageView_3;

        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.mylistview_item_title);
            imageView_1 = (ImageView) view.findViewById(R.id.mylistview_item_image_1);
            imageView_2 = (ImageView) view.findViewById(R.id.mylistview_item_image_2);
            imageView_3 = (ImageView) view.findViewById(R.id.mylistview_item_image_3);
            view.setTag(this);
        }

        @Override
        public void show() {
            titleView.setText(title);
            com.chen91apps.echos.utils.ImageLoader.load(imageView_1, "thumbnail", imageUrl_1);
            com.chen91apps.echos.utils.ImageLoader.load(imageView_2, "thumbnail", imageUrl_2);
            com.chen91apps.echos.utils.ImageLoader.load(imageView_3, "thumbnail", imageUrl_3);
        }
    }
}
