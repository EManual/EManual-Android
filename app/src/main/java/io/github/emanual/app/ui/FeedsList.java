package io.github.emanual.app.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import io.github.emanual.app.R;
import io.github.emanual.app.api.EmanualAPI;
import io.github.emanual.app.entity.FeedsItemObject;
import io.github.emanual.app.event.BookDownloadedEvent;
import io.github.emanual.app.event.UnPackFinishEvent;
import io.github.emanual.app.ui.adapter.FeedsListAdapter;
import io.github.emanual.app.utils.AppPath;
import io.github.emanual.app.utils.ZipUtils;

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

    @Subscribe(threadMode = ThreadMode.Async)
    public void onBookDownloaded(BookDownloadedEvent event) {
        try {
            ZipUtils.unZipFiles(event.getFile().getAbsolutePath(), AppPath.getBookPath(getContext()) + File.separator + event.getFeedsItemObject().getName() + File.separator);
            //删除压缩包
            if (event.getFile().exists()) {
                event.getFile().delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new UnPackFinishEvent(e));
            return;

        }
        EventBus.getDefault().post(new UnPackFinishEvent(null));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnpackFinishEvent(UnPackFinishEvent event) {
        if (event.getException() != null) {
            Toast.makeText(getContext(), event.getException().getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "下载并解压成功", Toast.LENGTH_SHORT).show();
    }
}
