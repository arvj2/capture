package com.claro.cfc.scheduler.manager.util;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public enum Environment {
    DEVELOPMENT{
        @Override
        public String providerHost() {
            return "t3://localhost:7001";
        }
    },
    STAGING{
        @Override
        public String providerHost() {
            return "t3://172.27.17.161:8020";
        }
    },
    PRODUCTION{
        @Override
        public String providerHost() {
            return "";
        }
    };

    public abstract String providerHost();
}
