package com.chen91apps.echos.channel;

import com.chen91apps.echos.MainActivity;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ChannelInfo {
    public static ChannelInfo info = new ChannelInfo();

    private LinkedList<String> categories;

    private ChannelInfo()
    {
        categories = new LinkedList<>();
        categories.add("推荐");
        categories.add("娱乐");
        categories.add("军事");
        categories.add("教育");
        categories.add("文化");
        categories.add("健康");
        categories.add("财经");
        categories.add("体育");
        categories.add("汽车");
        categories.add("科技");
        categories.add("社会");
    }

    public static String getTitle(int index)
    {
        return info.categories.get(index);
    }

    public static String getString(int index)
    {
        if (index == 0) return "";
        return info.categories.get(index);
    }

    public static List<Integer> getIntList(List<ChannelBean> list, int size)
    {
        List<Integer> ret = new LinkedList<Integer>();
        for (int i = 1; i <= size; ++i) {
            ChannelBean s = list.get(i);
            for (int j = 0; j < info.categories.size(); ++j)
                if (s.getName().equals(getTitle(j)))
                    ret.add(j);
        }
        return ret;
    }

    public static int size()
    {
        return info.categories.size();
    }

    public static List<Integer> getIndexes()
    {
        String str = MainActivity.acache.getAsString("channel_select");
        Type type = new TypeToken<LinkedList<Integer>>(){}.getType();
        List<Integer> indexes = new Gson().fromJson(str, type);
        if (indexes == null)
        {
            indexes = new LinkedList<>();
        }
        return indexes;
    }

    public static void putIndexes(List<Integer> indexes)
    {
        String str = new Gson().toJson(indexes);
        MainActivity.acache.put("channel_select", str);
    }
}
