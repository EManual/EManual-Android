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
     * 获取文档目录,/data/data/<App Name>/files/book
     * @param context
     * @return
     */
    public static String getBookPath(Context context){
        return getAppFilesPath(context) + File.separator + "book";
    }
}
