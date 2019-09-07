package com.chen91apps.echos.channel;

public class ChannelBean {
    private String name;
    private int spanSize;
    private int layoutId;

    public ChannelBean() {
        this.name = "";
    }

    public ChannelBean(String name, int spanSize, int layoutId) {
        this.name = name;
        this.spanSize = spanSize;
        this.layoutId = layoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
