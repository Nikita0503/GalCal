package com.mydomain.galcal.home;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mydomain.galcal.APIUtils.APIUtils;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.editEvent.EditEventFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nikita on 21.01.2019.
 */

public class HomeEventsListAdapter extends RecyclerView.Adapter<HomeEventsListAdapter.ViewHolder> {

    private String mToken;
    private ArrayList<DayEventData> mList;
    private HomeFragment mFragment;

    public HomeEventsListAdapter(String token, ArrayList<DayEventData> list, HomeFragment fragment) {
        mToken = token;
        mList = list;
        mFragment = fragment;
    }

    //NonNull
    //@Override
    public HomeEventsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item_home, parent, false);
        return new HomeEventsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeEventsListAdapter.ViewHolder holder, final int position) {
        String startTime = "";
        String endTime = "";
        if(mList.get(position).startTime!=null) {
            startTime = mList.get(position).startTime.substring(0, 11);
        }
        if(mList.get(position).endTime!=null) {
            endTime = mList.get(position).endTime.substring(0, 11);
        }
        holder.textViewEvent.setText(mList.get(position).title);
        if(!mList.get(position).type.equals("holidays")) {
            if(!mList.get(position).isAllDay) {
                if (startTime.equals(endTime) || endTime.equals("")) {
                    holder.textViewEventTime.setText(mList.get(position).startTime.substring(11, 16));
                } else {
                    String start = mList.get(position).startTime;
                    String end = mList.get(position).endTime;
                    SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM d", Locale.ENGLISH);
                    try {
                        startTime = newDateFormat.format(oldDateFormat.parse(start));
                        endTime = newDateFormat.format(oldDateFormat.parse(end));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.textViewEventTime.setText(startTime + " - " + endTime);

                }
            }else{
                holder.textViewEventTime.setText("All Day");
            }
        }else{
            holder.textViewEventTime.setText("Holiday");
            holder.textViewEventTime.setTypeface(holder.textViewEventTime.getTypeface(), Typeface.ITALIC);
            holder.textViewEvent.setTypeface(holder.textViewEvent.getTypeface(), Typeface.ITALIC);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditEventFragment fragment = new EditEventFragment();
                fragment.setToken(mToken);
                fragment.setEventData(mList.get(position));
                FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.main_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //holder.textViewEventTime.setText(mList.get(position).eventTime);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoFragment fragment = new VideoFragment();
                fragment.setVideoId(mList.get(position).videoId);
                FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Log.d("Queue", "= " + position);*/
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewEvent;
        TextView textViewEventTime;

        public ViewHolder(View itemView){
            super(itemView);
            textViewEvent = (TextView) itemView.findViewById(R.id.event);
            textViewEventTime = (TextView) itemView.findViewById(R.id.event_time);
        }
    }
}
