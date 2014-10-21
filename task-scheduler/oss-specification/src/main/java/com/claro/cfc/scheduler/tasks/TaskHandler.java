package com.claro.cfc.scheduler.tasks;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/5/2014.
 */
public abstract interface TaskHandler extends Serializable{

    public abstract Configuration getConfiguration();

    public abstract String getDisplayName();

    public abstract TaskExecutionResult perform(Task task);
}
