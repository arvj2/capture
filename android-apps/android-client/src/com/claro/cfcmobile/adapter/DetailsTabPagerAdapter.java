package com.claro.cfcmobile.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.claro.cfcmobile.activities.fragments.DispatchItemDetailFragment;

/**
 * Created by Christopher Herrera on 3/12/14.
 *
 * @Refactored completely by Jansel R. Abreu (Vanwolf),
 */
public class DetailsTabPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private String codeDetail;
    private Context context;


    public DetailsTabPagerAdapter(Context cxt,FragmentManager fm, String codeDetail ) {
        super(fm);
        this.context = cxt;
        this.fm = fm;
        this.codeDetail = codeDetail;
    }


    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public Fragment getItem(int index) {
        return new DispatchItemDetailFragment(context,codeDetail, index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
