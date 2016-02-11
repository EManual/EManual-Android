package io.github.emanual.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.emanual.app.R;
import io.github.emanual.app.utils.ParseUtils;

public class ArticleListAdapter extends BaseAdapter {
    List<String> data;
    Context context;

    public ArticleListAdapter(Context context, List<String> data) {
        this.data = data;
        this.context = context;
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
                    R.layout.adapter_articlelist, null);
            h = new ViewHolder(convertView);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();

        }
        h.title.setText(ParseUtils.getArticleName(data.get(position)));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
