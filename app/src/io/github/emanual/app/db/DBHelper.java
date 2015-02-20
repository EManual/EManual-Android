package io.github.emanual.app.db;

import io.github.emanual.app.entity.Article;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context) {
		super(context, DB_Name, null, version);

	}

	private static final String DB_Name = "manual_java.db";
	private static final int version = 1;

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "
				+ Article.class.getSimpleName()
				+ "(title varchar(32),url varchar(64) unique,saveTime long,content varchar(4096),isFavourite integer)";
		Log.d("debug", "exec SQL--> " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
