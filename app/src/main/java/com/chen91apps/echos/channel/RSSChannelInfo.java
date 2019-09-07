package com.chen91apps.echos.channel;

import android.util.Pair;

import com.chen91apps.echos.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class RSSChannelInfo {
    public static RSSChannelInfo info = new RSSChannelInfo();

    private LinkedList<Pair<String, String>> categories;

    private RSSChannelInfo()
    {
    }

    public static String getTitle(int index)
    {
        return info.categories.get(index).first;
    }

    public static String getUrl(int index)
    {
        if (index == 0) return "";
        return info.categories.get(index).second;
    }

    public static int size()
    {
        return info.categories.size();
    }

    public static void load()
    {
        String str = MainActivity.acache.getAsString("channel_rss");
        Type type = new TypeToken<LinkedList<Pair<String, String>>>(){}.getType();
        info.categories = new Gson().fromJson(str, type);
        if (info.categories == null)
        {
            info.categories = new LinkedList<>();
            info.categories.add(new Pair<>("推荐", ""));
        }
    }

    public static void save()
    {
        String str = new Gson().toJson(info.categories);
        MainActivity.acache.put("channel_rss", str);
    }

    public static void set(List<Pair<String, String>> data)
    {
        info.categories.clear();
        info.categories.addAll(data);
    }

    public static List<Pair<String, String>> get()
    {
        return info.categories;
    }
}
