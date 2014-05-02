package io.github.emanual.app.utils;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;

public class MyDBManager {

	public static DbUtils getDBUtils(Context context){
		return DbUtils.create(context, "manual_java.db").configDebug(true);
	}
}
