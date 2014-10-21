package com.claro.cfcmobile.robospice.response;

import android.util.Log;

/**
 * Created by Christopher Herrera on 2/18/14.
 */

public class OKResponse {

    public OKResponse(){
        Log.d("OKResponse", "Object Created");
    }

    private boolean ok;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
