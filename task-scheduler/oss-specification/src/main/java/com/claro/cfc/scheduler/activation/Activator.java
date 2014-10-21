package com.claro.cfc.scheduler.activation;

import com.claro.cfc.scheduler.tasks.TaskHandler;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/5/2014.
 */
public abstract interface Activator extends Serializable{
    public abstract TaskHandler activate(ActivationContext context);
}
