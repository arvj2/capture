package com.claro.cfc.scheduler.manager;

import com.claro.cfc.scheduler.Scheduler;
import com.claro.cfc.scheduler.activation.Activator;
import com.claro.cfc.scheduler.manager.util.Environment;
import com.claro.cfc.scheduler.manager.util.Environments;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Properties;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/26/2014.
 */
public class SchedulerManagerListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.err.println("\n\n************ Initializing Scheduler ************\n");
        getScheduler().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.err.println("\n\n************ Destroying Scheduler ************\n");
        getScheduler().stop();
    }

    private Scheduler getScheduler(){
        Logger log = Logger.getLogger( SchedulerManagerListener.class );
        Environment env = Environments.getDefault();

        log.info( "Configured Environment: "+env );
        Properties props = new Properties();

        log.info( "Configuring host: "+env.providerHost() );
        props.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory" );
        props.put(Context.PROVIDER_URL, env.providerHost() );


        InitialContext context;
        try{
            context = new InitialContext(props);
        }catch( Exception ex) {
            log.error( ex );
            throw new IllegalStateException(ex);
        }

        try {
            Object aS = context.lookup("TaskScheduler#com.claro.cfc.scheduler.Scheduler");
            if( aS instanceof Scheduler){
                return (Scheduler)aS;
            }else{
                throw new IllegalArgumentException("Registered Scheduler is not instance of com.claro.cfc.scheduler.Scheduler");
            }
        }catch ( Exception ex) {
            throw  new IllegalStateException( ex );
        }
    }
}
