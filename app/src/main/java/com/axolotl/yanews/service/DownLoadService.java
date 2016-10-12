package com.axolotl.yanews.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.axolotl.yanews.BuildConfig;
import com.axolotl.yanews.YaNewApp;
import com.axolotl.yanews.event.DownloadEvent;
import com.axolotl.yanews.retrofit.OauthClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import timber.log.Timber;

public class DownLoadService extends IntentService {

    public static final String EXTRA_URL = "extra_url";


    @Inject
    OauthClient.Endpoints mAPi;


    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;
    private File outputFile;
    private String downloadUrl;

    public DownLoadService() {
        super("Download Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YaNewApp.getNetComponent(this).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        downloadUrl = intent.getStringExtra(EXTRA_URL);
        int end = downloadUrl.lastIndexOf(File.separator);
        String fileName = downloadUrl.substring(end, downloadUrl.length());
        outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("正在下载新版本")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());


        initDownload();

    }

    private void initDownload() {
        Call<ResponseBody> request = mAPi.downloadApk(downloadUrl);
        try {
            downloadFile(request.execute().body());
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            DownloadEvent download = new DownloadEvent();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }


    private void sendNotification(DownloadEvent download) {
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText("已下载 " + download.getCurrentFileSize() + "/" + totalFileSize + " MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private Intent getInstallIntent() {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", outputFile);
        Timber.d(uri.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }


    private void onDownloadComplete() {
        Intent notificationIntent = getInstallIntent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        DownloadEvent download = new DownloadEvent();
//        download.setProgress(100);
//        EventBus.getDefault().post(download);

        notificationManager.cancel(0);
        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("下载成功");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }


}
