package io.github.emanual.app.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: jayin
 * Date: 1/23/16
 */
public class BookResource {

    public static List<String> getBookList(Context context){
        List<String> result = new ArrayList<String>();
        File bookDir = new File(AppPath.getBookPath(context));
        if(!bookDir.exists()){
            bookDir.mkdirs();
        }
        result.addAll(Arrays.asList(bookDir.list()));
        return result;
    }
}
