package io.github.emanual.app.ui.base.activity;

import android.os.Bundle;

import butterknife.Bind;
import io.github.emanual.app.R;
import io.github.emanual.app.ui.base.activity.BaseActivity;
import io.github.emanual.app.widget.SwipeBackLayout;

/**
 * Author: jayin
 * Date: 1/11/16
 */
public abstract class SwipeBackActivity extends BaseActivity {
    @Bind(R.id.swipBackLayout) SwipeBackLayout mSwipeBackLayout;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSwipeBackLayout.setCallBack(new SwipeBackLayout.CallBack() {
            @Override
            public void onFinish() {
                finish();
            }
        });
    }
}
