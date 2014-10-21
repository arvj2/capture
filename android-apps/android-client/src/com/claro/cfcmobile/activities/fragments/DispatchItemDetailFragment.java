package com.claro.cfcmobile.activities.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.claro.cfcmobile.adapter.DispatchItemDetailCursorAdapter;
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
public class DispatchItemDetailFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String orderNumber;
    private DispatchItemDetailCursorAdapter mAdapter;
    private int page;
    private String selection;
    private PageSelection pager;


    public DispatchItemDetailFragment(){
    }


    public DispatchItemDetailFragment(Context context, String itemCode, int page) {
        this.pager = new PageSelection(context);
        this.orderNumber = itemCode;
        this.page = page;
        selection = pager.getSelect(page, itemCode);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if( null != savedInstanceState ){
            selection   = savedInstanceState.getString( "select" );
            orderNumber = savedInstanceState.getString( "code" );
        }

        init();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DispatchItemsContract.CONTENT_URI, null, selection, null, "");
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }


    private void init() {
        mAdapter = new DispatchItemDetailCursorAdapter(getActivity(), null);

        setListAdapter(mAdapter);
        setListShown(false);
        getListView().setVerticalScrollBarEnabled(false);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString( "select",selection );
        outState.putString( "code",orderNumber );
    }
}
