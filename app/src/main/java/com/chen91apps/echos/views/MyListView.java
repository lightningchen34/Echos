package com.chen91apps.echos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class MyListView extends ListView implements AbsListView.OnScrollListener{

    private View headerView;
    private View footerView;
    private MyListViewPullListener pullListener;

    public MyListView(Context context) {
        super(context);
        init(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private void init(Context context)
    {
        // TODO INIT
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        // TODO
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        // TODO
    }

    public interface MyListViewPullListener
    {
        void toRefreshListView();
        void toUpdateListView();
    }

    public void setPullListener(MyListViewPullListener pullListener)
    {
        this.pullListener = pullListener;
    }
}
