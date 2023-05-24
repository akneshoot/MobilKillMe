package com.example.practica4.viewmodel;


import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PlaceholderAPI {
    @GET("/posts/{id}")
    public Call<PlaceholderPost> getPostWithID(@Path("id") int id);

    @POST("/posts")
    public Call<PlaceholderPost> postData(@Body PlaceholderPost placeholderPost);
}

