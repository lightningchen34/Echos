package com.chen91apps.echos.utils.listitem;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        if (view == null) {
            view = LayoutInflater.from(context).inflate(data.get(i).getLayoutId(), viewGroup, false);
            data.get(i).setup(view);
        }
        ((ListItemInfo.ViewHolder) view.getTag()).show();
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
