package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.entity.FavArticle;
import io.github.emanual.app.utils.MyDBManager;
import io.github.emanual.app.utils.ParseUtils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class FavouriteList extends BaseActivity implements OnItemClickListener {
	DbUtils db;
	ActionBar mActionBar;
	ListView lv;
	String[] titles = new String[] {};
	String[] urls = new String[] {};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_favouritelist);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		db = MyDBManager.getDBUtils(getContext());
		List<DbModel> models = null;
		Log.i("debug", "initData----");
		try {
			// title 可以由url构造
			models = db.findDbModelAll(Selector.from(FavArticle.class)
					.select("url").orderBy("saveTime", true));
		} catch (DbException e) {
			e.printStackTrace();
		}

		List<String> res_url = new ArrayList<String>();
		List<String> res_title = new ArrayList<String>();
		if (models == null)
			Log.d("debug", models + "is null!!!");
		for (DbModel m : models) {
			res_title.add(ParseUtils.getArticleNameByUrl(m.getString("url")));
			res_url.add(m.getString("url"));
		}
		titles = (String[]) res_title.toArray(new String[res_title.size()]);
		urls = (String[]) res_url.toArray(new String[res_url.size()]);
		// Log.d("debug", "result-->"+res.toString());
		Log.d("debug", "init data finished");
	}

	@Override
	protected void initLayout() {
		mActionBar = getActionBar();
		lv = (ListView) _getView(R.id.listview);
		lv.setAdapter(new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_list_item_1, titles));
		lv.setOnItemClickListener(this);

		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_cleanup:
			DbUtils db = MyDBManager.getDBUtils(getContext());
			try {
				db.deleteAll(FavArticle.class);
				toast("已清空");
				finish();
			} catch (DbException e) {
				e.printStackTrace();
				toast("清楚失败");
			}
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.favouritelist, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		toast(urls[position]);
		try {
			DbModel model = db.findDbModelFirst(Selector.from(FavArticle.class)
					.select("content").where("url", "=", urls[position]));
			Intent intent = new Intent(getContext(), Detail.class);
			intent.putExtra("url", urls[position]);
			intent.putExtra("content", model.getString("content"));
			startActivity(intent);
		} catch (DbException e) {
			e.printStackTrace();
			toast("数据库异常");
		}
	}

}
