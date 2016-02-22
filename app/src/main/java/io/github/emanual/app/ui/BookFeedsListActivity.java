package io.github.emanual.app.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import io.github.emanual.app.entity.FeedsItemEntity;
import io.github.emanual.app.ui.adapter.FeedsListAdapter;
import io.github.emanual.app.ui.base.activity.SwipeRefreshActivity;
import io.github.emanual.app.ui.event.BookDownloadEndEvent;
import io.github.emanual.app.ui.event.BookDownloadFaildEvent;
import io.github.emanual.app.ui.event.BookDownloadProgressEvent;
import io.github.emanual.app.ui.event.BookDownloadStartEvent;
import io.github.emanual.app.ui.event.UnPackFinishEvent;
import io.github.emanual.app.utils.AppPath;
import io.github.emanual.app.utils.SwipeRefreshLayoutUtils;
import io.github.emanual.app.utils.ZipUtils;
import timber.log.Timber;

/**
 * Book Feeds
 */
public class BookFeedsListActivity extends SwipeRefreshActivity {

    ProgressDialog mProgressDialog;

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Override protected void initData(Bundle savedInstanceState) {
    }

    @Override protected void initLayout(Bundle savedInstanceState) {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_feeds_list);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchData();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onBookDownloadStart(BookDownloadStartEvent event) {
        mProgressDialog.setTitle("正在下载..");
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        mProgressDialog.show();

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onBookDownloadProgress(BookDownloadProgressEvent event) {

        Timber.d(event.getBytesWritten() + "/" + event.getTotalSize());
        mProgressDialog.setMessage(String.format("大小:%.2f M", 1.0 * event.getTotalSize() / 1024 / 1024));
        mProgressDialog.setMax((int) event.getTotalSize());
        mProgressDialog.setProgress((int) event.getBytesWritten());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onBookdownloadFaild(BookDownloadFaildEvent event) {
        Toast.makeText(getContext(), "出错了，错误码：" + event.getStatusCode(), Toast.LENGTH_LONG).show();
        mProgressDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onBookdownloadEnd2(BookDownloadEndEvent event) {
        mProgressDialog.setTitle("正在解压..");
    }

    /**
     * 下载完毕
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.Async)
    public void onBookDownloadEnd(BookDownloadEndEvent event) {
        try {
            ZipUtils.unZipFiles(event.getFile().getAbsolutePath(), AppPath.getBooksPath(getContext()) + File.separator + event.getFeedsItemEntity().getName() + File.separator);
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
            toast(event.getException().getMessage());
            return;
        }
        toast("下载并解压成功");
        mProgressDialog.dismiss();
    }


    @Override protected int getContentViewId() {
        return R.layout.acty_feeds_list;
    }

    private void fetchData() {
        EmanualAPI.getBookFeeds(new AsyncHttpResponseHandler() {
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


    @Override public void onRefresh() {
        fetchData();
    }
}
