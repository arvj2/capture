package com.claro.cfc.scheduler.services;

import org.hibernate.annotations.*;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Entity
@Table(name = "TASK")
@org.hibernate.annotations.NamedQueries({
        @NamedQuery( name="tasks.updateStatus",query =  "UPDATE TaskEntity e SET e.status=:status, modified=:modified WHERE e.taskId=:taskId" )
})
public class TaskEntity {
    private long taskId;
    private long actionId;
    private Timestamp modified;
    private Timestamp created;
    private TaskStatusEntity status;
    private TaskTypeEntity type;
    private TaskLogEntity log;

    @Id
    @Column(name = "TASK_ID")
    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "ACTION_ID")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Basic
    @Column(name = "MODIFIED")
    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @Basic
    @Column(name = "CREATED")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskEntity that = (TaskEntity) o;

        if (actionId != that.actionId) return false;
        if (taskId != that.taskId) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (modified != null ? !modified.equals(that.modified) : that.modified != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (taskId ^ (taskId >>> 32));
        result = 31 * result + (int) (actionId ^ (actionId >>> 32));
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @Fetch(FetchMode.SELECT)
    @ManyToOne
    @JoinColumn(name = "STATUS", referencedColumnName = "STATUS_ID")
    public TaskStatusEntity getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEntity status) {
        this.status = status;
    }

    @Fetch(FetchMode.SELECT)
    @ManyToOne
    @JoinColumn(name = "TASK_TYPE_ID", referencedColumnName = "TASK_TYPE_ID", nullable = false)
    public TaskTypeEntity getType() {
        return type;
    }

    public void setType(TaskTypeEntity type) {
        this.type = type;
    }

    @Fetch(FetchMode.SELECT)
    @OneToOne
    @JoinColumn(name = "TASK_ID", referencedColumnName = "TASK_ID", nullable = false)
    public TaskLogEntity getLog() {
        return log;
    }

    public void setLog(TaskLogEntity log) {
        this.log = log;
    }
}
