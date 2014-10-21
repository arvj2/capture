package com.claro.cfc.oss.utils;

import com.claro.cfc.scheduler.tasks.TaskExecutionResult;
import com.claro.cfc.scheduler.tasks.TaskResponseStatus;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 9/29/2014.
 */
public class GenericTaskExecutionResult implements TaskExecutionResult{

    private boolean success;
    private String errorType;
    private String errorMessage;
    private String ingoingXml;
    private String outgoingXml;

    @Override
    public boolean success() {
        return success;
    }

    public void success( boolean error ){
        this.success = error;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public void setMessage( String message ){
        this.errorMessage = message;
    }

    @Override
    public String getIngoingXml() {
        return  ingoingXml;
    }

    public void setIngoingXml(String ingoingXml) {
        this.ingoingXml = ingoingXml;
    }

    @Override
    public String getOutgoingXml() {
        return outgoingXml;
    }

    public void setOutgoingXml(String outgoingXml) {
        this.outgoingXml = outgoingXml;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    @Override
    public TaskResponseStatus getResponseStatus() {
        return null == errorType ? TaskResponseStatus.UNKNOWN : TaskResponseStatus.valueOf(errorType);
    }

    @Override
    public String toString() {
        return "GenericTaskExecutionResult{" +
                "success=" + success +
                ", errorType='" + errorType + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", ingoingXml='" + ingoingXml + '\'' +
                ", outgoingXml='" + outgoingXml + '\'' +
                '}';
    }
}

