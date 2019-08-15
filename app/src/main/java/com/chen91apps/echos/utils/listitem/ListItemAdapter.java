package com.chen91apps.echos.utils.listitem;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chen91apps.echos.R;

import java.util.LinkedList;

public class ListItemAdapter extends BaseAdapter {

    private LinkedList<ListItemInfo> data;
    private Context context;

    public ListItemAdapter(LinkedList<ListItemInfo> data, Context context)
    {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mylistview_item, viewGroup, false);

            vh = new ViewHolder();
            vh.titleView = (TextView) view.findViewById(R.id.mylistview_item_title);
            vh.subtitleView = (TextView) view.findViewById(R.id.mylistview_item_subtitle);
            vh.imageView = (ImageView) view.findViewById(R.id.mylistview_item_image);
            view.setTag(vh);
        } else
        {
            vh = (ViewHolder) view.getTag();
        }
        com.chen91apps.echos.utils.ImageLoader.load(vh.imageView, "thumbnail", data.get(i).getImageURL());
        vh.titleView.setText(data.get(i).getTitle());
        vh.subtitleView.setText(data.get(i).getSubtitle());

        return view;
    }

    Bitmap getBitmap()
    {
        return null;
    }

    class ViewHolder
    {
        public TextView titleView;
        public TextView subtitleView;
        public ImageView imageView;
    }

    class ImageLoader
    {
        private ImageView imageView;
        // private
    }
}
