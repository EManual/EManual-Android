package io.github.emanual.app;

import io.github.emanual.app.api.RestClient;
import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		RestClient.init(getApplicationContext());
	}
}
