package com.claro.cfcmobile.dto;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/15/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */

public class Attribute implements Serializable {

    public Attribute() {
    }

    private String name;

    private String value = null;


    public Attribute(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Attribute name cannot be null ");
        }
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute val = (Attribute) object;

        return null != name ? (name.equals(val.name)) : (null == val.name);
    }

    public int hashCode() {
        return name.hashCode() ^ 31;
    }

    public String toString() {
        return getName() + " = " + getValue();
    }

    public static final Attribute create( String key, String value ){
        return new Attribute( key,value );
    }
}
