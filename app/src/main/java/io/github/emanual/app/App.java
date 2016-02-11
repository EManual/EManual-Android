package io.github.emanual.app;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.avos.avoscloud.AVOSCloud;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.github.emanual.app.api.RestClient;
import timber.log.Timber;

public class App extends Application {
    private final String LeanCloud_App_Id = "7hyoc6are05n823dd424ywvf752gem2w96inlkl3yiann6vw";
    private final String LeanCloud_App_Key = "tgufkdybjtb4gvsbwcatiwd9wx49adxrmf8qkpwunh0h3wx3";

    @Override
    public void onCreate() {
        super.onCreate();
        RestClient.init(getApplicationContext());

        //
        Fresco.initialize(getApplicationContext());

        //init leancloud
        AVOSCloud.initialize(this, LeanCloud_App_Id, LeanCloud_App_Key);

        //android bootstrap
        TypefaceProvider.registerDefaultIconSets();

        Timber.plant(new ReleaseTree());

        if(BuildConfig.DEBUG){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }

            Timber.plant(new Timber.DebugTree());
        }

    }

    /** A tree which logs important information for crash reporting. */
    private static class ReleaseTree extends Timber.Tree {


        @Override protected void log(int priority, String tag, String message, Throwable t) {
            //v/d 均不打log
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            int MAX_LOG_LENGTH = 4000;
            if (message.length() < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message);
                } else {
                    Log.println(priority, tag, message);
                }
                return;
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            for (int i = 0, length = message.length(); i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + MAX_LOG_LENGTH);
                    String part = message.substring(i, end);
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part);
                    } else {
                        Log.println(priority, tag, part);
                    }
                    i = end;
                } while (i < newline);
            }
        }
    }
}
