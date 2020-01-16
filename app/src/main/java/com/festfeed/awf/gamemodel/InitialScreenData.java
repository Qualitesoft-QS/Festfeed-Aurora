
package com.festfeed.awf.gamemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InitialScreenData {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("image")
    @Expose
    private String image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
