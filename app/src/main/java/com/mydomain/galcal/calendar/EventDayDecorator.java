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
            Date date;
            String dateStr;
            for (int i = 0; i < mEvents.size(); i++) {
                date = oldDateFormat.parse(mEvents.get(i).startTime);
                dateStr = newDateFormat.format(date);
                if(currentDate.equals(dateStr)){
                    if(mEvents.get(i).isHoliday) {
                        return false;
                    }else {
                        return true;
                    }

                }
            }
        }catch (Exception c){

        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {

        dayViewFacade.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.event_calendar_decorator));
    }
}
