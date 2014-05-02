package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.adapter.TopicListAdapter;
import io.github.emanual.java.app.api.JavaAPI;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TopicList extends BaseActivity implements OnRefreshListener,
		OnItemClickListener {
	ActionBar mActionBar;
	SwipeRefreshLayout swipeRefreshLayout;
	ListView lv;
	String kind = null, title = null;
	List<String> data;
	TopicListAdapter adapter;
	long last_motify = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_topiclist);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		kind = getStringExtra("kind");
		title = getStringExtra("title");
		if (kind == null || title == null) {
			throw new NullPointerException("You need a `kind` and `title`");
		}
		data = new ArrayList<String>();
		adapter = new TopicListAdapter(this, data);
	}

	@Override
	protected void initLayout() {
		mActionBar = getActionBar();
		mActionBar.setTitle(title);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		swipeRefreshLayout = (SwipeRefreshLayout) _getView(R.id.swipeRefreshLayout);
		lv = (ListView) _getView(R.id.listview);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_blue_light);
		lv.setAdapter(adapter);
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
		JavaAPI.getKindInfo(kind, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				swipeRefreshLayout.setRefreshing(true);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					JSONArray array = response.getJSONArray("result");
					last_motify = response.getInt("last_motify");
					ArrayList<String> _tipics = new ArrayList<String>();
					for (int i = 0; i < array.length(); i++) {
						_tipics.add(array.getString(i));
					}
					data.clear();
					data.addAll(_tipics);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("debug", "TopicList-->parse error!");
					toast("哎呀,出错了！");
					
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Log.e("debug", "TopicList-->get newfeedlist error:" + arg0);
				toast("哎呀,出错了！");
			}

			@Override
			public void onFinish() {
				swipeRefreshLayout.setRefreshing(false);
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(getContext(), ArticleList.class);
		intent.putExtra("kind", kind);
		intent.putExtra("topic", data.get(position));
		startActivity(intent);
	}

}
