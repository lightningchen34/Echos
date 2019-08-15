package com.chen91apps.echos.utils.listitem;

import android.view.View;
import android.widget.TextView;

import com.chen91apps.echos.R;

public class PlainListItemInfo extends BaseListItemInfo {

    private String title;
    private String subtitle;

    private ViewHolder vh;

    public PlainListItemInfo(String title, String subtitle)
    {
        super(R.layout.mylistview_item_plain);
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    public void setup(View view) {
        vh = new ViewHolder();
        vh.title = (TextView) view.findViewById(R.id.mylistview_item_title);
        vh.subtitle = (TextView) view.findViewById(R.id.mylistview_item_subtitle);
        view.setTag(this);
    }

    @Override
    public void show() {
        vh.title.setText(title);
        vh.subtitle.setText(subtitle);
    }

    private class ViewHolder
    {
        TextView title;
        TextView subtitle;
    }
}
