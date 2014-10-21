package com.claro.cfcmobile.activities.prefs;

import android.os.Bundle;
import com.claro.cfcmobile.R;

import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/25/14.
 */
public class PreferenceActivity extends android.preference.PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle( "Configuracion" );
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.main_prefs,target);
    }
}
