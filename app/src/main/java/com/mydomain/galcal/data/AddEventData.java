
package com.mydomain.galcal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddEventData {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("is_all_day")
    @Expose
    public Boolean isAllDay;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("notes")
    @Expose
    public String notes;
    @SerializedName("remind_time")
    @Expose
    public String remindTime;

    public AddEventData(String title, String type, Boolean isAllDay, String startTime, String endTime, String location, String notes, String remindTime) {
        this.title = title;
        this.type = type;
        this.isAllDay = isAllDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.notes = notes;
        this.remindTime = remindTime;
    }
}
