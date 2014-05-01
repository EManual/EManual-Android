package io.github.emanual.app;

import java.util.ArrayList;
import java.util.List;

import io.github.emanual.app.entity.FavArticle;
import io.github.emanual.app.utils.MyDBManager;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import android.test.AndroidTestCase;
import android.util.Log;

public class DBTest extends AndroidTestCase {

	public void query() {
		String[] strs = new String[] { "one", "two", "three", "four", "five",
				"six", "seven", "eight", "nine", "ten" };
		DbUtils db = MyDBManager.getDBUtils(getContext());
		List<DbModel> models = null;
		Log.i("debug", "initData----");
		try {
			models = db.findDbModelAll(Selector.from(FavArticle.class).select(
					"title"));
		} catch (DbException e) {
			e.printStackTrace();
		}

		List<String> res = new ArrayList<String>();
		if (models == null)
			Log.d("debug", models + "is null!!!");
		for (DbModel m : models) {
			res.add(m.getString("title"));
			Log.d("debug", m.getString("title"));
		}
		strs = (String[]) res.toArray(new String[res.size()]);
		Log.d("debug", "result-->" + res.toString());
		Log.d("debug", "res's size=" + res.size());
		Log.d("debug", "init data finished  " + strs.length);
		Log.d("debug", strs.toString());
		for (String s : strs) {
			Log.d("debug", "title--> " + s);
		}
	}
}
