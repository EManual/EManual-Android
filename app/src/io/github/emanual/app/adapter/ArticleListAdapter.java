package io.github.emanual.app.adapter;

import io.github.emanual.app.R;
import io.github.emanual.app.utils.ParseUtils;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArticleListAdapter extends BaseAdapter {
	List<String> data;
	Context context;
	public ArticleListAdapter(Context context,List<String> data){
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
		ViewHolder h =  null;
		if(convertView == null){
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_articlelist, null);
			h.title = (TextView)convertView.findViewById(R.id.tv_title);
			convertView.setTag(h);
		}else{
			h =(ViewHolder)convertView.getTag();
			
		}
		Log.i("debug",data.get(position) );
		h.title.setText(ParseUtils.getArticleName(data.get(position)));
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
	}

}
