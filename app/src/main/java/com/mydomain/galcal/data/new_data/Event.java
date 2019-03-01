package com.mydomain.galcal.data.new_data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Nikita on 01.03.2019.
 */


public class Event {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("is_holiday")
    @Expose
    public Boolean isHoliday;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("is_all_day")
    @Expose
    public Boolean isAllDay;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("remind_time")
    @Expose
    public Object remindTime;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("notes")
    @Expose
    public String notes;
}
