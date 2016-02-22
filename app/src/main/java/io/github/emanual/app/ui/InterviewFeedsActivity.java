package io.github.emanual.app.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import io.github.emanual.app.R;
import io.github.emanual.app.api.EmanualAPI;
import io.github.emanual.app.entity.FeedsItemEntity;
import io.github.emanual.app.ui.adapter.FeedsListAdapter;
import io.github.emanual.app.ui.base.activity.SwipeRefreshActivity;
import io.github.emanual.app.utils.SwipeRefreshLayoutUtils;
import timber.log.Timber;

public class InterviewFeedsActivity extends SwipeRefreshActivity {

    ProgressDialog mProgressDialog;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Override protected void initData(Bundle savedInstanceState) {

    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_feeds_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchData();
    }

    @Override protected int getContentViewId() {
        return R.layout.acty_interview_feeds;
    }

    @Override public void onRefresh() {
//        EmanualAPI.getIn
        fetchData();
    }

    private void fetchData() {
        EmanualAPI.getInterviewFeeds(new AsyncHttpResponseHandler() {
            @Override public void onStart() {
                super.onStart();
                SwipeRefreshLayoutUtils.setRefreshing(getSwipeRefreshLayout(), true);
            }

            @Override public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    List<FeedsItemEntity> feeds = FeedsItemEntity.createByJSONArray(new String(responseBody), FeedsItemEntity.class);
                    Timber.d(feeds.toString());
                    recyclerView.setAdapter(new FeedsListAdapter(getContext(), feeds));
                } catch (Exception e) {
                    toast("哎呀,网络异常!");
                }
            }

            @Override public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }

            @Override public void onFinish() {
                super.onFinish();
                SwipeRefreshLayoutUtils.setRefreshing(getSwipeRefreshLayout(), false);
            }

            @Override public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);

            }
        });
    }

}
