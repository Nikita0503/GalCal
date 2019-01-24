package com.mydomain.galcal.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.week.WeekAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikita on 24.01.2019.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private ArrayList<DayEventData> mEvents;
    private CalendarFragment mFragment;

    public CalendarAdapter(CalendarFragment fragment){
        mFragment = fragment;
        mEvents = new ArrayList<DayEventData>();
        //mFragment.fetchNewEvents();
        // mFragment.fetchNewEvents(mLastDate);
        //mList.add(new DayOfWeekEventData("123", new ArrayList<DayEventData>()));
        //mList.add(new DayOfWeekEventData("133", new ArrayList<DayEventData>()));
        //mList.add(new DayOfWeekEventData("143", new ArrayList<DayEventData>()));

    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item_in_month_calendar, parent, false);

        return new CalendarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, final int position) {
        try {
            //holder.textViewDate.setText(mList.get(position).date);
            holder.textViewEvent.setText(mEvents.get(position).title);
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = oldFormat.parse(mEvents.get(position).startTime);
            holder.textViewTime.setText(new SimpleDateFormat("HH:mm").format(date));
        }catch (Exception c){
            c.printStackTrace();
        }
    }

    public void setEvents(ArrayList<DayEventData> events){
        mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTime;
        TextView textViewEvent;

        public ViewHolder(View itemView){
            super(itemView);
            textViewTime = (TextView) itemView.findViewById(R.id.time);
            textViewEvent = (TextView) itemView.findViewById(R.id.event1);
        }


    }
}