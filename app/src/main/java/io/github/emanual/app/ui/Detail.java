package io.github.emanual.app.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import io.github.emanual.app.R;
import io.github.emanual.app.api.RestClient;
import io.github.emanual.app.ui.base.activity.SwipeBackActivity;
import io.github.emanual.app.utils.SwipeRefreshLayoutUtils;
import io.github.emanual.app.utils._;
import timber.log.Timber;

/**
 * 查看NewsFeeds or 文章详情
 *
 * @author jayin
 */
@SuppressLint("SetJavaScriptEnabled")
public class Detail extends SwipeBackActivity
        implements OnRefreshListener {
    public static final String EXTRA_LINK = "link";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_SHARE_PATH = "sharePath";
    public static final String EXTRA_FEEDBACK_CONTENT = "feedbackContent";

    public static final String FEEDBACK_CONTENT_TPL = "我发现《%s》有错误,路径为: %s ";

    ActionBar mActionBar;
    Toolbar mToolbar;
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.webview) WebView webview;
    String link;// 接受2种URL,一种是url,另一种文件路径path
    String title;
    String sharePath; //分享路径
    String feedback_content;//反馈内容
    Menu mMenu = null;


    @Override protected void initData(Bundle savedInstanceState) {

        link = getIntent().getStringExtra(EXTRA_LINK);
        title = getIntent().getStringExtra(EXTRA_TITLE);
        sharePath = getIntent().getStringExtra(EXTRA_SHARE_PATH);
        feedback_content = getIntent().getStringExtra(EXTRA_FEEDBACK_CONTENT);

        if (link == null || title == null || sharePath == null || feedback_content == null) {
            finish();
        }
    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setGeolocationEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webview.setWebChromeClient(new MyWebChromeClient());
        webview.setWebViewClient(new MyWebViewClient());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(title);

        onRefresh();

//		showBannerAd();
    }

    @Override protected int getContentViewId() {
        return R.layout.acty_detail;
    }

//	private void showBannerAd() {
//		ViewGroup containerView = (ViewGroup) findViewById(R.id.banner_ad_container);
//		if (adBannerView != null
//				&& containerView.indexOfChild(adBannerView) >= 0) {
//			containerView.removeView(adBannerView);
//		}
//		adBanner = Ads.showBannerAd(this,
//				(ViewGroup) findViewById(R.id.banner_ad_container), TAG_BANNER);
//		adBannerView = adBanner.getView();
//	}

    /**
     * 刷新前
     */
    private void onPreRefresh() {
        SwipeRefreshLayoutUtils.setRefreshing(swipeRefreshLayout, true);
        unDisplayMenu(mMenu);
    }

    @Override public void onRefresh() {
        if (_.isURL(link)) {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(link, new AsyncHttpResponseHandler() {
                @Override public void onStart() {
                    onPreRefresh();
                }

                @Override public void onSuccess(int statusCode,
                                                Header[] headers, byte[] data) {
                    load(new String(data));
                    Timber.d("onSuccess");
                }

                @Override public void onFailure(int statusCode,
                                                Header[] headers, byte[] data, Throwable arg3) {
                    try {
                        load(_.readFile(getAssets().open("404.md")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Timber.d("onFailure");
                }

                @Override public void onFinish() {
                    onPostRefresh();
                }
            });
        } else {
            // read file
            new ReadFileTask(link).execute();
        }

    }

    /**
     * 刷新完毕
     */
    private void onPostRefresh() {
        SwipeRefreshLayoutUtils.setRefreshing(swipeRefreshLayout, false);
        displayMenu(mMenu);
    }

    @Override protected void onStart() {
//		adBanner.startAutoScroll();
        super.onStart();
    }

    @Override protected void onStop() {
//		adBanner.stopAutoScroll();
        super.onStop();
    }

    private void load(String content) {
        try {
            String tpl = _.readFile(getAssets().open("preview.html"));
            webview.loadDataWithBaseURL("about:blank",
                    tpl.replace("{markdown}", TextUtilsCompat.htmlEncode(content)), "text/html", "UTF-8",
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ShareCompat.IntentBuilder
                        .from(this)
                        .setType("text/plain")
                        .setText(
                                "<" + title + "> " + RestClient.URL_Preview
                                        + "?path=" + sharePath)
                        .setChooserTitle("分享到")

                        .startChooser();
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_feedback_report:
                Intent intent = new Intent(this, Feedback.class);
                intent.putExtra(Feedback.EXTRA_TYPE, Feedback.TYPE_REPORT);
                intent.putExtra(Feedback.EXTRA_CONTENT, feedback_content);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        return displayMenu(mMenu);
    }

    private boolean displayMenu(Menu menu) {
        if (menu == null) {
            // 加载UI比网络访问还慢,menu依然为null
            // see:https://github.com/EManual/EManual-Client-Java/issues/18
            return true;
        }
        if (menu.size() == 0)
            getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    private boolean unDisplayMenu(Menu menu) {
        if (menu != null)
            menu.clear();
        return true;
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override public void onProgressChanged(WebView view, int newProgress) {
            // 这部分ui显示逻辑应该在onRefresh()中控制
            // if (newProgress == 100) {
            // swipeRefreshLayout.setRefreshing(false);
            // displayMenu(mMenu);
            // } else {
            // swipeRefreshLayout.setRefreshing(true);
            // unDisplayMenu(mMenu);
            // }
        }

        @Override public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Timber.d(consoleMessage.message() + " -- From line "
                            + consoleMessage.lineNumber() + " of "
                            + consoleMessage.sourceId());
            return true;
        }
    }

    class MyWebViewClient extends WebViewClient {

        @Override public void onLoadResource(WebView view, String url) {
            Timber.d("onLoadResource--> " + url);
        }

        @Override public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Timber.d("onPageFinished--> " + url);
        }

        @Override public void onPageStarted(WebView view, String url,
                                            Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Timber.d("onPageStarted--> " + url);
        }

        @Override public boolean shouldOverrideKeyEvent(WebView view,
                                                        KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override public boolean shouldOverrideUrlLoading(WebView view,
                                                          String url) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        @Override public void onReceivedError(WebView view, int errorCode,
                                              String description, String failingUrl) {
            // toast("Error Code--->"+errorCode+"   failingUrl--> "+failingUrl);
            // view.loadUrl("file:///android_asset/404.html");
        }
    }

    class ReadFileTask extends AsyncTask<Void, Void, String> {
        private String path;

        public ReadFileTask(String path) {
            this.path = path;
        }

        @Override protected void onPreExecute() {
            onPreRefresh();
        }

        @Override protected String doInBackground(Void... params) {
            if(new File(this.path).exists()){
                return _.readFile(this.path);
            }else{
                try {
                    return _.readFile(getAssets().open("404.md"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override protected void onPostExecute(String result) {
            load(result);
            onPostRefresh();
        }

    }

}
