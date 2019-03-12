package com.theppwd.androidchallenge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Events extends AppCompatActivity {

    final private String BASE_URL = "https://challenge.myriadapps.com";

    private Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
            .build();

    private RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getSharedPreferences("token", Context.MODE_PRIVATE).edit().remove("token").apply();
        startActivity(new Intent(Events.this, Login.class));
        Toast.makeText(this, "Logout Succeeded.", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.events_toolbar);
        setSupportActionBar(toolbar);

        retrofitInterface.getJsonArray(BASE_URL + "/api/v1/events", getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", null)).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {

                JsonObject jsonObject = new JsonObject();

                ArrayList<String> ids = new ArrayList<>(), headers = new ArrayList<>(), startDateTimes = new ArrayList<>(), endDateTimes = new ArrayList<>(), imageUrls = new ArrayList<>();

                SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                SimpleDateFormat newFormat = new SimpleDateFormat("M/d/yy h:mm a", Locale.US);
                SimpleDateFormat hourFormat = new SimpleDateFormat("h:mm a", Locale.US);

                if (response.body() != null) {
                    for (JsonElement event : response.body()) {
                        jsonObject.add(event.getAsJsonObject().get("id").toString(), event);

                        ids.add(event.getAsJsonObject().get("id").toString());

                        headers.add(event.getAsJsonObject().get("title").toString().substring(1, event.getAsJsonObject().get("title").toString().length() -1));

                        try {
                            startDateTimes.add(newFormat.format(oldFormat.parse(event.getAsJsonObject().get("start_date_time").toString().substring(1, event.getAsJsonObject().get("start_date_time").toString().length() - 7))));
                            endDateTimes.add(hourFormat.format(oldFormat.parse(event.getAsJsonObject().get("end_date_time").toString().substring(1, event.getAsJsonObject().get("end_date_time").toString().length() - 7))));
                        } catch (ParseException e) {
                            startDateTimes.add("Error getting date.");
                            endDateTimes.add("Error getting date.");

                            e.printStackTrace();
                        }

                        imageUrls.add(event.getAsJsonObject().get("image_url").toString().substring(1, event.getAsJsonObject().get("image_url").toString().length() - 1));
                    }

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    recyclerView.setAdapter(new EventsViewAdapter(Events.this, ids, headers, startDateTimes, endDateTimes, imageUrls));
                    recyclerView.setLayoutManager(new LinearLayoutManager(Events.this));

                } else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                Toast.makeText(Events.super.getBaseContext(), "Retrieve failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
