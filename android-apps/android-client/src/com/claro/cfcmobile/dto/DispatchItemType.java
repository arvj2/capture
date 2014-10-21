package com.claro.cfcmobile.dto;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/28/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public enum DispatchItemType {
    ORDEN, AVERIA, UNKNOWN;

    public static final DispatchItemType get(String type) {
        for( DispatchItemType itype : values() ){
           if( type.equals( itype.toString() ) )
               return itype;
        }
        return UNKNOWN;
    }
}
