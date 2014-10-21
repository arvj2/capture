package com.claro.cfc.scheduler;

import com.claro.cfc.scheduler.activation.ServiceActivator;
import com.claro.cfc.scheduler.services.TaskServiceEntity;
import com.claro.cfc.scheduler.services.model.ServiceModel;
import com.claro.cfc.scheduler.tasks.TaskHandler;
import com.claro.cfc.scheduler.util.CalendarHelper;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.Timer;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Remote
@Stateless(name = "Scheduler", mappedName = "TaskScheduler")
public class TaskScheduler implements Scheduler {

    private static final long MAX_ACTIVATION_RETRY_INTENT = 4;

    @Resource
    private TimerService ts;

    private final Logger log;

    private int retryIntent = 1;

    private String name;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private SchedulerContextImp schedulerContext;


    public TaskScheduler() {
        this.name = "TaskScheduler[" + Thread.currentThread() + "]";
        log = Logger.getLogger(name);
        log.info("Creating TaskScheduler....");
    }

    public String getName() {
        return name;
    }


    @Timeout
    public void wakeup(Timer timer) {
        if (isRunning.get())
            return;

        log.info( "************* Number of timers "+ts.getTimers().size()+" ******************" );

        isRunning.set(true);
        retryIntent = 1;
        boolean done = false;
        log.info("TaskScheduler timeout collapsed, Initiating OSS update...");

        while (!done && MAX_ACTIVATION_RETRY_INTENT >= retryIntent) {
            try {
                if( null == schedulerContext )
                    schedulerContext = SchedulerContextImp.getDefault();

                Map<Long, TaskHandler> handlers = ServiceActivator.activate(schedulerContext, loadServices());
                scheduleHandlers(handlers);
                done = true;
            } catch (Exception ex) {
                done = false;
                log.info(ex);
            }

            /**
             * If exists an error during activation, exponential backoff algorithm is used to retry intent
             */
            if (!done) {
                try {
                    int backoff = 0;
                    if (MAX_ACTIVATION_RETRY_INTENT >= retryIntent) {
                        backoff = (int) Math.floor(Math.pow(2, retryIntent) - 1);
                        backoff = new Random(backoff + 1).nextInt(backoff + 1);
                    }
                    log.error("There was an  error trying to activate service retrying  in "+backoff+" minutes  on retryIntent #"+retryIntent);
                    TimeUnit.MINUTES.sleep(backoff);
                    ++retryIntent;
                } catch (InterruptedException ex) {
                    log.error(ex);
                }
            }
        }
        if (done)
            log.info("OSS Activation task performed successfully.!");
        else {
            log.info("OSS Update could no tbe possible due on activation problem.");
            destroy();
        }

        scheduleTimerForFuture();
        isRunning.set(false);
    }


    private void scheduleTimerForFuture(){
        /**
         * Esta es la parte que va
         */
//        Calendar tomorrow = CalendarHelper.tomorrow(8);
//        Calendar now = CalendarHelper.toDay();

        Calendar tomorrow = Calendar.getInstance();
//        tomorrow.set( Calendar.HOUR_OF_DAY,8 );
        tomorrow.set( Calendar.SECOND,0);
        tomorrow.set( Calendar.MILLISECOND,0 );
        tomorrow.set( Calendar.MINUTE, tomorrow.get( Calendar.MINUTE )+5 );

        Calendar now = CalendarHelper.toDay();
        /**
         * Esto tambien que va
         */
//        if( tomorrow.getTime().before(now.getTime()) )
//            tomorrow.roll( Calendar.DAY_OF_YEAR,1 );


        ts.createTimer(Math.abs( tomorrow.getTimeInMillis()-now.getTimeInMillis()),null);
        log.info( "\n\n*********** Re-Scheduled at "+now.getTime() +" to wake up at "+tomorrow.getTime()+" ***********\n" );
    }

    private List<TaskServiceEntity> loadServices() {
        return ServiceModel.getServices();
    }

    private void scheduleHandlers(Map<Long, TaskHandler> handlers) {
        if (null == handlers || 0 == handlers.size()) {
            log.info("No Services available, "+handlers);
            destroy();
            return;
        }

        final int threadCount = 5;
        try {
            final CountDownLatch sync = new CountDownLatch(handlers.size());
            ExecutorService exec = Executors.newFixedThreadPool(threadCount);

            for (long context : handlers.keySet()) {
                TaskHandler handler = handlers.get(context);
                if( null == handler )
                    continue; // This would never happen

                if ( null != handler.getConfiguration() && handler.getConfiguration().taskIsEnabled()) {
                    exec.execute(new AsyncTasksWorker(context, handler, sync, this));
                } else {
                    sync.countDown();
                }
            }

            sync.await();
            exec.shutdownNow();
            log.info("All service were performed successfully");
            destroy();
        } catch (InterruptedException ex) {
            log.error("Error trying to get blocking operation ", ex);
        }
    }


    private void destroy() {
        log.info("Destroying " + name + "...");
        schedulerContext = null;
    }


    @Override
    public void start() {
        Calendar tomorrow = Calendar.getInstance();
//        tomorrow.set( Calendar.HOUR_OF_DAY,8 );
        tomorrow.set( Calendar.SECOND,0);
        tomorrow.set( Calendar.MILLISECOND,0 );
        tomorrow.set( Calendar.MINUTE, tomorrow.get( Calendar.MINUTE )+5 );

        Calendar now = CalendarHelper.toDay();
        if( tomorrow.getTime().before(now.getTime()) )
            tomorrow.roll( Calendar.DAY_OF_YEAR,1 );

        log.info( "\n\n*********** Scheduled at "+now.getTime() +" to wake up at "+tomorrow.getTime()+"****************\n" );
        ts.createTimer( Math.abs( tomorrow.getTimeInMillis()-now.getTimeInMillis() ),null );
    }


    @Override
    public void stop() {
        Collection<Timer> timers = ts.getTimers();
        for (Timer timer : timers) {
            log.info("Stopping timer wih remaining timeout " + timer.getTimeRemaining());
            timer.cancel();
        }
        log.info("Stopping " + name + "...");
        destroy();
    }
}

