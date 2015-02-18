package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.utils.AndroidUtils;
import android.app.ActionBar;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class About extends BaseActivity {
	ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_about);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initLayout() {
		mActionBar = getActionBar();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		try {
			String text =String.format("%s v%s", getResources().getString(R.string.application_name),AndroidUtils.getAppVersionName(getContext()));
			((TextView)_getView(R.id.tv_version)).setText(text);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
