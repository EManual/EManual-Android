package io.github.emanual.app.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.emanual.app.entity.BookJSONObject;

/**
 * Author: jayin
 * Date: 1/23/16
 */
public class BookResource {
    /**
     * 获取所有books/<*bookName>
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
     * 获取所有books/<bookName>/book.json
     * @param context
     * @return
     */
    public static List<BookJSONObject> getBookJSONList(Context context) {
        List<BookJSONObject> books = new ArrayList<>();
        File bookDir = new File(AppPath.getBooksPath(context));
        if (!bookDir.exists()) {
            bookDir.mkdirs();
        }
        for (String bookName : bookDir.list()) {
            String json = _.readFile(AppPath.getBookJSONFilePath(context, bookName));
            BookJSONObject bookJSONObject = BookJSONObject.createByJSON(json, BookJSONObject.class);
            books.add(bookJSONObject);

        }
        return books;
    }
}
