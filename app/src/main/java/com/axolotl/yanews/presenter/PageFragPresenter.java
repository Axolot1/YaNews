package com.axolotl.yanews.presenter;

import com.axolotl.yanews.fragment.PageFragment;
import com.axolotl.yanews.model.NewsModel;
import com.axolotl.yanews.retrofit.entity.News;

import io.realm.RealmResults;
import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/3.
 */
public class PageFragPresenter {

    private final PageFragment mView;
    private Subscription listDataSubscription;
    private NewsModel mModel;
    private String mChannelId;
    private int counter = 0;

    public PageFragPresenter(PageFragment view, NewsModel model, String channelId) {
        Timber.d("PageFragPresenter create");
        this.mView = view;
        this.mModel = model;
        this.mChannelId = channelId;
    }


    public void onPause() {
        if (listDataSubscription != null) {
            listDataSubscription.unsubscribe();
        }
    }

    public void onViewCreated() {
        Timber.d("%s onViewCreated", mChannelId);
        if (listDataSubscription != null) {
            listDataSubscription.unsubscribe();
            counter = 0;
        }
        listDataSubscription = mModel.getNewsByChannel(mChannelId)
                .subscribe(new Action1<RealmResults<News>>() {
                    @Override
                    public void call(RealmResults<News> newses) {
                        mView.showData(newses);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable.getLocalizedMessage());
                    }
                });
    }

    public void onDestroy() {
    }

    public void refreshList() {
        Timber.d("%s refreshList", mChannelId);
        mModel.reloadNewsByChannel(mChannelId);
        mView.finishRefresh();
    }
}
