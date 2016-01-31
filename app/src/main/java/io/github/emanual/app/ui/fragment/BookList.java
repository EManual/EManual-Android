package io.github.emanual.app.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.BookInfoObject;
import io.github.emanual.app.event.FinishQueryBookListEvent;
import io.github.emanual.app.event.QueryBookListEvent;
import io.github.emanual.app.ui.adapter.BookListAdapter;
import io.github.emanual.app.utils.BookResource;


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

    @Subscribe(threadMode = ThreadMode.Async)
    public void onQueryBookList(QueryBookListEvent event) {
        List<String> bookNameList = BookResource.getBookNameList(getContext());
        List<BookInfoObject> bookInfoObjectList = new ArrayList<>();
        for (int i=0; i < bookNameList.size(); i++) {
            BookInfoObject bookInfoObject = new BookInfoObject();
            bookInfoObject.setName(bookNameList.get(i));
            bookInfoObjectList.add(bookInfoObject);

        }
        EventBus.getDefault().post(new FinishQueryBookListEvent(bookInfoObjectList));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onFinishQueryBookList(FinishQueryBookListEvent event) {
        Log.d("debug", event.getBookInfoObjectList().toString());
        recyclerView.setAdapter(new BookListAdapter(getContext(), event.getBookInfoObjectList()));
    }
}
