package com.claro.cfcmobile.activities.fragments.forms;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.fragments.forms.prefs.Memento;
import com.claro.cfcmobile.dto.Attribute;
import com.claro.cfcmobile.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
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


public class FibraFormFragment extends BaseFormFragment {

    private static final String FORM_TYPE = "FIBRA";

    private EditText phone;
    private Spinner localidad;
    private EditText terminalFo;
//    private EditText direccion;
    private boolean sameAddress;
    private short addressState = -1;
    private EditText splitterPort;

    private Memento memento;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memento = new Memento(this.getClass().getSimpleName()+":"+orderNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fibra_form,container,false);
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

        attributes.add(Attribute.create(Constants.PHONE, phone.getText().toString()) );
        attributes.add(Attribute.create(Constants.LOCALIDAD, localidad.getSelectedItem().toString()) );
        attributes.add(Attribute.create(Constants.TERMINAL_FO, terminalFo.getText().toString().toUpperCase()) );
        attributes.add(Attribute.create(Constants.CABINA, getCabina(terminalFo.getText().toString().toUpperCase())));
        attributes.add(Attribute.create(Constants.DIRECCION_CORRECTA, "" + sameAddress));
        attributes.add(Attribute.create(Constants.SPLITTER_PORT, splitterPort.getText().toString().toUpperCase()) );
        attributes.add(Attribute.create(Constants.SPLITTER, "SPT-"+terminalFo.getText().toString().toUpperCase() ));

        return attributes;
    }

    @Override
    protected void enabledComponents(boolean enabled) {
    }

    private void init() {
        initComponents();
    }


    private void initComponents() {
        phone        = (EditText) getActivity().findViewById(R.id.edit_phone);
        localidad    = (Spinner) getActivity().findViewById(R.id.edit_localidad);
        terminalFo   = (EditText) getActivity().findViewById(R.id.edit_terminal_fo);

        splitterPort = (EditText) getActivity().findViewById( R.id.edit_splitter_port );
        terminalFo.addTextChangedListener( new TextWatcher() {
//            CharSequence sequence = "";
//            boolean validating;

            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i2, int i3) {
//                this.sequence = sequence;
            }
            @Override
            public void onTextChanged(CharSequence sequence, int i, int i2, int i3) {
//                if( validating )
//                    return;
//
//                if( !validInput(sequence) ) {
//                    validating = true;
//                    terminalFo.setText( this.sequence );
//                    Log.e( "*********","replacing" );
//                    validating = false;
//                    return;
//                }

                if( null != terminalFo ){
                    splitterPort.setText( "SPT-"+terminalFo.getText()+"-SP" );
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        terminalFo.setText( "FC" );

        RadioButton radio = ( RadioButton ) getActivity().findViewById( R.id.radio_direccion_yes );
        radio.setOnClickListener( ADDRESS_SELECTION_LISTENER );

        radio = ( RadioButton ) getActivity().findViewById( R.id.radio_direccion_no );
        radio.setOnClickListener( ADDRESS_SELECTION_LISTENER );
    }


    private String getCabina( String terminal ){
        if( null == terminal )
            return  "";
        String cabina = terminal.substring( 0,terminal.indexOf('-'));
        return cabina;
    }


    @Override
    protected boolean validateInputs() {
        boolean validated = true;

        validated &= Pattern.compile(Constants.REGEX_PHONE).matcher( phone.getText().toString() ).find();
        if(!validated){
            toast(R.string.validation_phone);
            return validated;
        }

        validated &= Pattern.compile(Constants.REGEX_TERMINAL_FO).matcher( terminalFo.getText().toString() ).find();
        if( !validated ){
            toast( R.string.validation_terminal_fo );
            return validated;
        }

        validated &= Pattern.compile("SPT-" + terminalFo.getText().toString() + "-SP\\d{2}").matcher( splitterPort.getText().toString() ).find();
        if( !validated ){
            toast( R.string.validation_splitter_port );
            return validated;
        }

        validated &= !TextUtils.isEmpty(localidad.getSelectedItem().toString());
        if( !validated ){
            toast( R.string.validation_localidad );
            return validated;
        }


        if (-1 == addressState) {
            toast(R.string.validation_address);
            return false;
        }

        return validated;
    }

    @Override
    public void onStop() {
        super.onStop();

        memento.addToHistory( "phone",phone.getText().toString() );
        memento.addToHistory( "localidad",""+localidad.getSelectedItemPosition() );
        memento.addToHistory( "terminalFo",terminalFo.getText().toString() );
        memento.addToHistory( "splitterPort",splitterPort.getText().toString() );
        memento.saveMemento(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        memento.loadMemento( getActivity() );

        phone.setText( memento.getFromHistory( "phone" )+"");
        if( null != memento.getFromHistory("terminalFo") && !TextUtils.isEmpty(memento.getFromHistory("terminalFo")+"" ))
            terminalFo.setText( memento.getFromHistory( "terminalFo" )+"" );

        if( null != memento.getFromHistory("splitterPort") && !TextUtils.isEmpty(memento.getFromHistory("splitterPort")+"" ))
            splitterPort.setText( memento.getFromHistory( "splitterPort" )+"" );

        int selection = 0;
        try {
            selection = Integer.valueOf(memento.getFromHistory("localidad") + "");
        } catch (Exception e) {
            selection = 0;
        }
        localidad.setSelection( selection );

    }

    @Override
    protected void onPostSent() {
        memento.removeMemento(getActivity());
    }

    protected final View.OnClickListener ADDRESS_SELECTION_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if( view.getId() == R.id.radio_direccion_yes ){
                addressState = 1;
                sameAddress = true;
            }else{
                addressState = 2;
                sameAddress = false;
            }
        }
    };
}
