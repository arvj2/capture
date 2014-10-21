package com.claro.cfc.scheduler.services;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Entity
@Table(name = "TASK_TYPE")
public class TaskTypeEntity {
    private long taskTypeId;
    private String typeName;
    private List<TaskEntity> tasks;

    @Id
    @Column(name = "TASK_TYPE_ID")
    public long getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(long taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    @Basic
    @Column(name = "TYPE_NAME")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskTypeEntity that = (TaskTypeEntity) o;

        if (taskTypeId != that.taskTypeId) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (taskTypeId ^ (taskTypeId >>> 32));
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "type")
    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
