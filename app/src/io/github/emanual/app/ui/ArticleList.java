package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.adapter.ArticleListAdapter;
import io.github.emanual.app.api.JavaAPI;
import io.github.emanual.app.api.RestClient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ArticleList extends BaseActivity implements OnRefreshListener,
		OnItemClickListener, OnScrollListener {
	ActionBar mActionBar;
	SwipeRefreshLayout swipeRefreshLayout;
	ListView lv;
	String kind = null, topic = null;
	List<String> data;
	ArticleListAdapter adapter;
	int page = 1, maxPage = 1;
	long last_motify = 0;
	boolean hasMore = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_articlelist);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		kind = getStringExtra("kind");
		topic = getStringExtra("topic");
		if (kind == null || topic == null) {
			throw new NullPointerException("You need a `kind` and `topic`");
		}
		data = new ArrayList<String>();
		adapter = new ArticleListAdapter(this, data);
	}

	@Override
	protected void initLayout() {
		mActionBar = getActionBar();
		mActionBar.setTitle(topic);
		swipeRefreshLayout = (SwipeRefreshLayout) _getView(R.id.swipeRefreshLayout);
		lv = (ListView) _getView(R.id.listview);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light);
		lv.setAdapter(adapter);
		lv.setOnScrollListener(this);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onRefresh() {
		JavaAPI.getTopicInfo(kind, topic, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					last_motify = response.getInt("last_motify");
					maxPage = response.getInt("pages");
					JavaAPI.getArticleList(1, kind, topic,
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									try {
										JSONArray array = response
												.getJSONArray("result");
										ArrayList<String> _topics = new ArrayList<String>();
										for (int i = 0; i < array.length(); i++) {
											_topics.add(array.getString(i));
										}

										data.addAll(_topics);
										adapter.notifyDataSetChanged();
										hasMore = true;
										page = 1;
									} catch (JSONException e) {
										e.printStackTrace();
										toast("parse error!");
										swipeRefreshLayout.setRefreshing(false);
									}
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {
									toast("get article list error.ErrorCode="
											+ arg0);
								}

								@Override
								public void onFinish() {
									swipeRefreshLayout.setRefreshing(false);
								}
							});
				} catch (JSONException e) {
					e.printStackTrace();
					toast("parse error!");
					swipeRefreshLayout.setRefreshing(false);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toast("get topic info error!");
				swipeRefreshLayout.setRefreshing(false);
			}
		});

	}

	public void onLoadMore() {
		if (page + 1 > maxPage)
			return;
		JavaAPI.getArticleList(page + 1, kind, topic,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							JSONArray array = response.getJSONArray("result");
							ArrayList<String> _topics = new ArrayList<String>();
							for (int i = 0; i < array.length(); i++) {
								_topics.add(array.getString(i));
							}

							data.addAll(_topics);
							adapter.notifyDataSetChanged();
							hasMore = true;
							page += 1;
						} catch (JSONException e) {
							e.printStackTrace();
							toast("parse error!");
							swipeRefreshLayout.setRefreshing(false);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						toast("get article list error.ErrorCode=" + arg0);
					}

					@Override
					public void onFinish() {
						swipeRefreshLayout.setRefreshing(false);
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent  = new Intent(getContext(),Detail.class);
		Log.i("debug",RestClient.URL_Preview+"?"+JavaAPI.getArticleParam(kind, topic, data.get(position))); 
 		intent.putExtra("url", RestClient.URL_Preview+"?"+JavaAPI.getArticleParam(kind, topic, data.get(position)));
 		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (!swipeRefreshLayout.isRefreshing()) {
			if (firstVisibleItem + visibleItemCount >= totalItemCount
					&& totalItemCount != 0 && hasMore) {
				onLoadMore();
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
