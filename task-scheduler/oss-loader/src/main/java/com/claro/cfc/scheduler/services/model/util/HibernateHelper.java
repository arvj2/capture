package com.claro.cfc.scheduler.services.model.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class HibernateHelper {

    private static final ThreadLocal<SessionFactory> threadLocal = new ThreadLocal<SessionFactory>() {
        @Override
        protected SessionFactory initialValue() {
            return new AnnotationConfiguration().configure().buildSessionFactory();
        }
    };

    public static SessionFactory getSessionFactory() {
        return threadLocal.get();
    }
}
