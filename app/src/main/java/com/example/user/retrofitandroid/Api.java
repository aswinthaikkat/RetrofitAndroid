package com.example.user.retrofitandroid;



import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://simplifiedcoding.net/demos/";
    @GET("marvel")
    Call <List<Marvel>> getMarvel();

    @GET("marvel")
    Observable <List<Marvel>> getMarvel2();
}
