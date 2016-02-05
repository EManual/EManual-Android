package io.github.emanual.app.ui.base.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import butterknife.Bind;
import io.github.emanual.app.R;

/**
 * Author: jayin
 * Date: 2/5/16
 */
public abstract class SwipeRefreshActivity extends SwipeBackActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light);
    }
}
