package com.chen91apps.echos.utils.listitem;

import android.content.Context;
import android.text.Layout;
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
        view = LayoutInflater.from(context).inflate(R.layout.mylistview_item, viewGroup, false);
        TextView titleView = (TextView) view.findViewById(R.id.mylistview_item_title);
        TextView subtitleView = (TextView) view.findViewById(R.id.mylistview_item_subtitle);
        ImageView imageView = (ImageView) view.findViewById(R.id.mylistview_item_image);

        imageView.setImageBitmap(data.get(i).getImage());
        titleView.setText(data.get(i).getTitle());
        subtitleView.setText(data.get(i).getSubtitle());

        return view;
    }
}
