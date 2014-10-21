package com.claro.cfcmobile.activities.fragments.browseable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.claro.cfcmobile.R;
import com.claro.cfcmobile.utils.CommonUtils;

public class PrivacyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init() {
        getActivity().setTitle(getString(R.string.privacy_policy_title));
        setupTextView();
    }


    private void setupTextView() {
        TextView text = (TextView) getActivity().findViewById(R.id.text1);
        text.setText(Html.fromHtml(getString(R.string.privacy_terms)));
        CommonUtils.setupWrappedText(text);
    }
}
