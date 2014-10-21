package com.claro.otau.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 4/15/14.
 * <p/>
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad. Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 * Este sistema y sus recursos son propiedad de CLARO y es para el uso exclusivo del Personal
 * autorizado por esta entidad.Su uso indebido puede conllevar acciones disciplinarias y/o
 * legales.Toda actividad realizada en este sistema está siendo registrada y monitoreada.
 */
public class DownloadBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Come from", "not null");
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
