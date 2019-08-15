package com.chen91apps.echos.utils.listitem;

import android.view.View;

public abstract class ListItemInfo {
    private int layoutId;
    private ViewHolder viewHolder;

    public ListItemInfo(int layoutId)
    {
        this.layoutId = layoutId;
    }

    public int getLayoutId() {
        return this.layoutId;
    }

    protected abstract ViewHolder getViewHolder(View view);

    public void setup(View view)
    {
        this.viewHolder = getViewHolder(view);
    }

    public ViewHolder getViewHolder()
    {
        return this.viewHolder;
    }

    public abstract class ViewHolder
    {
        public abstract void show();
    }
}
