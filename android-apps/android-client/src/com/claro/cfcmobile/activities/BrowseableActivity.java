package com.claro.cfcmobile.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import com.claro.cfcmobile.R;

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

public class BrowseableActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browseable_layout);

        attendBrowseIntent();
    }


    private void attendBrowseIntent() {
        Uri data = getIntent().getData();
        String frName = null;

        if (data != null)
            frName = data.getLastPathSegment();

        if (frName != null) {
            Fragment fragment = getFragmentByClassName(frName);

            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

            //trans.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            trans.replace(R.id.content, fragment);
            trans.commit();
        }
    }


    private Fragment getFragmentByClassName(String fname) {
        Fragment fragment = null;
        if (fname != null) {
            try {
                fragment = Fragment.instantiate(this, fname);
            } catch (Exception ie) {
                ie.printStackTrace();
            }
        }
        return fragment;
    }

}
