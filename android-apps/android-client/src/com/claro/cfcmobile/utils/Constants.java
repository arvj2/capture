package com.claro.cfcmobile.utils;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/16/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public final class Constants {

    public static final String PHONE = "TELEFONO";
    public static final String CABINA = "CABINA";
    public static final String CUENTA_FIJA = "CUENTA_FIJA";
    public static final String PAR_FEEDER = "PAR_FEEDER";
    public static final String TERMINAL = "TERMINAL";
    public static final String PAR_LOCAL = "PAR_LOCAL";
    public static final String DIRECCION_CORRECTA = "DIRECCION_CORRECTA";
    public static final String TIPO_PUERTO = "TIPO_PUERTO";
    public static final String DSLAM = "DSLAM";
    public static final String LOCALIDAD = "LOCALIDAD";
    public static final String PUERTO = "PUERTO";
    public static final String TERMINAL_FO = "TERMINAL_FO";
    public static final String SPLITTER_PORT = "SPLITTER_PORT";
    public static final String SPLITTER = "SPLITTER";


    public static final String REGEX_PHONE = "8(0|2|4)9\\-\\d{3}-\\d{4}";
    public static final String REGEX_FEEDER = "^[a-zA-Z0-9]+-\\d{4}";
    public static final String REGEX_TERMINAL_FO = "FC[A-Z].{1,5}-[A-Z]\\d{1,2}";
    public static final String REGEX_FOUR_DIGITS = "\\d{4}";
    public static final String REGEX_TERMINAL = "^[a-zA-Z0-9]+-[a-zA-Z0-9]+$";
    public static final String REGEX_PAR_LOCAL = "^[a-zA-Z0-9]+-\\d{4}$";

    /**
     *
     *
     * Cabina-FO: FC[A-Z].{1,5}
     Terminal-FO: {Cabina ingresada}-[A-Z]\d{1,2}
     Splitter: SPT-{Terminal ingresado}
     Splitterport: SPT-{TERMINAL ingresado}-SP.{2,3}

     *
     */

    public static final String STATUS_FLAG_PREFERENCES = "status.flag";
}
