package io.github.emanual.app.db;

import io.github.emanual.app.entity.Article;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 文章操作封装，
 * 其实主键就是url,唯一的
 * @author Jayin Ton
 *
 */
public class ArticleDAO {
	DBHelper helper;
	String table = Article.class.getSimpleName();

	public ArticleDAO(Context context) {
		helper = new DBHelper(context);
	}
     
	/**
	 * 插入
	 * @param a
	 * @return
	 */
	public boolean insert(Article a) {
		SQLiteDatabase db = null;
		long count = 0;
		try {
			db = helper.getWritableDatabase();
			count = db.insert(
					table,
					null,
					getCV(a.getTitle(), a.getUrl(), a.getSaveTime(),
							a.getContent(), a.getIsFavourite()));
		} finally {
			if (db != null)
				db.close();
		}
		return count != -1;
	}
     /**
      * 根据url删除
      * @param url
      * @return
      */
	public boolean delete(String url) {
		SQLiteDatabase db = null;
		long count = 0;
		try {
			db = helper.getWritableDatabase();
			String whereClause = "url = ?";
			String[] whereArgs = new String[] { url };
			count = db.delete(table, whereClause, whereArgs);
		} finally {
			if (db != null)
				db.close();
		}
		return count != 0;
	}
	
    /**
     * 删除全部
     * @return
     */
	public boolean deleteAll() {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(table, null, null);
		} finally {
			if (db != null)
				db.close();
		}
		return true;
	}
    /**
     * 删除收藏列表<br>
     * ps:不是真删除,只是标志位非收藏
     * @return
     */
	public boolean deleteAllFavourite() {
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String whereClause = "isFavourite = ?";
			String[] whereArgs = new String[] { "1" };
			ContentValues cv = new ContentValues();
			cv.put("isFavourite", 0);
			db.update(table, cv, whereClause, whereArgs);
		} finally {
			if (db != null)
				db.close();
		}
		return true;
	}
   /**
    * 
    * @param a
    * @return
    */
	public boolean update(Article a) {
		SQLiteDatabase db = null;
		long count = 0;
		try {
			db = helper.getWritableDatabase();
			ContentValues cv = getCV(a.getTitle(), a.getUrl(), a.getSaveTime(),
					a.getContent(), a.getIsFavourite());
			String whereClause = "url = ?";
			String[] whereArgs = new String[] { a.getUrl() };
			count = db.update(table, cv, whereClause, whereArgs);
		} finally {
			if (db != null)
				db.close();
		}
		return count != 0;
	}
    /**
     * 查询收藏列表
     * to do 分页查询
     * @return
     */
	public List<Article> queryFavourite() {
		List<Article> res = new ArrayList<Article>();
		SQLiteDatabase db = null;
		try {
			db = helper.getReadableDatabase();
			Cursor c = db.rawQuery(
					"select * from " + Article.class.getSimpleName()
							+ " where isFavourite = ?", new String[] { "1" });
			while (c.moveToNext()) {
				Article a = (Article) gen(Article.class, c);
				System.out.println(a);
				res.add(a);
			}
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}
    /**
     * 根据url查询文章内容
     * @param url
     * @return
     */
	public String queryContent(String url) {
		String res = null;
		SQLiteDatabase db = null;
		try {
			db = helper.getReadableDatabase();
			String selection = "url = ?";
			String[] selectionArgs = new String[] { url };
			String[] columns = new String[] { "content" };
			Cursor c = db.query(table, columns, selection, selectionArgs, null,
					null, null);
			while (c.moveToNext()) {
				res = c.getString(c.getColumnIndex("content"));
				break;
			}
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}
    /**
     * 主要用来判断是否存在该文章
     * @param url
     * @return
     */
	public Article queryFirst(String url) {
		Article res = null;
		SQLiteDatabase db = null;
		try {
			db = helper.getReadableDatabase();
			String selection = "url = ?";
			String[] selectionArgs = new String[] { url };
			Cursor c = db.query(Article.class.getSimpleName(), null, selection,
					selectionArgs, null, null, null);
			while (c.moveToNext()) {
				return (Article) gen(Article.class, c);
			}
		} finally {
			if (db != null)
				db.close();
		}
		return res;
	}

	public static ContentValues getCV(String title, String url, long saveTime,
			String content, int isFavourite) {
		ContentValues cv = new ContentValues();
		cv.put("title", title);
		cv.put("url", url);
		cv.put("saveTime", saveTime);
		cv.put("content", content);
		cv.put("isFavourite", isFavourite);
		return cv;
	}
    /**
     * 根据游标返回的一行构造指定的类
     * @param cls
     * @param c
     * @return
     */
	@SuppressWarnings("unchecked")
	public static <T> T gen(Class<?> cls, Cursor c) {
		T t = null;
		try {
			t = (T) cls.newInstance();
			for (int i = 0; i < c.getColumnCount(); i++) {
				String columnName = c.getColumnName(i);
				String columnValue = c.getString(c.getColumnIndex(columnName));

				// 提取属性
				Field field = cls.getDeclaredField(columnName); // 可能没有该方法？出现异常
				// 应该跳过?还是直接dump掉?
				String fieldType = field.getType().getName(); // 属性的类型 long,int
																// ,or String
//				System.out.println(fieldType + "-->" + columnName);
				// 获得setter方法
				Method method = getSetMethod(cls, field);
				// 数据转换并赋值
				if (fieldType.equals("int")) {
					method.invoke(t, Integer.parseInt(columnValue));
				} else if (fieldType.equals("long")) {
					method.invoke(t, Long.parseLong(columnValue));
				} else if (fieldType.equals("float")) {
					method.invoke(t, Float.parseFloat(columnValue));
				} else if (fieldType.equals("double")) {
					method.invoke(t, Double.parseDouble(columnValue));
				} else if (fieldType.equals(String.class.getName())) {
					// System.out.println("String 内容-->"+columnValue);
					method.invoke(t, columnValue);
				} else {
					throw new IllegalArgumentException("仅支持int long String 类型");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	 
	public static Method getSetMethod(Class<?> entityType, Field field) {
		String fieldName = field.getName();
		Method m = null;
		String setMethod = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
//		System.out.println("执行--->" + setMethod);
		try {
			m = entityType.getDeclaredMethod(setMethod, field.getType());

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return m;
	}

}
