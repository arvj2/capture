package com.claro.cfc.scheduler;

import java.io.Serializable;

/**
 * Created by Jansel R. Abreu  (jrodr) on 6/6/2014.
 */
public final class SchedulerContextImp implements SchedulerContext, Serializable{

    private static ThreadLocal<SchedulerContextImp> contextThreadLocal = new ThreadLocal<SchedulerContextImp>();

    private SchedulerContextImp(){

    }

    static SchedulerContextImp getDefault(){
        if( null != contextThreadLocal.get() )
            return contextThreadLocal.get();

        synchronized ( SchedulerContextImp.class ){
            if( null == contextThreadLocal.get() )
                contextThreadLocal.set( new SchedulerContextImp() );
        }
        return contextThreadLocal.get();
    }
}
