package com.axolotl.yanews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.FrameLayout;

import com.axolotl.yanews.fragment.NewsListFragment;
import com.axolotl.yanews.retrofit.OauthClient;
import com.axolotl.yanews.service.NewsBackupService;
import com.axolotl.yanews.utils.Prefs;
import com.axolotl.yanews.utils.UpdateManager;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final long MINIMUM_NETWORK_WAIT_MIN = 30;

    @BindView(R.id.container)
    FrameLayout container;
    private NewsListFragment mNewsListFragment;


    @Inject
    OauthClient.Endpoints mApi;

    @Inject
    Prefs mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        YaNewApp.getNetComponent(this).inject(this);
        startPeriodicTask();
        if (savedInstanceState == null && needCheck()) {
            new UpdateManager(this, container, mApi).start();
        }

    }

    private void startPeriodicTask() {
        Timber.d("start schedule");
        long period = 60 * 30L;
        long flex = 10L;
        String periodicTag = "periodic";

        // create a periodic task to pull news once every hour after the app has been opened. This
        // is so Widget data stays up to date.
        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(NewsBackupService.class)
                .setPeriod(period)
                .setFlex(flex)
                .setTag(periodicTag)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .setPersisted(true)
                .build();
        GcmNetworkManager.getInstance(this).schedule(periodicTask);
    }

    public boolean needCheck() {
        long lastRequest = mPrefs.getLastCheckTime();
        if (TimeUnit.MINUTES.convert(System.currentTimeMillis() - lastRequest, TimeUnit.MILLISECONDS)
                > MINIMUM_NETWORK_WAIT_MIN) {
            mPrefs.updateCheckTime();
            return true;
        }
        return false;
    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
