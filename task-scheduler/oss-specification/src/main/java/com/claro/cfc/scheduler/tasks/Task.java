package com.claro.cfc.scheduler.tasks;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/5/2014.
 */
public abstract interface Task extends Serializable{

    public abstract long getActionId();

    public abstract long getTaskId();

    public List<Attribute> getAttributes();
}
