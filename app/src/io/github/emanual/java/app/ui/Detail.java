package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.api.RestClient;
import io.github.emanual.java.app.utils._;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.widget.AdBanner;

/**
 * 查看NewsFeeds or 文章详情
 * 
 * @author jayin
 * 
 */
@SuppressLint("SetJavaScriptEnabled") public class Detail extends BaseActivity
		implements OnRefreshListener {
	ActionBar mActionBar;
	@InjectView(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
	@InjectView(R.id.webview) WebView webview;
	String link;// 接受2种URL,一种是url,另一种文件路径path
	String title;
	String sharePath; //分享路径
	Menu mMenu = null;
	boolean isLoading = false;
	// 广告
	private static final String TAG_BANNER = "1ecf3f37f1a348d3a0a2e5f7bfca623d";
	private AdBanner adBanner;
	private View adBannerView;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_detail);
		ButterKnife.inject(this);
		initData();
		initLayout();
	}

	@Override protected void initData() {
		link = getIntent().getStringExtra("link");
		title = getIntent().getStringExtra("title");
		sharePath = getIntent().getStringExtra("sharePath");
		Log.d("debug", "当前文章的URL--> " + link);
		if (link == null || title == null) {
			finish();
		}
	}

	@Override protected void initLayout() {
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(new MyWebChromeClient());
		webview.setWebViewClient(new MyWebViewClient());

		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle(title);
		// mActionBar.setTitle(ParseUtils.getArticleNameByUrl(url));
		// mActionBar.setTitle()

		isLoading = false;
		onRefresh();

		showBannerAd();
	}

	private void showBannerAd() {
		ViewGroup containerView = (ViewGroup) findViewById(R.id.banner_ad_container);
		if (adBannerView != null
				&& containerView.indexOfChild(adBannerView) >= 0) {
			containerView.removeView(adBannerView);
		}
		adBanner = Ads.showBannerAd(this,
				(ViewGroup) findViewById(R.id.banner_ad_container), TAG_BANNER);
		adBannerView = adBanner.getView();
	}

	/**
	 * 刷新前
	 */
	private void onPreRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		unDisplayMenu(mMenu);
		isLoading = true;
	}

	@Override public void onRefresh() {
		if (isLoading)
			return;
		if (_.isURL(link)) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(link, new AsyncHttpResponseHandler() {
				@Override public void onStart() {
					onPreRefresh();
				}

				@Override public void onSuccess(int statusCode,
						Header[] headers, byte[] data) {
					load(new String(data));
					debug("onSuccess");
				}

				@Override public void onFailure(int statusCode,
						Header[] headers, byte[] data, Throwable arg3) {
					try {
						load(_.readFile(getAssets().open("404.md")));
					} catch (IOException e) {
						e.printStackTrace();
					}
					debug("onFailure");
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
		swipeRefreshLayout.setRefreshing(false);
		isLoading = false;
		displayMenu(mMenu);
	}

	@Override protected void onStart() {
		adBanner.startAutoScroll();
		super.onStart();
	}

	@Override protected void onStop() {
		adBanner.stopAutoScroll();
		super.onStop();
	}

	private void load(String content) {
		try {
			String tpl = _.readFile(getAssets().open("preview.html"));
			webview.loadDataWithBaseURL("about:blank",
					tpl.replace("{markdown}", content), "text/html", "UTF-8",
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
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		Log.i("debug", "onCreateOptionsMenu");
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
			Log.d("debug",
					consoleMessage.message() + " -- From line "
							+ consoleMessage.lineNumber() + " of "
							+ consoleMessage.sourceId());
			return true;
		}
	}

	class MyWebViewClient extends WebViewClient {

		@Override public void onLoadResource(WebView view, String url) {
			Log.d("debug", "onLoadResource--> " + url);
		}

		@Override public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d("debug", "onPageFinished--> " + url);
		}

		@Override public void onPageStarted(WebView view, String url,
				Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.d("debug", "onPageStarted--> " + url);
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
			try {
				return _.readFile(this.path);
			} catch (FileNotFoundException e) {
				try {
					return _.readFile(getAssets().open("404.md"));
				} catch (IOException e1) {
					e1.printStackTrace();
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
