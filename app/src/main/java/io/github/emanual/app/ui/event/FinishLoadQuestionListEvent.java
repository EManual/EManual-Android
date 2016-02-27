package io.github.emanual.app.ui.event;

import java.util.List;

import io.github.emanual.app.entity.QuestionEntity;

/**
 * Author: jayin
 * Date: 2/27/16
 */
public class FinishLoadQuestionListEvent extends BaseEvent {

    private List<QuestionEntity> questionEntityList;

    public FinishLoadQuestionListEvent(List<QuestionEntity> data) {
        this.questionEntityList = data;
    }

    public List<QuestionEntity> getQuestionEntityList() {
        return questionEntityList;
    }
}
