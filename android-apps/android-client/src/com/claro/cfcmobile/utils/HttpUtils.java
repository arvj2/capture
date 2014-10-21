package com.claro.cfcmobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Arrays;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/14/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public final class HttpUtils {

    /**
     * staging
     * http://172.27.17.161:8020
     *
     * production
     * http://172.27.6.201:7040
     *
     */
    private static final String HOST = "http://172.27.17.161:8020";

    public static final String SERVER_URL = HOST+"/cfc/services/v2/support";

    public static final String PATH_REMOTE_VERSION = "/versions/mobile";

    public static final String PATH_TEMPLATE_LOGIN = "/technicians/$@/login/?imei=$";

    public static final String PATH_TEMPLATE_ITEMS = "/technicians/$@/orders/?imei=$";

    public static final String PATH_TEMPLATE_ACTIONS = "/technicians/$/actions";

    public static final String URL_OTA_UPDATER = HOST+"/captura-facilidades/OTAUpdated/updater/OTAUpdater.apk";



    private static String keychar[] = { " ", "\\|" };
    private static String codechar[] = { "+", "%7c" };


    public static String matchSymbol(String source, String split,String join,String[] values) {

        String[] params = source.split(split);
        for (int i = 0; i < params.length; ++i) {
            params[i] = params[i].trim();
            params[i] = params[i].replaceAll("\\$", values[i]);
        }
        return Arrays.asList(params).toString().replaceAll("^\\[|\\]$", "").replaceAll("(, |,| ,| , )", join);
    }

    public static String URLEncode(String url) {
        String result = url;
        for (int i = 0; i < keychar.length; ++i)
            result = result.replaceAll(keychar[i], codechar[i]);
        return result;
    }

    public static String URLDecode(String url) {
        String result = url;
        for (int i = 0; i < keychar.length; ++i)
            result = result.replaceAll(codechar[i], keychar[i]);
        return result;
    }

    public static boolean isNewtworkAvailable(Context context) {
        return isConnectedAs(context, ConnectivityManager.TYPE_WIFI) || isConnectedAs(context, ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isConnectedAs(Context context, int type) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo info = cm.getNetworkInfo(type);
            return info.isAvailable() && info.isConnected();
        }
        return false;
    }
}
