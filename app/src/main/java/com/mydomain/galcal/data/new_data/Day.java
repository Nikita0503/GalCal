package com.mydomain.galcal.data.new_data;

import java.util.ArrayList;

/**
 * Created by Nikita on 01.03.2019.
 */

public class Day {
    public String date;
    public ArrayList<Event> events;

    public Day(String date, ArrayList<Event> events) {
        this.date = date;
        this.events = events;
    }
}
