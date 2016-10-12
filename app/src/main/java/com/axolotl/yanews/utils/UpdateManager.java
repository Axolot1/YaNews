package com.axolotl.yanews.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.ViewGroup;

import com.axolotl.yanews.R;
import com.axolotl.yanews.retrofit.OauthClient;
import com.axolotl.yanews.retrofit.entity.CheckVersionRes;
import com.axolotl.yanews.service.DownLoadService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.EmptyPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/30.
 */

public class UpdateManager {
    private Context mContext;
    private ViewGroup mView;
    private PermissionListener permissionListener;
    private OauthClient.Endpoints mApi;
    private String mApkUrl;

    public UpdateManager(Context mContext, ViewGroup view, OauthClient.Endpoints api) {
        this.mContext = mContext;
        mView = view;
        createPermissionListener();
        mApi = api;
    }

    public void start() {
        int localVersion = -1;
        try {
            localVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Timber.d("package not found %s", e.getLocalizedMessage());
        }
        if (localVersion == -1) {
            return;
        }
        Timber.d("local version: %d", localVersion);
        mApi.checkVersion(localVersion).enqueue(new Callback<CheckVersionRes>() {
            @Override
            public void onResponse(Call<CheckVersionRes> call, Response<CheckVersionRes> response) {
                if (response.body().getResult().equals("CANCEL")) {
                    Timber.d("version up to date");
                    return;
                }
                mApkUrl = response.body().getResult();
                //request permission
                showUpdateDialog();


            }

            @Override
            public void onFailure(Call<CheckVersionRes> call, Throwable t) {

            }
        });

    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        String message = "软件有更新，要下载安装吗？\n";
        builder.setMessage(message);

        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dexter.checkPermission(permissionListener, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏当前对话框
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void createPermissionListener() {
        permissionListener = new CompositePermissionListener(new EmptyPermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                super.onPermissionGranted(response);
                startDownload();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                super.onPermissionRationaleShouldBeShown(permission, token);
                token.cancelPermissionRequest();
            }
        }, SnackbarOnDeniedPermissionListener.Builder
                .with(mView, "下载新版本需要读写存储空间")
                .withOpenSettingsButton("设置")
                .build());
    }


    private void startDownload() {
        Intent intent = new Intent(mContext, DownLoadService.class);
        intent.putExtra(DownLoadService.EXTRA_URL, mApkUrl);
        mContext.startService(intent);
    }


}
