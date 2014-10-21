package com.claro.cfc.oss.environment;

/**
 * Created by Jansel R. Abreu (jrodr) on 6/6/2014.
 */
public enum Environment {
    DEVELOPMENT{
        @Override
        public String m6Host() {
            return "http://nttmssmgd0008:8010/COFEE_MSSWeb/com/verizon/api/services/QuickOrderManager.jws";
        }

        @Override
        public String geffHost() {
            return "http://nttappsweb0003/GEFFSERVICE/CFCService.asmx";
        }
    },
    STAGING{
        @Override
        public String m6Host() {
            return "http://nttmssmgd0008:8010/COFEE_MSSWeb/com/verizon/api/services/QuickOrderManager.jws";
        }

        @Override
        public String geffHost() {
            return "http://nttappsweb0003/GEFFSERVICE/CFCService.asmx";
        }
    },
    PRODUCTION{
        @Override
        public String m6Host() {
            return "http://ntpm6int0001:8010/COFEE_MSSWeb/com/verizon/api/services/QuickOrderManager.jws";
        }

        @Override
        public String geffHost() {
            return "http://ntpappsweb0004/GEFFSERVICES/CFCService.asmx";
        }
    };

    public abstract String m6Host();

    public abstract String geffHost();
}
