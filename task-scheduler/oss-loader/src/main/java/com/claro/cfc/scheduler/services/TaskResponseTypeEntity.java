package com.claro.cfc.scheduler.services;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Entity
@Table(name = "TASK_RESPONSE_TYPE")
public class TaskResponseTypeEntity {
    private long rid;
    private String rtype;
    private List<TaskLogEntity> tasks;

    @Id
    @Column(name = "RID")
    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    @Basic
    @Column(name = "RTYPE")
    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskResponseTypeEntity that = (TaskResponseTypeEntity) o;

        if (rid != that.rid) return false;
        if (rtype != null ? !rtype.equals(that.rtype) : that.rtype != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (rid ^ (rid >>> 32));
        result = 31 * result + (rtype != null ? rtype.hashCode() : 0);
        return result;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "responseType")
    public List<TaskLogEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskLogEntity> tasks) {
        this.tasks = tasks;
    }
}
