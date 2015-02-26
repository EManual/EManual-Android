package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.utils.AndroidUtils;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class About extends BaseActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_about);
		ButterKnife.inject(this);
		initData();
		initLayout();
	}

	@Override protected void initData() {

	}

	@Override protected void initLayout() {
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.acty_about);

		try {
			String text = String.format("%s v%s",
					getResources().getString(R.string.application_name),
					AndroidUtils.getAppVersionName(getContext()));
			((TextView) _getView(R.id.tv_version)).setText(text);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@OnClick(R.id.btn_homepage) public void homepage(TextView tv_homepage) {
		Intent intent = new Intent(this, Browser.class);
		intent.putExtra(Browser.EXTRA_URL, tv_homepage.getText().toString());
		startActivity(intent);

	}

}
