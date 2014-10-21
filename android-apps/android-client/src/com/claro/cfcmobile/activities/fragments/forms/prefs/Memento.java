package com.claro.cfcmobile.activities.fragments.forms.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 6/16/2014.
 */
public final class Memento {

    private static final String TAG = Memento.class.getCanonicalName();

    private Map<String, Object> values = new HashMap<String, Object>();
    private SharedPreferences prefs;
    private boolean isReady;
    private Context context;
    private String name;


    public Memento( String name) {
        this.name = name;
    }


    public void removeMemento( Context context ){
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
        isReady = false;
    }


    public void saveMemento(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue().toString());
        }
        editor.commit();
        isReady = false;
    }


    public void loadMemento(Context context) {
        isReady = true;
        this.context = context;

        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        values = (Map<String, Object>) prefs.getAll();
    }


    public void addToHistory(String key, Object value) {
        values.put(key, value);
    }

    public Object getFromHistory(String key) {
        if (!isReady) {
            Log.e(TAG, "Memento was not restored");
            return null;
        }
        return null == values.get(key) ? "" : values.get(key);
    }

}
