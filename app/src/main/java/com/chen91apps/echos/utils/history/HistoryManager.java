package com.chen91apps.echos.utils.history;

import com.chen91apps.echos.MainActivity;
import com.chen91apps.echos.utils.articles.ArticlePack;
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

    public static void setHistoryCount(int _count)
    {
        count = _count;
        MainActivity.acache.put("history_count", Integer.valueOf(count));
    }

    public static HistoryBean getHistory(int index)
    {
        if (index <= 0 || index > getHistoryCount())
        {
            return null;
        } else
        {
            String str = MainActivity.acache.getAsString("history_" + index);
            HistoryBean bean = new Gson().fromJson(str, new TypeToken<HistoryBean>(){}.getType());
            if (bean == null) return null;
            String x = MainActivity.acache.getAsString("history_key_" + bean.getContent().getKey());
            if (x != null && x.equals(index + ""))
                return bean;
            else
                return null;
        }
    }

    public static void addHistory(int type, ArticlePack content)
    {
        addHistory(new HistoryBean(type, content));
    }

    public static void addHistory(HistoryBean bean)
    {
        setHistoryCount(getHistoryCount() + 1);
        bean.setId(getHistoryCount());
        String str = new Gson().toJson(bean);
        MainActivity.acache.put("history_" + getHistoryCount(), str);
        MainActivity.acache.put("history_key_" + bean.getContent().getKey(), getHistoryCount() + "");
    }

    public static void clearHistory()
    {
        setHistoryCount(0);
    }

    public static List<HistoryBean> getHistoryList(int begin)
    {
        List<HistoryBean> ret = new LinkedList<>();
        if (begin <= 0 || begin > getHistoryCount())
            begin = getHistoryCount();
        for (int i = 0; ret.size() < 20; ++i)
        {
            if (begin - i > 0)
            {
                HistoryBean bean = getHistory(begin - i);
                if (bean != null) {
                    ret.add(bean);
                }
            } else
            {
                break;
            }
        }
        return ret;
    }

    public static class HistoryBean
    {
        private int id;
        private int type;
        private ArticlePack content;

        public HistoryBean(int type, ArticlePack content) {
            this.type = type;
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public ArticlePack getContent() {
            return content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
