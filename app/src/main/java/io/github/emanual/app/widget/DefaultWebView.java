package io.github.emanual.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Author: jayin
 * Date: 3/8/16
 */
public class DefaultWebView extends WebView{


    public DefaultWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DefaultWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefaultWebView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setAppCacheEnabled(true);
        this.getSettings().setDatabaseEnabled(true);
        this.getSettings().setGeolocationEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }
}
