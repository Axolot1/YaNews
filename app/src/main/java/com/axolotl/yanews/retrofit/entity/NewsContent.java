package com.axolotl.yanews.retrofit.entity;

import org.parceler.Parcel;

import io.realm.NewsContentRealmProxy;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by axolotl on 16/9/23.
 */

@Parcel(implementations = {NewsContentRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {NewsContent.class})
public class NewsContent extends RealmObject {

    @Ignore
    public static final int TYPE_TEXT = 1;
    @Ignore
    public static final int TYPE_PIC = 2;

    private String content;
    //    "height": 356,
//            "width": 500,
//            "url": "http://n.sinaimg.cn/translate/20160923/0zmn-fxwevmc5284649.jpg"
    private int height;
    private int width;
    private int type;

    public NewsContent() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NewsContent(int type) {
        this.type = type;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
