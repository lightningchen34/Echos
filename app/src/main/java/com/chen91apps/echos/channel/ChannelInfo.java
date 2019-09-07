package com.chen91apps.echos.channel;

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

    public static List<Integer> getIntList(List<String> list)
    {
        List<Integer> ret = new LinkedList<Integer>();
        for (String s : list) {
            for (int i = 0; i < info.categories.size(); ++i)
                if (s.equals(getTitle(i)))
                    ret.add(i);
        }
        return ret;
    }


}
