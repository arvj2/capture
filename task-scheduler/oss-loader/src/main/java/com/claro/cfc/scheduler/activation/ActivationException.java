package com.claro.cfc.scheduler.activation;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public class ActivationException extends Exception{
    public ActivationException(String message) {
        super(message);
    }

    public ActivationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivationException(Throwable cause) {
        super(cause);
    }
}
