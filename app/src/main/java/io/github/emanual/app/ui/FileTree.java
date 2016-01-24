package io.github.emanual.app.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnItemClick;
import io.github.emanual.app.R;
import io.github.emanual.app.entity.FileTreeObject;
import io.github.emanual.app.ui.adapter.FileTreeAdapter;
import io.github.emanual.app.utils.EManualUtils;
import io.github.emanual.app.utils.UmengAnalytics;
import io.github.emanual.app.utils._;

public class FileTree extends SwipeBackActivity {
    private String root = ""; //根目录
    private String cur_path = "";  //当前路径
    private FileTreeObject mFileTreeObject;
    private String module; //模块

    @Bind(R.id.lv_filetree) ListView lv;
    private List<FileTreeObject> data;
    private FileTreeAdapter adapter;

    private ArrayList<String> rnames = new ArrayList<String>(); // 记录目录的原名称

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("DefaultLocale") @Override protected void initData(Bundle savedInstanceState) {
        if (getIntent().getStringExtra("LANG_PATH") != null) {
            cur_path = root = getIntent().getStringExtra("LANG_PATH");// ->
            // MD_PATH/lang
        } else {
            toast("目录不存在");
            finish();
        }
        getFileTreeInfo();
        data = new ArrayList<FileTreeObject>();
        if (mFileTreeObject != null) {
            data.addAll(mFileTreeObject.getFiles());
        }
        adapter = new FileTreeAdapter(this, data);

        module = root.substring(root.lastIndexOf("/") + 1, root.length());
        rnames.add(module.substring(0, 1).toUpperCase() + module.substring(1)); // ->
        // lang
        // 首字母大写

        onUmengAnalytics(module, null);

    }

    @Override protected void initLayout(Bundle savedInstanceState) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(rnames.get(rnames.size() - 1));

        lv.setAdapter(adapter);
    }

    @Override protected int getContentViewId() {
        return R.layout.acty_filetree;
    }

    // read cur_path/info.json
    private void getFileTreeInfo() {
        String info_json = null;
        try {
            info_json = _.readFile(cur_path + File.separator + "info.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (info_json == null) {
            // 不存在info.json，后端生成错误，理论上是闪退，但是为了更友好还是关闭
            toast("目录出错:不存在该文件");
            finish();
        } else {
            mFileTreeObject = FileTreeObject.create(info_json);
        }
    }

    private void updateTree() {
        data.clear();
        if (!cur_path.equals(root)) {
            data.add(FileTreeObject.getParentDirectory());
        }
        getFileTreeInfo();
        data.addAll(mFileTreeObject.getFiles());
        adapter.notifyDataSetChanged();

        getSupportActionBar().setTitle(rnames.get(rnames.size() - 1));
    }

    @OnItemClick(R.id.lv_filetree) public void click(int position) {
        // 返回上一级
        if ("..".equals(data.get(position).getName())) {
            cur_path = new File(cur_path).getParent();
            rnames.remove(rnames.size() - 1);
            updateTree();
        } else {
            File f = new File(cur_path + File.separator, data.get(position)
                    .getName());
            if (f.isDirectory()) {
                // 进入文件夹
                cur_path = f.getAbsolutePath();
                rnames.add(EManualUtils.getFileNameWithouExtAndNumber(data.get(
                        position).getRname()));
                updateTree();
            } else {
                // 处理:显示这个文件
                String link = cur_path + File.separator
                        + data.get(position).getName();
                String title = EManualUtils.getResouceTitle(data.get(position).getRname());
                String _path = "/";
                for (String n : rnames) {
                    _path += n + "/";
                }
                _path += title;

                onUmengAnalytics(null, _path);

                Intent intent = new Intent(this, Detail.class);
                intent.putExtra(Detail.EXTRA_LINK, link);
                intent.putExtra(Detail.EXTRA_TITLE, title);
                intent.putExtra(Detail.EXTRA_SHARE_PATH,
                        EManualUtils.genSharePath(data.get(position).getPath()));
                intent.putExtra(Detail.EXTRA_FEEDBACK_CONTENT, String.format(Detail.FEEDBACK_CONTENT_TPL, title, _path));

                startActivity(intent);
            }
        }
        debug("cur--> " + cur_path);
        debug("root--> " + root);
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

    //统计看了X语言的X文章(文章为路径) or 查看了x语言模块
    public void onUmengAnalytics(String lang, String file_path) {
        Map<String, String> m = new HashMap<String, String>();

        if (lang != null) {
            //统计查看语言模块
            m.put("module", module);

            MobclickAgent.onEventValue(this, UmengAnalytics.ID_EVENT_VIEW_MODULE, m, UmengAnalytics.DEAFULT_DURATION);
        } else {
            m.put("module", module);
            m.put("file_path", file_path);

            MobclickAgent.onEventValue(this, UmengAnalytics.ID_EVENT_VIEW_FILE, m, UmengAnalytics.DEAFULT_DURATION);
        }

    }
}
