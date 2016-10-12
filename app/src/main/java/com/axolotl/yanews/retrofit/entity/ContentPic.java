
package com.axolotl.yanews.retrofit.entity;

import org.parceler.Parcel;

import io.realm.ContentPicRealmProxy;
import io.realm.RealmObject;

@Parcel(implementations = {ContentPicRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {ContentPic.class})
public class ContentPic extends RealmObject {


    Integer height;

    Integer width;

    String url;

    /**
     * @return The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
