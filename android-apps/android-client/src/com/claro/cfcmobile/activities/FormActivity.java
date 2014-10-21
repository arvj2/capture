package com.claro.cfcmobile.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.fragments.forms.*;

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

/**
 * En el momento de procesar el formulario, llevarlo al home, limpiando el stack, pues el detalle se quedara activo si no se hace.
 */
public class FormActivity extends BaseActvity {
    public static final String EXTRAS_FORM_ORDER_NUMBER = "com.claro.cfcmobile.activities.FormActivity.EXTRAS_FORM_ORDER_NUMBER";
    public static final String EXTRAS_FORM_ORDER_TYPE   = "com.claro.cfcmobile.activities.FormActivity.EXTRAS_FORM_ORDER_TYPE";

    private String orderNumber;
    private String orderType;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        init();
    }


    private void init() {
        if (null != getIntent().getExtras()){
            orderNumber = getIntent().getExtras().getString(EXTRAS_FORM_ORDER_NUMBER);
            orderType = getIntent().getExtras().getString( EXTRAS_FORM_ORDER_TYPE );
        }

        initActionbar();
    }


    private void initActionbar() {
        ActionBar actionbar = getSupportActionBar();

        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);

        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.action_form_list, android.R.layout.simple_spinner_dropdown_item);
        actionbar.setListNavigationCallbacks(adapter, NAVIGATION_LISTENER);
    }


    private void attachFragment(Class<? extends Fragment> fragment) {
        Fragment fr = getSupportFragmentManager().findFragmentByTag(fragment.toString());

        if (null == fr) {
            Bundle extras = new Bundle();
            extras.putString(EXTRAS_FORM_ORDER_NUMBER, orderNumber);
            extras.putString(EXTRAS_FORM_ORDER_TYPE, orderType );

            fr = Fragment.instantiate(this, fragment.getName());
            fr.setArguments(extras);
        }

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.content, fr);
        trans.commit();
    }


    private final ActionBar.OnNavigationListener NAVIGATION_LISTENER = new ActionBar.OnNavigationListener() {
        @Override
        public boolean onNavigationItemSelected(int position, long id) {
            switch (position) {
                case 0:
                    attachFragment(SelectForm.class);
                    return true;
                case 1:
                    attachFragment(PSTNFormFragment.class);
                    return true;
                case 2:
                    attachFragment(FibraFormFragment.class);
                    return true;
                case 3:
                    attachFragment(PSTNFibraFormFragment.class);
                    return true;
                case 4:
                    attachFragment(PSTNDataFormFragment.class);
                    return true;
//                case 5:
//                    attachFragment(FrameFormFragment.class);
//                    return true;
                default:
                    return false;
            }
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            finish();
            return true;
        }
        return false;
    }
}
