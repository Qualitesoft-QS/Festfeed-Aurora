
package com.festfeed.awf.gamemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {

    @SerializedName("initial_screen_data")
    @Expose
    private InitialScreenData initialScreenData;
    @SerializedName("eventid")
    @Expose
    private String eventid;
    @SerializedName("instructions_screen_data")
    @Expose
    private InstructionsScreenData instructionsScreenData;
    @SerializedName("qr_game_icons")
    @Expose
    private List<QrGameIcon> qrGameIcons = null;

    public InitialScreenData getInitialScreenData() {
        return initialScreenData;
    }

    public void setInitialScreenData(InitialScreenData initialScreenData) {
        this.initialScreenData = initialScreenData;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public InstructionsScreenData getInstructionsScreenData() {
        return instructionsScreenData;
    }

    public void setInstructionsScreenData(InstructionsScreenData instructionsScreenData) {
        this.instructionsScreenData = instructionsScreenData;
    }

    public List<QrGameIcon> getQrGameIcons() {
        return qrGameIcons;
    }

    public void setQrGameIcons(List<QrGameIcon> qrGameIcons) {
        this.qrGameIcons = qrGameIcons;
    }

}
