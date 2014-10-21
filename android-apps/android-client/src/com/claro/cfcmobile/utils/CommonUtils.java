package com.claro.cfcmobile.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

public final class CommonUtils {

    private static final class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }


    public static void stripUnderline(TextView text) {
        Spannable span = new SpannableString(text.getText());
        URLSpan[] urls = span.getSpans(0, span.length(), URLSpan.class);

        for (URLSpan s : urls) {
            int start = span.getSpanStart(s);
            int end = span.getSpanEnd(s);
            span.removeSpan(s);
            s = new URLSpanNoUnderline(s.getURL());
            span.setSpan(s, start, end, 0);
        }
        text.setText(span);
    }


    public static void setupWrappedText(final TextView text) {
        final ViewTreeObserver observer = text.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int maxLines = text.getHeight() / text.getLineHeight();
                text.setMaxLines(maxLines);
                text.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }


}
