package com.claro.cfcmobile.activities.fragments;

import android.content.Context;
import android.util.SparseArray;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.db.contracts.DispatchItemsContract;

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
public final class PageSelection {
    private SparseArray<String> selects = new SparseArray<String>();
    private Context context;

    public PageSelection(Context context) {
        this.context = context;
    }

    public String getSelect(int index, String... args) {
        String select = selects.get( selects.keyAt(index) );
        if (null == select) {
            select = prepareSelect(index, args);
            selects.put( index,select );
        }
        return select;
    }

    private String prepareSelect(int index, String... args) {
        switch (index) {
            case 0:
                return getPageGeneral(args);
            case 1:
                return getPageOrderTicket(args);
            case 2:
                return getPageFacilities(args);
            case 3:
                return getPageMisc(args);
            default:
                return getPageGeneral(args);
        }
    }

    private String getPageGeneral(String... args) {
        String inClause = context.getString(R.string.select_general_in_clause);

        StringBuilder sb = new StringBuilder();
        sb.append("TRIM(" + DispatchItemsContract.VALUE + ") <> '' ");
        sb.append(" AND TRIM(" + DispatchItemsContract.VALUE + ") <> '-' ");
        sb.append(" AND " + DispatchItemsContract.VALUE + " IS NOT NULL ");
        sb.append(" AND " + DispatchItemsContract.CODE + " = " + args[0]);
        sb.append(" AND TRIM(" + DispatchItemsContract.KEY + ") IN ");
        sb.append(inClause);

        return sb.toString();
    }

    private String getPageOrderTicket(String... args) {
        String inClause = context.getString(R.string.select_order_ticket_in_clause);

        StringBuilder sb = new StringBuilder();
        sb.append("TRIM(" + DispatchItemsContract.VALUE + ") <> '' ");
        sb.append(" AND TRIM(" + DispatchItemsContract.VALUE + ") <> '-' ");
        sb.append(" AND " + DispatchItemsContract.VALUE + " IS NOT NULL ");
        sb.append(" AND " + DispatchItemsContract.CODE + " = " + args[0]);
        sb.append(" AND TRIM(" + DispatchItemsContract.KEY + ") IN ");
        sb.append(inClause);
        return sb.toString();
    }

    private String getPageFacilities(String... args) {
        String inClause = context.getString(R.string.select_facilities_in_clause);

        StringBuilder sb = new StringBuilder();

        sb.append(DispatchItemsContract.CODE + " = " + args[0]);
        sb.append(" AND TRIM(" + DispatchItemsContract.KEY + ") IN ");
        sb.append(inClause);
        return sb.toString();
    }

    private String getPageMisc(String... args) {
        String inClause = context.getString(R.string.select_misc_in_clause);

        StringBuilder sb = new StringBuilder();
        sb.append("TRIM(" + DispatchItemsContract.VALUE + ") <> '' ");
        sb.append(" AND TRIM(" + DispatchItemsContract.VALUE + ") <> '-' ");
        sb.append(" AND " + DispatchItemsContract.VALUE + " IS NOT NULL ");
        sb.append(" AND " + DispatchItemsContract.CODE + " = " + args[0]);
        sb.append(" AND TRIM(" + DispatchItemsContract.KEY + ") NOT IN ");
        sb.append(inClause);

        return sb.toString();
    }
}
