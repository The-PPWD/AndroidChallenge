package com.theppwd.androidchallenge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsViewAdapter extends RecyclerView.Adapter<EventsViewAdapter.ViewHolder> {

    // Variable declarations used in the constructor (2 lines)
    private Context context;
    private ArrayList<String> ids, headers, startDateTimes, endDateTimes, imageUrls;

    EventsViewAdapter(Context context, ArrayList<String> ids, ArrayList<String> headers, ArrayList<String> startDateTimes, ArrayList<String> endDateTimes, ArrayList<String> imageUrls) {
        this.context = context;
        this.ids = ids;
        this.headers = headers;
        this.startDateTimes = startDateTimes;
        this.endDateTimes = endDateTimes;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.recyclerHeader.setText(headers.get(i));
        viewHolder.recyclerBody.setText(startDateTimes.get(i) + " - " + endDateTimes.get(i));

        // Loads image (1 line)
        Picasso.with(context).load(imageUrls.get(i)).into(viewHolder.recyclerImage);

        viewHolder.recyclerItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // Crafts and starts the individual event page for which recycler view was clicked (3 lines)
               Intent intent = new Intent(context, Event.class);
               intent.putExtra("id", ids.get(i));
               context.startActivity(intent);
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
