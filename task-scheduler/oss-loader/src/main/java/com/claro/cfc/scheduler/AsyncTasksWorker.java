package com.claro.cfc.scheduler;

import com.claro.cfc.scheduler.services.TaskEntity;
import com.claro.cfc.scheduler.services.TaskLogEntity;
import com.claro.cfc.scheduler.services.TaskStatusEnum;
import com.claro.cfc.scheduler.services.model.TaskModel;
import com.claro.cfc.scheduler.tasks.*;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class AsyncTasksWorker implements Runnable {

    private final Logger log;

    private Long context;
    private String name;
    private TaskHandler handler;
    private CountDownLatch sync;
    private TaskScheduler scheduler;

    public AsyncTasksWorker(Long context, TaskHandler handler, CountDownLatch sync, TaskScheduler scheduler) {
        this.context = context;
        this.handler = handler;
        this.sync = sync;
        this.scheduler = scheduler;
        name = "AsyncTasksWorker(:context:" + context + ")[" + scheduler.getName() + "]";
        log = Logger.getLogger(name);
    }


    @Override
    public void run() {
        log.info(Thread.currentThread() + " Running task for context " + context + ", begin");
        List<TaskImp> tasks = getTasks();
        for (TaskImp task : tasks) {
            try {
                TaskExecutionResult result = handler.perform(task);
                processTaskResult(handler, task, result);
            }catch ( Exception ex ){
                log.error( "*********** Error processing Task #"+task.getTaskId()+" for handler "+handler.getDisplayName()+" ***********" );
                ex.printStackTrace();
            }
        }
        if( null != sync )
            sync.countDown();
    }


    private void processTaskResult(TaskHandler handler,Task task, TaskExecutionResult result) {
        if (null == result) {
            log.error("Error: TaskScheduler is getting null TaskExecutionResult for " + handler.getDisplayName() + " handler ");
            return;
        }

        log.info( "********** Updating Action "+task.getActionId()+" *********** " );
        log.info( "********** TaskResultInfo  "+result+" **********" );
        TaskModel.setActionToProcessed(task.getActionId());

        TaskStatusEnum status = !result.success() ? TaskStatusEnum.ABORTED : TaskStatusEnum.PROCESSED;
        TaskModel.updateTaskStatus(task.getTaskId(),status);

        Timestamp current = new Timestamp( new Date().getTime() );

        TaskLogEntity taskLog = new TaskLogEntity();
        taskLog.setCreated(current);
        taskLog.setMessage( result.getMessage() );
        taskLog.setSuccess( result.success() ? 1 : 0 );
        taskLog.setTaskId( task.getTaskId() );
        taskLog.setIngoingXml( result.getIngoingXml() );
        taskLog.setOutgoingXml( result.getOutgoingXml() );
        taskLog.setResponseType( TaskModel.getTaskResponseType( result.getResponseStatus() ) );

        TaskModel.insertTaskLog(taskLog);
    }


    private List<TaskImp> getTasks() {
        final List<TaskEntity> rawTasks = TaskModel.getTaskByContext(context);
        final List<TaskImp> tasks = new ArrayList<TaskImp>();

        if (null != rawTasks && 0 != rawTasks.size()) {
            for (TaskEntity entity : rawTasks) {

                long actionId = entity.getActionId();
                long taskId = entity.getTaskId();

                tasks.add(new TaskImp(taskId, actionId, getAttributes(entity.getActionId())));
            }
        }
        return tasks;
    }


    private List<Attribute> getAttributes(Long actionId) {
        return TaskModel.getAttributes(actionId);
    }
}
