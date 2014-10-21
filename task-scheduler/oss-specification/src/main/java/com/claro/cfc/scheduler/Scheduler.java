package com.claro.cfc.scheduler;

import javax.ejb.Timer;
import java.io.Serializable;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public abstract interface Scheduler extends Serializable, Startable, Stopable {
    public abstract void wakeup(Timer timer);
}
