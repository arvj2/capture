package com.claro.cfcmobile.robospice.request;

import android.util.Log;
import com.claro.cfcmobile.robospice.response.VersionResponse;
import com.claro.cfcmobile.utils.HttpRestRequester;
import com.claro.cfcmobile.utils.HttpUtils;
import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/25/2014.
 */
public class VersionRequest extends SpiceRequest<VersionResponse> {

    public VersionRequest() {
        super(VersionResponse.class);
    }

    @Override
    public VersionResponse loadDataFromNetwork() throws Exception {

        String urlRequest = HttpUtils.SERVER_URL + HttpUtils.PATH_REMOTE_VERSION;
        String response = HttpRestRequester.makeHttpGetRequest(urlRequest);

        Log.e("VersionRequest", "VersionRequest response " + response);
        try {
            return new Gson().fromJson(response, VersionResponse.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String createCacheKey() {
        return "VersionRequest";
    }

}
