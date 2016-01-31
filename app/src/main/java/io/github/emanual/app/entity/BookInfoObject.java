package io.github.emanual.app.entity;

import java.io.Serializable;

/**
 * Book: info.json 参考 https://github.com/EManualResource/feeds-book/blob/gh-pages/feeds/all.json#L2-L16
 * Author: jayin
 * Date: 1/23/16
 */
public class BookInfoObject implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "BookInfoObject{" +
                "name='" + name + '\'' +
                '}';
    }
}
