package com.claro.cfc.scheduler.activation;

import com.claro.cfc.scheduler.SchedulerContext;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/5/2014.
 */
public abstract interface ActivationContext extends Serializable{
    public abstract SchedulerContext getShedulerContext();

    public abstract ActivationToken getToken();
}
