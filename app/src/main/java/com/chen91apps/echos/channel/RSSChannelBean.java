package com.chen91apps.echos.channel;

public class RSSChannelBean {
    private String name;
    private String url;
    private int spanSize;
    private int layoutId;

    public RSSChannelBean() {
    }

    public RSSChannelBean(String name, String url, int spanSize, int layoutId) {
        this.name = name;
        this.url = url;
        this.spanSize = spanSize;
        this.layoutId = layoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }
}
