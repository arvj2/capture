package com.claro.cfcmobile.activities.fragments.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.dto.Attribute;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 6/16/2014.
 */
public class SelectForm extends BaseFormFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_form, container, false);
    }


    @Override
    protected String getFormType() {
        return "<select>";
    }

    @Override
    protected List<Attribute> getAttributes() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void enabledComponents(boolean enabled) {
    }

    @Override
    protected boolean validateInputs() {
        return false;
    }

    @Override
    protected void onPostSent() {
    }
}
