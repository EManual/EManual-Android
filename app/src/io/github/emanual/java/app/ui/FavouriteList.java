package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.adapter.FavouriteListAdapter;
import io.github.emanual.java.app.db.ArticleDAO;
import io.github.emanual.java.app.entity.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class FavouriteList extends BaseActivity implements OnItemClickListener,
		OnItemSelectedListener {
	ActionBar mActionBar;
	@InjectView(R.id.listview) ListView lv;
	List<Article> data;
	List<Boolean> selected;
	FavouriteListAdapter adapter;
	ArticleDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_favouritelist);
		ButterKnife.inject(this);
		initData();
		initLayout();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		List<Article> tmp = dao.queryFavourite();
		data.clear();
		data.addAll(tmp);
		adapter.notifyDataSetChanged();
		
	}

	@Override
	protected void initData() {
		dao = new ArticleDAO(getContext());
		data = dao.queryFavourite();
		selected = new ArrayList<Boolean>();
		adapter = new FavouriteListAdapter(getContext(), data, selected);
		restSelected();
	}

	@Override
	protected void initLayout() {
		mActionBar = getActionBar();
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

			// db.deleteAll(Article.class);
			// data.clear();
			// restSelected();
			// adapter.notifyDataSetChanged();
//			dao.deleteAll();
			dao.deleteAllFavourite();
			toast("已清空");
			finish();

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
	 
			// get content
//			DbModel model = db.findDbModelFirst(Selector.from(Article.class)
//					.select("content")
//					.where("url", "=", data.get(position).getUrl()));
			String content = dao.queryContent(data.get(position).getUrl());
			Intent intent = new Intent(getContext(), Detail.class);
			intent.putExtra("url", data.get(position).getUrl());
			intent.putExtra("content", content);
			startActivity(intent);
	 
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
				List<Article> tmp = new ArrayList<Article>();
				for (int i = 0; i < data.size(); i++) {
					// should be clean up
					if (selected.get(i)) {
//						try {
//							db.delete(data.get(i));
//						} catch (DbException e) {
//							e.printStackTrace();
//						}
						dao.delete(data.get(i).getUrl());
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
