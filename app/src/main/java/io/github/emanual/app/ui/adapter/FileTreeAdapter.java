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
import io.github.emanual.app.entity.FileTreeObject;
import io.github.emanual.app.utils.EManualUtils;

public class FileTreeAdapter extends BaseAdapter {
    List<FileTreeObject> data;
    Context context;

    public FileTreeAdapter(Context context, List<FileTreeObject> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_filetree, null);
            h = new ViewHolder(convertView);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }
        FileTreeObject item = data.get(position);
        h.title.setText(EManualUtils.getFileNameWithouExtAndNumber(item.getRname()));
        if (item.getMode().equals(FileTreeObject.MODE_FILE)) {
            h.icon.setBackgroundResource(R.drawable.ic_icon_file);
        } else {
            h.icon.setBackgroundResource(R.drawable.ic_icon_document);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_title) TextView title;
        @Bind(R.id.iv_icon) ImageView icon;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
