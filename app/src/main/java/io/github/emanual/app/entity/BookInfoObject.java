package io.github.emanual.app.entity;

import java.io.Serializable;

/**
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
