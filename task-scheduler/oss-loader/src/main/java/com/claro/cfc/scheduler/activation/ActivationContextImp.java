package com.claro.cfc.scheduler.activation;

import com.claro.cfc.scheduler.SchedulerContext;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class ActivationContextImp implements ActivationContext , Serializable{
    private ActivationToken token;
    private SchedulerContext context;

    @Override
    public SchedulerContext getShedulerContext() {
        return context;
    }

    @Override
    public ActivationToken getToken() {
        return token;
    }

    public void setToken(ActivationToken token) {
        this.token = token;
    }

    public void setSchedulerContext(SchedulerContext context) {
        this.context = context;
    }
}
