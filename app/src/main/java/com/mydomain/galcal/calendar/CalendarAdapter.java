package com.mydomain.galcal.calendar;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mydomain.galcal.MainActivity;
import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.mydomain.galcal.editEvent.EditEventFragment;
import com.mydomain.galcal.week.WeekAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nikita on 24.01.2019.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private String mToken;
    private ArrayList<DayEventData> mEvents;
    private CalendarFragment mFragment;

    public CalendarAdapter(CalendarFragment fragment, String token){
        mFragment = fragment;
        mToken = token;
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
            if(mEvents.get(position).type.equals("holidays")) {
                holder.textViewEvent.setTypeface(holder.textViewEvent.getTypeface(), Typeface.ITALIC);
            }
            if(mEvents.get(position).isAllDay){
                holder.textViewTime.setText("All Day");
            }else {
                SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date date = oldFormat.parse(mEvents.get(position).startTime);

                String timeStart = new SimpleDateFormat("HH:mm").format(date);
                String[] bufferTimeStart = timeStart.split(":");
                String AM_PM = "";
                int hoursStart = Integer.parseInt(bufferTimeStart[0]);
                if(hoursStart < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                    hoursStart = hoursStart-12;
                }
                timeStart = hoursStart + ":" + bufferTimeStart[1] +" "+AM_PM;

                holder.textViewTime.setText(timeStart);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditEventFragment fragment = new EditEventFragment();
                    fragment.setToken(mToken);
                    fragment.setEventData(mEvents.get(position));
                    FragmentManager manager = mFragment.getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.main_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    MainActivity activity = (MainActivity) mFragment.getActivity();
                    if(activity.isTutirial){
                        mFragment.hideStep9();
                    }
                }
            });
        }catch (Exception c){
            c.printStackTrace();
        }
    }

    public void setEvents(ArrayList<DayEventData> events){
        /*try {
            ArrayList<DayEventData> eventData = new ArrayList<DayEventData>();
            for (int i = 0; i < events.size(); i++) {
                SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("'T'HH:mm:ss'Z'");
                String dayTime = timeFormat.format(oldFormat.parse(events.get(i).startTime));
                String dateStart = newFormat.format(oldFormat.parse(events.get(i).startTime));
                String dateEnd = newFormat.format(oldFormat.parse(events.get(i).endTime));
                if(!dateStart.equals(dateEnd)){
                    Date currentDate = newFormat.parse(dateStart);
                    Date endDate = newFormat.parse(dateEnd);
                    while (currentDate.before(endDate)){
                        String dateDay = newFormat.format(currentDate)+dayTime;
                        DayEventData dayEventData = new DayEventData();
                        dayEventData = events.get(i);
                        dayEventData.todayData = dateDay;
                        eventData.add(dayEventData);
                        currentDate.setTime(currentDate.getTime()+86400000);
                    }

                }else{
                    String dateDay = events.get(i).startTime;
                    DayEventData dayEventData = new DayEventData();
                    dayEventData = events.get(i);
                    dayEventData.todayData = dateDay;
                    eventData.add(dayEventData);

                }

            }*/
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
