package com.theppwd.androidchallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsViewAdapter extends RecyclerView.Adapter<EventsViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> headers, startDateTimes, endDateTimes, imageUrls;

    EventsViewAdapter(Context context, ArrayList<String> headers, ArrayList<String> startDateTimes, ArrayList<String> endDateTimes, ArrayList<String> imageUrls) {
        this.context = context;
        this.headers = headers;
        this.startDateTimes = startDateTimes;
        this.endDateTimes = endDateTimes;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        viewHolder.recyclerHeader.setText(headers.get(i));
        viewHolder.recyclerBody.setText(startDateTimes.get(i) + " - " + endDateTimes.get(i)); // Fix

        Picasso.with(context).load(imageUrls.get(i)).into(viewHolder.recyclerImage);

        viewHolder.recyclerItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.d("DEBUG ", "You clicked on " + i);
           }
        });

    }

    @Override
    public int getItemCount() {
        return headers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recyclerItem;
        ImageView recyclerImage;
        TextView recyclerHeader, recyclerBody;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItem = itemView.findViewById(R.id.recycler_item);
            recyclerImage = itemView.findViewById(R.id.recycler_image);
            recyclerHeader = itemView.findViewById(R.id.recycler_header);
            recyclerBody = itemView.findViewById(R.id.recycler_body);

        }

    }

}
