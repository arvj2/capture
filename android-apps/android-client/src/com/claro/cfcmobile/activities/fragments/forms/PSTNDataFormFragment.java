package com.claro.cfcmobile.activities.fragments.forms;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.dto.Attribute;
import com.claro.cfcmobile.utils.Constants;
import com.claro.cfcmobile.widget.MaskedEditText;

import java.util.List;
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


public class PSTNDataFormFragment extends PSTNFormFragment {
    private static final String FORM_TYPE = "PSTN+DATA";

    private EditText dslam;
    private Spinner puerto;
    private EditText puertoMask;
    private String[] portRegex;
    private int port;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pstn_data_form,container,false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    protected String getFormType() {
        return FORM_TYPE;
    }

    @Override
    protected List<Attribute> getAttributes() {
        List<Attribute> attributes = super.getAttributes();


        String puertoStr = puerto.getSelectedItem().toString();
        String puertoMaskStr = puertoMask.getText().toString();


        attributes.add(Attribute.create(Constants.TIPO_PUERTO, puertoStr.toUpperCase()) );
        attributes.add(Attribute.create(Constants.PUERTO, puertoMaskStr) );
        attributes.add(Attribute.create(Constants.DSLAM, dslam.getText().toString().toUpperCase()) );

        return attributes;
    }

    @Override
    protected void enabledComponents(boolean enabled) {

    }

    protected void init() {
        super.init();
        initComponents();
    }


    private void initComponents() {

        portRegex = getActivity().getResources().getStringArray( R.array.ports_regex );

        puerto      = (Spinner) getActivity().findViewById(R.id.edit_puerto);
        dslam       = (EditText) getActivity().findViewById( R.id.edit_dslam);
        puertoMask = (EditText) getActivity().findViewById(R.id.edit_puerto_specificacion);

        final String[] puertosMasks = getResources().getStringArray(R.array.fibra_tipo_puerto_masks);
        puerto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                puertoMask.setHint(puertosMasks[position]);
//                puertoMask.setMask(puertosMasks[position]);
                port = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected boolean validateInputs() {
        boolean validated = super.validateInputs();

        validated &= Pattern.compile(portRegex[port]).matcher( puertoMask.getText().toString() ).find();
        if( !validated ){
            toast( R.string.validation_port );
            return validated;
        }

        validated &= !TextUtils.isEmpty(dslam.getText().toString());
        if( !validated ){
            toast(R.string.validation_dslam);
            return validated;
        }

        return validated;
    }

    @Override
    public void onStop() {
        super.onStop();
        memento.addToHistory( "dslam", dslam.getText().toString() );
        memento.addToHistory( "puerto",puerto.getSelectedItemPosition()+"" );
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * loaded by super
         */
//        memento.loadMemento( getActivity() );

        dslam.setText( memento.getFromHistory("dslam")+"");
        int selection = 0;
        try {
            selection = Integer.valueOf(memento.getFromHistory("puerto") + "");
        } catch (Exception e) {
            selection = 0;
        }
        puerto.setSelection( selection );
    }
}
