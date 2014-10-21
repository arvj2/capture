package com.claro.cfcmobile.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.claro.cfcmobile.activities.prefs.UserKnowledge;
import com.claro.cfcmobile.db.contracts.DIListContract;
import com.claro.cfcmobile.db.contracts.DispatchItemsContract;
import com.claro.cfcmobile.utils.Constants;
import com.claro.cfcmobile.utils.HttpRestRequester;
import com.claro.cfcmobile.utils.HttpUtils;
import com.claro.cfcmobile.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/12/14.
 */
public class DIDownloaderService extends IntentService {
    private static final String NAME = "Dispatch Item Downloader Service";

    public static final int ACTION_BIND_SUBSCRIBE = 1;
    public static final int ACTION_BIND_PING = 2;
    public static final int ACTION_BIND_PING_AND_SUBSCRIBE = 3;
    public static final int ACTION_BIND_NOTIFICATION_ISSUED = 4;

    private boolean loadingData;

    private SparseArray<Messenger> listeners = new SparseArray<Messenger>();


    public DIDownloaderService() {
        super(NAME);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(handler).getBinder();
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e(NAME, "Trying to handle onHandleIntent");

        String card = UserKnowledge.getUserCard(this);
        if (TextUtils.isEmpty(card)) {
            Log.e(NAME, "can't begin dispatch items downloader due to missed card");
            return;
        }

        loadingData = true;
        publish(loadingData);

        Log.e(NAME, "Begin Dispatch Items download process for card " + card);
        String deviceId = Utils.getDeviceImei(this);

        String path = HttpUtils.matchSymbol(HttpUtils.PATH_TEMPLATE_ITEMS, "@", "", new String[]{card, deviceId});
        String url = HttpUtils.SERVER_URL + path;

        Log.e(NAME, "Attempt to make request to " + url);
        HttpGet get = new HttpGet(url);


        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, HttpRestRequester.CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HttpRestRequester.SOCKET_TIMEOUT);

