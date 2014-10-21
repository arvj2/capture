package com.claro.cfcmobile.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.robospice.request.VersionRequest;
import com.claro.cfcmobile.robospice.response.VersionResponse;
import com.claro.cfcmobile.robospice.services.CfcService;
import com.claro.cfcmobile.utils.Utils;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/23/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public class BaseActvity extends ActionBarActivity {

    private VersionResponse version;

    private SpiceManager spiceManager = new SpiceManager(CfcService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkRemoteVersion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }


    protected void checkRemoteVersion() {

        Log.e("BaseActivity", "checkRemoteVersion");

        if (!Utils.updaterInstalled(this)) {
            Log.e("BaseActivity:checkRemoteVersion", "Updater not installed, bypass");
            return;
        }

        VersionRequest request = new VersionRequest();
        String cacheKey = request.createCacheKey();

        spiceManager.execute(request, cacheKey, DurationInMillis.ALWAYS_EXPIRED, new VersionRequestListener());
    }


    public class VersionRequestListener implements RequestListener<VersionResponse> {

        @Override
        public void onRequestFailure(SpiceException e) {
        }

        @Override
        public void onRequestSuccess(VersionResponse version) {
            if (null == version) {
                Log.e("BaseActivity:VersionRequestListener", "Got null version");
                return;
            }

            int remoteVersion = 0;
             try{
                 remoteVersion = Double.valueOf( version.getVersion() ).intValue();
             }catch ( Exception ex) {
                 //Ignored
             }

            Log.e( "BaseActivity","remoteVersion = "+remoteVersion+", localVersion= "+Utils.getVersionCode(BaseActvity.this)  );

            if ( remoteVersion == Utils.getVersionCode( BaseActvity.this )) {
                Log.e("BaseActivity:VersionRequestListener", "Server reported same version");
                return;
            }

            Toast.makeText(BaseActvity.this, R.string.new_app_version, Toast.LENGTH_LONG).show();

            BaseActvity.this.version = version;
            Intent updater = new Intent("android.intent.action.VIEW");
            updater.addCategory("android.intent.category.BROWSABLE");
            updater.setData(Uri.parse(version.getUpdateLink()));

            Log.e("BaseActivity", "Got version: " + version);
            startActivity(updater);
//            startActivityForResult(updater, UPDATER_REQUEST_CODE);
        }

    }
}