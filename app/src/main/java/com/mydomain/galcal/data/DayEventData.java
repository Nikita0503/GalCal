
package com.mydomain.galcal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DayEventData {

    public String todayData;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("is_all_day")
    @Expose
    public Boolean isAllDay;
    @SerializedName("is_holiday")
    @Expose
    public Boolean isHoliday;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("remind_time")
    @Expose
    public String remindTime;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("notes")
    @Expose
    public String notes;
    @SerializedName("type")
    @Expose
    public String type;
}
