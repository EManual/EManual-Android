package io.github.emanual.java.app.adapter;

import io.github.emanual.java.app.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewFeedsAdapter extends BaseAdapter {
	List<String> data;
    Context context;
	public NewFeedsAdapter(Context context,List<String> data){
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
		if(convertView==null){
			h  = new ViewHolder();
			convertView  = LayoutInflater.from(context).inflate(R.layout.adapter_newfeeds, null);
			h.title  = (TextView)convertView.findViewById(R.id.tv_title);
			h.time  = (TextView)convertView.findViewById(R.id.tv_time);
			convertView.setTag(h);
		}else{
			h = (ViewHolder)convertView.getTag();
		}
		String filename= data.get(position).split("\\.")[0];
		String[] s = filename.split("-");
		String time = s[0]+"-"+s[1]+"-"+s[2];
		String title = s[3];
		h.title.setText(title);
		h.time.setText(time);
		return convertView;
	}
	
	class ViewHolder{
		TextView title,time;
	}
}