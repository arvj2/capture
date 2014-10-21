package com.claro.cfcmobile.activities.fragments.forms;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.dto.Attribute;
import com.claro.cfcmobile.utils.Constants;

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


public class PSTNFibraFormFragment extends PSTNFormFragment {
    private static final String FORM_TYPE = "PSTN+FIBRA";

    private EditText terminalFo;
    private EditText splitterPort;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pstn_fibra_form, container, false);
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

        attributes.add(Attribute.create(Constants.TERMINAL_FO, terminalFo.getText().toString().toUpperCase()));
        attributes.add(Attribute.create(Constants.CABINA, getCabina(terminalFo.getText().toString().toUpperCase())));
        attributes.add(Attribute.create(Constants.SPLITTER_PORT, splitterPort.getText().toString().toUpperCase() ) );
        attributes.add(Attribute.create(Constants.SPLITTER, "SPT-"+terminalFo.getText().toString().toUpperCase() ));

        return attributes;
    }

    @Override
    protected void enabledComponents(boolean enabled) {
    }

    protected void init() {
        super.init();
        initComponents();
    }

    private String getCabina( String terminal ){
        if( null == terminal )
            return  "";
        String cabina = terminal.substring( 0,terminal.indexOf('-'));
        return cabina;
    }


    private void initComponents() {
        terminalFo = (EditText) getActivity().findViewById(R.id.edit_terminal_fo);

        splitterPort = (EditText) getActivity().findViewById( R.id.edit_splitter_port );
        terminalFo.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence sequence, int i, int i2, int i3) {
                if( null != terminalFo ){
                    splitterPort.setText( "SPT-"+terminalFo.getText()+"-SP" );
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        terminalFo.setText( "FC" );
    }

    @Override
    protected boolean validateInputs() {
        boolean validated = super.validateInputs();


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
        return validated;
    }



    @Override
    public void onStop() {
        super.onStop();
        memento.addToHistory( "terminalFo",terminalFo.getText().toString() );
        memento.addToHistory( "splitterPort",splitterPort.getText().toString() );

    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * loaded by super
         */
//        memento.loadMemento( getActivity() );

        if( null != memento.getFromHistory("terminalFo") && !TextUtils.isEmpty(memento.getFromHistory("terminalFo") + ""))
            terminalFo.setText( memento.getFromHistory( "terminalFo" )+"" );

        if( null != memento.getFromHistory("splitterPort") && !TextUtils.isEmpty(memento.getFromHistory("splitterPort")+"" ))
            splitterPort.setText( memento.getFromHistory( "splitterPort" )+"" );
    }
}
