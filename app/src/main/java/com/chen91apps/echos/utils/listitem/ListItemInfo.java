package com.chen91apps.echos.utils.listitem;

import android.view.View;

public abstract class ListItemInfo {
    private int layoutId;
    private View view;
    private ViewHolder viewHolder;

    private Object content;

    public ListItemInfo(int layoutId, Object content)
    {
        this.layoutId = layoutId;
        this.view = null;
        this.viewHolder = null;
        this.content = content;
    }

    public View getView() {
        return view;
    }

    public Object getContent() {
        return content;
    }

    public int getLayoutId() {
        return this.layoutId;
    }

    protected abstract ViewHolder getViewHolder(View view);

    public void setup(View view)
    {
        this.view = view;
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
