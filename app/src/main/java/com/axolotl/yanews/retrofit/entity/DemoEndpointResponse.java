package com.axolotl.yanews.retrofit.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DemoEndpointResponse {


    @SerializedName("data")
    @Expose
    private String data;

    /**
     * @return The data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(String data) {
        this.data = data;
    }

}
