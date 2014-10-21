package com.claro.cfc.scheduler.tasks;

import javax.management.RuntimeOperationsException;
import java.io.Serializable;


/**
 * Created by Jansel R. Abreu (jrodr) on 6/5/2014.
 */
public final class Attribute implements Serializable {

    public Attribute() {
    }

    private String name;

    private String value = null;


    public Attribute(String name, String value) {
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null "));
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


    public static final Attribute createAttribute(String key, String value) {
        return new Attribute(key, value);
    }
}
