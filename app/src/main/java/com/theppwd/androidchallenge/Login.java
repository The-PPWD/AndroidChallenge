package com.theppwd.androidchallenge;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    // Base URL for the api call (1 line)
    final private String BASE_URL = "https://challenge.myriadapps.com";

    private Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
            .build();

    private RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Skips the login activity if the token is found in local storage (3 lines)
        if (getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", null) != null) {
            startActivity(new Intent(Login.this, Events.class));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btLogin = (Button) findViewById(R.id.login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Extracts the username and password from the text boxes (2 lines)
                String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();

                // Only makes the api call if there is a username and password to send (1 line)
                if (! username.isEmpty() && ! password.isEmpty()) {

                    // The api call (21 lines)
                    retrofitInterface.authenticate(username, password).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                            if (response.body() != null) {
                                try {
                                    // Attempts to store the token locally (1 line)
                                    getSharedPreferences("token", Context.MODE_PRIVATE).edit().putString("token", response.body().get("token").getAsString()).apply();

                                    // Starts the Events activity (1 line)
                                    startActivity(new Intent(Login.this, Events.class));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    onFailure(call, new Throwable());
                                }
                            } else {
                                onFailure(call, new Throwable());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull  Call<JsonObject> call, @NonNull Throwable t) {
                            Toast.makeText(Login.super.getBaseContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(Login.super.getBaseContext(), "Username & Password fields required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
