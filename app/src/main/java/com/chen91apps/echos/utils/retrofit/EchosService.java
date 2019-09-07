package com.chen91apps.echos.utils.retrofit;

import com.chen91apps.echos.utils.userinfos.LoginLog;
import com.chen91apps.echos.utils.userinfos.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EchosService
{
    @GET("login.php")
    Call<LoginLog> login(@Query("username") String username, @Query("password") String password);

    @GET("user.php")
    Call<UserInfo> getUser();

    @GET("index.php")
    Call<String> get(@Query("type") String type);
}
