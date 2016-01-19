package io.github.emanual.app.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import io.github.emanual.app.R;
import io.github.emanual.app.api.EmanualAPI;
import io.github.emanual.app.entity.FeedsItemObject;
import io.github.emanual.app.ui.adapter.FeedsListAdapter;

public class FeedsList extends SwipeBackActivity {

    @Bind(R.id.recylerview) RecyclerView recyclerView;

    @Override protected void initData() {
        super.initData();
    }

    @Override protected void initLayout() {
        super.initLayout();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_feeds_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fetchData();
    }

    @Override protected int getContentViewId() {
        return R.layout.acty_feeds_list;
    }

    private void fetchData() {
        EmanualAPI.getFeeds(new AsyncHttpResponseHandler() {
            @Override public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    List<FeedsItemObject> feeds = FeedsItemObject.createFeedsItemObjects(new String(responseBody));
                    recyclerView.setAdapter(new FeedsListAdapter(getContext(), feeds));
                } catch (Exception e) {
                    toast("哎呀,网络异常!");
                }
            }

            @Override public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
