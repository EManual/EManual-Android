package io.github.emanual.java.app;

import butterknife.ButterKnife;
import io.github.emanual.java.app.api.RestClient;
import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		RestClient.init(getApplicationContext());
		ButterKnife.setDebug(true);
	}
}
