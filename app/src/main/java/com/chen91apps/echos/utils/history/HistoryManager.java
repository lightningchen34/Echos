package com.chen91apps.echos.utils.history;

import com.chen91apps.echos.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

public class HistoryManager {
    private static Integer count;

    private HistoryManager()
    {
        //
    }

    public static int getHistoryCount()
    {
        if (count == null) {
            count = (Integer) MainActivity.acache.getAsObject("history_count");
            if (count == null)
                count = 0;
        }
        return count;
    }

    public static void setHistoryCount(int count)
    {
        MainActivity.acache.put("history_count", Integer.valueOf(count));
    }

    public static HistoryBean getHistory(int index)
    {
        if (index <= 0 || index > count)
        {
            return null;
        } else
        {
            String str = MainActivity.acache.getAsString("history_" + index);
            HistoryBean bean = new Gson().fromJson(str, new TypeToken<HistoryBean>(){}.getType());
            return bean;
        }
    }

    public static void addHistory(int type, Object content)
    {
        addHistory(new HistoryBean(type, content));
    }

    public static void addHistory(HistoryBean bean)
    {
        String str = new Gson().toJson(bean);
        setHistoryCount(getHistoryCount() + 1);
        MainActivity.acache.put("history_" + getHistoryCount(), str);
    }

    public static void clearHistory()
    {
        setHistoryCount(0);
    }

    public static List<HistoryBean> getHistoryList(int begin)
    {
        List<HistoryBean> ret = new LinkedList<>();
        if (begin <= 0 || begin > count)
            begin = count;
        for (int i = 0; i < 20; ++i)
        {
            if (begin - i > 0)
            {
                ret.add(getHistory(begin - i));
            } else
            {
                break;
            }
        }
        return ret;
    }

    public static class HistoryBean
    {
        private int type;
        private Object content;

        public HistoryBean(int type, Object content) {
            this.type = type;
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public Object getContent() {
            return content;
        }
    }
}
