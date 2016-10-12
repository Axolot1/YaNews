package com.axolotl.yanews.retrofit.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

public class OAuthToken {


    @SerializedName("oauth_token")
    @Expose
    private String oauthToken;

    /**
     * @return The oauthToken
     */
    public String getOauthToken() {
        return oauthToken;
    }

    /**
     * @param oauthToken The oauth_token
     */
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

}
