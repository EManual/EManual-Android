package io.github.emanual.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.emanual.app.R;
import io.github.emanual.app.ui.adapter.MainFragmentPagerAdapter;
import io.github.emanual.app.ui.fragment.Explore;
import io.github.emanual.app.ui.fragment.NewFeeds;
import io.github.emanual.app.ui.fragment.ResourceCenter;
import io.github.emanual.app.widget.NewVersionDialog;

public class Main extends BaseActivity {
    @InjectView(R.id.tabs) PagerSlidingTabStrip tabs;
    @InjectView(R.id.viewpager) ViewPager viewPager;

    List<Fragment> fragments = new ArrayList<Fragment>();
    String[] titles;
    NewVersionDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_main);
        ButterKnife.inject(this);
        initData();
        initLayout();

        UmengUpdateAgent.setUpdateAutoPopup(true);
        UmengUpdateAgent.update(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initData() {
        titles = getResources().getStringArray(R.array.title_main);
    }

    @Override
    protected void initLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setIcon(R.drawable.ic_icon_code_small_pure);

        dialog = new NewVersionDialog(getContext());

        fragments.add(new NewFeeds());
        fragments.add(new ResourceCenter());
        fragments.add(new Explore());
        if (viewPager == null) Log.d("debug", "viewPager is null");
        viewPager.setAdapter(new MainFragmentPagerAdapter(
                getSupportFragmentManager(), fragments, titles));
        tabs.setViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(getContext(), About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
