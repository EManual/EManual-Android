package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.api.JavaAPI;
import io.github.emanual.app.ui.adapter.TopicListAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

public class TopicList extends BaseActivity implements OnRefreshListener,
        OnItemClickListener {
    ActionBar mActionBar;
    @InjectView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.listview) ListView lv;
    String kind = null, title = null;
    List<String> data;
    TopicListAdapter adapter;
    long last_motify = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_topiclist);
        ButterKnife.inject(this);
        initData();
        initLayout();
    }

    @Override
    protected void initData() {
        kind = getStringExtra("kind");
        title = getStringExtra("title");
        if (kind == null || title == null) {
            throw new NullPointerException("You need a `kind` and `title`");
        }
        data = new ArrayList<String>();
        adapter = new TopicListAdapter(this, data);
    }

    @Override
    protected void initLayout() {
        mActionBar = getActionBar();
        mActionBar.setTitle(title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        JavaAPI.getKindInfo(kind, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("result");
                    last_motify = response.getInt("last_motify");
                    ArrayList<String> _tipics = new ArrayList<String>();
                    for (int i = 0; i < array.length(); i++) {
                        _tipics.add(array.getString(i));
                    }
                    data.clear();
                    data.addAll(_tipics);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("debug", "TopicList-->parse error!");
                    toast("哎呀,出错了！");

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                Log.e("debug", "TopicList-->无法获取该信息 ErrorCode=" + statusCode);
                toast("哎呀,出错了！");
            }

            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(getContext(), ArticleList.class);
        intent.putExtra("kind", kind);
        intent.putExtra("topic", data.get(position));
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        if (title != null && !title.equals("")) {
            MobclickAgent.onPageStart(title);
        } else {
            MobclickAgent.onPageStart("TopicList");
        }

    }

    public void onPause() {
        super.onPause();
        if (title != null && !title.equals("")) {
            MobclickAgent.onPageEnd(title);
        } else {
            MobclickAgent.onPageEnd("TopicList");
        }
    }

}
