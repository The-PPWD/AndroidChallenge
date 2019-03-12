package com.theppwd.androidchallenge;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpeakerFragment extends Fragment {

    // Base URL for the api call
    final private String BASE_URL = "https://challenge.myriadapps.com";

    private Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
            .build();

    private RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

    // Variable declarations to be used later (2 lines)
    private JsonObject data;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        // Crafts and runs a new runnable & thread for a synchronous api call (23 lines)
        Runnable r = new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                try {
                    // data carries the server's response (1 line)
                    data = retrofitInterface.getJsonObject(BASE_URL + "/api/v1/speakers/" + getArguments().getString("id"), getActivity().getSharedPreferences("token", Context.MODE_PRIVATE).getString("token", null)).execute().body();

                    // Creates a view for the fragment (1 line)
                    view = inflater.inflate(R.layout.fragment_speaker, container, false);

                    // Edits the name and bio fields (2 lines)
                    ((TextView) view.findViewById(R.id.speaker_name)).setText(data.get("first_name").toString().substring(1, data.get("first_name").toString().length() - 1) + " " + data.get("last_name").toString().substring(1, data.get("last_name").toString().length() - 1));
                    ((TextView) view.findViewById(R.id.speaker_bio)).setText(data.get("bio").toString().substring(1, data.get("bio").toString().length() - 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            Thread thread = new Thread(r);
            thread.start();

            // Awaits for this thread to finish before proceeding (1 line)
            thread.join();

            // Load the speaker's image (1 line)
            Picasso.with(view.getContext()).load(data.get("image_url").toString().substring(1, data.get("image_url").toString().length() - 1)).resize(0, 200).into((ImageView) view.findViewById(R.id.speaker_image));
            return view;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // In case of a failure above, this will return a blank fragment (1 line)
        return inflater.inflate(R.layout.fragment_speaker, container, false);

    }
}
