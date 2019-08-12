package com.chen91apps.echos;

import java.net.URL;

class ListInfoPair
{
    public final static int TYPE_LIST = 0;
    public final static int TYPE_MANAGER = 1;

    public String title;
    public String url;
    public int type;

    ListInfoPair(String title, String url, int type)
    {
        this.title = title;
        this.url = url;
        this.type = type;
    }
};
