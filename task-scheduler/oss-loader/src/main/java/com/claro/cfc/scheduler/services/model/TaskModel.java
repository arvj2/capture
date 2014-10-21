package com.claro.cfc.scheduler.services.model;

import com.claro.cfc.scheduler.services.*;
import com.claro.cfc.scheduler.services.model.util.HibernateHelper;
import com.claro.cfc.scheduler.tasks.Attribute;
import com.claro.cfc.scheduler.tasks.TaskResponseStatus;
import com.claro.cfc.scheduler.util.CalendarHelper;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class TaskModel {

    private static final Logger log = Logger.getLogger(TaskModel.class);

    public static List<TaskEntity> getTaskByContext(Long context) {

        TaskStatusEntity status = getTaskStatus(TaskStatusEnum.PENDING);
        if( null != status ) {
            final Session session = HibernateHelper.getSessionFactory().openSession();
            try {

                Date toDay = CalendarHelper.removeTime(CalendarHelper.toDay().getTime());
                Date tomorrow = CalendarHelper.removeTime(CalendarHelper.tomorrow().getTime());

                log.info("\n\n************* Getting task from " + toDay + " to " + tomorrow + " for context " + context + " with status " + status.getStatus() + " *************\n");
                Criteria criteria = session.createCriteria(TaskEntity.class);
                criteria.add(Restrictions.eq("type.taskTypeId", context));
                criteria.add(Restrictions.ge("created", new Timestamp(toDay.getTime())));
                criteria.add(Restrictions.lt("created", new Timestamp(tomorrow.getTime())));
                criteria.add(Restrictions.eq("status", status));

                return criteria.list();

            } catch (HibernateException ex) {
                log.error("Error at getTaskByContext(" + context + ") call ", ex);
            } finally {
                if (session.isOpen() || session.isConnected()) {
                    session.flush();
                    session.close();
                }
            }
        }
        return Collections.EMPTY_LIST;
    }




    public static List<Attribute> getAttributes(Long actionId) {
        final Session session = HibernateHelper.getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(ActionValuesEntity.class);
            criteria.add(Restrictions.eq("actionId", actionId));

            List<Attribute> attributes = new ArrayList<Attribute>();

            List<ActionValuesEntity> actionValues = criteria.list();
            for (int i = 0; i < actionValues.size(); ++i) {
                ActionValuesEntity av = actionValues.get(i);

                attributes.add(Attribute.createAttribute(av.getAkey(), av.getAvalue()));
            }

            return attributes;
        } catch (HibernateException ex) {
            log.error("Error ar getAttributes(" + actionId + ") call ", ex);
        } finally {
            if (session.isOpen() || session.isConnected()) {
                session.flush();
                session.close();
            }
        }

        return Collections.EMPTY_LIST;
    }




    public static boolean setActionToProcessed(Long actionId) {
        final Session session = HibernateHelper.getSessionFactory().openSession();
        boolean updated = true;

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Date current = CalendarHelper.toDay().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss ");
            String date = formatter.format(current);
            Query query = session.createSQLQuery("UPDATE ACTIONS SET PROCESSED = 1, PROCESSED_DATE='" + date + "' WHERE ACTION_ID="+actionId);
            query.executeUpdate();

            tx.commit();
        } catch (HibernateException ex) {
            updated = false;
            log.error("Error at setActionToProcessed(" + actionId + ") call", ex);
            if (null != tx && !tx.wasCommitted()) {
                tx.rollback();
            }
        } finally {
            if (session.isOpen() || session.isConnected()) {
                session.flush();
                session.close();
            }
        }

        return updated;
    }




    public static TaskStatusEntity getTaskStatus(TaskStatusEnum status) {
        final Session session = HibernateHelper.getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(TaskStatusEntity.class);
            criteria.add(Restrictions.eq("status", status.toString()));

            List<TaskStatusEntity> statuses = criteria.list();
            if (null != statuses && 0 != statuses.size())
                return statuses.get(0);

        } catch (HibernateException ex) {
            log.error("Error at getTaskStatus(" + status + ") call", ex);
        } finally {
            if (session.isOpen() || session.isConnected()) {
                session.flush();
                session.close();
            }
        }
        return null;
    }


    public static boolean updateTaskStatus( Long taskId, TaskStatusEnum status ){
        TaskStatusEntity statusEntity = getTaskStatus(status);

        boolean updated = false;
        if( null != statusEntity ){
            final Session session = HibernateHelper.getSessionFactory().openSession();
            Transaction tx = null;
            try{
                tx = session.beginTransaction();

                Timestamp current = new Timestamp( new Date().getTime() );

                Query query = session.getNamedQuery( "tasks.updateStatus" );
                query.setParameter( "status",statusEntity );
                query.setParameter( "modified",current );
                query.setParameter( "taskId", taskId );

                query.executeUpdate();

                tx.commit();
                updated = true;
            }catch ( HibernateException ex ){
                log.error( "Error at updateTaskStatus("+taskId+","+status+") call ",ex );
                if( null != tx && !tx.wasCommitted() )
                    tx.rollback();
                updated = false;
            }finally {
                if( session.isOpen() || session.isConnected() ){
                    session.flush();
                    session.close();
                }
            }
        }
        return updated;
    }


    public static boolean insertTaskLog( TaskLogEntity taskLog ){
        boolean saved = false;
        if( null != taskLog ) {
            final Session session = HibernateHelper.getSessionFactory().openSession();
            Transaction tx = null;

            try{
                tx = session.beginTransaction();
                session.saveOrUpdate(taskLog);
                tx.commit();
               saved = true;
            }catch ( HibernateException ex ){
                log.error( "Error at insertTaskLog("+taskLog+") call ",ex );
                if( null == tx && !tx.wasCommitted() )
                    tx.rollback();
            }finally {
                if( session.isOpen() || session.isConnected() ){
                    session.flush();
                    session.close();
                }
            }
        }
        return saved;
    }



    public static TaskResponseTypeEntity getTaskResponseType( TaskResponseStatus type ){
        final Session session = HibernateHelper.getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(TaskResponseTypeEntity.class);
            type = null == type ? TaskResponseStatus.UNKNOWN : type;
            criteria.add(Restrictions.eq("rtype", type.toString()));

            List<TaskResponseTypeEntity> types = criteria.list();
            if (null != types && 0 != types.size())
                return types.get(0);

        } catch (HibernateException ex) {
            log.error("Error at getTaskResponseType(" + type + ") call", ex);
        } finally {
            if (session.isOpen() || session.isConnected()) {
                session.flush();
                session.close();
            }
        }
        return null;
    }

}

