package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.utils.ParseUtils;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class Detail extends BaseActivity implements OnRefreshListener {
	ActionBar mActionBar;
	SwipeRefreshLayout swipeRefreshLayout;
	WebView webview;
	String url = null, content = null;
	String interfaceName = "Android";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_detail);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		url = getIntent().getStringExtra("url");
		Log.d("debug","当前文章的URL--> "+url);
		content = getIntent().getStringExtra("content");
		if (url == null) {
			finish();
			toast("you hava to pass a url for this");
		}
	}

	@Override
	protected void initLayout() {
		swipeRefreshLayout = (SwipeRefreshLayout) _getView(R.id.swipeRefresh);
		webview = (WebView) _getView(R.id.webview);
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
		mActionBar.setTitle(ParseUtils.getArticleNameByUrl(url));
		
		swipeRefreshLayout.setRefreshing(true);
		onRefresh();
	}

	@Override
	public void onRefresh() {
		load();
	}

	private void load() {
		webview.addJavascriptInterface(new WebAppInterface(getContext()), interfaceName);
		webview.loadUrl("file:///android_asset/preview1.html");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_favourite:
			toast("faviourite");
			return true;
		case R.id.action_share:
			ShareCompat.IntentBuilder
					.from(this)
					.setType("text/plain")
					.setText(
							"我发现了一篇很不错的文章<<"
									+ ParseUtils.getArticleNameByUrl(url)
									+ ">> " + webview.getUrl())
					.setChooserTitle("分享到").startChooser();
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				swipeRefreshLayout.setRefreshing(false);
			}else{
				swipeRefreshLayout.setRefreshing(true);
			}
		}
		
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			Log.d("debug", consoleMessage.message() + " -- From line "
                    + consoleMessage.lineNumber() + " of "
                    + consoleMessage.sourceId() );
			return true;
		}
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
			Log.d("debug","onLoadResource--> "+url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			Log.d("debug","onPageFinished--> "+url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			Log.d("debug","onPageStarted--> "+url);
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			return super.shouldOverrideKeyEvent(view, event);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			return true;
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			toast("Error Code--->"+errorCode+"   failingUrl--> "+failingUrl);
		}
	}

	public class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface(Context c) {
			mContext = c;
		}

		/** Show a toast from the web page */
		@JavascriptInterface
		public void showToast(String toast) {
			Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		}

		public String getUrl() {
			return url;
		}

		public String getContent() {
			return content;
		}
	}

}
