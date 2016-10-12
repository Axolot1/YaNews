package com.axolotl.yanews;

import android.app.Application;
import android.content.Context;

import com.axolotl.yanews.dagger.AppModule;
import com.axolotl.yanews.dagger.DaggerMainComponent;
import com.axolotl.yanews.dagger.MainComponent;
import com.facebook.stetho.Stetho;
import com.karumi.dexter.Dexter;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/2.
 */
public class YaNewApp extends Application {

    private MainComponent mMaimComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        Dexter.initialize(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Configure Realm for the application
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        initRealmConfiguration();

    }

    private void initRealmConfiguration() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config); // Make this Realm the default
    }

    private void initializeInjector() {
        mMaimComponent = DaggerMainComponent.builder().appModule(new AppModule(this)).build();
    }

    public static MainComponent getNetComponent(Context context) {
        return ((YaNewApp) context.getApplicationContext()).mMaimComponent;
    }
}
