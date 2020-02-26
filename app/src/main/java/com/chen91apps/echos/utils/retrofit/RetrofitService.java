package com.chen91apps.echos.utils.retrofit;

import com.chen91apps.echos.utils.articles.News;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService
{
    //https://api2.newsminer.net/svc/news/queryNewsList?size=15&startDate=2019-07-01&endDate=2019-07-03&words=特朗普&categories=科技
    @GET("svc/news/queryNewsList")
    Call<News> getNews(@Query("size")int size, @Query("startDate")String startDate,@Query("endDate")String endDate,@Query("words")String words,@Query("categories")String type);
}
