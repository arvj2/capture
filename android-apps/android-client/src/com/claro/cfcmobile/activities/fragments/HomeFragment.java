package com.claro.cfcmobile.activities.fragments;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.*;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.ViewFlipper;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.activities.DispatchItemDetailsActivity;
import com.claro.cfcmobile.activities.FormActivity;
import com.claro.cfcmobile.activities.prefs.CompactPreferenceActviity;
import com.claro.cfcmobile.activities.prefs.PreferenceActivity;
import com.claro.cfcmobile.adapter.ActionBarExpandAdapter;
import com.claro.cfcmobile.adapter.DispatchItemCursorAdapter;
import com.claro.cfcmobile.db.contracts.DIListContract;
import com.claro.cfcmobile.dto.BudgetSpinnerItem;
import com.claro.cfcmobile.services.DIDownloaderService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Christopher Herrera on 2/20/14.
 *
 * @Refactored completely by Jansel R. Abreu (Vanwolf),*
 */
public class HomeFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private static final String TAG = "HomeFragment";

    //56891334
    private static final int FLIPP_LOADING = 0;
    private static final int FLIPP_NO_CONTENT = 1;
    private static final int FLIPP_CONTENT = 2;

    private static final int SERVICE_SUBSCRIBER_ID = 1;

    private ViewFlipper flipper;
    private ListView listContent;
    private DispatchItemCursorAdapter mAdapter;
    private List<BudgetSpinnerItem> expandItems;

    private Intent service;
    private MenuItem refreshMenuItem;
    private Messenger portToService;
    private Messenger gatewayService;

    private ServiceConnection connection;

    private String dispatchFilter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        service = new Intent(getActivity(), DIDownloaderService.class);
        prepareServiceConnection();
        init();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        refreshMenuItem = menu.findItem(R.id.action_bar_progress);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_progress:
                refreshData();
                return true;
            case R.id.action_settings:
                onConfiguration();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(service, connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(connection);
    }

    private void init() {
        initDepends();
        initComponents();
        initActionbar();

        launchLoading();
    }


    private void prepareServiceConnection() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.e(TAG, "Connected to service");
                portToService = new Messenger(iBinder);
                issueServiceSubscription();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e(TAG, "Disconnected from the service");
                portToService = null;
                gatewayService = null;
            }
        };
    }


    public void onConfiguration() {
        if (Build.VERSION_CODES.HONEYCOMB > Build.VERSION.SDK_INT) {
            startActivity(new Intent(getActivity(), CompactPreferenceActviity.class));
        } else {
            startActivity(new Intent(getActivity(), PreferenceActivity.class));
        }
    }


    private void refreshData() {
        if (null != connection)
            getActivity().unbindService(connection);

        prepareServiceConnection();
        getActivity().stopService(service);
        getActivity().startService(service);
        getActivity().bindService(service, connection, Context.BIND_AUTO_CREATE);
    }


    private void flipLoading(boolean loading) {
        if (loading && null != refreshMenuItem.getActionView())
            return;
        if (loading) {
            if (0 == mAdapter.getCount())
                flipToChild(FLIPP_LOADING);

            refreshMenuItem.setActionView(R.layout.actionbar_progress_layout);
        } else {
            refreshMenuItem.setActionView(null);
            if (0 == mAdapter.getCount())
                flipToChild(FLIPP_NO_CONTENT);
        }
    }


    private void initComponents() {
        flipper = (ViewFlipper) getActivity().findViewById(R.id.feedback_flipper);
        listContent = (ListView) getActivity().findViewById(R.id.list_content);

        if (null != mAdapter) {
            listContent.setAdapter(mAdapter);
            registerForContextMenu(listContent);
            listContent.setOnItemClickListener(this);
        }

        flipToChild(FLIPP_LOADING);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != listContent)
            unregisterForContextMenu(listContent);
    }


    private void initDepends() {
        mAdapter = new DispatchItemCursorAdapter(getActivity(), null);
    }


    private void launchLoading() {
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }


    private void initActionbar() {
        expandItems = getExpandItems();

        ActionBar actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionbar.setDisplayShowTitleEnabled(false);

        SpinnerAdapter adapter = new ActionBarExpandAdapter(getActivity(), R.layout.simple_spinner_text_item, R.layout.simple_spinner_dropdown_item_1, expandItems);
        actionbar.setListNavigationCallbacks(adapter, NAVIGATION_LISTENER);
    }


    private List<BudgetSpinnerItem> getExpandItems() {
        return Arrays.asList(
                new BudgetSpinnerItem("Todos los items", 0),
                new BudgetSpinnerItem("Ordenes", 0),
                new BudgetSpinnerItem("Averias", 0)
        );
    }


    protected void flipToChild(int child) {
        if (flipper == null) {
            flipper.setInAnimation(getActivity(), android.R.anim.fade_in);
            flipper.setOutAnimation(getActivity(), android.R.anim.fade_out);
        }
        flipper.setDisplayedChild(child);
    }


    /**
     * From here need future reformat
     */

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        String select = null;
        if (null != dispatchFilter) {
            select = DIListContract.TYPE + " LIKE '%" + dispatchFilter + "%' ";
        }
        return new CursorLoader(getActivity(), DIListContract.CONTENT_URI, null, select, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        if (0 == cursor.getCount())
            flipToChild(FLIPP_NO_CONTENT);
        else
            flipToChild(FLIPP_CONTENT);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
    }

    private void onSeeItemDetail(int position) {
        Cursor cursor = (Cursor) mAdapter.getItem(position);
        String code = cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.CODE));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.TYPE));

        if (null == code || null == type )
            return;

        Intent detail = new Intent(getActivity(), DispatchItemDetailsActivity.class);
        detail.putExtra(DispatchItemDetailsActivity.EXTRAS_DISPATCH_ITEM_CODE, code);
        detail.putExtra(DispatchItemDetailsActivity.EXTRAS_DISPATCH_ITEM_TYPE, type);

        startActivity(detail);
    }


    private void onEditForm(int position) {
        Cursor cursor = (Cursor) mAdapter.getItem(position);
        String code = cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.CODE));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(DIListContract.TYPE));

        if (null == code || null == type )
            return;


        Intent form = new Intent( getActivity(), FormActivity.class );
        form.putExtra( FormActivity.EXTRAS_FORM_ORDER_NUMBER, code );
        form.putExtra( FormActivity.EXTRAS_FORM_ORDER_TYPE,type );

        startActivity(form);
    }



    private void issueServiceSubscription() {
        if (null == portToService)
            return;
        if (null == gatewayService)
            gatewayService = new Messenger(handler);

        Log.e(TAG, "Issued service Subscription");

        Message msg = handler.obtainMessage(DIDownloaderService.ACTION_BIND_PING_AND_SUBSCRIBE, SERVICE_SUBSCRIBER_ID);
        msg.replyTo = gatewayService;
        try {
            portToService.send(msg);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.equals(listContent)) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.popup_menu, menu);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.app_menu_di_detail) {
            onSeeItemDetail(info.position);
            return true;
        }
        if (item.getItemId() == R.id.app_menu_di_form) {
            onEditForm(info.position);
            return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        onSeeItemDetail(position);
    }


    private final ActionBar.OnNavigationListener NAVIGATION_LISTENER = new ActionBar.OnNavigationListener() {
        @Override
        public boolean onNavigationItemSelected(int position, long id) {
            boolean handled = false;
            switch (position) {
                case 0:
                    dispatchFilter = null;
                    handled = true;
                    break;
                case 1:
                    dispatchFilter = "ORDEN";
                    handled = true;
                    break;
                case 2:
                    dispatchFilter = "AVERIA";
                    handled = true;
                    break;
                default:
                    handled = false;
                    break;
            }
            if (handled)
                getActivity().getSupportLoaderManager().restartLoader(0, null, HomeFragment.this);
            return handled;
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DIDownloaderService.ACTION_BIND_NOTIFICATION_ISSUED:
                    Log.e(TAG, "Responding to notification issue: " + msg.obj);
                    flipLoading((Boolean) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
}
