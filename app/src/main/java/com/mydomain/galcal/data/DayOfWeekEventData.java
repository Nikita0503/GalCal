package com.mydomain.galcal.data;

import java.util.ArrayList;

/**
 * Created by Nikita on 23.01.2019.
 */

public class DayOfWeekEventData {
    public String date;
    public ArrayList<DayEventData> events;

    public DayOfWeekEventData(String date, ArrayList<DayEventData> events) {
        this.date = date;
        this.events = events;
    }
}
