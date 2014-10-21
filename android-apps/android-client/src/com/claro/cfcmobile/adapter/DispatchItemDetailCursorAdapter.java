package com.claro.cfcmobile.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.db.contracts.DispatchItemsContract;

/**
 * Created by Christopher Herrera on 3/13/14.
 */
public class DispatchItemDetailCursorAdapter extends CursorAdapter {

    LayoutInflater layoutInflater;

    public DispatchItemDetailCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.entry_dispatch_item_detail, null);

        ViewHolder holder = new ViewHolder();
        holder.key = (TextView) view.findViewById(R.id.key);
        holder.value = (TextView) view.findViewById(R.id.value);

        view.setTag(holder);
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.key.setText(cursor.getString(cursor.getColumnIndexOrThrow(DispatchItemsContract.KEY)));
        holder.value.setText(cursor.getString(cursor.getColumnIndexOrThrow(DispatchItemsContract.VALUE)));
    }

    private class ViewHolder {
        TextView key;
        TextView value;
    }
}
