package io.github.emanual.app.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.BookJSONEntity;
import io.github.emanual.app.ui.adapter.BookListAdapter;
import io.github.emanual.app.ui.base.fragment.BaseFragment;
import io.github.emanual.app.ui.event.FinishQueryBookListEvent;
import io.github.emanual.app.ui.event.QueryBookListEvent;
import io.github.emanual.app.ui.event.UnPackFinishEvent;
import io.github.emanual.app.utils.BookResource;
import timber.log.Timber;


public class BookList extends BaseFragment {
    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    public static BookList newInstance(String param1, String param2) {
        BookList fragment = new BookList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override protected void initData(Bundle savedInstanceState) {

    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EventBus.getDefault().post(new QueryBookListEvent());
    }

    @Override protected int getContentViewId() {
        return R.layout.fragment_book_list;
    }

    /**
     * 新书下载解压完成后更新书本列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnPackFinish(UnPackFinishEvent event){
        EventBus.getDefault().post(new QueryBookListEvent());
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void onQueryBookList(QueryBookListEvent event) {
        List<BookJSONEntity> bookJSONEntities = BookResource.getBookJSONList(getContext());
        EventBus.getDefault().post(new FinishQueryBookListEvent(bookJSONEntities));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onFinishQueryBookList(FinishQueryBookListEvent event) {
        Timber.d(event.getData().toString());
        recyclerView.setAdapter(new BookListAdapter(getContext(), event.getData()));
    }
}
