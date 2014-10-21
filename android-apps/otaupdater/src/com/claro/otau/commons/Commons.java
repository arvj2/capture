package com.claro.otau.commons;

import android.os.Environment;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/15/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public class Commons {

    public static final String DOWNLOAD_EXTERNAL_PART = "/download/";

    public static final String DOWNLOAD_EXTERNAL_URI  = Environment.getExternalStorageDirectory().getAbsolutePath().concat( DOWNLOAD_EXTERNAL_PART );

}
