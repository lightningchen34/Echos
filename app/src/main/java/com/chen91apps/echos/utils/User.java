package com.chen91apps.echos.utils;

import android.content.Context;
import android.widget.Toast;

import com.chen91apps.echos.MainActivity;
import com.chen91apps.echos.utils.retrofit.EchosService;
import com.chen91apps.echos.utils.userinfos.LoginLog;
import com.chen91apps.echos.utils.userinfos.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User {

    private UserInfo info;
    private OnLoginStateChanged loginStateChanged;
    private EchosService echosService;

    public User(EchosService echosService, OnLoginStateChanged loginStateChanged)
    {
        this.echosService = echosService;
        this.loginStateChanged = loginStateChanged;
        this.info = null;
        init();
    }

    public void init()
    {
        Call<UserInfo> call = echosService.getUser();
        Callback<UserInfo> cb = new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                info = response.body();
                if (info.getCreate_time() == -1)
                {
                    loginStateChanged.OnLoginStateChanged(false);
                } else
                {
                    loginStateChanged.OnLoginStateChanged(true);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                System.out.println("Fail");
            }
        };

        call.enqueue(cb);
    }

    public void login(String username, String password, LoginResponder loginResponder)
    {
        Call<LoginLog> call = echosService.login(username, password);
        Callback<LoginLog> cb = new Callback<LoginLog>() {
            @Override
            public void onResponse(Call<LoginLog> call, Response<LoginLog> response) {
                LoginLog log = response.body();
                info = log.getUser();
                if (log.getState() == 1)
                {
                    loginStateChanged.OnLoginStateChanged(true);
                    if (loginResponder != null)
                        loginResponder.LoginResponder(true, log.getLog());
                } else
                {
                    loginStateChanged.OnLoginStateChanged(false);
                    if (loginResponder != null)
                        loginResponder.LoginResponder(false, log.getLog());
                }
            }

            @Override
            public void onFailure(Call<LoginLog> call, Throwable t) {
                System.out.println("Fail");
            }
        };

        call.enqueue(cb);
    }

    public void logout(LogoutResponder logoutResponder)
    {
        Call<LoginLog> call = echosService.login("", "");
        Callback<LoginLog> cb = new Callback<LoginLog>() {
            @Override
            public void onResponse(Call<LoginLog> call, Response<LoginLog> response) {
                LoginLog log = response.body();
                info = log.getUser();
                if (log.getState() == 1)
                {
                    loginStateChanged.OnLoginStateChanged(true);
                    if (logoutResponder != null)
                        logoutResponder.LogoutResponder(false);
                } else
                {
                    loginStateChanged.OnLoginStateChanged(false);
                    if (logoutResponder != null)
                        logoutResponder.LogoutResponder(true);
                }
            }

            @Override
            public void onFailure(Call<LoginLog> call, Throwable t) {
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

    public interface OnLoginStateChanged
    {
        void OnLoginStateChanged(boolean state);
    }

    public interface LoginResponder
    {
        void LoginResponder(boolean state, String log);
    }

    public interface LogoutResponder
    {
        void LogoutResponder(boolean state);
    }
}
