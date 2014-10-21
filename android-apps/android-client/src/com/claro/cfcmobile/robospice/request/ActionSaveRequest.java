package com.claro.cfcmobile.robospice.request;

import android.util.Log;
import com.claro.cfcmobile.dto.Attribute;
import com.claro.cfcmobile.robospice.response.OKResponse;
import com.claro.cfcmobile.utils.HttpRestRequester;
import com.claro.cfcmobile.utils.HttpUtils;
import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/16/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public class ActionSaveRequest extends SpiceRequest<OKResponse>{
    public static final String TAG = "ActionSaveRequest";

    private String card;
    private List<Attribute> attributes;

    public ActionSaveRequest( String card, List<Attribute> attributes ){
        super(OKResponse.class);
        this.card = card;
        this.attributes = attributes;

        Log.e(TAG, "ActionSaveRequest Request Created(" + card + ")");
    }


    @Override
    public OKResponse loadDataFromNetwork() throws Exception {
        if( null == attributes || 0 == attributes.size() )
            return  null;

        Gson gson = new Gson();
        String json = gson.toJson( attributes );


        Log.e(TAG, "Sending data " + json);

        String path = HttpUtils.matchSymbol(HttpUtils.PATH_TEMPLATE_ACTIONS, "@", "", new String[]{card});
        String url  = HttpUtils.SERVER_URL + path;

        String response = HttpRestRequester.makeHttpPostRequest( url,json );
        if (response == null) {
            Log.e(TAG, "Response is null");
            return null;
        }
        Log.e(TAG, "Action Send  response:" + response);
        return gson.fromJson(response, OKResponse.class);
    }

    public String createCacheKey() {
        return "ActionSaved=" + card;
    }
}
