package com.claro.cfc.scheduler.tasks;

import java.util.List;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class TaskImp implements Task {

    private long taskId;
    private long actionId;
    private List<Attribute> attributes;

    public TaskImp(long taskId, long actionId, List<Attribute> attributes) {
        this.attributes = attributes;
        this.taskId = taskId;
        this.actionId = actionId;
    }

    @Override
    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Override
    public String toString() {
        return "TaskImp{" +
                "taskId=" + taskId +
                ", actionId=" + actionId +
                ", attributes=" + attributes +
                '}';
    }
}
