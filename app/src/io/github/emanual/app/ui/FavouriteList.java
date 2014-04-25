package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FavouriteList extends BaseActivity {
	ActionBar mActionBar;
	ListView lv;
	String[] strs = new String[] { "one", "two", "three", "four", "five",
			"six", "seven", "eight", "nine", "ten" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_favouritelist);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		mActionBar = getActionBar();
		lv = (ListView) _getView(R.id.listview);
		lv.setAdapter(new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_list_item_1, strs));
		
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_cleanup:
			toast("clean up");
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

}
