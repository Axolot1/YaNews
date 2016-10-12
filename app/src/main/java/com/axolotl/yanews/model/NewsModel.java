package com.axolotl.yanews.model;

import com.axolotl.yanews.retrofit.entity.News;


import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by axolotl on 16/9/23.
 */

public class NewsModel {
    NewsRepository mRepository;

    public NewsModel(NewsRepository mRepository) {
        this.mRepository = mRepository;
    }

    public Observable<RealmResults<News>> getNewsByChannel(String channelId){
        return mRepository.loadNews(channelId, false);
    }

    public Observable<RealmResults<News>> reloadNewsByChannel(String channelId){
        return mRepository.loadNews(channelId, true);
    }


}
