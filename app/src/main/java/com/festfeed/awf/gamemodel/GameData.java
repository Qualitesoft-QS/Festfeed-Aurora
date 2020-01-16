
package com.festfeed.awf.gamemodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameData {

    @SerializedName("configurations")
    @Expose
    private List<Configuration> configurations = null;

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

}
