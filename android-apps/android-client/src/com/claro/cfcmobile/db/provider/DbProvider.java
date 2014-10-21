package com.claro.cfcmobile.db.provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.claro.cfcmobile.db.contracts.DIListContract;
import com.claro.cfcmobile.db.contracts.DispatchItemsContract;
import com.tjeannin.provigen.InvalidContractException;
import com.tjeannin.provigen.ProviGenProvider;

/**
 * Created by Christopher Herrera on 2/20/14.
 */
public class DbProvider extends ProviGenProvider {

    public static final String TAG = "LoginProvider";

    public DbProvider() throws InvalidContractException {
        super(new Class[] {DispatchItemsContract.class, DIListContract.class});
    }

    @Override
    public void onCreateDatabase(SQLiteDatabase database) {
        Log.d(TAG, "Database Created");
        super.onCreateDatabase(database);
    }
}
