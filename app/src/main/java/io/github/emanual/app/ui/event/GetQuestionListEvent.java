package io.github.emanual.app.ui.event;

import io.github.emanual.app.entity.BaseEntity;

/**
 * 获取问题列表
 * Author: jayin
 * Date: 2/27/16
 */
public class GetQuestionListEvent extends BaseEntity {

    private String interviewName;

    public GetQuestionListEvent(String interviewName) {
        this.interviewName = interviewName;
    }

    public String getInterviewName() {
        return interviewName;
    }
}
