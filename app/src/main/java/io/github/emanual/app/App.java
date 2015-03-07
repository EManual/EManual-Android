package io.github.emanual.app;

import io.github.emanual.app.api.RestClient;
import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.wandoujia.ads.sdk.Ads;

public class App extends Application {
	private final String AD_AppKey_ID = "100023017";
	private final String AD_SECRET_KEY = "588db5663b8403d4c67ded503871d19a";
	private final String LeanCloud_App_Id = "7hyoc6are05n823dd424ywvf752gem2w96inlkl3yiann6vw";
	private final String LeanCloud_App_Key = "tgufkdybjtb4gvsbwcatiwd9wx49adxrmf8qkpwunh0h3wx3";
	@Override
	public void onCreate() {
		super.onCreate();
		RestClient.init(getApplicationContext());
		
		// Init AdsSdk.
		try {
			Ads.init(this, AD_AppKey_ID, AD_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//init leancloud
		AVOSCloud.initialize(this, LeanCloud_App_Id,LeanCloud_App_Key);
	}
}
