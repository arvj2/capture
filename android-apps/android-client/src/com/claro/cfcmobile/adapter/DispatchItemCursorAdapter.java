package com.claro.cfcmobile.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.db.contracts.DIListContract;
import com.claro.cfcmobile.dto.DispatchItemType;
import com.claro.cfcmobile.utils.Constants;
import com.claro.cfcmobile.widget.TextView;

/**
 * Created by Christopher Herrera on 2/20/14.
 *
 * @Refactored completely by Jansel R. Abreu (Vanwolf),
 */
public class DispatchItemCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;
    private SharedPreferences prefs;

    public DispatchItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        if (null == layoutInflater) {
            layoutInflater = LayoutInflater.from(context);
            prefs = context.getSharedPreferences(Constants.STATUS_FLAG_PREFERENCES,Context.MODE_PRIVATE);
        }

        View view = layoutInflater.inflate(R.layout.entry_dispatch_item, viewGroup, false);
        ViewHolder holder = new ViewHolder();

        holder.id = (TextView) view.findViewById(R.id.item_id);
        holder.itemType = (TextView) view.findViewById(R.id.item_di_type);
        holder.itemSent = (TextView) view.findViewById( R.id.item_sent);
        holder.itemTech = (TextView) view.findViewById(R.id.item_tech);
        holder.date = (TextView) view.findViewById(R.id.item_date);
        holder.expand = (ImageButton) view.findViewById(R.id.app_overflow_menu);

        view.setTag(holder);
        return view;
    }


    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String code = cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.CODE));
        holder.id.setText(code);
        holder.date.setText(cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.COMMITMENT_DATE)));
        holder.itemTech.setText(cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.TECHNOLOGY)));

        DispatchItemType type = DispatchItemType.get(cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.TYPE)));

        if (null == type)
            type = DispatchItemType.UNKNOWN;

        Drawable bgType = context.getResources().getDrawable(R.drawable.bg_item_type_ticket);
        if (DispatchItemType.ORDEN == type)
            bgType = context.getResources().getDrawable(R.drawable.bg_item_type_orden);

        holder.itemType.setText(type.toString());
        holder.itemType.setBackground(bgType);

        boolean sent = prefs.getBoolean(code,false);
        holder.itemSent.setVisibility( sent ? View.VISIBLE : View.INVISIBLE );

        holder.expand.setTag(cursor.getPosition());

    }


    private static class ViewHolder {
        TextView id;
        TextView itemType;
        TextView itemTech;
        TextView itemSent;
        TextView date;
        ImageButton expand;
    }
}
