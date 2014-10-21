package com.claro.cfc.scheduler.services;

import javax.persistence.*;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Entity
@Table(name = "TASK_SERVICE")
public class TaskServiceEntity {
    private long tsid;
    private String serviceJndi;
    private String displayName;
    private long tcontext;

    @Id
    @Column(name = "TSID")
    public long getTsid() {
        return tsid;
    }

    public void setTsid(long tsid) {
        this.tsid = tsid;
    }

    @Basic
    @Column(name = "SERVICE_JNDI")
    public String getServiceJndi() {
        return serviceJndi;
    }

    public void setServiceJndi(String serviceJndi) {
        this.serviceJndi = serviceJndi;
    }

    @Basic
    @Column(name = "DISPLAY_NAME")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Basic
    @Column(name = "TCONTEXT")
    public long getTcontext() {
        return tcontext;
    }

    public void setTcontext(long tcontext) {
        this.tcontext = tcontext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskServiceEntity that = (TaskServiceEntity) o;

        if (tsid != that.tsid) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (serviceJndi != null ? !serviceJndi.equals(that.serviceJndi) : that.serviceJndi != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (tsid ^ (tsid >>> 32));
        result = 31 * result + (serviceJndi != null ? serviceJndi.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaskServiceEntity{" +
                "tsid=" + tsid +
                ", serviceJndi='" + serviceJndi + '\'' +
                ", displayName='" + displayName + '\'' +
                ", tcontext=" + tcontext +
                '}';
    }
}
