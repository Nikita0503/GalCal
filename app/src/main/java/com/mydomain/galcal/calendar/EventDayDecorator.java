package com.mydomain.galcal.calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.mydomain.galcal.R;
import com.mydomain.galcal.data.DayEventData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nikita on 23.01.2019.
 */

public class EventDayDecorator implements DayViewDecorator {

    private Context mContext;
    private ArrayList<DayEventData> mEvents;

    public EventDayDecorator(Context context, ArrayList<DayEventData> events) {
        mContext = context;
        mEvents = events;
    }



    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        try {
            int day = calendarDay.getDay();
            int month = calendarDay.getMonth();
            int year = calendarDay.getYear();
            String currentDate = year+"-"+month+"-"+day;
            SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH);
            Date dateStart;
            Date dateEnd;
            String dateStr;
            String dateStrEnd;
            for (int i = 0; i < mEvents.size(); i++) {
                if(mEvents.get(i).type.equals("holidays")){
                    dateStart = oldDateFormat.parse(mEvents.get(i).startTime);
                    dateStr = newDateFormat.format(dateStart);
                    if(currentDate.equals(dateStr)){
                        return true;
                    }
                }else {
                    dateStart = oldDateFormat.parse(mEvents.get(i).startTime);
                    dateEnd = oldDateFormat.parse(mEvents.get(i).endTime);
                    dateStr = newDateFormat.format(dateStart);
                    dateStrEnd = newDateFormat.format(dateEnd);
                    while (dateStart.before(dateEnd)){
                        if(currentDate.equals(dateStr)){
                            return true;
                        }
                        dateStart.setTime(dateStart.getTime()+86400000);
                        dateStr = newDateFormat.format(dateStart);
                    }
                }
            }
        }catch (Exception c){
            c.printStackTrace();
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {

        dayViewFacade.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.event_calendar_decorator));
    }
}
