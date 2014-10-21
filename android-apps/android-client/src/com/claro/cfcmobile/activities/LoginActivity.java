package com.claro.cfcmobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.prefs.UserKnowledge;
import com.claro.cfcmobile.robospice.request.LoginRequest;
import com.claro.cfcmobile.robospice.response.OKResponse;
import com.claro.cfcmobile.robospice.services.CfcService;
import com.claro.cfcmobile.services.DIDownloaderService;
import com.claro.cfcmobile.utils.Utils;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by Christopher Herrera on 2/20/14.
 * <p/>
 * Refactored by Jansel R. Abreu (Vanwolf)
 */
public class LoginActivity extends Activity {

    private static String TAG = "SplashScreen";

    protected static final int FLIP_LOADING = 0;
    protected static final int FLIP_ERROR = 1;

    private static final int BUTTON_FLIP_CHILD = 0;
    private static final int FEEDBACK_FLIP_CHILD = 1;


    private ViewFlipper flipper;
    private ViewFlipper flipper2;
    private TextView imei;
    private EditText tarjetaEdit;
    private SpiceManager spiceManager = new SpiceManager(CfcService.class);
    private String credencialTarjeta;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imei = (TextView) findViewById(R.id.imei_edit);
        tarjetaEdit = (EditText) findViewById(R.id.tarjeta_edit);
        tarjetaEdit.setRawInputType(Configuration.KEYBOARD_12KEY);
        imei.setText(Utils.getDeviceImei(this));
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


    private void tryLogUser(String card, String deviceId) {
        TextView loadingStatus = (TextView) findViewById(R.id.feedback_loading_text);
        loadingStatus.setText(getResources().getText(R.string.login_feedback));

        enabledWidgets(false);
        fadeSelfFlipView(R.id.feedback_flipper2, FEEDBACK_FLIP_CHILD);
        fadeFlipView(R.id.feedback_flipper, FLIP_LOADING);
        performLoginRequest(card,deviceId);
    }


    private void persistCard() {
        UserKnowledge.setUserCard(this, tarjetaEdit.getText().toString());
        startDIDonwloaderService();
    }

    private void startDIDonwloaderService() {
        Intent service = new Intent(this, DIDownloaderService.class);
        startService(service);
    }


    private void performLoginRequest(final String card, final String imei) {
        TextView loadingStatus = (TextView) findViewById(R.id.feedback_loading_text);
        loadingStatus.setText(getResources().getText(R.string.login_feedback));

        enabledWidgets(false);
        fadeFlipView(R.id.feedback_flipper, FLIP_LOADING);


        Log.d(TAG, "Started performLoginRequest");
        LoginRequest lr = new LoginRequest(card, imei);
        String cacheKey = lr.createCacheKey();

        spiceManager.execute(lr, cacheKey, DurationInMillis.ALWAYS_EXPIRED, new LoginRequestListener());
    }


    public class LoginRequestListener implements RequestListener<OKResponse> {

        @Override
        public void onRequestFailure(SpiceException e) {
            loginFaseUnPassed();
        }

        @Override
        public void onRequestSuccess(OKResponse loginResponse) {
            if (null != loginResponse && loginResponse.isOk()) {
                enabledWidgets(true);
                persistCard();
                passActivity();
            } else {
                loginFaseUnPassed();
            }

        }
    }


    public void onLogin(View view) {
        credencialTarjeta = tarjetaEdit.getText().toString();

        if (null != credencialTarjeta && credencialTarjeta.length() > 0) {
            tryLogUser(credencialTarjeta, Utils.getDeviceImei(this));
        } else {
            Toast.makeText(this, "Favor intente de nuevo", Toast.LENGTH_SHORT).show();
        }
    }


    public void onRetry(View view) {
        onLogin(view);
    }


    private void enabledWidgets(boolean enabled) {
        Button sigin = (Button) findViewById(R.id.button1);
        sigin.setEnabled(enabled);
    }


    private void passActivity() {
        UserKnowledge.setUserLogged(this, true);
        Class<?> activity = null;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void loginFaseUnPassed() {
        TextView error = (TextView) findViewById(R.id.feedback_error_text);
        error.setText(getResources().getString(R.string.unable_authentication));

        fadeFlipView(R.id.feedback_flipper, FLIP_ERROR);
    }

    protected void fadeSelfFlipView(int flipperId, int child) {
        if (flipper2 == null) {
            flipper2 = (ViewFlipper) findViewById(flipperId);
            flipper2.setInAnimation(this, android.R.anim.fade_in);
            flipper2.setOutAnimation(this, android.R.anim.fade_out);
        }
        flipper2.setDisplayedChild(child);
    }

    protected void fadeFlipView(int flipperId, int child) {
        if (flipper == null) {
            flipper = (ViewFlipper) findViewById(flipperId);
            flipper.setInAnimation(this, android.R.anim.fade_in);
            flipper.setOutAnimation(this, android.R.anim.fade_out);
        }
        flipper.setDisplayedChild(child);
    }

}