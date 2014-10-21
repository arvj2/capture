package com.claro.cfc.oss.m6;

import com.claro.cfc.scheduler.activation.ActivationContext;
import com.claro.cfc.scheduler.activation.Activator;
import com.claro.cfc.scheduler.tasks.TaskHandler;

import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/29/2014.
 */
@Remote
@Stateless(name = "M6ActivatorEJB", mappedName = "M6Activator" )
public class M6Activator implements Activator{
    public M6Activator() {
    }

    @Override
    public TaskHandler activate(ActivationContext context) {
        return new M6TaskHandler(context);
    }
}
