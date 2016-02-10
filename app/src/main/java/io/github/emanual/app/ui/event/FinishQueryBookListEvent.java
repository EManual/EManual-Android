package io.github.emanual.app.ui.event;

import java.util.List;

import io.github.emanual.app.entity.BookJSONEntity;

/**
 * 完成查询本地的BookList事件
 * Author: jayin
 * Date: 1/25/16
 */
public class FinishQueryBookListEvent extends BaseEvent {

    List<BookJSONEntity> data;

    public FinishQueryBookListEvent(List<BookJSONEntity> data) {
        this.data = data;
    }

    public List<BookJSONEntity> getData() {
        return data;
    }

    public void setData(List<BookJSONEntity> data) {
        this.data = data;
    }

    @Override public String toString() {
        return "FinishQueryBookListEvent{" +
                "data=" + data +
                '}';
    }
}

