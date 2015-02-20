package io.github.emanual.app;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import io.github.emanual.app.api.JavaAPI;
import io.github.emanual.app.utils.AndroidUtils;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.util.Log;

public class CoreService extends Service {
	public static final String Action_CheckVersion = "Action_CheckVersion";

	 
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action  = null;
		if(intent != null){
			action = intent.getAction();
		}
		if(action !=  null){
			Log.d("debug", "CoreService-->action-->"+action);
			if (action.equals(Action_CheckVersion)) {
				checkVersion();
			}
		}
		return Service.START_NOT_STICKY;
	}

	private void checkVersion() {
		JavaAPI.getVersionInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Intent intent = new Intent(Action_CheckVersion);
				try {
					
					int versionCode = response.getInt("versionCode");
					String versionName = response.getString("versionName");
					String description = response.getString("description");
					String url = response.getString("url");
					
					if (versionCode > AndroidUtils
							.getAppVersionCode(getApplicationContext())) {
						intent.putExtra("versionCode", versionCode);
						intent.putExtra("versionName", versionName);
						intent.putExtra("description", description);
						intent.putExtra("url", url);
						sendBroadcast(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
