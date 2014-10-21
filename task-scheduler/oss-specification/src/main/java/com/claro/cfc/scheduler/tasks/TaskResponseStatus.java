package com.claro.cfc.scheduler.tasks;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/29/2014.
 */
public enum TaskResponseStatus implements Serializable{
    NO_ERROR,NO_CHANGE,ASSIGNED,NOT_FOUND,UNKNOWN;
}
