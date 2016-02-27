package io.github.emanual.app.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.InterviewJSONEntity;
import io.github.emanual.app.entity.QuestionEntity;
import io.github.emanual.app.ui.adapter.QuestionListAdapter;
import io.github.emanual.app.ui.base.activity.SwipeBackActivity;
import io.github.emanual.app.ui.event.FinishLoadQuestionListEvent;
import io.github.emanual.app.ui.event.GetQuestionListEvent;
import io.github.emanual.app.utils.InterviewResource;
import timber.log.Timber;

public class QuestionListActivity extends SwipeBackActivity {
    public static final String EXTRA_INTERVIEW = "EXTRA_INTERVIEW";
    public static final String EXTRA_INDEX_PAGE = "INDEX_PAGE";

    InterviewJSONEntity interviewJSONEntity;
    ProgressDialog mProgressDialog;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    @Override protected void initData(Bundle savedInstanceState) {
        interviewJSONEntity = (InterviewJSONEntity)getIntent().getSerializableExtra(EXTRA_INTERVIEW);
        if(interviewJSONEntity == null){
            finish();
        }
    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        if(isFinishing()){
            return;
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_question_list);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EventBus.getDefault().post(new GetQuestionListEvent(interviewJSONEntity.getInfo().getName()));
    }

    @Override protected int getContentViewId() {
        return R.layout.acty_question_list;
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void onGetQuestionsList(GetQuestionListEvent event){
        List<QuestionEntity> data = InterviewResource.getQuestionList(getContext(), event.getInterviewName());
        EventBus.getDefault().post(new FinishLoadQuestionListEvent(data));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onFinishLoadQuestionList(FinishLoadQuestionListEvent event){
        recyclerView.setAdapter(new QuestionListAdapter(getContext(), event.getQuestionEntityList()));
    }
}
