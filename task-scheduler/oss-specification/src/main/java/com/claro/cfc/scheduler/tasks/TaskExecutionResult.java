package com.claro.cfc.scheduler.tasks;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/29/2014.
 */
public abstract interface TaskExecutionResult extends Serializable{

    public abstract boolean success();

    public abstract String getMessage();

    public abstract String getIngoingXml();

    public abstract String getOutgoingXml();

    public abstract TaskResponseStatus getResponseStatus();
}
