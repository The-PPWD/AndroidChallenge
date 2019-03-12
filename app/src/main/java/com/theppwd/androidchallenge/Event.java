package com.theppwd.androidchallenge;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Event extends AppCompatActivity {

    // Base URL for the api call (1 line)
    final private String BASE_URL = "https://challenge.myriadapps.com";

    private Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
            .build();

    private RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Clear the token from local storage (1 line)
        getSharedPreferences("token", Context.MODE_PRIVATE).edit().remove("token").apply();

        // Start the login activity (1 line)
        startActivity(new Intent(Event.this, Login.class));

        // Notify a successful logout (1 line)
        Toast.makeText(this, "Logout Succeeded.", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Variable declaration for future use (11 lines)
        final Toolbar TOOLBAR;

        final ImageView IMAGE = (ImageView) findViewById(R.id.event_image);
        final TextView HEADER = (TextView) findViewById(R.id.event_header);
        final TextView DATE_TIME = (TextView) findViewById(R.id.event_date_time);
        final TextView DESCRIPTION = (TextView) findViewById(R.id.event_description);
        final TextView LOCATION = (TextView) findViewById(R.id.event_location);

        final SimpleDateFormat OLD_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        final SimpleDateFormat NEW_FORMAT = new SimpleDateFormat("M/d/yy h:mm a", Locale.US);
        final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("h:mm a", Locale.US);

        TOOLBAR = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(TOOLBAR);
        TOOLBAR.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        TOOLBAR.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Event.this, Events.class));
            }
        });

        // Retrieve arguments passed by Events (1 line)
        Bundle bundle = getIntent().getExtras();

        // A bundle argument is needed for the api call; this prevents a NullPointerException (1 line)
        if (bundle != null) {
            // The api call (49 line)
            retrofitInterface.getJsonObject(BASE_URL + "/api/v1/events/" + bundle.getString("id"), getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", null)).enqueue(new Callback<JsonObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    // If the server replied with contents in the body (1 line)
                    if (response.body() != null) {

                        // Visibility is by default set to INVISIBLE so that, if there is no internet connection, an empty activity is displayed instead of partial views (2 lines)
                        ((View) findViewById(R.id.divider)).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.speakers)).setVisibility(View.VISIBLE);

                        TOOLBAR.setTitle(response.body().get("title").toString().substring(1, response.body().get("title").toString().length() - 1));

                        // Load main image (1 line)
                        Picasso.with(Event.this).load(response.body().get("image_url").toString().substring(1, response.body().get("image_url").toString().length() - 1)).fit().into(IMAGE);

                        HEADER.setText(response.body().get("title").toString().substring(1, response.body().getAsJsonObject().get("title").toString().length() -1));

                        DESCRIPTION.setText(response.body().get("event_description").toString().substring(1, response.body().getAsJsonObject().get("event_description").toString().length() -1));

                        // Adds the location vector (1 line)
                        LOCATION.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_place_24dp, 0, 0, 0);
                        LOCATION.setText("\t" + response.body().get("location").toString().substring(1, response.body().getAsJsonObject().get("location").toString().length() -1));

                        try {
                            // Formats the date range (1 line)
                            DATE_TIME.setText(NEW_FORMAT.format(OLD_FORMAT.parse(response.body().get("start_date_time").toString().substring(1, response.body().get("start_date_time").toString().length() - 7))) + " - " + HOUR_FORMAT.format(OLD_FORMAT.parse(response.body().get("end_date_time").toString().substring(1, response.body().get("end_date_time").toString().length() - 7))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Adds a speaker fragment for every speaker (12 lines)
                        for (int lcv = 0; lcv <= response.body().get("speakers").getAsJsonArray().size() - 1; lcv++) {
                            String id = response.body().get("speakers").getAsJsonArray().get(lcv).getAsJsonObject().get("id").toString();

                            Bundle fragmentBundle = new Bundle();
                            fragmentBundle.putString("id", id);

                            SpeakerFragment speakerFragment = new SpeakerFragment();
                            speakerFragment.setArguments(fragmentBundle);

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.activity_event, speakerFragment).commit();
                        }

                    } else {
                        onFailure(call, new Throwable());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    Toast.makeText(Event.super.getBaseContext(), "Retrieve failed.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Event.super.getBaseContext(), "Retrieve failed.", Toast.LENGTH_SHORT).show();
        }

    }
}
