package com.chen91apps.echos.utils.listitem;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen91apps.echos.R;
import com.chen91apps.echos.utils.articles.PostComments;

public class CommentListItemInfo extends ListItemInfo {

    private PostComments.DataBean data;

    public int getId()
    {
        return data.getComment_id();
    }

    public CommentListItemInfo(PostComments.DataBean data)
    {
        super(R.layout.comment_item, data);
        this.data = data;
    }

    @Override
    protected ListItemInfo.ViewHolder getViewHolder(View view) {
        return new CommentListItemInfo.ViewHolder(view);
    }

    private class ViewHolder extends ListItemInfo.ViewHolder
    {
        LinearLayout quoteLayout;
        TextView quoteUserView;
        TextView quoteContentView;
        TextView contentView;
        TextView userView;

        public ViewHolder(View view) {
            quoteLayout = (LinearLayout) view.findViewById(R.id.comment_quote);
            quoteUserView = (TextView) view.findViewById(R.id.comment_quote_user);
            quoteContentView = (TextView) view.findViewById(R.id.comment_quote_content);
            contentView = (TextView) view.findViewById(R.id.comment_content);
            userView = (TextView) view.findViewById(R.id.comment_user_and_time);
            view.setTag(this);
        }

        @Override
        public void show() {
            if (data.getQuote() == null)
            {
                quoteLayout.setVisibility(View.GONE);
            } else
            {
                quoteUserView.setText("引用 " + data.getQuote().getAuthor() + "：");
                quoteContentView.setText(data.getQuote().getContent());
            }
            contentView.setText(data.getContent());
            userView.setText(data.getAuthor() + " 发表于 " + data.getCreate_time());
        }
    }
}
