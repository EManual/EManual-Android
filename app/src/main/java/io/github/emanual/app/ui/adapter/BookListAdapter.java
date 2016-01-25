package io.github.emanual.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.BookInfoObject;

/**
 * Author: jayin
 * Date: 1/24/16
 */
public class BookListAdapter extends  RecyclerView.Adapter<BookListAdapter.ViewHolder>{
    Context context;
    List<BookInfoObject> data;

    public BookListAdapter(Context context, List<BookInfoObject> bookInfoObjectList){
        this.context = context;
        this.data = bookInfoObjectList;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_booklist, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        final BookInfoObject item = data.get(position);
        holder.tv_name.setText(item.getName());
    }

    @Override public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name) TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
