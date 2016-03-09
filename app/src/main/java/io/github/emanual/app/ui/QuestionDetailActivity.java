package io.github.emanual.app.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.QuestionEntity;
import io.github.emanual.app.ui.base.activity.SwipeBackActivity;
import io.github.emanual.app.ui.event.QuestionUpdateEvent;
import io.github.emanual.app.widget.DefaultWebView;
import timber.log.Timber;

public class QuestionDetailActivity extends SwipeBackActivity {

    @Bind(R.id.webview) DefaultWebView webView;

    public static final String EXTRA_INTERVIEW = "EXTRA_QUESTION";
    private static final String CALL_BY_NATIVE = "javascript:(function(){ Bridge.callByNative(%s) })()";

    Map<String, Handler.Callback> callbacks = new HashMap<String, Handler.Callback>();
    int guid = 0;



    QuestionEntity questionEntity;

    @Override protected void initData(Bundle savedInstanceState) {
        questionEntity = (QuestionEntity)getSerializableExtra(EXTRA_INTERVIEW);
        if(questionEntity == null){
            finish();
        }

        Timber.d(questionEntity.toString());
    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        if(isFinishing()){
            return;
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_interview_detail);

        webView.setWebChromeClient(new MyWebChromeClient());


        webView.loadUrl("file:///android_asset/preview-question/index.html");

        Timber.d("question json:");
        Timber.d(new Gson().toJson(questionEntity));
    }


    @Override protected int getContentViewId() {
        return R.layout.acty_question_detail;
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_question_detail, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_show_answer:
                Toast.makeText(getContext(), "action_show_answer", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onFinishLoadQuestionList(QuestionUpdateEvent event){
        webView.loadUrl(String.format(CALL_BY_NATIVE, String.format("window.")));
    }


    class MyWebChromeClient extends WebChromeClient {

        @Override public void onReceivedTitle(WebView view, String title) {
            getSupportActionBar().setTitle(title);
        }

        @Override public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Timber.d(consoleMessage.message() + " -- From line "
                    + consoleMessage.lineNumber() + " of "
                    + consoleMessage.sourceId());
            return true;
        }

        @Override public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            handleWeb(message);
            result.confirm("");
            return true;
        }

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress == 100){
//                EventBus.getDefault().post(new QuestionUpdateEvent(questionEntity));
                handleNative(String.format("window.updateQuestion(%s)", new Gson().toJson(questionEntity)), new Handler.Callback() {
                    @Override public boolean handleMessage(Message msg) {
                        Timber.d("handleNative callback@");
                        return true;
                    }
                });
                //window.updateQuestion(%s)'.replace('%s',JSON.stringify(q))
            }
        }
    }

    //本地call web
    void handleNative(String script, Handler.Callback callback) {
        Timber.d("handleNative script: " + script);
        try {
            //
            JSONObject input = new JSONObject();
            String token = "" + ++guid;
            input.put("token", token);
            input.put("script", script);
            callbacks.put(token, callback);

            // Bridge
            webView.loadUrl(String.format(CALL_BY_NATIVE, input.toString()));
        } catch (JSONException e) {
            Timber.e(e.toString());
        }
    }

    void handleWeb(String message) {
        Timber.d("handleWeb input: " + message);
        try {
            //
            JSONObject input = new JSONObject(message);
            String token = input.getString("token");
            String name = input.optString("name", "");

            // Native call Web
            if (name.equals("")) {
                String ret = input.getString("ret");

                Bundle data = new Bundle();
                data.putString("ret", ret);
                Message msg = new Message();
                msg.setData(data);
                callbacks.get(token).handleMessage(msg);
            }
            // Web call Native
            else {
                JSONObject param = input.getJSONObject("param");

                JSONObject output = new JSONObject();
                JSONObject ret = new JSONObject();
                output.put("token", token);
                output.put("ret", ret);
                if (name.equals("model")) {
                    ret.put("result", Build.MODEL);
                } else if (name.equals("add")) {
                    ret.put("result", param.getInt("num1") + param.getInt("num2"));
                }

                // ���� Bridge
                webView.loadUrl(String.format(CALL_BY_NATIVE, output.toString()));
            }
        } catch (JSONException e) {
            Timber.e(e.toString());
        }
    }


    //load question
    //render
    //do action - feed back :1. show the answer at once
    //do action - next : note: NO NEXT
    //do action - pre  : not : NO Preview



}
