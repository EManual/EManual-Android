package io.github.emanual.app.utils;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

public class MyDBManager {

	public static DbUtils getDBUtils(Context context){
		return DbUtils.create(context, "manual_java.db");
	}
}
