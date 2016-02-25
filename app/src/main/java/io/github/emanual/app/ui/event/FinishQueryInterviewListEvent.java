package io.github.emanual.app.ui.event;

import java.util.List;

import io.github.emanual.app.entity.InterviewJSONEntity;

/**
 * Author: jayin
 * Date: 2/25/16
 */
public class FinishQueryInterviewListEvent extends BaseEvent {
    List<InterviewJSONEntity> data;

    public FinishQueryInterviewListEvent(List<InterviewJSONEntity> data) {
        this.data = data;
    }

    public List<InterviewJSONEntity> getData() {
        return data;
    }

    public void setData(List<InterviewJSONEntity> data) {
        this.data = data;
    }

    @Override public String toString() {
        return "FinishQueryInterviewListEvent{" +
                "data=" + data +
                '}';
    }
}
