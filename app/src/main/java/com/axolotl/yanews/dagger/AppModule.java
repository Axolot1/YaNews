package com.axolotl.yanews.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.axolotl.yanews.model.NewsLoader;
import com.axolotl.yanews.model.NewsModel;
import com.axolotl.yanews.model.NewsRepository;
import com.axolotl.yanews.utils.Prefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by axolotl on 16/4/6.
 */
@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }


    @Provides
    @Singleton
    public Prefs providePrefs(Application application) {
        return new Prefs(application);
    }


    @Provides
    @Singleton
    public NewsRepository provideRepo(NewsLoader loader){
        return new NewsRepository(loader);
    }

    @Provides
    @Singleton
    public NewsModel provideModel(NewsRepository repo){
        return new NewsModel(repo);
    }




}
