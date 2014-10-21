package com.claro.cfc.scheduler.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class Environments {
    private static Environment mDefault = null;

    public static Environment getDefault() {
        if (null == mDefault)
            mDefault = loadDefault();
        return mDefault;
    }


    private static Environment loadDefault() {
        Logger logger = Logger.getLogger( Environments.class );

        Properties env = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("env.properties");
        if( null != in ) {
            try {
                env.load(in);
            }catch ( IOException ex ){
                logger.error( "[TaskScheduler]: Error trying to load default environment file" );
                logger.error( ex );
            }
        }

        logger.info( "[TaskScheduler]: Environment object "+env );
        final String value = (String) env.get( "invariant.environment" );
        logger.info( "[TaskScheduler]: Environment configured as "+value );
        if( null != value )
            return Environment.valueOf( value );

        throw new IllegalStateException( "[TaskScheduler]: Could not possible load environments" );
    }
}

