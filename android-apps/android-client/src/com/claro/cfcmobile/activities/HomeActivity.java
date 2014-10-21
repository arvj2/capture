package com.claro.cfcmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.claro.cfcmobile.R;

/**
 * Created by Christopher Herrera on 2/20/14.
 *
 * @Refactored completely by Jansel R. Abreu (Vanwolf),
 */
public class HomeActivity extends BaseActvity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("********", "OnNewIntent");
    }
}