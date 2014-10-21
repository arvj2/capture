package com.claro.cfcmobile.activities.fragments.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.widget.TextView;

/**
 * Created by Christopher Herrera on 3/13/14.
 */
public class FacilitiesInputDialogFragment extends DialogFragment {

    public static final String TAG ="FacilitiesInputDialogFragment";

    public static final String CURRENT_TEXT = "currentText";
    public static final String CALLING_FACILITY = "callingFacility";
    public static final String DI_ORDERNUMBER = "DIORDERNUMBER";


    public static FacilitiesInputDialogFragment newInstance(String currentText, String callingFacility,String orderNumber){
        FacilitiesInputDialogFragment f = new FacilitiesInputDialogFragment();
        Bundle args = new Bundle();
        Log.d(TAG, "Calling Facility= " + callingFacility + "  current text= " + currentText);
        args.putString(CURRENT_TEXT, currentText);
        args.putString(CALLING_FACILITY,callingFacility);
        args.putString(DI_ORDERNUMBER,orderNumber);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String currentText = getArguments().getString(CURRENT_TEXT);
        final String callingFacility = getArguments().getString(CALLING_FACILITY);
        final String orderNumber = getArguments().getString(DI_ORDERNUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_action_edit);
        builder.setTitle(callingFacility);


        final EditText input = new EditText(getActivity());
        TextView text = new TextView(getActivity(),null);
        text.setText("999:999:999");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(currentText);
        builder.setView(text);
        builder.setView(input);


        builder.setPositiveButton("Insertar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                     if(!input.getText().toString().equalsIgnoreCase("")){
                         Log.d("Input", input.getText().toString());
                       //  ((DispatchItemDetailsActivity)getActivity()).insertFacilities(callingFacility, input.getText().toString(),orderNumber);
//                         ((DispatchItemDetailsActivity)getActivity()).updateFacilitiesFragmentUpdate();
                         dialog.dismiss();
                     }
            }
        });

        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        return builder.create();
    }



    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_DarkActionBar);
    }*/


}
