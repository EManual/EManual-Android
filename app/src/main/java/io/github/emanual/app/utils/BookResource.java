package io.github.emanual.app.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.emanual.app.entity.BookJSONEntity;

/**
 * Author: jayin
 * Date: 1/23/16
 */
public class BookResource {
    /**
     * 获取所有books/* 所有文件名(英文书名)列表
     * @param context
     * @return
     */
    public static List<String> getBookNameList(Context context) {
        List<String> books = new ArrayList<>();
        File bookDir = new File(AppPath.getBooksPath(context));
        if (!bookDir.exists()) {
            bookDir.mkdirs();
        }
        books.addAll(Arrays.asList(bookDir.list()));
        return books;
    }

    /**
     * 获取所有books/<bookName>/book/book.json
     * @param context
     * @return
     */
    public static List<BookJSONEntity> getBookJSONList(Context context) {
        List<BookJSONEntity> books = new ArrayList<>();
        File bookDir = new File(AppPath.getBooksPath(context));
        if (!bookDir.exists()) {
            bookDir.mkdirs();
        }
        for (String bookName : bookDir.list()) {
            String json = _.readFile(AppPath.getBookJSONFilePath(context, bookName));
            BookJSONEntity bookJSONEntity = BookJSONEntity.createByJSON(json, BookJSONEntity.class);
            books.add(bookJSONEntity);

        }
        return books;
    }
}
