package com.claro.cfc.oss.geff;

import com.claro.cfc.scheduler.activation.ActivationContext;
import com.claro.cfc.scheduler.activation.Activator;
import com.claro.cfc.scheduler.tasks.TaskHandler;

import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/29/2014.
 */
@Remote
@Stateless(name = "GeffActivatorEJB", mappedName = "GeffActivator" )
public class GeffActivator implements Activator{
    public GeffActivator() {
    }

    @Override
    public TaskHandler activate(ActivationContext context) {
        return new GeffTaskHandler(context);
    }
}
