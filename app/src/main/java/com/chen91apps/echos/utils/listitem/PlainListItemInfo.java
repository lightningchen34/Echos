package com.chen91apps.echos.utils.listitem;

import android.view.View;
import android.widget.TextView;

import com.chen91apps.echos.R;

public class PlainListItemInfo extends ListItemInfo {

    private String title;
    private String subtitle;

    public PlainListItemInfo(String title, String subtitle)
    {
        super(R.layout.mylistview_item_plain);
        this.title = title;
        this.subtitle = subtitle;
    }

    @Override
    protected ListItemInfo.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    private class ViewHolder extends ListItemInfo.ViewHolder
    {
        TextView titleView;
        TextView subtitleView;

        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.mylistview_item_title);
            subtitleView = (TextView) view.findViewById(R.id.mylistview_item_subtitle);
            view.setTag(this);
        }

        @Override
        public void show() {
            titleView.setText(title);
            subtitleView.setText(subtitle);
        }
    }
}
