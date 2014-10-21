package com.claro.cfc.scheduler.tasks;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/5/2014.
 */
public abstract class Configuration implements Serializable{
    public boolean taskIsEnabled(){
        return true;
    }
}
