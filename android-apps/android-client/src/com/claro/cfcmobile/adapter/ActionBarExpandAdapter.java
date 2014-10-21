package com.claro.cfcmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.dto.BudgetSpinnerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/25/14.
 */
public class ActionBarExpandAdapter extends BaseAdapter {

    private List<BudgetSpinnerItem> items;
    private int resTextId;
    private int resLayoutId;
    private LayoutInflater inflater;
    private Context context;


    public ActionBarExpandAdapter(Context context, int resTextId, int resLayoutId, List<BudgetSpinnerItem> items) {
        this.items = null == items ? new ArrayList<BudgetSpinnerItem>() : items;
        this.resTextId = resTextId;
        this.resLayoutId = resLayoutId;
        this.context = context;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            if (null == inflater)
                inflater = LayoutInflater.from(context);
            view = inflater.inflate(this.resTextId, viewGroup, false);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        if (null == holder) {
            holder = new ViewHolder();
            holder.textOne = (TextView) view.findViewById(R.id.cfc_title);
            holder.textTwo = (TextView) view.findViewById( R.id.item_type_up);
        }

        BudgetSpinnerItem item = items.get( i );
        holder.textOne.setText( R.string.app_name );
        holder.textTwo.setText( item.getDesc() );

        return view;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            if (null == inflater)
                inflater = LayoutInflater.from(context);
            convertView = inflater.inflate( resLayoutId,parent,false);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        if( null == holder ){
            holder = new ViewHolder();
            holder.textOne = (TextView) convertView.findViewById( R.id.item_type );
            holder.textTwo = (TextView) convertView.findViewById( R.id.budget_count );
        }


        BudgetSpinnerItem item = items.get( position );
        holder.textOne.setText( item.getDesc() );
        holder.textTwo.setText( item.getCount() == 0 ? "" : String.valueOf(item.getCount()) );

        return convertView;
    }


    private class ViewHolder {
        TextView textOne;
        TextView textTwo;
    }

}
