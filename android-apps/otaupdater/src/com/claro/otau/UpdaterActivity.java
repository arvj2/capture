package com.claro.otau;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.claro.otau.commons.Commons;
import com.claro.otau.crypt.TargetCypher;

import java.util.ArrayList;
import java.util.List;

public class UpdaterActivity extends Activity {

    public static final String TAG = "OTAUpdater";

    /**
     * Called when the activity is first created.
     * <p/>
     * Receive as mas 160 characters from sms dispatcher that was previously 110 plain characters that generate 152 characters
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getIntent().getData()) {
            Uri uri = getIntent().getData();
            String target = extractTarget(uri);
            target = TargetCypher.decrypt(target);
            Log.e(TAG, target);
            dispatchDownload(target);
        } else
            finish();
    }

    private String extractTarget(Uri uri) {
        if (null == uri)
            return null;
        List<String> paths = new ArrayList<String>(uri.getPathSegments());
        paths.remove(0);
        String rawPaths = paths.toString();

        /**
         * Esta parte es necesaria debedido a que la url enscriptada puede tener [/] lo que hace que Uri interprete que es un
         * path mas de la url cuando no es asi. lo que se hace es extraer la 1ra parte del hash que es [update],
         * y luego de procede a unir todos los demas paths, que corresponden al hash de la uri completa
         *
         * Ej.
         * http://otau.claro.com:00/update/SlJRfUrIZIhK5UjAoCM4MXEZh5hx7TGJelzwhhmWUb7Ot7WcIvEHyAIOAOsw4d5l+Uxh72qTgAegloe9kHwXY9fLOsIBOF7Dk1fdo8/5Y8JaNNoWvWo1Yd1pCAMRsCMnyEcTh+oe7T+/+28ZMPl6dk5KBHfSRfyIK4chmAFzbVzReHalo0vucZEEnRfH4wfcDenveL6bA1rTgB1P3lnLWY7Ods/4iJsKNV4eRVtgMaUA9RwP+aBIZn2dV10AWuhUQVyRokJI0tsKLOkZ2BPV4ZCwpMTwkGkFjqChoP7dlJo7QaVduMxud579ESla04QjF7MwQez1VdUzFFMQLr4tLcbMk9UirXO2NkhlkKXzp0vvX1tMG+mFZ7BGIKIKPcP8
         *
         * Tiene slash, luego de el siguiente regez, se unen todas las partes y se desencripta.
         */
        rawPaths = rawPaths.replaceAll("\\[", "").replaceAll("\\s?\\,\\s+", "/").replaceAll("\\]", "");
        return rawPaths;
    }

    private void dispatchDownload(String target) {
        if (null == target) {
            Toast.makeText(this, "La actualizacion no fue posible por incopatibilidad", Toast.LENGTH_LONG).show();
            return;
        }
        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri targetUri = Uri.parse(target);
        DownloadManager.Request targetRequest = new DownloadManager.Request(targetUri);

        String rawString = "file://" + Commons.DOWNLOAD_EXTERNAL_URI + String.valueOf(target.hashCode());


        Uri destUri = Uri.parse(rawString);
        targetRequest.setDestinationUri(destUri);
        targetRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        targetRequest.setVisibleInDownloadsUi(true);
        targetRequest.setTitle("Descargando actualizacion");
        dm.enqueue(targetRequest);
        Toast.makeText(this, "La aplicacion fue encolada para actualizacion", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}
