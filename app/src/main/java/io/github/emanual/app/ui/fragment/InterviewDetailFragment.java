package io.github.emanual.app.ui.fragment;

import android.os.Bundle;

import io.github.emanual.app.R;
import io.github.emanual.app.entity.InterviewJSONEntity;
import io.github.emanual.app.ui.base.fragment.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class InterviewDetailFragment extends BaseFragment {

    public static final String EXTRA_INTERVIEW = "EXTRA_INTERVIEW";
    public static final String EXTRA_INDEX_PAGE = "INDEX_PAGE";

    InterviewJSONEntity interviewJSONEntity;
    int indexPage = 0;

    public InterviewDetailFragment newInstance(InterviewJSONEntity interviewJSONEntity, int index) {
        InterviewDetailFragment fragment = new InterviewDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(InterviewDetailFragment.EXTRA_INTERVIEW, interviewJSONEntity);
        args.putInt(InterviewDetailFragment.EXTRA_INDEX_PAGE, index);
        fragment.setArguments(args);
        return fragment;
    }


    @Override protected void initData(Bundle savedInstanceState) {
        interviewJSONEntity = (InterviewJSONEntity) getArguments().getSerializable(EXTRA_INTERVIEW);
        indexPage = getArguments().getInt(EXTRA_INDEX_PAGE);

    }

    @Override protected void initLayout(Bundle savedInstanceState) {

    }

    @Override protected int getContentViewId() {
        return R.layout.fragment_interview_detail;
    }

    //load question
    //render
    //do action - feed back :1. show the answer at once
    //do action - next : note: NO NEXT
    //do action - pre  : not : NO Preview



}
