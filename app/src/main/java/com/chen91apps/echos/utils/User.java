package com.chen91apps.echos.utils;

import com.chen91apps.echos.MainActivity;
import com.chen91apps.echos.utils.retrofit.EchosService;
import com.chen91apps.echos.utils.retrofit.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void init(EchosService echosService)
    {
        /*
        cookie = MainActivity.acache.getAsString("cookie");
        System.out.println("cookie");
        System.out.println(cookie);
        */
        Call<String> call = echosService.getUser();
        Callback<String> cb = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String str = response.body();
                System.out.println(str);
                System.out.println("go");
                if (str == "error")
                {
                    //
                } else
                {
                    info = new UserInfo();
                    info.username = str;
                    info.nickname = str;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Fail");
            }
        };

        call.enqueue(cb);
    }

    public boolean checkLogin() {
        return (info != null);
    }

    public UserInfo getInfo()
    {
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
