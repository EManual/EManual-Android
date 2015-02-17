package io.github.emanual.java.app.fragment;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.adapter.NewFeedsAdapter;
import io.github.emanual.java.app.api.NewFeedsAPI;
import io.github.emanual.java.app.api.RestClient;
import io.github.emanual.java.app.entity.NewsFeedsObject;
import io.github.emanual.java.app.ui.Detail;
import io.github.emanual.java.app.utils.EManualUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

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

import com.loopj.android.http.AsyncHttpResponseHandler;

public class NewFeeds extends BaseFragment implements OnRefreshListener,
		OnScrollListener {
	@InjectView(R.id.lv_newfeeds)ListView lv;
	@InjectView(R.id.swipeRefreshLayout)SwipeRefreshLayout swipeRefreshLayout;
	boolean hasMore = true, isloading = false;
	int page = 1, maxPage = 1;
	long last_motify = 0;
	NewFeedsAPI api = new NewFeedsAPI();
	NewFeedsAdapter adapter;
	List<NewsFeedsObject> data = new ArrayList<NewsFeedsObject>();

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
				intent.putExtra("link", String.format(RestClient.URL_NewsFeeds,data.get(position).getPath()));
				intent.putExtra("title", EManualUtils.getNewsFeedsTitle(data.get(position).getRname()));
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
		api.getNewFeeds(1, new MyAsyncHttpResponseHandler(1));
	}
	


	public void onLoadMore() {
		api.getNewFeeds(page+1, new MyAsyncHttpResponseHandler(page+1));
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
	
	class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler{
		private int mPage = 1;
		
		public MyAsyncHttpResponseHandler(int mPage){
			this.mPage = mPage;
		}
		
		@Override public void onStart() {
			swipeRefreshLayout.setRefreshing(true);
		}
		
		@Override public void onSuccess(int statusCode, Header[] headers, byte[] response) {
			if(mPage == 1){
				//refresh
				List<NewsFeedsObject> names = NewsFeedsObject.createNewsFeedsObjects(new String(response));
				data.clear();
				data.addAll(names);
				adapter.notifyDataSetChanged();
				page = mPage;
				hasMore = true;
			}else{
				//loadmore
				List<NewsFeedsObject> names = NewsFeedsObject.createNewsFeedsObjects(new String(response));
				data.addAll(names);
				adapter.notifyDataSetChanged();
				page = mPage;
			}
		}
		
		@Override public void onFailure(int statusCode, Header[] header, byte[] response,
				Throwable arg3) {
			if(statusCode == 404){
				hasMore = false;
				toast("没有更多了");
			}else{
				Log.e("debug", "NewFeeds-->get newfeedlist error:" + statusCode);
				toast("哎呀,出错了！");
			}
		}
		@Override public void onFinish() {
			swipeRefreshLayout.setRefreshing(false);
			isloading = false;
		}
	}
	
}
