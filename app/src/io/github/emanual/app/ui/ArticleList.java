package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.api.JavaAPI;
import io.github.emanual.app.api.RestClient;
import io.github.emanual.app.ui.adapter.ArticleListAdapter;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ArticleList extends BaseActivity implements OnRefreshListener,
		OnItemClickListener, OnScrollListener {
	ActionBar mActionBar;
	@InjectView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
	@InjectView(R.id.listview) ListView lv;
	String kind = null, topic = null;
	List<String> data;
	ArticleListAdapter adapter;
	int page = 1, maxPage = 1;
	long last_motify = 0;
	boolean hasMore = true, isloading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_articlelist);
		ButterKnife.inject(this);
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
		mActionBar.setDisplayHomeAsUpEnabled(true);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light);
		lv.setAdapter(adapter);
		lv.setOnScrollListener(this);
		lv.setOnItemClickListener(this);
		onRefresh();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh() {
		isloading = true;
		JavaAPI.getTopicInfo(kind, topic, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				swipeRefreshLayout.setRefreshing(true);
			}

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
										ArrayList<String> _articles = new ArrayList<String>();
										for (int i = 0; i < array.length(); i++) {
											_articles.add(array.getString(i));
										}
										data.clear();
										data.addAll(_articles);
										adapter.notifyDataSetChanged();
										page = 1;
										if (_articles.size() < 10) {
											hasMore = false;
										} else {
											hasMore = true;
										}
									} catch (JSONException e) {
										e.printStackTrace();
										Log.e("debug", "ArticleList-->parse error!");
										toast("哎呀,出错了！");
									}
								}

								@Override
								public void onFailure(int statusCode, Header[] headers,
										Throwable throwable, JSONObject errorResponse) {
									Log.e("debug", "ArticleList-->无法获取该信息 ErrorCode=" + statusCode);
									toast("哎呀,出错了！");
								}

								@Override
								public void onFinish() {
									swipeRefreshLayout.setRefreshing(false);
									isloading = false;
								}
							});
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("debug", "ArticleList-->parse error!" );
					toast("哎呀,出错了！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Log.e("debug", "ArticleList-->无法获取该信息 ErrorCode=" + statusCode);
				toast("哎呀,出错了！");
			}

			@Override
			public void onFinish() {
				swipeRefreshLayout.setRefreshing(false);
				isloading = false;
			}
		});

	}

	public void onLoadMore() {
		JavaAPI.getArticleList(page + 1, kind, topic,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						swipeRefreshLayout.setRefreshing(true);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							JSONArray array = response.getJSONArray("result");
							ArrayList<String> _articles = new ArrayList<String>();
							for (int i = 0; i < array.length(); i++) {
								_articles.add(array.getString(i));
							}
							data.addAll(_articles);
							adapter.notifyDataSetChanged();
							hasMore = true;
							page += 1;
							if (page >= maxPage || _articles.size() < 10)
								hasMore = false;
						} catch (JSONException e) {
							e.printStackTrace();
							Log.e("debug", "ArticleList-->parse error!" );
							toast("哎呀,出错了！");
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						Log.e("debug", "ArticleList-->无法获取该信息 ErrorCode=" + statusCode);
						toast("哎呀,出错了！");
					}

					@Override
					public void onFinish() {
						swipeRefreshLayout.setRefreshing(false);
						isloading = false;
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(getContext(), Detail.class);
		intent.putExtra("url", RestClient.URL_Java+"/"+kind+"/"+topic+"/"+data.get(position));
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (swipeRefreshLayout.isRefreshing() || isloading)
			return;

		if (firstVisibleItem + visibleItemCount >= totalItemCount
				&& totalItemCount != 0 && hasMore) {
			isloading = true;
			onLoadMore();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
