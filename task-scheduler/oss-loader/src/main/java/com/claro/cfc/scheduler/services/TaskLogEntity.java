package com.claro.cfc.scheduler.services;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Entity
@Table(name = "TASK_LOG")
public class TaskLogEntity {
    private long taskLogId;
    private int success;
    private long taskId;
    private String message;
    private String outgoingXml;
    private String ingoingXml;
    private Timestamp created;
    private TaskResponseTypeEntity responseType;

    @Id
    @SequenceGenerator(name = "logs.sec_task_log",sequenceName = "SEC_TASK_LOG" )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "logs.sec_task_log" )
    @Column(name = "TASK_LOG_ID")
    public long getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(long taskLogId) {
        this.taskLogId = taskLogId;
    }

    @Basic
    @Column(name = "OUTGOING_XML")
    public String getOutgoingXml() {
        return outgoingXml;
    }

    public void setOutgoingXml(String outgoingXml) {
        this.outgoingXml = outgoingXml;
    }

    @Basic
    @Column(name = "INGOING_XML")
    public String getIngoingXml() {
        return ingoingXml;
    }

    public void setIngoingXml(String ingoingXml) {
        this.ingoingXml = ingoingXml;
    }

    @Basic
    @Column(name = "CREATED")
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }


    @Basic
    @Column( name= "SUCCESS" )
    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Basic
    @Column( name= "MESSAGE" )
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Basic
    @Column( name= "TASK_ID" )
    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskLogEntity that = (TaskLogEntity) o;

        if (taskLogId != that.taskLogId) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        if (ingoingXml != null ? !ingoingXml.equals(that.ingoingXml) : that.ingoingXml != null) return false;
        if (outgoingXml != null ? !outgoingXml.equals(that.outgoingXml) : that.outgoingXml != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (taskLogId ^ (taskLogId >>> 32));
        result = 31 * result + (outgoingXml != null ? outgoingXml.hashCode() : 0);
        result = 31 * result + (ingoingXml != null ? ingoingXml.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @Fetch(FetchMode.SELECT)
    @ManyToOne
    @JoinColumn(name = "RESPONSE_TYPE", referencedColumnName = "RID", nullable = false)
    public TaskResponseTypeEntity getResponseType() {
        return responseType;
    }

    public void setResponseType(TaskResponseTypeEntity responseType) {
        this.responseType = responseType;
    }
}
