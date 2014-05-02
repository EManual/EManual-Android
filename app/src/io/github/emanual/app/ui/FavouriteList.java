package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.adapter.FavouriteListAdapter;
import io.github.emanual.app.entity.FavArticle;
import io.github.emanual.app.utils.MyDBManager;
import io.github.emanual.app.utils.ParseUtils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class FavouriteList extends BaseActivity implements OnItemClickListener,
		OnItemSelectedListener {
	DbUtils db;
	ActionBar mActionBar;
	ListView lv;
	List<FavArticle> data;
	List<Boolean> selected;
	FavouriteListAdapter adapter;

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
		try {
			// title 可以由url构造
			models = db.findDbModelAll(Selector.from(FavArticle.class)
					.select("id", "url", "saveTime").orderBy("saveTime", true));
		} catch (DbException e) {
			e.printStackTrace();
		}
		data = new ArrayList<FavArticle>();
		selected = new ArrayList<Boolean>();
		adapter = new FavouriteListAdapter(getContext(), data, selected);
		for (DbModel m : models) {
			FavArticle fa = new FavArticle();
			fa.setTitle(ParseUtils.getArticleNameByUrl(m.getString("url")));
			fa.setUrl(m.getString("url"));
			fa.setSaveTime(m.getLong("saveTime"));
			fa.setId(m.getInt("id"));
			data.add(fa);
		}
		restSelected();
	}

	@Override
	protected void initLayout() {
		mActionBar = getActionBar();
		lv = (ListView) _getView(R.id.listview);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		lv.setOnItemClickListener(this);
		lv.setMultiChoiceModeListener(new MyMultiChoiceMode());
		lv.setAdapter(adapter);
		lv.setOnItemSelectedListener(this);
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_cleanup:
			DbUtils db = MyDBManager.getDBUtils(getContext());
			try {
				db.deleteAll(FavArticle.class);
//				data.clear();
//				restSelected();
//				adapter.notifyDataSetChanged();
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
		try {
			// get content
			DbModel model = db.findDbModelFirst(Selector.from(FavArticle.class)
					.select("content")
					.where("url", "=", data.get(position).getUrl()));
			Intent intent = new Intent(getContext(), Detail.class);
			intent.putExtra("url", data.get(position).getUrl());
			intent.putExtra("content", model.getString("content"));
			startActivity(intent);
		} catch (DbException e) {
			e.printStackTrace();
			toast("数据库异常");
		}
	}

	private void restSelected() {

		selected.clear();
		for (int i = 0; i < data.size(); i++) {
			selected.add(false);
		}
		adapter.notifyDataSetInvalidated();
	}

	class MyMultiChoiceMode implements MultiChoiceModeListener {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.favouritelist_selected, menu);
			mode.setTitle("选择删除项");
			setSubtitle(mode);
			restSelected();
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getItemId() == R.id.action_cleanup_selected) {
				List<FavArticle> tmp = new ArrayList<FavArticle>();
				for (int i = 0; i < data.size(); i++) {
					// should be clean up
					if (selected.get(i)) {
						try {
							db.delete(data.get(i));
						} catch (DbException e) {
							e.printStackTrace();
						}
					} else {
						tmp.add(data.get(i));
					}
				}
				data.clear();
				data.addAll(tmp);
				adapter.notifyDataSetChanged();
				toast("已删除所选项");
				mode.finish();
				return true;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			restSelected();
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			setSubtitle(mode);
			selected.set(position, checked);
			adapter.notifyDataSetInvalidated();
		}

		private void setSubtitle(ActionMode mode) {
			final int checkedCount = lv.getCheckedItemCount();
			switch (checkedCount) {
			case 0:
				mode.setSubtitle(null);
				break;
			case 1:
				mode.setSubtitle("已选择 " + 1 + " 个");
				break;
			default:
				mode.setSubtitle("已选择 " + checkedCount + " 个");
				break;
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		toast("you select :" + position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		toast("onNothingSelected");
	}

}
