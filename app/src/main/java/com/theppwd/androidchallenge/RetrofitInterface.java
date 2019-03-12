package com.theppwd.androidchallenge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetrofitInterface {

    @POST("api/v1/login")
    @FormUrlEncoded
    Call<JsonObject> authenticate(@Field("Username") String username, @Field("Password") String password);

    @GET
    Call<JsonArray> getJsonArray(@Url String url, @Header("authorization") String token);

    @GET
    Call<JsonObject> getJsonObject(@Url String url, @Header("authorization") String token);

}
