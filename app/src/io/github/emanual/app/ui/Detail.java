package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class Detail extends BaseActivity implements OnRefreshListener{
	ActionBar mActionBar;
	SwipeRefreshLayout swipeRefreshLayout;
	WebView webview;
	String url = null;
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
		if(url==null){
			finish();
		    toast("you hava to pass a url for this");
		}
	}

	@Override
	protected void initLayout() {
		swipeRefreshLayout = (SwipeRefreshLayout)_getView(R.id.swipeRefresh);
		webview = (WebView)_getView(R.id.webview);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light);
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(url);
		webview.setWebChromeClient(new MyWebChromeClient());
		webview.setWebViewClient(new MyWebViewClient());

		swipeRefreshLayout.setRefreshing(true);
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				swipeRefreshLayout.setRefreshing(false);

			}
		}).start();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_favourite:
			toast("faviourite");
			return true;
		case R.id.action_share:
			toast("share");
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
	
	class MyWebChromeClient extends WebChromeClient{
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if(newProgress ==100){
				swipeRefreshLayout.setRefreshing(false);
			}
		}
	}
	
	class MyWebViewClient extends WebViewClient{

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			return super.shouldOverrideKeyEvent(view, event);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			return super.shouldOverrideUrlLoading(view, url);
		}
		
	}

}
