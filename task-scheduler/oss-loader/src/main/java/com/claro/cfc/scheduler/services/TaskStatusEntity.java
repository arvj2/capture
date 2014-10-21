package com.claro.cfc.scheduler.services;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Entity
@Table(name = "TASK_STATUS")
public class TaskStatusEntity {
    private long statusId;
    private String status;
    private List<TaskEntity> tasks;

    @Id
    @Column(name = "STATUS_ID")
    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskStatusEntity that = (TaskStatusEntity) o;

        if (statusId != that.statusId) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (statusId ^ (statusId >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "status")
    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
