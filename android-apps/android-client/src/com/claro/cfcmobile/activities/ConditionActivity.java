package com.claro.cfcmobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.prefs.UserKnowledge;
import com.claro.cfcmobile.utils.CommonUtils;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/11/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales. Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales. Toda actividad realizada en este sistema está siendo registrada y monitoreada.we
 */
public class ConditionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        init();
    }


    private void init() {

        TextView text1 = (TextView) findViewById(android.R.id.text1);
        TextView text2 = (TextView) findViewById(android.R.id.text2);

        setMovementLinkText(text1, text2);
        stripUnderlineText(text1, text2);
    }

    private void setMovementLinkText(TextView... texts) {
        if (texts == null)
            return;
        for (TextView text : texts)
            setLinkMovement(text);
    }

    private void stripUnderlineText(TextView... texts) {
        if (texts == null)
            return;
        for (TextView text : texts)
            CommonUtils.stripUnderline(text);
    }

    public void onAccept(View view) {
        UserKnowledge.setConditionUpTake(this, true);
        passActivity();
    }


    public void onClose(View view) {
        finish();
    }


    private void passActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void setLinkMovement(TextView text) {
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
