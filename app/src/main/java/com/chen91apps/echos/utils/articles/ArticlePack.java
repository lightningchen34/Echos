package com.chen91apps.echos.utils.articles;

public class ArticlePack {
    public News.DataBean news;
    public Post.DataBean post;
    public RSSData.ItemBean rss;

    public String getKey()
    {
        if (news != null)
        {
            return "0:" + news.getUrl();
        } else if (post != null)
        {
            return "1:" + post.getPost_id();
        } else
        {
            return "2:" + rss.getLink().getValue();
        }
    }
}
