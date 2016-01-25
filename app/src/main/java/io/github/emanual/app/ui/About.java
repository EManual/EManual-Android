package io.github.emanual.app.ui;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import io.github.emanual.app.R;
import io.github.emanual.app.utils.AndroidUtils;

public class About extends SwipeBackActivity {

    @Bind(R.id.tv_version) TextView tv_version;

    @Override protected void initData(Bundle savedInstanceState) {

    }

    @Override protected void initLayout(Bundle savedInstanceState) {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_about);

        try {
            String text = String.format("%s v%s",
                    getResources().getString(R.string.application_name),
                    AndroidUtils.getAppVersionName(getContext()));
            tv_version.setText(text);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override protected int getContentViewId() {
        return R.layout.acty_about;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
