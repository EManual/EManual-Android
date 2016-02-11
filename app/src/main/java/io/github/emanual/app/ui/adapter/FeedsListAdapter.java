package io.github.emanual.app.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import io.github.emanual.app.R;
import io.github.emanual.app.api.RestClient;
import io.github.emanual.app.entity.FeedsItemEntity;
import io.github.emanual.app.ui.event.BookDownloadEndEvent;
import io.github.emanual.app.ui.event.BookDownloadFaildEvent;
import io.github.emanual.app.ui.event.BookDownloadProgressEvent;
import io.github.emanual.app.ui.event.BookDownloadStartEvent;
import io.github.emanual.app.utils.AppPath;
import timber.log.Timber;

/**
 * Author: jayin
 * Date: 1/19/16
 */
public class FeedsListAdapter extends RecyclerView.Adapter<FeedsListAdapter.ViewHolder> {
    Context context;
    List<FeedsItemEntity> data;

    public FeedsListAdapter(Context context, List<FeedsItemEntity> data) {
        this.context = context;
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public List<FeedsItemEntity> getData() {
        return data;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_feedslist, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
        final FeedsItemEntity item = data.get(position);
        holder.tv_name.setText(item.getName_cn());
        holder.iv_icon.setImageURI(Uri.parse(item.getIcon_url()));
        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String tarFileName = item.getUrl().split("/")[item.getUrl().split("/").length-1];
                RestClient.getHttpClient().get(item.getDownloadUrl(), new FileAsyncHttpResponseHandler(new File(AppPath.getDownloadPath(getContext()), tarFileName)) {
                    @Override public void onStart() {
                        super.onStart();
                        EventBus.getDefault().post(new BookDownloadStartEvent());

                    }

                    @Override public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        EventBus.getDefault().post(new BookDownloadFaildEvent(statusCode, headers, throwable));
                    }

                    @Override public void onSuccess(int statusCode, Header[] headers, File file) {
                        Timber.d("下载完成");
                        //发消息通知下载完毕，继续解压
                        EventBus.getDefault().post(new BookDownloadEndEvent(file, item));
                    }

                    @Override public void onFinish() {
                        super.onFinish();
                    }

                    @Override public void onProgress(long bytesWritten, long totalSize) {
                        super.onProgress(bytesWritten, totalSize);

                        EventBus.getDefault().post(new BookDownloadProgressEvent(bytesWritten, totalSize));
                    }
                });
            }
        });
    }

    @Override public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.container) ViewGroup container;
        @Bind(R.id.tv_name) TextView tv_name;
        @Bind(R.id.iv_icon) SimpleDraweeView iv_icon;
        @Bind(R.id.btn_download) View btn_download;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
