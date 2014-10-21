package com.claro.cfcmobile.activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.claro.cfcmobile.activities.prefs.UserKnowledge;
import com.claro.cfcmobile.services.DIDownloaderService;
import com.claro.cfcmobile.utils.HttpUtils;
import com.claro.cfcmobile.utils.Utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/11/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public class DispatchActivity extends Activity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareDependencies();

        Class<?> activity;

        if (!UserKnowledge.isConditionUpTaked(this)) {
            activity = ConditionActivity.class;
        } else if (!UserKnowledge.isUserLogged(this)) {
            activity = LoginActivity.class;
        } else
            activity = HomeActivity.class;


        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void startDIDonwloaderService() {
        Log.e("***********", "Dispatch Activity is starting service");
        Intent service = new Intent(DispatchActivity.this, DIDownloaderService.class);
        startService(service);
    }

    private void downloadOTAUpdated() {

        if( Utils.updaterInstalled(this) )
            return;
        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri targetUri = Uri.parse(HttpUtils.URL_OTA_UPDATER);
        DownloadManager.Request targetRequest = new DownloadManager.Request(targetUri);

        String rawString = "file://" +  Utils.DOWNLOAD_EXTERNAL_URI + String.valueOf(HttpUtils.URL_OTA_UPDATER.hashCode());


        Uri destUri = Uri.parse(rawString);
        targetRequest.setDestinationUri(destUri);
        targetRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        targetRequest.setVisibleInDownloadsUi(true);
        targetRequest.setTitle("Descargando OTAUpdater");
        dm.enqueue(targetRequest);
        Toast.makeText(DispatchActivity.this, "Se ha iniciado la descarga de OTAUpdater", Toast.LENGTH_LONG).show();
    }

    private void prepareDependencies() {
        final Lock lock = new ReentrantLock();
        final Condition con = lock.newCondition();

        try {
            lock.lock();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        lock.lock();
                        handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                startDIDonwloaderService();
                                downloadOTAUpdated();
                            }
                        };
                        con.signalAll();
                    } finally {
                        lock.unlock();
                    }
                    Looper.loop();
                }
            }).start();

            while (null == handler)
                con.awaitUninterruptibly();

            handler.obtainMessage().sendToTarget();
        } finally {
            lock.unlock();
        }
    }

}
