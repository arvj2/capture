package com.claro.cfcmobile.activities.fragments.forms;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.fragments.forms.prefs.Memento;
import com.claro.cfcmobile.dto.Attribute;
import com.claro.cfcmobile.utils.Constants;
import com.claro.cfcmobile.widget.MaskedEditText;

import java.util.ArrayList;
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

public class FrameFormFragment extends BaseFormFragment {
    private static final String FORM_TYPE = "FRAME";

    private EditText phone;
    private EditText dslam;
    private Spinner localidad;
    private Spinner puerto;
    private EditText puertoMask;
    private String[] portRegex;
    private int port;


    private Memento memento;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memento = new Memento(this.getClass().getSimpleName()+":"+orderNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frame_form,container,false);
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
        List<Attribute> attributes = new ArrayList<Attribute>();

        String puertoStr = puerto.getSelectedItem().toString();
        String puertoMaskStr = puertoMask.getText().toString();

        attributes.add(Attribute.create(Constants.PHONE, phone.getText().toString() ) );
        attributes.add(Attribute.create(Constants.LOCALIDAD, localidad.getSelectedItem().toString()));
        attributes.add(Attribute.create(Constants.PUERTO, puertoMaskStr ) );
        attributes.add(Attribute.create(Constants.TIPO_PUERTO, puertoStr.toUpperCase()) );
        attributes.add(Attribute.create(Constants.DSLAM, dslam.getText().toString().toUpperCase()) );

        return attributes;
    }

    @Override
    protected void enabledComponents(boolean enabled) {

    }

    private void init() {
        initComponents();
    }


    private void initComponents() {

        portRegex  = getActivity().getResources().getStringArray( R.array.ports_regex );


        phone       = (EditText) getActivity().findViewById(R.id.edit_phone);
        dslam       = (EditText) getActivity().findViewById(R.id.edit_dslam);
        puerto      = (Spinner) getActivity().findViewById(R.id.edit_puerto);
        localidad = (Spinner) getActivity().findViewById(R.id.edit_localidad);
        puertoMask = (EditText) getActivity().findViewById(R.id.edit_puerto_specificacion);

        final String[] puertosMasks = getResources().getStringArray(R.array.fibra_tipo_puerto_masks);
        puerto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                puertoMask.setHint(puertosMasks[position]);
               // puertoMask.setMask(puertosMasks[position]);
                port = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected boolean validateInputs() {
        boolean validated = true;

        validated &= Pattern.compile(Constants.REGEX_PHONE).matcher(phone.getText().toString()).find();
        if (!validated) {

            toast(R.string.validation_phone);
            return validated;
        }


        validated &= Pattern.compile(portRegex[port]).matcher( puertoMask.getText().toString() ).find();
        if( !validated ){
            toast( R.string.validation_port );
            return validated;
        }

        validated &= Pattern.compile("^[a-zA-Z0-9-]+$").matcher( dslam.getText().toString() ).find();
        if( !validated ){
            toast(R.string.validation_dslam);
            return validated;
        }

        return validated;

    }

    @Override
    public void onStop() {
        super.onStop();

        memento.addToHistory( "phone",phone.getText().toString()+"" );
        memento.addToHistory( "dslam",dslam.getText().toString()+"" );
        memento.addToHistory( "localidad",localidad.getSelectedItemPosition()+"" );
        memento.addToHistory( "puerto",puerto.getSelectedItemPosition()+"" );

        memento.saveMemento(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        memento.loadMemento(getActivity());

        phone.setText( memento.getFromHistory( "phone" )+"" );
        dslam.setText( memento.getFromHistory( "dslam" )+"" );

        int selection = 0;
        try {
            selection = Integer.valueOf(memento.getFromHistory("localidad") + "");
        } catch (Exception e) {
            selection = 0;
        }
        localidad.setSelection( selection );

        try {
            selection = Integer.valueOf(memento.getFromHistory("puerto") + "");
        } catch (Exception e) {
            selection = 0;
        }
        puerto.setSelection( selection );
    }



    @Override
    protected void onPostSent() {
        memento.removeMemento(getActivity());
    }
}
