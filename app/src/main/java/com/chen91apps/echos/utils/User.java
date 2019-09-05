package com.chen91apps.echos.utils;

import com.chen91apps.echos.MainActivity;

public class User {
    public static User user = new User();

    private String cookie;

    public class UserInfo
    {
        public String username;
        public String nickname;
        public int create_time;
    }

    private UserInfo info;

    private User()
    {
        info = null;
        cookie = null;
    }

    public void init()
    {
        cookie = MainActivity.acache.getAsString("cookie");
        System.out.println("cookie");
        System.out.println(cookie);
    }

    public boolean checkLogin() {
        if (cookie == null)
        {
            return false;
        }
        // TODO
        return false;
    }

    public UserInfo getInfo()
    {
        if (info == null && cookie != null)
        {
            // TODO
        }
        return info;
    }

    public String getCookie()
    {
        return cookie;
    }

    public void setCookie(String cookie)
    {
        this.cookie = cookie;
        MainActivity.acache.put("cookie", this.cookie);
    }
}
