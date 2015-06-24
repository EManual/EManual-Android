package io.github.emanual.app.ui.fragment;

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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.emanual.app.R;
import io.github.emanual.app.api.NewFeedsAPI;
import io.github.emanual.app.api.RestClient;
import io.github.emanual.app.entity.NewsFeedsObject;
import io.github.emanual.app.ui.Detail;
import io.github.emanual.app.ui.adapter.NewFeedsAdapter;
import io.github.emanual.app.utils.EManualUtils;
import io.github.emanual.app.utils.SwipeRefreshLayoutUtils;
import io.github.emanual.app.utils.UmengAnalytics;

public class NewFeeds extends BaseFragment implements OnRefreshListener {
    @InjectView(R.id.lv_newfeeds) ListView lv;
    @InjectView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    boolean hasMore = true;
    int page = 1, maxPage = 1;
    long last_motify = 0;
    NewFeedsAPI api = new NewFeedsAPI();
    NewFeedsAdapter adapter;
    ArrayList<NewsFeedsObject> data = new ArrayList<NewsFeedsObject>();

    @SuppressWarnings("unchecked") @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newfeeds, container, false);
        ButterKnife.inject(this, v);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
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
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String link = String.format(RestClient.URL_NewsFeeds, data.get(position).getPath());
                String title = EManualUtils.getNewsFeedsTitle(data.get(position).getRname());

                onUmengAnalytics(title);

                Intent intent = new Intent(getActivity(), Detail.class);
                intent.putExtra(Detail.EXTRA_LINK, link);
                intent.putExtra(Detail.EXTRA_TITLE, title);
                intent.putExtra(Detail.EXTRA_SHARE_PATH, EManualUtils.genSharePath(data.get(position).getPath()));
                intent.putExtra(Detail.EXTRA_FEEDBACK_CONTENT, String.format(Detail.FEEDBACK_CONTENT_TPL, title, "/新鲜事/" + title));
                startActivity(intent);
            }
        });

        if (savedInstanceState != null) {
            ArrayList<NewsFeedsObject> save_data = (ArrayList<NewsFeedsObject>) savedInstanceState.getSerializable("data");
            this.data.addAll(save_data);
        }
        if (data.size() == 0) {
            onRefresh();
        }
        return v;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", this.data);
    }

    @Override
    public void onRefresh() {
        api.getNewFeeds(1, new MyAsyncHttpResponseHandler(1));
    }


    public void onLoadMore() {
        api.getNewFeeds(page + 1, new MyAsyncHttpResponseHandler(page + 1));
    }

    //统计看了啥文章
    public void onUmengAnalytics(String title) {
        Map<String, String> m = new HashMap<String, String>();
        m.put("title", title);

        MobclickAgent.onEventValue(getActivity(), UmengAnalytics.ID_EVENT_VIEW_NEWSFEEDS, m, UmengAnalytics.DEAFULT_DURATION);
    }

    class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
        private int mPage = 1;

        public MyAsyncHttpResponseHandler(int mPage) {
            this.mPage = mPage;
        }

        @Override public void onStart() {
            SwipeRefreshLayoutUtils.setRefreshing(swipeRefreshLayout, true);
        }

        @Override public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            try{
                List<NewsFeedsObject> names = NewsFeedsObject.createNewsFeedsObjects(new String(response));
                if (mPage == 1) {
                    //refresh
                    data.clear();
                    data.addAll(names);
                    adapter.notifyDataSetChanged();
                    page = mPage;
                    hasMore = true;
                } else {
                    //loadmore
                    data.addAll(names);
                    adapter.notifyDataSetChanged();
                    page = mPage;
                }
            }catch (Exception e){
                toast("哎呀,网络异常!");
            }
        }

        @Override public void onFailure(int statusCode, Header[] header, byte[] response,
                                        Throwable arg3) {
            if (statusCode == 404) {
                hasMore = false;
                toast("没有更多了");
            } else {
                Log.e("debug", "NewFeeds-->get newfeedlist error:" + statusCode);
                toast("哎呀,出错了！");
            }
        }

        @Override public void onFinish() {
            SwipeRefreshLayoutUtils.setRefreshing(swipeRefreshLayout, false);
        }
    }
}
