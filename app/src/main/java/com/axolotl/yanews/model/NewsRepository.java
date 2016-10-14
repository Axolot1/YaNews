package com.axolotl.yanews.model;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.axolotl.yanews.retrofit.NewsApi;
import com.axolotl.yanews.retrofit.entity.News;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

/**
 * Created by axolotl on 16/9/23.
 */

public class NewsRepository implements Closeable {
    private static final long MINIMUM_NETWORK_WAIT_SEC = 120; // Minimum 2 minutes between each network request

    private final Realm realm;


    private NewsLoader mDataLoader;

    private Map<String, Long> lastNetworkRequest = new HashMap<>();

    @UiThread
    public NewsRepository(NewsLoader loader) {
        this.realm = Realm.getDefaultInstance();
        mDataLoader = loader;
    }


    public Observable<RealmResults<News>> loadNews(String channelId, boolean forceReload) {
        // Start loading data from the network if needed
        // It will put all data into Realm
        if (forceReload || timeSinceLastNetworkRequest(channelId) > MINIMUM_NETWORK_WAIT_SEC) {
            mDataLoader.loadNewsByChannel(channelId, realm);
            lastNetworkRequest.put(channelId, System.currentTimeMillis());
        }

        // Return the data in Realm. The query result will be automatically updated when the network requests
        // save data in Realm
        return realm.where(News.class).equalTo(News.CHANNELID, channelId)
                .findAllSorted(News.PUB_DATE, Sort.DESCENDING)
                .asObservable();
    }

    private long timeSinceLastNetworkRequest(@NonNull String sectionKey) {
        Long lastRequest = lastNetworkRequest.get(sectionKey);
        if (lastRequest != null) {
            return TimeUnit.SECONDS.convert(System.currentTimeMillis() - lastRequest, TimeUnit.MILLISECONDS);
        } else {
            return Long.MAX_VALUE;
        }
    }


    @Override
    public void close() throws IOException {
        realm.close();
    }
}
