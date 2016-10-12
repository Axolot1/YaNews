
package com.axolotl.yanews.retrofit.entity;

import com.axolotl.yanews.retrofit.RealmListParcelConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

import javax.annotation.Generated;

import io.realm.NewsRealmProxy;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

@Parcel(implementations = {NewsRealmProxy.class},
        value = Parcel.Serialization.FIELD,
        analyze = {News.class})
@Generated("org.jsonschema2pojo")
public class News extends RealmObject {

    public static final int TYPE_TEXT = 2;
    public static final int TYPE_IMAGE = 3;
    public static final int TYPE_HEAD = 4;
    public static final String LINK = "link";
    public static final String CHANNELID = "channelId";
    public static final String PUB_DATE = "pubDate";

    @SerializedName("allList")
    @Expose
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    RealmList<NewsContent> allList;

    @SerializedName("pubDate")
    @Expose
    String pubDate;

    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("channelName")
    @Expose
    String channelName;

    @SerializedName("imageurls")
    @Expose
    @ParcelPropertyConverter(RealmListParcelConverter.class)
    RealmList<ContentPic> imageurls;

    @SerializedName("desc")
    @Expose
    String desc;
    @SerializedName("source")
    @Expose
    String source;
    @SerializedName("channelId")
    @Expose
    String channelId;

    @PrimaryKey
    @SerializedName("link")
    @Expose
    String link;

    @Ignore
    int type;


    public int getType() {
        if (imageurls == null || imageurls.size() == 0)
            return TYPE_TEXT;
        return TYPE_IMAGE;
    }

    public RealmList<NewsContent> getAllList() {
        return allList;
    }

    public void setAllList(RealmList<NewsContent> allList) {
        this.allList = allList;
    }


    public void setImageurls(RealmList<ContentPic> imageurls) {
        this.imageurls = imageurls;
    }

    /**
     * @return The pubDate
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * @param pubDate The pubDate
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The channelName
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * @param channelName The channelName
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * @return The imageurls
     */
    public List<ContentPic> getImageurls() {
        return imageurls;
    }


    /**
     * @return The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return The source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return The channelId
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * @param channelId The channelId
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * @return The link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    public void setLink(String link) {
        this.link = link;
    }

}
