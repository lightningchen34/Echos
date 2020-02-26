package com.chen91apps.echos.channel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chen91apps.echos.R;

import java.util.Collections;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ChannelBean> mList;
    private List<ChannelBean> recommendList;  //推荐频道
    private int selectedSize;
    private int fixSize;           //已选频道中固定频道大小
    private boolean isRecommend;   //当前是否显示推荐频道
    private onItemRangeChangeListener onItemRangeChangeListener;


    public ChannelAdapter(Context mContext, List<ChannelBean> mList, List<ChannelBean> recommendList) {
        this.mContext = mContext;
        this.mList = mList;
        this.recommendList = recommendList;
    }

    public void setOnItemRangeChangeListener(ChannelAdapter.onItemRangeChangeListener onItemRangeChangeListener) {
        this.onItemRangeChangeListener = onItemRangeChangeListener;
    }

    public int getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(int selectedSize) {
        this.selectedSize = selectedSize;
    }

    public int getFixSize() {
        return fixSize;
    }

    public void setFixSize(int fixSize) {
        this.fixSize = fixSize;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        if (viewType == R.layout.adapter_channel) {
            return new ChannelHolder(view);
        } else if (viewType == R.layout.adapter_tab) {
            return new TabHolder(view);
        } else {
            return new TitleHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChannelHolder) {
            setChannel((ChannelHolder) holder, mList.get(position));
        } else if (holder instanceof TabHolder) {
            setTab((TabHolder) holder);
        } else {

        }
    }

    private void setChannel(final ChannelHolder holder, ChannelBean bean) {
        final int position = holder.getLayoutPosition();
        holder.name.setText(bean.getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getLayoutPosition() < selectedSize + 1) {
                    //tab上面的 点击移除
                    if (holder.getLayoutPosition() > fixSize) {
                        removeFromSelected(holder);
                    }
                } else {
                    //tab下面的 点击添加到已选频道
                    selectedSize++;
                    itemMove(holder.getLayoutPosition(), selectedSize);
                    notifyItemChanged(selectedSize);
                    if (onItemRangeChangeListener != null) {
                        onItemRangeChangeListener.refreshItemDecoration();
                    }
                }
            }
        });
        holder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //返回true 防止长按拖拽事件跟点击事件冲突
                return true;
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromSelected(holder);
            }
        });

        //tab下面的不显示删除按钮
        if (position - 1 < fixSize || position > selectedSize) {
            holder.delete.setVisibility(View.GONE);
        } else {
            holder.delete.setVisibility(View.VISIBLE);
        }
    }

    private void setTab(final TabHolder holder) {
        holder.recommend.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Layout layout = holder.recommend.getLayout();
                holder.recommend.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        holder.recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecommend) {
                    holder.recommend.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    isRecommend = true;
                    mList.addAll(recommendList);
                    notifyDataSetChanged();
                }
            }
        });
        final ViewTreeObserver observer = holder.itemView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //计算tab的Y，用于后面的动画 需要一直监听
                return true;
            }
        });
    }

    private void removeFromSelected(ChannelHolder holder) {
        int position = holder.getLayoutPosition();
        holder.delete.setVisibility(View.GONE);
        ChannelBean bean = mList.get(position);
        //移除的频道属于当前tab显示的频道，直接调用系统的移除动画
        itemMove(position, selectedSize + 1);
        notifyItemRangeChanged(selectedSize + 1, 1);
        if (onItemRangeChangeListener != null) {
            //如果设置了itemDecoration，必须调用recyclerView.invalidateItemDecorations(),否则间距会不对
            onItemRangeChangeListener.refreshItemDecoration();
        }
        selectedSize--;
    }

    private void removeAnimation(final View view, final float x, final float y, final int position) {
        final int fromX = view.getLeft();
        final int fromY = view.getTop();
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX", 0, x - fromX);
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "translationY", 0, y - fromY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY, alpha);
        set.setDuration(350);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                recommendList.add(0, mList.get(position));
                mList.remove(position);
                notifyItemRemoved(position);
                onItemRangeChangeListener.refreshItemDecoration();
                //这里需要重置view的属性
                resetView(view, x - fromX, y - fromY);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 重置view的位置
     *
     * @param view
     * @param toX
     * @param toY
     */
    private void resetView(View view, float toX, float toY) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX", -toX, 0);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "translationY", -toY, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY, alpha);
        set.setDuration(0);
        set.setStartDelay(5);
        set.start();
    }

    void itemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getLayoutId();
    }

    class ChannelHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView delete;

        public ChannelHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.channel_name);
            delete = itemView.findViewById(R.id.channel_delete);
        }
    }

    class TabHolder extends RecyclerView.ViewHolder {
        TextView recommend;

        public TabHolder(View itemView) {
            super(itemView);
            recommend = itemView.findViewById(R.id.recommend_channel);
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder {

        public TitleHolder(View itemView) {
            super(itemView);
        }
    }

    public interface onItemRangeChangeListener {
        void refreshItemDecoration();
    }
}