        HttpClient client = new DefaultHttpClient( params );
        HttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            Log.e(NAME, "Some error happens at download time " + e);
            e.printStackTrace();
        }

        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            try {
                String line = EntityUtils.toString(response.getEntity());
                processResponse(line);
            } catch (IOException ioe) {
                Log.e(NAME, "Some error happens at download time " + ioe);
                ioe.printStackTrace();
            }
        } else {
            if( null != response )
                Log.e(NAME, "Some error happens and retrieved error code " + response.getStatusLine().getStatusCode() + ", exiting...");
            else
                Log.e(NAME, "Something go completely wrong with null response");
        }

        Log.e(NAME, "Exiting from download ------- ");
        loadingData = false;
        publish(loadingData);
    }

    private void processResponse(String line) {
        Log.e(NAME, "Parsing response: " + line);
        Gson gson = new Gson();
        try {
            try {
                JsonObject jsonObject = gson.fromJson(line, JsonObject.class);

                JsonElement element = jsonObject.get("retain");
                processRetainsOrders(element);

                element = jsonObject.get("release");
                processReleaseOrders(element);

            } catch (Exception ex) {
                Log.e(NAME, "Error while parsing json");
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processRetainsOrders(JsonElement eRetain) {
        if (null != eRetain) {
            Log.e(NAME, "Processing Insertion....");

            ContentResolver resolver = getContentResolver();

            JsonArray retain = eRetain.getAsJsonArray();
            for (int i = 0; retain.size() > i; ++i) {
                JsonObject dItem = retain.get(i).getAsJsonObject();
                if (null != dItem) {
                    String type = dItem.get("type").getAsString();
                    String code = dItem.get("code").getAsString();
                    String tech = null;

                    JsonArray attrs = dItem.get("attributes").getAsJsonArray();

                    ContentValues[] attributes = new ContentValues[attrs.size()];

                    for (int j = 0; attrs.size() > j; ++j) {
                        attributes[j] = new ContentValues();

                        JsonObject kvp = attrs.get(j).getAsJsonObject();

                        String name = kvp.get("name").getAsString();
                        String value = kvp.get("value").getAsString();

                        attributes[j].put( DispatchItemsContract.KEY, name);
                        attributes[j].put( DispatchItemsContract.VALUE, value);
                        attributes[j].put( DispatchItemsContract.CODE, code);

                        if ("tecnologia".equals(name.toLowerCase()))
                            tech = value;
                    }
                    resolver.bulkInsert(DispatchItemsContract.CONTENT_URI, attributes);

                    ContentValues order = new ContentValues();
                    order.put(DIListContract.CODE, code);
                    order.put(DIListContract.TECHNOLOGY, (null != tech) ? tech : "UNKNOWN");
                    order.put(DIListContract.COMMITMENT_DATE, new Date().toString());
                    order.put(DIListContract.STATUS, 1);
                    order.put(DIListContract.TYPE, type);

                    try {
                        resolver.insert(DIListContract.CONTENT_URI, order);
                    }catch( Exception ex ){
                       Log.e(NAME, "Error trying to insert ID with  code " + code);
                    }
                }
            }
        } else {
            Log.e(NAME, "Look as empty retain orders");
        }
    }


    private void processReleaseOrders(JsonElement element) {
        if (null != element) {
            Log.e(NAME, "Processing release....");

            JsonElement pairsElement = element.getAsJsonObject().get( "pairs" );

            if( null == pairsElement )
                return;

            Log.e(NAME, "Processing release on " + pairsElement);

            JsonArray pairs = pairsElement.getAsJsonArray();

            ContentResolver resolver = getContentResolver();
            StringBuilder builder = new StringBuilder();

            SharedPreferences pref = getSharedPreferences(Constants.STATUS_FLAG_PREFERENCES,MODE_PRIVATE);

            for (int i = 0; pairs.size() > i; ++i) {
                JsonObject pair = pairs.get( i ).getAsJsonObject();

                builder.append(DIListContract.CODE)
                        .append(" LIKE '" + pair.get("code").getAsString() + "' AND ")
                        .append(DIListContract.TYPE)
                        .append(" LIKE '" + pair.get("type").getAsString() + "' ");

                resolver.delete(DIListContract.CONTENT_URI, builder.toString(), null);
                builder.setLength(0);

                try {
                    pref.edit().remove(pair.get("code").getAsString()).commit();
                    Log.e( NAME ,"Deleting preferemces.code="+pair.get( "code" ).getAsString() );
                }catch ( Exception ex ){
                    // Ignored
                }
            }
        }
    }


    private void ping(Message msg) {
        Log.e(NAME, "Trying to perform ping request");
        if (null != msg.replyTo) {
            Message reply = Message.obtain(null, ACTION_BIND_NOTIFICATION_ISSUED, loadingData);
            try{
                msg.replyTo.send(reply);
            }catch ( RemoteException ex ){
                ex.printStackTrace();
            }
        }
    }

    /**
     * Para esta accion se requiere que en arg1 se envie un ID unico del subcriber.
     */
    private void subscribe(Message msg) {
        Log.e(NAME, "Trying to perform subscribe request");
        if (null != msg.replyTo) {
            listeners.remove(msg.arg1);
            listeners.put(msg.arg1, msg.replyTo);
        }
    }

    private void publish(boolean status) {
        if (null == listeners || 0 == listeners.size() )
            return;

        for( int i=0;listeners.size()>i; ++i ){
            int key = listeners.keyAt(i);
            Messenger messenger = listeners.get(key);
            try{
                Message msg = Message.obtain(null, ACTION_BIND_NOTIFICATION_ISSUED, status);
                messenger.send(msg);
            }catch ( RemoteException ex ){
                Log.e(NAME, "Looks like a dead");
                listeners.delete(key);
            }
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(NAME, "Handling message");
            switch (msg.what) {
                case ACTION_BIND_PING:
                    ping(msg);
                    break;
                case ACTION_BIND_SUBSCRIBE:
                    subscribe(msg);
                    break;
                case ACTION_BIND_PING_AND_SUBSCRIBE: {
                    Log.e(NAME, "Trying to perform ping and subscribe request");
                    ping(msg);
                    subscribe(msg);
                    break;
                }
                default:break;
            }
        }
    };





}

