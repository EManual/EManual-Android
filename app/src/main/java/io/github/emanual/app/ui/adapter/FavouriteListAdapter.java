package io.github.emanual.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.Article;

public class FavouriteListAdapter extends BaseAdapter {

    List<Article> data;
    Context context;
    List<Boolean> selected;

    public FavouriteListAdapter(Context context, List<Article> data, List<Boolean> selected) {
        this.data = data;
        this.context = context;
        this.selected = selected;
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_favouritelist, null);
            h = new ViewHolder(convertView);
            convertView.setTag(h);

        } else {
            h = (ViewHolder) convertView.getTag();
        }
        h.tv_title.setText(data.get(position).getTitle());
        h.iv_checked.setVisibility(selected.get(position) ? View.VISIBLE : View.INVISIBLE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_title) TextView tv_title;
        @Bind(R.id.iv_checked) ImageView iv_checked;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
