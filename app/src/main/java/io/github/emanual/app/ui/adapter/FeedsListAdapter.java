package io.github.emanual.app.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.FeedsItemObject;

/**
 * Author: jayin
 * Date: 1/19/16
 */
public class FeedsListAdapter extends RecyclerView.Adapter<FeedsListAdapter.ViewHolder> {
    Context context;
    List<FeedsItemObject> data;

    public FeedsListAdapter(Context context, List<FeedsItemObject> data) {
        this.context = context;
        this.data = data;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_feedslist, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
        FeedsItemObject item = data.get(position);
        holder.tv_name.setText(item.getName());
        holder.iv_icon.setImageURI(Uri.parse(item.getIcon_url()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(FeedsListAdapter.this.context, position+"", Toast.LENGTH_SHORT).show();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
