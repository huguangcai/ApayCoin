package com.ysxsoft.apaycoin.downloadapk;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ysxsoft.apaycoin.R;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MyIntentService extends IntentService {
    private static final String ACTION_DOWNLOAD = "intentservice.action.download";

    private static final String DOWNLOAD_URL = "downloadUrl";
    private static final String APK_PATH = "apkPath";

    private CompositeDisposable cd = new CompositeDisposable();
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startUpdateService(Context context, String url, String apkPath) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(DOWNLOAD_URL, url);
        intent.putExtra(APK_PATH, apkPath);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("开始下载")
                        .setAutoCancel(true)
                        .setContentText("版本更新");

                notificationManager.notify(0, builder.build());

                String url = intent.getStringExtra(DOWNLOAD_URL);
                String apkPath = intent.getStringExtra(APK_PATH);
                handleUpdate(url, apkPath);
            }
        }
    }

    private void handleUpdate(String url, String apkPath) {
        subscribeEvent();
        UpdateManager.downloadApk(this, url, apkPath, cd);
    }

    private void subscribeEvent() {
        RxBus.getDefault().toObservable(DownloadBean.class)
                .subscribe(new Observer<DownloadBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        cd.add(d);
                    }

                    @Override
                    public void onNext(DownloadBean downloadBean) {
                        int progress = (int) Math.round(downloadBean.getBytesReaded() / (double) downloadBean.getTotal() * 100);
                        builder.setContentInfo(String.valueOf(progress) + "%").setProgress(100, progress, false);
                        notificationManager.notify(0, builder.build());

                        if (progress == 100)
                            notificationManager.cancel(0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscribeEvent();
                    }

                    @Override
                    public void onComplete() {
                        subscribeEvent();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
