package com.claro.cfcmobile.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 5/14/14.
 */
public class DownloadBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals( intent.getAction() ) ){
            long id = intent.getExtras().getLong( DownloadManager.EXTRA_DOWNLOAD_ID );

            DownloadManager dm = (DownloadManager) context.getSystemService( Context.DOWNLOAD_SERVICE );
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById( id );

            Cursor cursor = dm.query( query );
            if( null != cursor ){
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt( cursor.getColumnIndexOrThrow( DownloadManager.COLUMN_STATUS ) );
                    if( DownloadManager.STATUS_SUCCESSFUL == status ){
                        String localUri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));

                        Uri uri = Uri.parse(localUri);

                        Intent installItent = new Intent( Intent.ACTION_VIEW );
                        installItent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        installItent.setDataAndType(uri, "application/vnd.android.package-archive");

                        context.startActivity( installItent );
                    }

                }
                cursor.close();
            }
        }
    }
}
