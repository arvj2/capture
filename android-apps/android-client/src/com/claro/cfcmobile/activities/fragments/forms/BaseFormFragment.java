package com.claro.cfcmobile.activities.fragments.forms;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.FormActivity;
import com.claro.cfcmobile.activities.HomeActivity;
import com.claro.cfcmobile.activities.prefs.UserKnowledge;
import com.claro.cfcmobile.db.contracts.DIListContract;
import com.claro.cfcmobile.dto.Attribute;
import com.claro.cfcmobile.robospice.request.ActionSaveRequest;
import com.claro.cfcmobile.robospice.response.OKResponse;
import com.claro.cfcmobile.robospice.services.CfcService;
import com.claro.cfcmobile.utils.Constants;
import com.claro.cfcmobile.utils.Utils;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/15/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public abstract class BaseFormFragment extends Fragment {

    private static final String TAG = "BaseFormFragment";

    private MenuItem itemSend;

    protected String orderNumber;
    protected String orderType;

    private SpiceManager spiceManager = new SpiceManager(CfcService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (null != getArguments()) {
            orderNumber = getArguments().getString(FormActivity.EXTRAS_FORM_ORDER_NUMBER);
            orderType = getArguments().getString(FormActivity.EXTRAS_FORM_ORDER_TYPE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }


    @Override
    public void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    protected String getOrderNumber() {
        return orderNumber;
    }

    protected String getOrderType() {
        return orderType;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.form_menu, menu);
        itemSend = menu.findItem(R.id.action_form_send);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_form_send) {
            sendForm();
            return true;
        }
        return false;
    }


    protected void sendForm() {
        if (!validateInputs())
            return;

        List<Attribute> attributes = getAttributes();


        Attribute code = new Attribute(DIListContract.CODE, orderNumber);
        Attribute type = new Attribute(DIListContract.TYPE, orderType);
        Attribute imei = new Attribute("imei", Utils.getDeviceImei(getActivity()) );
        Attribute formType = new Attribute("FORM_TYPE", getFormType());

        attributes.add(code);
        attributes.add(type);
        attributes.add(formType);
        attributes.add(imei);

        Log.d(TAG, "Started performLoginRequest");
        ActionSaveRequest request = new ActionSaveRequest(UserKnowledge.getUserCard(getActivity()), attributes);

        String cacheKey = request.createCacheKey();

        flipLoading(true);
        enabledComponents(false);
        spiceManager.execute(request, cacheKey, DurationInMillis.ALWAYS_EXPIRED, new ActionSendResponseListener(orderNumber, orderType));
    }


    private void flipLoading(boolean loading) {
        if (loading && null != itemSend.getActionView())
            return;
        if (loading) {
            itemSend.setActionView(R.layout.actionbar_progress_layout);
        } else {
            itemSend.setActionView(null);
        }
    }


    private class ActionSendResponseListener implements RequestListener<OKResponse> {
        private String code;
        private String type;

        public ActionSendResponseListener(String code, String type) {
            this.code = code;
            this.type = type;
        }

        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(getActivity(), R.string.unable_to_send_form, Toast.LENGTH_LONG).show();
            flipLoading(false);
            enabledComponents(true);
        }

        @Override
        public void onRequestSuccess(OKResponse okResponse) {
            flipLoading(false);
            enabledComponents(true);

            if (null == okResponse || !okResponse.isOk()) {
                Toast.makeText(getActivity(), R.string.unable_to_send_form, Toast.LENGTH_LONG).show();
            } else {
                try {
                    onPostSent();
//                    removeSentOrder(code, type);
                    setOrderSentFlag( code,type );
                    Toast.makeText(getActivity(), R.string.form_sent, Toast.LENGTH_LONG).show();

                } catch (Exception ex) {
                    Log.e(TAG, "Exception while trying to remove order ");
                    ex.printStackTrace();
                }
                returnToHome();
            }
        }
    }

    private void returnToHome() {
        Intent home = new Intent(getActivity(), HomeActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(home);
        getActivity().finish();
    }


    @Deprecated
    /**
     *  Not needed anymore
     */
    private void removeSentOrder(String code, String type) {
        Log.e(TAG, "Removing order " + code + " of type " + type);
        ContentResolver resolver = getActivity().getContentResolver();
        StringBuilder builder = new StringBuilder();
        builder.append(DIListContract.CODE)
                .append(" LIKE '" + code + "' AND ")
                .append(DIListContract.TYPE)
                .append(" LIKE '" + type + "' ");

        Log.e(TAG, "Trying to remove with statements: " + builder.toString());

        resolver.delete(DIListContract.CONTENT_URI, builder.toString(), null);
        builder.setLength(0);
    }


   private void setOrderSentFlag( String code,String type ){
       SharedPreferences pref = getActivity().getSharedPreferences(Constants.STATUS_FLAG_PREFERENCES, Context.MODE_PRIVATE);
       pref.edit().putBoolean(code,true).commit();
   }

    protected boolean validInput( CharSequence sequence ){
        Pattern pattern = Pattern.compile( "^[a-zA-Z0-9-]+$" );
        Matcher matcher = pattern.matcher( sequence.toString() );
        return matcher.find();
    }

    protected void toast(int res) {
        Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
    }

    protected abstract String getFormType();

    protected abstract List<Attribute> getAttributes();

    protected abstract void enabledComponents(boolean enabled);

    protected abstract boolean validateInputs();

    protected abstract void onPostSent();
}
