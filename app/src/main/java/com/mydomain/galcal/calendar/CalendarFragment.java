package com.mydomain.galcal.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydomain.galcal.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

/**
 * Created by Nikita on 17.01.2019.
 */

public class CalendarFragment extends Fragment {

    private MaterialCalendarView mCalendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);
        mCalendarView = (MaterialCalendarView) view.findViewById(R.id.materialCalendarView);
        mCalendarView.setWeekDayLabels(new String[]{"M", "T", "W", "T", "F", "S", "S"});
        mCalendarView.setHeaderTextAppearance(R.style.TitleTextAppearance);
        mCalendarView.setWeekDayTextAppearance(R.style.WeekAppearance);
        mCalendarView.setDateTextAppearance(R.style.DayAppearance);
        return view;
    }

}
