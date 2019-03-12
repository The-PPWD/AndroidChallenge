package com.theppwd.androidchallenge;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpeakerFragment extends Fragment {

    final private String BASE_URL = "https://challenge.myriadapps.com";

    private Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
            .build();

    private RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    private JsonObject data;

    private View view;

    private ArrayList<View> views = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        /*
        retrofitInterface.getJsonObject(BASE_URL + "/api/v1/speakers/" + getArguments().getString("id"), getActivity().getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", null)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    Log.d("Response Body", response.body().toString());

                    View v = inflater.inflate(R.layout.activity_event, container, false);

                    ((TextView) v.findViewById(R.id.speaker_name)).setText(response.body().get("first_name").toString() + " " + response.body().get("last_name").toString());
                    ((TextView) v.findViewById(R.id.speaker_bio)).setText(response.body().get("bio").toString());

                    views.add(v);

                } else {
                    onFailure(call, new Throwable());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("DEBUG", call.request().url().toString());
                Toast.makeText(getActivity().getBaseContext(), "Retrieve failed.", Toast.LENGTH_SHORT).show();
            }
        });
        */

        //views.add(inflater.inflate(R.layout.fragment_speaker, container, false));

        Runnable r = new Runnable() {
            public void run() {
                try {
                    data = retrofitInterface.getJsonObject(BASE_URL + "/api/v1/speakers/" + getArguments().getString("id"), getActivity().getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", null)).execute().body();

                    Log.d("DEBUG", "Retrieved data");

                    view = inflater.inflate(R.layout.fragment_speaker, container, false);

                    ((TextView) view.findViewById(R.id.speaker_name)).setText(data.get("first_name").toString().substring(1, data.get("first_name").toString().length() - 1) + " " + data.get("last_name").toString().substring(1, data.get("last_name").toString().length() - 1));
                    ((TextView) view.findViewById(R.id.speaker_bio)).setText(data.get("bio").toString());

                    Log.d("DEBUG", ((TextView) view.findViewById(R.id.speaker_name)).getText().toString());

                    views.add(view);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            Thread thread = new Thread(r);
            thread.start();
            thread.join();
            Picasso.with(view.getContext()).load(data.get("image_url").toString().substring(1, data.get("image_url").toString().length() - 1)).resize(250, 0).into((ImageView) view.findViewById(R.id.speaker_image));
            return views.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.fragment_speaker, container, false);

    }
}
