package com.claro.cfcmobile.activities.prefs;

import android.content.Context;
import android.content.SharedPreferences;

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
public final class UserKnowledge {

    private static final String PREFS_NAMES;

    private static final String KEY_CONDITION = "com.claro.cfcmobile.activities.prefs.UserKnowledge.KEY_CONDITION";
    private static final String KEY_USER_LOGGED = "com.claro.cfcmobile.activities.prefs.UserKnowledge.KEY_USER_LOGGED";
    private static final String KEY_USER_CARD = "com.claro.cfcmobile.activities.prefs.UserKnowledge.KEY_USER_CARD";
    private static final String KEY_APP_VERSION = "com.claro.cfcmobile.activities.prefs.UserKnowledge.KEY_APP_VERSION";


    static{
        PREFS_NAMES = String.valueOf("com.claro.cfcmobile.activities.prefs.UserKnowledge.PREFS_NAMES".hashCode());
    }


    public static boolean isConditionUpTaked( Context context ){
        SharedPreferences prefs = context.getSharedPreferences( PREFS_NAMES, Context.MODE_PRIVATE );
        return prefs.getBoolean( KEY_CONDITION, false );
    }

    public static void setConditionUpTake( Context context, boolean uptaked ){
        SharedPreferences prefs = context.getSharedPreferences( PREFS_NAMES, Context.MODE_PRIVATE );
        prefs.edit()
             .putBoolean(KEY_CONDITION, uptaked)
             .commit();
     }

    public static void setUserLogged( Context context,boolean logged ){
        SharedPreferences prefs = context.getSharedPreferences( PREFS_NAMES, Context.MODE_PRIVATE );
        prefs.edit()
             .putBoolean( KEY_USER_LOGGED, logged )
             .commit();
    }

    public static boolean isUserLogged( Context context ){
        SharedPreferences prefs = context.getSharedPreferences( PREFS_NAMES, Context.MODE_PRIVATE );
        return prefs.getBoolean( KEY_USER_LOGGED, false );
    }


    public static void setUserCard( Context context,String card ){
        SharedPreferences prefs = context.getSharedPreferences( PREFS_NAMES, Context.MODE_PRIVATE );
        prefs.edit()
                .putString( KEY_USER_CARD, card )
                .commit();
    }

    public static String getUserCard( Context context ){
        SharedPreferences prefs = context.getSharedPreferences( PREFS_NAMES, Context.MODE_PRIVATE );
        return prefs.getString( KEY_USER_CARD, "" );
    }

}
