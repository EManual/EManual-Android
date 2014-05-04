package io.github.emanual.java.app.adapter;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.entity.Article;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FavouriteListAdapter extends BaseAdapter {

	List<Article> data;
	Context context;
	List<Boolean> selected;

	public FavouriteListAdapter(Context context, List<Article> data,List<Boolean> selected) {
		this.data = data;
		this.context = context;
		this.selected  =selected;
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
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_favouritelist, null);
			h.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			h.iv_checked = (ImageView) convertView
					.findViewById(R.id.iv_checked);
			convertView.setTag(h);

		} else {
			h = (ViewHolder) convertView.getTag();
		}
		h.tv_title.setText(data.get(position).getTitle());
		h.iv_checked.setVisibility(selected.get(position)?View.VISIBLE:View.INVISIBLE);
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		ImageView iv_checked;

	}

}
