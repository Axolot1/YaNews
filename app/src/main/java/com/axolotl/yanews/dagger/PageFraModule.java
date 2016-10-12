package com.axolotl.yanews.dagger;

import com.axolotl.yanews.fragment.PageFragment;
import com.axolotl.yanews.model.NewsModel;
import com.axolotl.yanews.presenter.PageFragPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by axolotl on 16/9/24.
 */
@Module
public class PageFraModule {
    PageFragment mView;
    String mChannelId;

    public PageFraModule(PageFragment mView, String channelId) {
        this.mView = mView;
        this.mChannelId = channelId;
    }

    @Provides
    public PageFragPresenter providePresenter(NewsModel model) {
        return new PageFragPresenter(mView, model, mChannelId);
    }
}
