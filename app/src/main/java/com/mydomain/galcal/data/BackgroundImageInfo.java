
package com.mydomain.galcal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BackgroundImageInfo {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("photographer")
    @Expose
    public String photographer;
    @SerializedName("model")
    @Expose
    public String model;
    @SerializedName("date")
    @Expose
    public String date;

}
