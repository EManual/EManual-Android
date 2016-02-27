package io.github.emanual.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.QuestionEntity;

/**
 * Author: jayin
 * Date: 2/27/16
 */
public class QuestionListAdapter extends  RecyclerView.Adapter<QuestionListAdapter.ViewHolder> {
    Context context;
    List<QuestionEntity> data;

    public QuestionListAdapter(Context context, List<QuestionEntity> data){
        this.context = context;
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public List<QuestionEntity> getData() {
        return data;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_questionlist, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        final QuestionEntity item = data.get(position);
        holder.tv_name.setText(item.getDescription());
        holder.layout_container.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(getContext(), item.getDescription(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext(), QuestionListActivity.class);
//                intent.putExtra(QuestionDetailActivity.EXTRA_INTERVIEW, item);
//                intent.putExtra(QuestionDetailActivity.EXTRA_INTERVIEW, item);
//                getContext().startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.layout_container) View layout_container;
        @Bind(R.id.tv_name) TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
