
package com.festfeed.awf.gamemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrGameIcon {

    @SerializedName("off")
    @Expose
    private String off;
    @SerializedName("on")
    @Expose
    private String on;
    @SerializedName("qr_value")
    @Expose
    private String qrValue;
    @SerializedName("position")
    @Expose
    private Integer position;

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getQrValue() {
        return qrValue;
    }

    public void setQrValue(String qrValue) {
        this.qrValue = qrValue;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
