package io.github.emanual.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.github.emanual.app.event.EmptyEvent;


public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initData();
    protected abstract void initLayout();
    protected abstract int getContentViewId();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
        initLayout();
    }

    /**
     * EventBus 3必须要有一个@Subscribe
     */
    @Subscribe
    public void onEmpty(EmptyEvent event){

    }

    /**
     * @param cls
     */
    public void openActivity(Class<?> cls) {
        this.startActivity(new Intent(this, cls));
    }

    /**
     * @param intent
     */
    public void openActivity(Intent intent) {
        this.startActivity(intent);
    }

    /**
     * @param content content of your want to Toast
     */
    public void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * @return
     */
    public Context getContext() {
        return this;
    }

    /**
     * 调试
     *
     * @param content
     */
    public void debug(String content) {
        Log.i("debug", this.getClass().getName() + ":" + content);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return serializable
     */
    @SuppressWarnings("unchecked") protected <V extends Serializable> V getSerializableExtra(
            final String name) {
        return (V) getIntent().getSerializableExtra(name);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return int -1 if not exist!
     */
    protected int getIntExtra(final String name) {
        return getIntent().getIntExtra(name, -1);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return string
     */
    protected String getStringExtra(final String name) {
        return getIntent().getStringExtra(name);
    }

    /**
     * Get intent extra
     *
     * @param name
     * @return string array
     */
    protected String[] getStringArrayExtra(final String name) {
        return getIntent().getStringArrayExtra(name);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override protected void onStop() {
        super.onStop();
    }

    @Override protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
