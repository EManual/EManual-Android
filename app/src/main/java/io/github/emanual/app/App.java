package io.github.emanual.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

import io.github.emanual.app.api.RestClient;

public class App extends Application {
    private final String LeanCloud_App_Id = "7hyoc6are05n823dd424ywvf752gem2w96inlkl3yiann6vw";
    private final String LeanCloud_App_Key = "tgufkdybjtb4gvsbwcatiwd9wx49adxrmf8qkpwunh0h3wx3";

    @Override
    public void onCreate() {
        super.onCreate();
        RestClient.init(getApplicationContext());

        //init leancloud
        AVOSCloud.initialize(this, LeanCloud_App_Id, LeanCloud_App_Key);
    }
}
