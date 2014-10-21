package com.claro.cfcmobile.activities.fragments.prefs;

import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/25/14.
 */
public class BasePreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener( this );
    }

    @Override
    public void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
     }
}
