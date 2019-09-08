package com.chen91apps.echos.utils.retrofit;

import com.chen91apps.echos.utils.articles.Favourite;
import com.chen91apps.echos.utils.articles.Post;
import com.chen91apps.echos.utils.articles.Post_Comments;
import com.chen91apps.echos.utils.articles.RSSData;
import com.chen91apps.echos.utils.userinfos.LoginLog;
import com.chen91apps.echos.utils.userinfos.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EchosService
{
    @GET("login.php")
    Call<LoginLog> login(@Query("username") String username, @Query("password") String password);

    @GET("user.php")
    Call<UserInfo> getUser();

    @GET("signup.php")
    Call<LoginLog> signup(@Query("username") String username, @Query("password") String password, @Query("nickname") String nickname);

    @GET("index.php")
    Call<String> get(@Query("type") String type);

    @GET("post.php")
    Call<Post> getPost(@Query("begin") int begin);

    @GET("follow.php")
    Call<Favourite> getFavorite(@Query("begin") int begin);

    @GET("mypost.php")
    Call<Post> getMyPost(@Query("mypost") int begin);

    @GET("check_follow.php")
    Call<Favourite> checkFavrite(@Query("key") String key);

    @GET("addfollow.php")
    Call<Favourite> addFavorite(@Query("note") String title, @Query("key") String key, @Query("content") String content);

    @GET("delfollow.php")
    Call<Favourite> delFavorite(@Query("key") String key);

    @GET("addpost.php")
    Call<Post> addPost(@Query("title") String title, @Query("content") String content);

    @GET("addfeedback.php")
    Call<String> addFeedback(@Query("content") String content);

    @GET("rss.php")
    Call<RSSData> rss(@Query("url") String url);

    @GET("post_comments.php")
    Call<Post_Comments> getComments(@Query("postid")int postid,@Query("begin")int begin);
}
