package io.github.emanual.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import io.github.emanual.app.api.EmanualAPI;

public class CoreService extends Service {
    public static final String Action_CheckVersion = "Action_CheckVersion";

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }
        if (action != null) {
            Log.d("debug", "CoreService-->action-->" + action);
            if (action.equals(Action_CheckVersion)) {
                checkVersion();
            }
        }
        return Service.START_NOT_STICKY;
    }

    private void checkVersion() {
        EmanualAPI.getVersionInfo(new JsonHttpResponseHandler() {
            @Override public void onSuccess(int statusCode, Header[] headers,
                                            JSONObject response) {
                Intent intent = new Intent(Action_CheckVersion);
                try {

                    int version_code = response.getInt("version_code");
                    String version_name = response.getString("version_name");
                    String download_url = response.getString("download_url");
                    String update_time = response.getString("update_time");
                    String size = response.getString("size");

                    String change_log = String.format("版本:%s  更新于%s\n大小:%s\n", version_name, update_time, size);
                    JSONArray change_logs = response.getJSONArray("change_log");
                    for (int i = 0; i < change_logs.length(); i++) {
                        JSONObject obj = change_logs.getJSONObject(i);
                        change_log += String.format(" * %s\n", obj.getString("content"));
                    }
                    //应该交由接受端判断如何处理
                    intent.putExtra("version_code", version_code);
                    intent.putExtra("version_name", version_name);
                    intent.putExtra("change_log", change_log);
                    intent.putExtra("download_url", download_url);
                    intent.putExtra("size", size);
                    sendBroadcast(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override public IBinder onBind(Intent intent) {
        return null;
    }

}
