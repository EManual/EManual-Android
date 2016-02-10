package io.github.emanual.app.entity;

import java.io.Serializable;

/**
 * 合并后的book.json
 * 实体类详见：https://github.com/EManualResource/book-docker-practice/blob/gh-pages/dist/book/book.json
 * Author: jayin
 * Date: 1/31/16
 */
public class BookJSONEntity extends BaseEntity implements Serializable {
    private String title;
    private BookInfoEntity info;

    public BookInfoEntity getInfo() {
        return info;
    }

    public void setInfo(BookInfoEntity info) {
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
