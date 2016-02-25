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
import io.github.emanual.app.entity.InterviewJSONEntity;
import io.github.emanual.app.ui.adapter.InterviewListAdapter;
import io.github.emanual.app.ui.base.fragment.BaseFragment;
import io.github.emanual.app.ui.event.FinishQueryInterviewListEvent;
import io.github.emanual.app.ui.event.QueryInterviewListEvent;
import io.github.emanual.app.ui.event.UnPackFinishEvent;
import io.github.emanual.app.utils.InterviewResource;
import timber.log.Timber;

/**
 * 面试笔试列表
 * Author: jayin
 * Date: 2/24/16
 */
public class InterviewListFragment extends BaseFragment{

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Override protected void initData(Bundle savedInstanceState) {

    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EventBus.getDefault().post(new QueryInterviewListEvent());
    }

    @Override protected int getContentViewId() {
        return R.layout.fragment_interview_list;
    }

    /**
     * 新书下载解压完成后更新书本列表
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnPackFinish(UnPackFinishEvent event){
        EventBus.getDefault().post(new QueryInterviewListEvent());
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void onQueryInterviewList(QueryInterviewListEvent event) {
        List<InterviewJSONEntity> interviewJSONEntities = InterviewResource.getInterviewJSONList(getContext());
        EventBus.getDefault().post(new FinishQueryInterviewListEvent(interviewJSONEntities));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onFinishQueryInterviewList(FinishQueryInterviewListEvent event) {
        Timber.d(event.getData().toString());
        recyclerView.setAdapter(new InterviewListAdapter(getContext(), event.getData()));
    }

}
