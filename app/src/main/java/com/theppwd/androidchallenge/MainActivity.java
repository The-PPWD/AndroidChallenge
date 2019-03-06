package com.theppwd.androidchallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    /* v Variable declarations */

    final private String BASE_URL = "https://challenge.myriadapps.com";

    private Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private Login login = retrofit.create(Login.class);

    private EditText etUsername, etPassword;

    private String username, password;

    private Button btLogin;

    /* ^ Variable declarations */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* v Retrieve UI elements */

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        btLogin = (Button) findViewById(R.id.login);

        /* ^ Retrieve UI elements */

        /* v Login button onClick */

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* v Retrieve text data from "Username" and "Password" text boxes */

                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                /* ^ Retrieve text data from "Username" and "Password" text boxes */

                /* v Authenticating to and retrieving data from the login login */

                login.authenticate(username, password).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("DEBUG: ", response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

                /* ^ Authenticating to and retrieving data from the login login */

            }
        });

        /* ^ Login button onClick */

    }

}
