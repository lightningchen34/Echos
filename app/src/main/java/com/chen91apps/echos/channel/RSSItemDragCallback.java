package com.chen91apps.echos.channel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chen91apps.echos.R;
import com.chen91apps.echos.utils.ThemeColors;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

public class RSSItemDragCallback extends ItemTouchHelper.Callback {
    private static final String TAG = "ItemDragCallback";
    private RSSChannelAdapter mAdapter;
    private Paint mPaint;    //虚线画笔
    private int mPadding;   //虚线框框跟按钮间的距离

    private Context context;

    public RSSItemDragCallback(RSSChannelAdapter mAdapter, int mPadding, Context context) {
        this.mAdapter = mAdapter;
        this.mPadding = mPadding;
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        PathEffect pathEffect = new DashPathEffect(new float[]{5f, 5f}, 5f);    //虚线
        mPaint.setPathEffect(pathEffect);
        this.context = context;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //固定位置及tab下面的channel不能拖动
        if (viewHolder.getLayoutPosition() < mAdapter.getFixSize() + 1 || viewHolder.getLayoutPosition() > mAdapter.getSelectedSize()) {
            return 0;
        }
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();   //拖动的position
        int toPosition = target.getAdapterPosition();     //释放的position
        //固定位置及tab下面的channel不能拖动
        if (toPosition < mAdapter.getFixSize() + 1 || toPosition > mAdapter.getSelectedSize())
            return false;
        mAdapter.itemMove(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (dX != 0 && dY != 0 || isCurrentlyActive) {
            //长按拖拽时底部绘制一个虚线矩形
            c.drawRect(viewHolder.itemView.getLeft(),viewHolder.itemView.getTop()-mPadding,viewHolder.itemView.getRight(),viewHolder.itemView.getBottom(),mPaint);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState==ACTION_STATE_DRAG){
            //长按时调用
            RSSChannelAdapter.ChannelHolder holder= (RSSChannelAdapter.ChannelHolder) viewHolder;
            holder.name.setBackgroundColor(ThemeColors.getColor(context, R.attr.background_color));
            holder.delete.setVisibility(View.GONE);
            holder.name.setElevation(5f);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        RSSChannelAdapter.ChannelHolder holder= (RSSChannelAdapter.ChannelHolder) viewHolder;
        holder.name.setBackgroundColor(ThemeColors.getColor(context, R.attr.background2_color));
        holder.name.setElevation(0f);
        holder.delete.setVisibility(View.VISIBLE);
    }
}