package com.claro.cfcmobile.activities.fragments.prefs;

import android.os.Bundle;
import com.claro.cfcmobile.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/25/14.
 */
public class SyncFragment extends BasePreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sync_preference_header);
        getActivity().getActionBar().setTitle( "Sincronizacion" );
    }
}
