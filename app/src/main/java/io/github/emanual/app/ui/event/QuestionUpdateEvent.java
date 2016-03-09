package io.github.emanual.app.ui.event;

import io.github.emanual.app.entity.QuestionEntity;

/**
 * Author: jayin
 * Date: 3/9/16
 */
public class QuestionUpdateEvent extends BaseEvent{
    private QuestionEntity questionEntity;

    public QuestionUpdateEvent(QuestionEntity questionEntity){
        this.questionEntity = questionEntity;
    }

    public QuestionEntity getQuestionEntity() {
        return questionEntity;
    }

    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }
}
