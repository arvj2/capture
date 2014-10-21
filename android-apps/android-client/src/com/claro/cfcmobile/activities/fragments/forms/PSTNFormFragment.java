package com.claro.cfcmobile.activities.fragments.forms;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.fragments.forms.prefs.Memento;
import com.claro.cfcmobile.dto.Attribute;
import com.claro.cfcmobile.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */

/**
 * @TODO terminal formato [seccion#1]-[seccion#2] [if cuenta_fija ] [seccion#1] = [primera seccion del par feeder] en caso contrario [session#1] es la cabina
 */
public class PSTNFormFragment extends BaseFormFragment {

    private static final String FORM_TYPE = "PSTN";

    private TextView cabinaLabel;
    private TextView parLocalLabel;

    private EditText phone;
    private EditText cabina;
    private CheckBox cuentaFija;
    private EditText parFeeder;
    private EditText terminal;
    private EditText parLocal;
    private Spinner localidad;
    //    private EditText direccion;
    private short addressState = -1;
    private boolean sameAddress;

    protected Memento memento;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memento = new Memento(this.getClass().getSimpleName() + ":" + orderNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pstn_form, container, false);
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


        attributes.add(Attribute.create(Constants.PHONE, phone.getText().toString()));

        if (!cuentaFija.isChecked()) {
            attributes.add(Attribute.create(Constants.CABINA, cabina.getText().toString().toUpperCase()));
            attributes.add(Attribute.create(Constants.PAR_LOCAL, parLocal.getText().toString().toUpperCase()));
        }

        attributes.add(Attribute.create(Constants.LOCALIDAD, localidad.getSelectedItem().toString()));
        attributes.add(Attribute.create(Constants.CUENTA_FIJA, cuentaFija.isChecked() ? "SI" : "NO"));
        attributes.add(Attribute.create(Constants.PAR_FEEDER, parFeeder.getText().toString().toUpperCase()));
        attributes.add(Attribute.create(Constants.TERMINAL, terminal.getText().toString().toUpperCase()));
        attributes.add(Attribute.create(Constants.DIRECCION_CORRECTA, "" + sameAddress));

        return attributes;
    }

    @Override
    protected void enabledComponents(boolean enabled) {

    }

    protected void init() {
        initComponents();
    }


    private void initComponents() {

        cabinaLabel = (TextView) getActivity().findViewById(R.id.label_cabina);
        parLocalLabel = (TextView) getActivity().findViewById(R.id.label_par_local);

        phone = (EditText) getActivity().findViewById(R.id.edit_phone);
        cabina = (EditText) getActivity().findViewById(R.id.edit_cabina);
        cuentaFija = (CheckBox) getActivity().findViewById(R.id.edit_cuenta_fija);
        parFeeder = (EditText) getActivity().findViewById(R.id.edit_par_feeder);
        terminal = (EditText) getActivity().findViewById(R.id.edit_terminal);
        parLocal = (EditText) getActivity().findViewById(R.id.edit_par_local);
        localidad = (Spinner) getActivity().findViewById(R.id.edit_localidad);


        cabina.addTextChangedListener(terminalReporterWatcher);
        parFeeder.addTextChangedListener(terminalReporterWatcher);


        cuentaFija.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                int visibility;
                if (checked) {
                    visibility = View.GONE;
                } else {
                    visibility = View.VISIBLE;
                }
                cabina.setVisibility(visibility);
                cabinaLabel.setVisibility(visibility);
                parLocal.setVisibility(visibility);
                parLocalLabel.setVisibility(visibility);

                terminal.setText(getTerminalFistSession() + "-");
            }
        });

        RadioButton radio = (RadioButton) getActivity().findViewById(R.id.radio_direccion_yes);
        radio.setOnClickListener(ADDRESS_SELECTION_LISTENER);

        radio = (RadioButton) getActivity().findViewById(R.id.radio_direccion_no);
        radio.setOnClickListener(ADDRESS_SELECTION_LISTENER);
    }

    @Override
    protected boolean validateInputs() {
        boolean validated = true;

        validated &= Pattern.compile(Constants.REGEX_PHONE).matcher(phone.getText().toString()).find();

        if (!validated) {
            toast(R.string.validation_phone);
            return validated;
        }

        if( !cuentaFija.isChecked() ) {
            validated &= Pattern.compile("^[a-zA-Z0-9-]+$").matcher(cabina.getText().toString()).find();
            if (!validated) {
                toast(R.string.validation_cabina);
                return validated;
            }
        }

        validated &= Pattern.compile(Constants.REGEX_FEEDER).matcher(parFeeder.getText().toString()).find();
        if (!validated) {
            toast(R.string.validation_par_feeder);
            return validated;
        }

        validated &= Pattern.compile(Constants.REGEX_TERMINAL).matcher(terminal.getText().toString()).find();
        if (!validated) {
            toast(R.string.validation_terminal);
            return validated;
        }

        if (!cuentaFija.isChecked()) {
            validated &= Pattern.compile(Constants.REGEX_PAR_LOCAL).matcher(parLocal.getText().toString()).find();
            if (!validated) {
                toast(R.string.validation_par_local);
                return validated;
            }
        }



        if (-1 == addressState) {
            toast(R.string.validation_address);
            return false;
        }

        return validated;
    }


    private String getTerminalFistSession() {
        String session = "";
        if (cuentaFija.isChecked()) {
            session = parFeeder.getText().toString().replaceAll("(?<=\\w)-\\w+", "");
        } else
            session = cabina.getText().toString();
        return session;
    }


    private TextWatcher terminalReporterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence sequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence sequence, int i, int i2, int i3) {
            parLocal.setText(getTerminalFistSession() + "-");
            terminal.setText(getTerminalFistSession() + "-");
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        memento.addToHistory("phone", phone.getText().toString());
        memento.addToHistory("cabina", cabina.getText().toString());
        memento.addToHistory("cuentaFija", "" + (cuentaFija.isChecked() ? 1 : 0));
        memento.addToHistory("parFeeder", parFeeder.getText().toString());
        memento.addToHistory("terminal", terminal.getText().toString());
        memento.addToHistory("parLocal", parLocal.getText().toString());
        memento.addToHistory("localidad", localidad.getSelectedItemPosition() + "");

        memento.saveMemento(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("***********", "PSTNFormFragment.onResume");
        memento.loadMemento(getActivity());
        phone.setText(memento.getFromHistory("phone") + "");
        cabina.setText(memento.getFromHistory("cabina") + "");
        cuentaFija.setChecked("1".equals(memento.getFromHistory("cuentaFija")));
        parFeeder.setText(memento.getFromHistory("parFeeder") + "");
        terminal.setText(memento.getFromHistory("terminal") + "");
        parLocal.setText(memento.getFromHistory("parLocal") + "");

        int selection = 0;
        try {
            selection = Integer.valueOf(memento.getFromHistory("localidad") + "");
        } catch (Exception e) {
            selection = 0;
        }
        localidad.setSelection(selection);
    }


    @Override
    protected void onPostSent() {
        memento.removeMemento(getActivity());
    }


    protected final View.OnClickListener ADDRESS_SELECTION_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.radio_direccion_yes) {
                addressState = 1;
                sameAddress = true;
            } else {
                addressState = 2;
                sameAddress = false;
            }
        }
    };
}


