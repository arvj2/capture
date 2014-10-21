package com.claro.cfc.scheduler.services.model;

import com.claro.cfc.scheduler.services.TaskServiceEntity;
import com.claro.cfc.scheduler.services.model.util.HibernateHelper;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class ServiceModel {

    private static final Logger log = Logger.getLogger(ServiceModel.class);

    public static List<TaskServiceEntity> getServices(){
        Session session = HibernateHelper.getSessionFactory().openSession();
        try{

            Criteria criteria = session.createCriteria( TaskServiceEntity.class );
            return criteria.list();
        }catch ( HibernateException ex ){
            log.error( "Error performing getServices() method ",ex );
        }finally {
            if( session.isOpen() || session.isConnected() ){
                session.flush();
                session.close();
            }
        }
        return Collections.EMPTY_LIST;
    }
}
