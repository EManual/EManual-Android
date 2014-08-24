package io.github.emanual.java.app;

import io.github.emanual.java.app.api.RestClient;
import android.app.Application;
import butterknife.ButterKnife;

import com.wandoujia.ads.sdk.Ads;

public class App extends Application {
	private final String AppKey_ID = "100012315";
	private final String SECRET_KEY = "34120bea3dbac756bb258e7f45e2feb3";
	@Override
	public void onCreate() {
		super.onCreate();
		RestClient.init(getApplicationContext());
		ButterKnife.setDebug(true);

		// Init AdsSdk.
		try {
			Ads.init(this, AppKey_ID, SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
