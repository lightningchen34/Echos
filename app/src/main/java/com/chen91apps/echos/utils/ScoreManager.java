package com.chen91apps.echos.utils;

import com.chen91apps.echos.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ScoreManager {
    private static ScoreManager scoreManager = new ScoreManager();

    private static HashMap<String, Float> scores;

    private static Random random = new Random();

    private static LinkedList<String> list;

    private static Comparator<Object> comp = new Comparator<Object>() {
        @Override
        public int compare(Object o, Object t1) {
            String a = (String) o;
            String b = (String) t1;
            float score = getSocre(a) - getSocre(b);
            if (score > 0) return -1;
            if (score < 0) return 1;
            return 0;
        }
    };

    private ScoreManager()
    {
    }

    public static void init()
    {
        String str = MainActivity.acache.getAsString("score_map");
        if (str == null || str.length() < 5)
        {
            scores = new HashMap<>();
        } else {
            scores = new Gson().fromJson(str, new TypeToken<HashMap<String, Float>>() {
            }.getType());
        }
        str = MainActivity.acache.getAsString("score_list");
        if (str == null || str.length() < 5) {
            list = new LinkedList<>();
        } else {
            list = new Gson().fromJson(str, new TypeToken<LinkedList<String>>() {
            }.getType());
        }
    }

    public static void save()
    {
        MainActivity.acache.put("score_map", new Gson().toJson(scores));
        MainActivity.acache.put("score_list", new Gson().toJson(list));
    }

    public static float getSocre(String key)
    {
        Float ret = scores.get(key);
        if (ret == null)
            ret = 0.0f;
        return ret;
    }

    public static void addScore(String key, float value)
    {
        scores.put(key, getSocre(key) + value);
        list.remove(key);
        list.add(key);
        Object[] arr = list.toArray();
        Arrays.sort(arr, comp);
        list.clear();
        for (Object obj : arr)
            list.add((String) obj);
        if (list.size() > 10)
            list.removeLast();

        if (random.nextInt() % 10 == 0)
            save();
    }

    public static List<String> getList()
    {
        return list;
    }
}
