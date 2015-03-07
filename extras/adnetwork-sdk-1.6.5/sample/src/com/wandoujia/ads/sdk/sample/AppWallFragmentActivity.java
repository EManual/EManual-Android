package com.wandoujia.ads.sdk.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.wandoujia.ads.sdk.fragment.AppListFragment;
import com.wandoujia.ads.sdk.fragment.TabsFragment;


/**
 * Created by liuyanshuang on 14-10-16.
 */
public class AppWallFragmentActivity extends FragmentActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.app_wall);

    Bundle args = new Bundle();
    args.putString(TabsFragment.KEY_TAG, MainActivity.TAG_LIST);
    args.putInt(TabsFragment.KEY_DETAIL_CONTAINER_ID, R.id.app_detail_container);
    args.putString(TabsFragment.KEY_DETAIL_BACK_STACK_NAME, null);

    TabsFragment fragment = new TabsFragment();
    fragment.setArguments(args);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.app_wall_container, fragment, AppListFragment.TAG)
        .commit();
  }
}
