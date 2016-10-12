package com.axolotl.yanews.retrofit;

import com.axolotl.yanews.retrofit.entity.ContentPic;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.RealmList;

/**
 * Created by axolotl on 16/9/23.
 */

public class ContentPicDeserializer implements JsonDeserializer<RealmList<ContentPic>> {

    @Override
    public RealmList<ContentPic> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RealmList<ContentPic> contents = new RealmList<>();
        JsonArray jsonArray = json.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            ContentPic contentPic = new ContentPic();
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            contentPic.setHeight(object.get("height").getAsInt());
            contentPic.setWidth(object.get("width").getAsInt());
            contentPic.setUrl(object.get("url").getAsString());
            contents.add(contentPic);
        }
        return contents;
    }
}
