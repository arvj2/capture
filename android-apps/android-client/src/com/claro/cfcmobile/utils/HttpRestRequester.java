package com.claro.cfcmobile.utils;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * Created by Christopher Herrera on 2/19/14.
 *
 * * Modified by Jansel R. Abreu (Vanwolf)
 */
public class HttpRestRequester {
    private static final String TAG = "HttpRestRequester";
    public  static final int CONNECTION_TIMEOUT = 30000;
    public  static final int SOCKET_TIMEOUT = 50000;



    public static String makeHttpGetRequest(String url){
        Log.e(TAG, "Creating request for url: " + url);

        HttpGet get = new HttpGet(url);
        DefaultHttpClient client = new DefaultHttpClient();

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
        client.setParams(params);

        String responseString = null;
        try{
           HttpResponse response = client.execute(get);
            if( response.getStatusLine().getStatusCode() == 200 )
                responseString = EntityUtils.toString(response.getEntity());
            else{
                Log.e(TAG, "Error trying to retrieve response, received error code " + response.getStatusLine().getStatusCode());
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return responseString;
    }


    public static String makeHttpPostRequest( String url,String json){
        DefaultHttpClient client = new DefaultHttpClient();

        Log.e(TAG, "Creating request for url: " + url);
        String responseString = null;

        HttpPost post = new HttpPost(url);
        try{
            StringEntity se = new StringEntity(json);
            post.setEntity(se);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");

            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
            client.setParams(params);

            HttpResponse response = client.execute(post);
            if( response.getStatusLine().getStatusCode() == 200 )
                responseString = EntityUtils.toString(response.getEntity());
            else{
                Log.e(TAG, "Some Error trying to get Response, get error code " + response.getStatusLine().getStatusCode());
            }
        }catch ( Exception ex ){
            Log.e(TAG, "Error executing post request " + ex.getCause());
            ex.printStackTrace();
        }
        return responseString;
    }

}
