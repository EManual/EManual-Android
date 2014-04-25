package io.github.emanual.app.fragment;

import io.github.emanual.app.R;
import io.github.emanual.app.adapter.NewFeedsAdapter;
import io.github.emanual.app.api.NewFeedsAPI;
import io.github.emanual.app.api.RestClient;
import io.github.emanual.app.ui.Detail;

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

import com.loopj.android.http.JsonHttpResponseHandler;

public class NewFeeds extends BaseFragment implements OnRefreshListener {
	ListView lv;
	SwipeRefreshLayout swipeRefreshLayout;
	String[] strs = new String[] { "one", "two", "three", "four", "five",
			"six", "seven", "eight", "nine", "ten" };
	boolean hasMore = true;
	int page = 1, maxPage = 1;
	long last_motify = 0;
	NewFeedsAPI api = new NewFeedsAPI();
	NewFeedsAdapter adapter;
	List<String> data = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_newfeeds, container, false);
		lv = (ListView) v.findViewById(R.id.lv_newfeeds);
		swipeRefreshLayout = (SwipeRefreshLayout) v
				.findViewById(R.id.swipeRefreshLayout);
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
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(getActivity(),Detail.class);
				intent.putExtra("url", RestClient.URL_Preview);
				startActivity(intent);
			}
		});
		return v;
	}

	public void onLoadMore() {
		if (page + 1 <= maxPage) {
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
						if (page + 1 >= maxPage)
							hasMore = false;
					} catch (JSONException e) {
						e.printStackTrace();
						toast("parse error!");
					}
				}

				@Override
				public void onFinish() {
					swipeRefreshLayout.setRefreshing(false);
				}
			});
		}
	}

	@Override
	public void onRefresh() {
		api.getInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					Log.i("debug",
							"last_motify:" + response.getLong("last_motify"));
					Log.i("debug", "last_motify:" + response.getInt("pages"));
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
								hasMore = true;
							} catch (JSONException e) {
								e.printStackTrace();
								toast("parse error!");
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							toast("get newfeedlist error:" + arg0);
						}

						@Override
						public void onFinish() {
							swipeRefreshLayout.setRefreshing(false);
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				toast("get info error:" + arg0);
				swipeRefreshLayout.setRefreshing(false);
			}

			@Override
			public void onFinish() {
			}
		});
	}
}
