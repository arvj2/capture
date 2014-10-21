package com.claro.cfcmobile.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.DispatchItemDetailsActivity;
import com.claro.cfcmobile.activities.fragments.dialogfragment.FacilitiesInputDialogFragment;
import com.claro.cfcmobile.db.contracts.DispatchItemsContract;

/**
 * Created by Christopher Herrera on 3/13/14.
 */
public class DispatchDetailsEditCursorAdapter extends CursorAdapter{

    LayoutInflater layoutInflater;
    Context context;

    public DispatchDetailsEditCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.dispatch_details_fragment_item_edit, null);
        ViewHolder holder = new ViewHolder();

        holder.keyText = (TextView) view.findViewById(R.id.di_detail_key);
        holder.valueText = (TextView) view.findViewById(R.id.di_detail_value);
        holder.editButton = (ImageButton) view.findViewById(R.id.di_detail_edit_btn);

        holder.keyIndex = cursor.getColumnIndexOrThrow(DispatchItemsContract.KEY);
        holder.valueIndex = cursor.getColumnIndexOrThrow(DispatchItemsContract.VALUE);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();


        holder.keyText.setText(cursor.getString(holder.keyIndex));
        holder.valueText.setText(cursor.getString(holder.valueIndex));
        holder.editButton.setImageResource(R.drawable.ic_action_edit);
//        holder.editButton.setTag(1,cursor.getString(holder.keyIndex));
//      holder.editButton.setTag(2,cursor.getString(holder.valueIndex));
        final String orderNumber = cursor.getString(cursor.getColumnIndexOrThrow(DispatchItemsContract.CODE));

        final String currentText = cursor.getString(holder.valueIndex);
        final String callingFacility = cursor.getString(holder.keyIndex);

        final FragmentManager fm = ((DispatchItemDetailsActivity)this.context).getSupportFragmentManager();
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Current Text= " + currentText, "Calling facility=" + callingFacility);
                FacilitiesInputDialogFragment f = FacilitiesInputDialogFragment.newInstance(currentText, callingFacility, orderNumber);
                f.show(fm,"DialogFragment");
            }
        });

    }

    private class ViewHolder{
        TextView keyText;
        TextView valueText;
        ImageButton editButton;
        int keyIndex;
        int valueIndex;
    }
}
