package com.festfeed.awf.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Overlay {

    @SerializedName("eventid")
    @Expose
    private String eventid;
    @SerializedName("SWLong")
    @Expose
    private Double sWLong;
    @SerializedName("SWLat")
    @Expose
    private Double sWLat;
    @SerializedName("NELong")
    @Expose
    private Double nELong;
    @SerializedName("NELat")
    @Expose
    private Double nELat;
    @SerializedName("ImageName")
    @Expose
    private String imageName;

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public Double getSWLong() {
        return sWLong;
    }

    public void setSWLong(Double sWLong) {
        this.sWLong = sWLong;
    }

    public Double getSWLat() {
        return sWLat;
    }

    public void setSWLat(Double sWLat) {
        this.sWLat = sWLat;
    }

    public Double getNELong() {
        return nELong;
    }

    public void setNELong(Double nELong) {
        this.nELong = nELong;
    }

    public Double getNELat() {
        return nELat;
    }

    public void setNELat(Double nELat) {
        this.nELat = nELat;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}