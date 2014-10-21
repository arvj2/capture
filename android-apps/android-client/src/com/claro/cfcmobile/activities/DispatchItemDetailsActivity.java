package com.claro.cfcmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.adapter.DetailsTabPagerAdapter;


/**
 * Created by Christopher Herrera on 3/3/14.
 *
 * @Refactored completely by Jansel R. Abreu (Vanwolf),
 */
public class DispatchItemDetailsActivity extends ActionBarActivity implements ActionBar.TabListener {

    public static final String EXTRAS_DISPATCH_ITEM_CODE = "com.claro.cfcmobile.activities.DispatchItemDetailsActivity.EXTRAS_DISPATCH_ITEM_CODE";
    public static final String EXTRAS_DISPATCH_ITEM_TYPE = "com.claro.cfcmobile.activities.DispatchItemDetailsActivity.EXTRAS_DISPATCH_ITEM_TYPE";

    private String itemCode;
    private String itemType;

    private ViewPager viewPager;
    private DetailsTabPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_items_detail);

        if (null != getIntent().getExtras()) {
            init(getIntent().getExtras());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_menu_di_form:
                onNewForm();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    private void init(Bundle extras) {
        itemCode = extras.getString(EXTRAS_DISPATCH_ITEM_CODE);
        itemType = extras.getString(EXTRAS_DISPATCH_ITEM_TYPE);

        initViewPager();
        initActionBar();
    }


    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(R.string.dispatch_item_title);

        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        String[] detailsTabs = getResources().getStringArray(R.array.details_tabs);
        for (String detail : detailsTabs) {
            actionBar.addTab(actionBar.newTab().setText(detail).setTabListener(this));
        }
    }

    private void initViewPager() {
        mAdapter = new DetailsTabPagerAdapter(this, getSupportFragmentManager(), itemCode);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(4);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private void onNewForm() {
        if (null == itemCode || null == itemType)
            return;

        Intent form = new Intent(this, FormActivity.class);
        form.putExtra(FormActivity.EXTRAS_FORM_ORDER_NUMBER, itemCode);
        form.putExtra(FormActivity.EXTRAS_FORM_ORDER_TYPE, itemType);

        startActivity(form);
    }

}
