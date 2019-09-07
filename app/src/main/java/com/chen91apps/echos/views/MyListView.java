package com.chen91apps.echos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.chen91apps.echos.R;

public class MyListView extends ListView implements AbsListView.OnScrollListener{

    private View headerView;
    private View footerView;

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
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch(ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                System.out.println("found touch down");
                if(firstVisibleItem == 0)
                {
                    this.canPull = true;
                    startY = (int)ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("found touch move");
                if(canPull)
                {
                    touchMove(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("found touch up");
                canPull = false;
                if(curState == RELEASE)
                {
                    curState = RELEASING;
                    refreshHeaderByState();
                    myListener.toRefreshListView();
                }
                else
                {
                    curState = NORMAL;
                    refreshHeaderByState();
                    paddingTop(-headerHeight);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void touchMove(MotionEvent ev)
    {
        int tempY = (int)ev.getY();
        int space = tempY - startY;
        int topdding = space - headerHeight;
        paddingTop(topdding);
        System.out.println("this is the space at touchMovwe "+space);
        if(space>headerHeight && space<headerHeight+100 && scrollStates == SCROLL_STATE_TOUCH_SCROLL)
        {
            curState = PULL;
            refreshHeaderByState();
        }
        if(space>headerHeight+100)
        {
            curState = RELEASE;
            refreshHeaderByState();
        }
        if(space<headerHeight)
        {
            curState = NORMAL;
            refreshHeaderByState();
        }
        return;
    }

    private void refreshHeaderByState()
    {
        TextView tv = (TextView)headerView.findViewById(R.id.textinfo);
        switch(curState)
        {
            case NORMAL:
                tv.setText("this is the normal state");
                break;
            case PULL:
                tv.setText("this is the pull state");
                break;
            case RELEASE:
                tv.setText("this is the release state");
                break;
            case RELEASING:
                tv.setText("this si the releasing state");
                break;
        }
        return;
    }

    public void refreshFinish()
    {
        curState = NORMAL;
        paddingTop(-headerHeight);
        refreshHeaderByState();
    }

    private void notifyView(View view)
    {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if(p == null)
        {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0,0,p.width);
        int height;int tempheight = p.height;
        if(tempheight>0)
            height = MeasureSpec.makeMeasureSpec(tempheight,MeasureSpec.EXACTLY);
        else
            height = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
    }

    private void paddingTop(int pt)
    {
        headerView.setPadding(headerView.getPaddingLeft(),
                pt,headerView.getPaddingRight(),
                headerView.getPaddingBottom());
        headerView.invalidate();
    }

    private int scrollStates;
    private int firstVisibleItem;
    private int startY;

    int curState = 0;
    final private int PULL = 1;
    final private int NORMAL = 0;
    final private int RELEASE = 2;
    final private int RELEASING = 3;
    int headerHeight;

    boolean canPull = false;
    private MyListViewPullListener myListener;

    public interface MyListViewPullListener
    {
        void toRefreshListView();
        void toUpdateListView();
    }
    public void setPullListener(MyListViewPullListener listener)
    {
        this.myListener = listener;
    }

    private void init(Context context)
    {
        headerView = View.inflate(getContext(), R.layout.mylistview_item_header,null);

        notifyView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        paddingTop(-headerHeight);

        this.setOnScrollListener(this);
        this.addHeaderView(headerView);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i)
    {
        this.scrollStates = i;
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2)
    {
        this.firstVisibleItem = i;
    }



}
