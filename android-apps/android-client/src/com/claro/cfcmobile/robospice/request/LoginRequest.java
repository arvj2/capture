package com.claro.cfcmobile.robospice.request;

import android.util.Log;
import com.claro.cfcmobile.robospice.response.OKResponse;
import com.claro.cfcmobile.utils.HttpRestRequester;
import com.claro.cfcmobile.utils.HttpUtils;
import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

/**
 * Created by Christopher Herrera on 2/18/14.
 * <p/>
 * Modified by Jansel R. Abreu (Vanwolf)
 */
public class LoginRequest extends SpiceRequest<OKResponse> {
    public static final String TAG = "LoginRequest";


    private String card;
    private String deviceId;
    private Gson gson = new Gson();

    public LoginRequest(String card, String deviceId) {
        super(OKResponse.class);
        this.card = card;
        this.deviceId = deviceId;
        Log.e(TAG, "LoginActivity Request Created(" + deviceId + "," + card + ")");
    }


    @Override
    public OKResponse loadDataFromNetwork() throws Exception {

        String path = HttpUtils.matchSymbol(HttpUtils.PATH_TEMPLATE_LOGIN, "@", "", new String[]{card, deviceId});
        String url  = HttpUtils.SERVER_URL + path;

        String response = HttpRestRequester.makeHttpGetRequest(url);
        if (response == null) {
            Log.d(TAG, "Response is null");
            return null;
        }
        Log.e(TAG, "Login response:" + response);
        return gson.fromJson(response, OKResponse.class);
    }

    public String createCacheKey() {
        return "Response=" + card;
    }
}
