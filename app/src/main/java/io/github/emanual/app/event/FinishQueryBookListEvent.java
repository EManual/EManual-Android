package io.github.emanual.app.event;

import java.util.List;

import io.github.emanual.app.entity.BookJSONObject;

/**
 * 完成查询本地的BookList事件
 * Author: jayin
 * Date: 1/25/16
 */
public class FinishQueryBookListEvent {

    List<BookJSONObject> data;

    public FinishQueryBookListEvent(List<BookJSONObject> data) {
        this.data = data;
    }

    public List<BookJSONObject> getData() {
        return data;
    }

    public void setData(List<BookJSONObject> data) {
        this.data = data;
    }

    @Override public String toString() {
        return "FinishQueryBookListEvent{" +
                "data=" + data +
                '}';
    }
}

