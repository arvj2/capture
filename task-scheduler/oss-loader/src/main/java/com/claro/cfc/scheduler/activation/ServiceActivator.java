package com.claro.cfc.scheduler.activation;

import com.claro.cfc.scheduler.SchedulerContext;
import com.claro.cfc.scheduler.services.TaskServiceEntity;
import com.claro.cfc.scheduler.tasks.TaskHandler;
import com.claro.cfc.scheduler.util.Environments;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class ServiceActivator {

    /**
     *
     * @param services
     * @return Map of key context=>task_type_id and it handlers
     * @throws ActivationException
     */
    public static final Map<Long,TaskHandler> activate(SchedulerContext schedulerContext,List<TaskServiceEntity> services) throws ActivationException {
        ActivationContextImp context = new ActivationContextImp();
        context.setToken(new NaiveActivatorToken());
        context.setSchedulerContext(schedulerContext);

        Map<Long,TaskHandler> handlers = new HashMap<Long, TaskHandler>();

        for (TaskServiceEntity service : services) {
            TaskHandler handler = activate(service, context);
            if( null != handler )
                handlers.put( service.getTcontext(), handler );
        }
        return handlers;
    }

    private static TaskHandler activate(TaskServiceEntity service, ActivationContext ac ) throws ActivationException {

        if( null == service || null == service.getServiceJndi())
            return null;

        final Logger log = Logger.getLogger(ServiceActivator.class);

        TaskHandler handler = null;

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        log.info( "Activating service "+service.getDisplayName()+"..." );
        props.put(Context.PROVIDER_URL, Environments.getDefault().providerHost());
        log.info( "Preparing host "+ Environments.getDefault().providerHost() );

        InitialContext context;
        try {
            context = new InitialContext(props);
        }catch ( NamingException ex ){
            throw  new ActivationException(ex);
        }

        try{
            if( null != context ) {
                Object object = context.lookup( service.getServiceJndi() );
                if( !(object instanceof Activator) )
                    throw new IllegalArgumentException( "Provided Service is not instance of "+Activator.class );

                Activator activator = ( Activator ) object;
                handler = activator.activate(ac);
                log.info( "Activated Task handler "+handler.getDisplayName()+" .... " );
            }
        }catch ( Exception ex ){
            log.info( "Skipping service "+service.getServiceJndi()+" activation ",ex );
        }
        return handler;
    }
}
