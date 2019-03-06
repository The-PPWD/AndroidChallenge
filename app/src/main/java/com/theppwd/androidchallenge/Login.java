package com.theppwd.androidchallenge;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Login {

    @POST("api/v1/login")
    @FormUrlEncoded
    Call<JsonObject> authenticate(@Field("Username") String username, @Field("Password") String password);

}
