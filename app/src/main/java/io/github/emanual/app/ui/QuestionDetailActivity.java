package io.github.emanual.app.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import io.github.emanual.app.R;
import io.github.emanual.app.ui.base.activity.SwipeBackActivity;
import io.github.emanual.app.ui.fragment.InterviewDetailFragment;

public class QuestionDetailActivity extends SwipeBackActivity {

    @Override protected void initData(Bundle savedInstanceState) {

    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_interview_detail);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new InterviewDetailFragment());
    }

    @Override protected int getContentViewId() {
        return R.layout.acty_question_detail;
    }

}
