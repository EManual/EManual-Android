package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.api.RestClient;
import io.github.emanual.java.app.entity.FavArticle;
import io.github.emanual.java.app.utils.MyDBManager;
import io.github.emanual.java.app.utils.ParseUtils;
import io.github.emanual.java.app.utils._;
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

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

@SuppressLint("SetJavaScriptEnabled")
public class Detail extends BaseActivity implements OnRefreshListener {
	ActionBar mActionBar;
	SwipeRefreshLayout swipeRefreshLayout;
	WebView webview;
	String url = null, content = null;
	String interfaceName = "Android";
	boolean isFavourite = false; // 是否已收藏

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
		content = getIntent().getStringExtra("content");
		Log.d("debug", "当前文章的URL--> " + url);
		if (url == null) {
			finish();
			toast("you hava to pass a url for this");
		}
		if(content == null){
			content = "";
		}
		Log.d("debug", "当前文章的content--> " + content);
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
		webview.addJavascriptInterface(new WebAppInterface(getContext()),
				interfaceName);
		webview.loadUrl("file:///android_asset/preview1.html");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_favourite:
			DbUtils db = MyDBManager.getDBUtils(getContext());
			FavArticle fa = new FavArticle();
			fa.setTitle(ParseUtils.getArticleNameByUrl(url));
			fa.setUrl(url);
			fa.setContent(content);
			if (isFavourite) {
				isFavourite = false;
				try {
					db.delete(FavArticle.class,WhereBuilder.b("url", "=", url));
				} catch (DbException e) {
					e.printStackTrace();
				}
				item.setIcon(R.drawable.ic_action_rating_not_important);
				toast("已取消收藏");
			} else {
				isFavourite = true;
				try {
					db.save(fa);
				} catch (DbException e) {
					e.printStackTrace();
				}
				item.setIcon(R.drawable.ic_action_rating_important);
				toast("已收藏");
			}
			return true;
		case R.id.action_share:
			ShareCompat.IntentBuilder
					.from(this)
					.setType("text/plain")
					.setText(
							"我发现了一篇很不错的文章<<"
									+ ParseUtils.getArticleNameByUrl(url)
									+ ">> " + RestClient.URL_Preview + "?path="
									+ _.encodeURL(url)).setChooserTitle("分享到")
					.startChooser();
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
		DbUtils db = MyDBManager.getDBUtils(getContext());
		FavArticle fa = null;
		try {
			fa = db.findFirst(Selector.from(FavArticle.class).where("url", "=",
					url));
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (fa == null) {
			isFavourite = false;
			menu.getItem(1).setIcon(R.drawable.ic_action_rating_not_important);
		} else {
			isFavourite = true;
			menu.getItem(1).setIcon(R.drawable.ic_action_rating_important);
		}
		return true;
	}

	class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				swipeRefreshLayout.setRefreshing(false);
			} else {
				swipeRefreshLayout.setRefreshing(true);
			}
		}

		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			Log.d("debug",
					consoleMessage.message() + " -- From line "
							+ consoleMessage.lineNumber() + " of "
							+ consoleMessage.sourceId());
			return true;
		}
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
			Log.d("debug", "onLoadResource--> " + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			Log.d("debug", "onPageFinished--> " + url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			Log.d("debug", "onPageStarted--> " + url);
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
			// toast("Error Code--->"+errorCode+"   failingUrl--> "+failingUrl);
			// view.loadUrl("file:///android_asset/404.html");
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

		public void setContent(String content) {
			Detail.this.content = content;
		}

	}

}
