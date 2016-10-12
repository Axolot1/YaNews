package com.axolotl.yanews.retrofit;

import com.axolotl.yanews.retrofit.entity.NewsContent;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by axolotl on 16/9/23.
 */

public class NewsContentDeserializer implements JsonDeserializer<RealmList<NewsContent>> {
    @Override
    public RealmList<NewsContent> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        RealmList<NewsContent> contents = new RealmList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement temp = jsonArray.get(i);
            NewsContent nc = new NewsContent();
            if (temp.isJsonPrimitive()) {
                nc.setContent(temp.getAsString());
                nc.setType(NewsContent.TYPE_TEXT);
            } else if (temp.isJsonObject()) {
                JsonObject object = temp.getAsJsonObject();
                nc.setHeight(object.get("height").getAsInt());
                nc.setWidth(object.get("width").getAsInt());
                nc.setContent(object.get("url").getAsString());
                nc.setType(NewsContent.TYPE_PIC);
            }
            contents.add(nc);
        }
        return contents;
    }
}
