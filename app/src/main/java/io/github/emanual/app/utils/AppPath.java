package io.github.emanual.app.utils;

import android.content.Context;

import java.io.File;

/**
 * Author: jayin
 * Date: 1/20/16
 */
public class AppPath {
    /**
     * 获取应用files/ 目录：/data/data/<App Name>/files
     * @param context
     * @return
     */
    public static String getAppFilesPath(Context context){
        return context.getFilesDir().getAbsolutePath();
    }
    /**
     * 获取文档下载目录,/data/data/<App Name>/files/download
     * @param context
     * @return
     */
    public static String getDownloadPath(Context context){
        return getAppFilesPath(context) + File.separator + "download";
    }
    /**
     * 获取文档目录,/data/data/<App Name>/files/books
     * @param context
     * @return
     */
    public static String getBooksPath(Context context){
        return getAppFilesPath(context) + File.separator + "books";
    }

    /**
     * 获取教程的根目录
     * @param context
     * @param bookName 书名
     * @return
     */
    public static String getBookPath(Context context, String bookName){
        return getBooksPath(context) + File.separator + bookName;
    }

    /**
     * 获取教程的入口文件(index.html)
     * @param context
     * @param bookName 书名
     * @return
     */
    public static String getBookIndexURL(Context context, String bookName){
        return "file:///"+getBookPath(context, bookName) + File.separator + "book" + File.separator + "index.html";
    }
}
