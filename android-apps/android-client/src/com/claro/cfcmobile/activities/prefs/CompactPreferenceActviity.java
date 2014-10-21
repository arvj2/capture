package com.claro.cfcmobile.activities.prefs;

import android.os.Bundle;
import com.claro.cfcmobile.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/25/14.
 */
public class CompactPreferenceActviity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.compact_main_prefs);
    }
}
