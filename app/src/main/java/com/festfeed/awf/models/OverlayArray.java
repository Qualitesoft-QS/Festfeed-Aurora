package com.festfeed.awf.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OverlayArray {
    @SerializedName("overlays")
    @Expose
    private List<Overlay> overlays = null;

    public List<Overlay> getOverlays() {
        return overlays;
    }

    public void setOverlays(List<Overlay> overlays) {
        this.overlays = overlays;
    }
}
