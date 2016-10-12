package com.axolotl.yanews.model;

import com.axolotl.yanews.event.NetWorkError;
import com.axolotl.yanews.retrofit.NewsApi;
import com.axolotl.yanews.retrofit.entity.CateResEntity;
import com.axolotl.yanews.retrofit.entity.News;
import com.axolotl.yanews.retrofit.entity.Pagebean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/23.
 */

public class NewsLoader {
    private NewsApi mApi;

    public NewsLoader(NewsApi mApi) {
        this.mApi = mApi;
    }

    public void loadNewsByChannel(final String channel, final Realm realm) {
        mApi.listNews(channel, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CateResEntity>() {
                    @Override
                    public void call(CateResEntity cateResEntity) {
                        Timber.d("Success: Data loaded: %s", channel);
                        if (cateResEntity.getShowapiResCode() == 0) {
                            processAndAddData(realm, cateResEntity.getShowapiResBody().getPagebean());
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        EventBus.getDefault().post(new NetWorkError(channel));
                        Timber.d("Failure: Data not loaded: %s - %s", channel, throwable.toString());
                    }
                });

    }


    public void processAndAddData(Realm realm, Pagebean pagebean) {
        final List<News> newses = pagebean.getNews();
        if (newses == null || newses.size() == 0)
            return;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (News news : newses) {
                    News localNews = realm.where(News.class).equalTo(News.LINK, news.getLink()).findFirst();
                    if (localNews == null || news.getAllList().size() != localNews.getAllList().size()) {
                        realm.copyToRealmOrUpdate(news);
                    }
                }
            }
        });
    }
}
