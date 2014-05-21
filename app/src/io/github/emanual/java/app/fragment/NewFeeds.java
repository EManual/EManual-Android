package io.github.emanual.java.app.fragment;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.adapter.NewFeedsAdapter;
import io.github.emanual.java.app.api.NewFeedsAPI;
import io.github.emanual.java.app.api.RestClient;
import io.github.emanual.java.app.ui.Detail;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class NewFeeds extends BaseFragment implements OnRefreshListener,
		OnScrollListener {
	@InjectView(R.id.lv_newfeeds)ListView lv;
	@InjectView(R.id.swipeRefreshLayout)SwipeRefreshLayout swipeRefreshLayout;
	boolean hasMore = true, isloading = false;
	int page = 1, maxPage = 1;
	long last_motify = 0;
	NewFeedsAPI api = new NewFeedsAPI();
	NewFeedsAdapter adapter;
	List<String> data = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_newfeeds, container, false);
		ButterKnife.inject(this, v);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light);

		adapter = new NewFeedsAdapter(getActivity(), data);
		lv.setAdapter(adapter);

		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

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
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), Detail.class);
				//RestClient.URL_Java_NewFeeds+"/"+data.get(position)
				intent.putExtra("url", RestClient.URL_Java_NewFeeds+"/"+data.get(position));
				startActivity(intent);
			}
		});
		onRefresh();
		return v;
	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		isloading = true;
		api.getInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					maxPage = response.getInt("pages");
					api.getNewFeeds(1, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							try {
								JSONArray array = response
										.getJSONArray("result");
								ArrayList<String> names = new ArrayList<String>();
								for (int i = 0; i < array.length(); i++) {
									names.add(array.getString(i));
								}
								data.clear();
								data.addAll(names);
								adapter.notifyDataSetChanged();
								page = 1;
								if (names.size() < 10) {
									hasMore = false;
								} else {
									hasMore = true;
								}
							} catch (JSONException e) {
								e.printStackTrace();
								toast("哎呀,出错了！");
								Log.e("debug", "NewFeeds-->parse error!");
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Log.e("debug", "NewFeeds-->get newfeedlist error:" + arg0);
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
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Log.e("debug", "NewFeeds-->get info error:" + arg0);
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
		api.getNewFeeds(page + 1, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				swipeRefreshLayout.setRefreshing(true);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					JSONArray array = response.getJSONArray("result");
					ArrayList<String> names = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						names.add(array.getString(i));
					}
					data.addAll(names);
					adapter.notifyDataSetChanged();
					page += 1;
					if (page >= maxPage || names.size() < 10)
						hasMore = false;
				} catch (JSONException e) {
					e.printStackTrace();
					toast("parse error!");
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toast("get NewFeed list error.ErrorCode=" + arg0);
			}

			@Override
			public void onFinish() {
				swipeRefreshLayout.setRefreshing(false);
				isloading = false;
			}
		});

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (swipeRefreshLayout.isRefreshing() || isloading)
			return;

		if (firstVisibleItem + visibleItemCount >= totalItemCount
				&& totalItemCount != 0 && hasMore) {
			isloading = false;
			onLoadMore();
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
