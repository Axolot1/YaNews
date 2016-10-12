package com.axolotl.yanews.service;

import com.axolotl.yanews.YaNewApp;
import com.axolotl.yanews.model.NewsLoader;
import com.axolotl.yanews.retrofit.NewsApi;
import com.axolotl.yanews.retrofit.entity.CateResEntity;
import com.axolotl.yanews.retrofit.entity.News;
import com.axolotl.yanews.retrofit.entity.Pagebean;
import com.axolotl.yanews.utils.Constants;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/30.
 */

public class NewsBackupService extends GcmTaskService {

    @Inject
    NewsApi mApi;
    Realm mRealm;


    public NewsBackupService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("backup service create");
        YaNewApp.getNetComponent(this).inject(this);

    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Timber.d("onRunTask");
        mRealm = Realm.getDefaultInstance();
        Observable.from(Constants.CATEGORY_IDS)
                .flatMap(new Func1<String, Observable<CateResEntity>>() {
                    @Override
                    public Observable<CateResEntity> call(String s) {
                        return mApi.listNews(s, 1);
                    }
                })
                .subscribe(new Subscriber<CateResEntity>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Backup Complete");
                        mRealm.close();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("Backup data fail %s", e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(CateResEntity cateResEntity) {
                        if (cateResEntity.getShowapiResCode() == 0) {
                            processAndAddData(mRealm, cateResEntity.getShowapiResBody().getPagebean());
                        }
                    }
                });
        return GcmNetworkManager.RESULT_SUCCESS;
    }

    public void processAndAddData(Realm realm, Pagebean pagebean) {
        final List<News> newses = pagebean.getNews();
        if (newses == null || newses.size() == 0)
            return;
        realm.executeTransaction(new Realm.Transaction() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
