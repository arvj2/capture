package com.claro.cfcmobile.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by Christopher Herrera on 2/18/14.
 *
 * Modified by jansel
 */
public final class Utils {

    public static final String DOWNLOAD_EXTERNAL_PART = "/download/";

    public static final String DOWNLOAD_EXTERNAL_URI  = Environment.getExternalStorageDirectory().getAbsolutePath().concat( DOWNLOAD_EXTERNAL_PART );

    public static final String getDeviceImei( Context context ){
        TelephonyManager tm = (TelephonyManager)context.getSystemService( Context.TELEPHONY_SERVICE );
        return tm.getDeviceId();
    }

    public static final boolean updaterInstalled( Context context ){
        PackageManager pm = context.getPackageManager();
        PackageInfo info;

        try{
            info = pm.getPackageInfo("com.claro.otau", PackageManager.GET_ACTIVITIES);
        }catch ( Exception ex ){
            ex.printStackTrace();
            return false;
        }
        if( null == info )
            return false;
        return true;
    }

    public static final int getVersionCode(Context context ){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo( context.getPackageName(),0 );
            return info.versionCode;
        }catch ( PackageManager.NameNotFoundException ex ){
            ex.printStackTrace();
        }
        return 0;
    }
}
