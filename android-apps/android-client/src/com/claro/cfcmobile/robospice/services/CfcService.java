package com.claro.cfcmobile.robospice.services;

import android.app.Application;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.springandroid.json.gson.GsonObjectPersisterFactory;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

/**
 * Created by Christopher Herrera on 2/19/14.
 */
public class CfcService extends SpiceService {

    //=================================================================================================================
    //CONSTANTS
    //=================================================================================================================
    public static final String TAG = "CfcService";

    //=================================================================================================================
    //OVERRIDEN METHODS
    //=================================================================================================================
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        //Init
        GsonObjectPersisterFactory gsonObjectPersisterFactory = new GsonObjectPersisterFactory(application);
        InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister(application);

        cacheManager.addPersister(gsonObjectPersisterFactory);
        cacheManager.addPersister(inFileStringObjectPersister);
        return cacheManager;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Starting Command");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public int getThreadCount() {
        return 3;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e(TAG, "Starting Service");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Stopping Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "Bound Service");
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "UnBound Service");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e(TAG, "Rebound Service");
    }
}
