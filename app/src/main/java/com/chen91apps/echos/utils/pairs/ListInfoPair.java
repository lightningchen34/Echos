package com.chen91apps.echos.utils.pairs;

public class ListInfoPair
{
    public final static int TYPE_NEWS = 0;
    public final static int TYPE_COMMUNITY = 1;
    public final static int TYPE_RSS = 2;

    public String title;
    public String url;
    public int type;

    public ListInfoPair(String title, String url, int type)
    {
        this.title = title;
        this.url = url;
        this.type = type;
    }
}
