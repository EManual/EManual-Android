package io.github.emanual.app.event;

import java.util.List;

import io.github.emanual.app.entity.BookInfoObject;

/**
 * Author: jayin
 * Date: 1/25/16
 */
public class FinishQueryBookListEvent {

    List<BookInfoObject> bookInfoObjectList;

    public FinishQueryBookListEvent(List<BookInfoObject> bookInfoObjectList) {
        this.bookInfoObjectList = bookInfoObjectList;
    }

    public List<BookInfoObject> getBookInfoObjectList() {
        return bookInfoObjectList;
    }

    public void setBookInfoObjectList(List<BookInfoObject> bookInfoObjectList) {
        this.bookInfoObjectList = bookInfoObjectList;
    }

    @Override public String toString() {
        return "FinishQueryBookListEvent{" +
                "bookInfoObjectList=" + bookInfoObjectList +
                '}';
    }
}

