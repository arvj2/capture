package com.claro.cfc.scheduler.services;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
@Entity
@Table(name = "ACTION_VALUES")
public class ActionValuesEntity {
    private long valueId;
    private String akey;
    private String avalue;
    private Timestamp created;
    private long actionId;

    @Id
    @Column(name = "VALUE_ID")
    @GeneratedValue(generator = "values.sec_action_values",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "values.sec_action_values",sequenceName = "sec_action_values")
    public long getValueId() {
        return valueId;
    }

    public void setValueId(long valueId) {
        this.valueId = valueId;
    }

    @Basic
    @Column(name = "AKEY")
    public String getAkey() {
        return akey;
    }

    public void setAkey(String akey) {
        this.akey = akey;
    }

    @Basic
    @Column(name = "AVALUE")
    public String getAvalue() {
        return avalue;
    }

    public void setAvalue(String avalue) {
        this.avalue = avalue;
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
    @Column(name = "ACTION_ID")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionValuesEntity that = (ActionValuesEntity) o;

        if (actionId != that.actionId) return false;
        if (valueId != that.valueId) return false;
        if (akey != null ? !akey.equals(that.akey) : that.akey != null) return false;
        if (avalue != null ? !avalue.equals(that.avalue) : that.avalue != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (valueId ^ (valueId >>> 32));
        result = 31 * result + (akey != null ? akey.hashCode() : 0);
        result = 31 * result + (avalue != null ? avalue.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (int) (actionId ^ (actionId >>> 32));
        return result;
    }
}
