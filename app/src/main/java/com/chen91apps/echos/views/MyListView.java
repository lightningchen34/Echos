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
                if(firstVisibleItem == 0)
                {
                    this.canPull = true;
                    startY = (int)ev.getY();
                }
                if(firstVisibleItem >= getCount()-visibleitems-1 && firstVisibleItem <= getCount()-visibleitems)
                {
                    this.footer_canPull = true;
                    startY = (int)ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(canPull)
                {
                    touchMove(ev);
                }
                if(footer_canPull)
                {
                    footer_touchMove(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
                canPull = false;
                footer_canPull = false;
                if(footer_curState == FOOT_RELEASE)
                {
                    footer_curState = FOOT_RELEASING;
                    paddingBottom(0);
                    refreshFooterByState();
                    myListener.toUpdateListView();
                }
                else if(curState == RELEASE)
                {
                    curState = RELEASING;
                    paddingTop(0);
                    refreshHeaderByState();
                    myListener.toRefreshListView();
                }
                else
                {
                    curState = NORMAL;
                    footer_curState = NORMAL;
                    refreshHeaderByState();
                    refreshFooterByState();
                    paddingTop(-headerHeight);
                    paddingBottom(-footerHeight);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void footer_touchMove(MotionEvent ev)
    {
        int tempY = (int)ev.getY();
        int space = startY - tempY;
        int bottomdding = space - footerHeight;
        paddingBottom(bottomdding);
        if(space>footerHeight && space<footerHeight+100 && scrollStates == SCROLL_STATE_TOUCH_SCROLL)
        {
            footer_curState = FOOT_PULL;
            refreshFooterByState();
        }
        if(space>footerHeight+100)
        {
            footer_curState = FOOT_RELEASE;
            refreshFooterByState();
        }
        if(space<headerHeight)
        {
            footer_curState = FOOT_RELEASING;
            refreshFooterByState();
        }
    }

    private void touchMove(MotionEvent ev)
    {
        int tempY = (int)ev.getY();
        int space = tempY - startY;
        int topdding = space - headerHeight;
        paddingTop(topdding);
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
                tv.setText("this is the releasing state");
                break;
        }
    }

    private void refreshFooterByState()
    {
        TextView tv = (TextView)footerView.findViewById(R.id.footer_textinfo);
        switch(footer_curState)
        {
            case FOOT_NORMAL:
                tv.setText("this is the normal state");
                break;
            case FOOT_PULL:
                tv.setText("this is the pull state");
                break;
            case FOOT_RELEASE:
                tv.setText("this is the release state");
                break;
            case FOOT_RELEASING:
                tv.setText("this is the releasing state");
                break;
        }
    }

    public void refreshFinish()
    {
        curState = NORMAL;
        footer_curState = FOOT_NORMAL;
        paddingTop(-headerHeight);
        paddingBottom(-footerHeight);
        refreshFooterByState();
        refreshHeaderByState();
        System.out.println("now the refresh is over");
    }

    private void notifyView(View view)
    {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if(p == null)
        {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0,0,p.width);
        int height;
        int tempheight = p.height;
        if(tempheight>0)
            height = MeasureSpec.makeMeasureSpec(tempheight,MeasureSpec.EXACTLY);
        else
            height = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        view.measure(width,height);
    }

    private void paddingBottom(int pt)
    {
        footerView.setPadding(footerView.getPaddingLeft(),
                footerView.getPaddingTop(),
                footerView.getPaddingRight(), pt);
        footerView.invalidate();
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
    private int visibleitems;
    private int startY;

    int curState = 0;
    int footer_curState = 0;
    final private int PULL = 1;
    final private int NORMAL = 0;
    final private int RELEASE = 2;
    final private int RELEASING = 3;

    final private int FOOT_NORMAL = 0;
    final private int FOOT_PULL = 1;
    final private int FOOT_RELEASE = 2;
    final private int FOOT_RELEASING = 3;

    int headerHeight;
    int footerHeight;

    boolean canPull = false;
    boolean footer_canPull = false;
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
        headerView = View.inflate(getContext(),R.layout.mylistview_item_header,null);
        footerView = View.inflate(getContext(),R.layout.mylistview_item_footer,null);

        notifyView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        paddingTop(-headerHeight);

        notifyView(footerView);
        footerHeight = footerView.getMeasuredHeight();
        paddingBottom(-footerHeight);

        this.setOnScrollListener(this);
        this.addHeaderView(headerView);
        this.addFooterView(footerView);
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
        this.visibleitems = i1;
    }



}
