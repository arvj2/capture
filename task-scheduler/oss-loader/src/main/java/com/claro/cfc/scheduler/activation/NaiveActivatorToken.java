package com.claro.cfc.scheduler.activation;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public final class NaiveActivatorToken implements ActivationToken,Serializable{
    private String uuid = UUID.randomUUID().toString();
    @Override
    public String toString() {
        return uuid;
    }
}
