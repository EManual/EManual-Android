package io.github.emanual.app.ui;

import io.github.emanual.app.R;
import io.github.emanual.app.adapter.FileTreeAdapter;
import io.github.emanual.app.entity.FileTreeObject;
import io.github.emanual.app.utils.EManualUtils;
import io.github.emanual.app.utils._;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class FileTree extends BaseActivity {
	private String root = "";
	private String cur_path = "";
	private FileTreeObject mFileTreeObject;

	@InjectView(R.id.lv_filetree) ListView lv;
	private List<FileTreeObject> data;
	private FileTreeAdapter adapter;

	private ArrayList<String> rnames = new ArrayList<String>(); // 记录目录的原名称

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_filetree);
		ButterKnife.inject(this);
		initData();
		initLayout();
	}

	@Override protected void initData() {
		if (getIntent().getStringExtra("LANG_PATH") != null) {
			cur_path = root = getIntent().getStringExtra("LANG_PATH");// ->
																		// MD_PATH/lang
		} else {
			toast("目录不存在");
			finish();
		}
		getFileTreeInfo();
		data = new ArrayList<FileTreeObject>();
		data.addAll(mFileTreeObject.getFiles());
		adapter = new FileTreeAdapter(this, data);

		String lang = root.substring(root.lastIndexOf("/") + 1, root.length());
		rnames.add(lang.substring(0, 1).toUpperCase() + lang.substring(1)); // ->
																			// lang
																			// 首字母大写

	}

	@Override protected void initLayout() {
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(rnames.get(rnames.size() - 1));

		lv.setAdapter(adapter);
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
				Intent intent = new Intent(this, Detail.class);
				intent.putExtra(Detail.EXTRA_LINK, link);
				intent.putExtra(Detail.EXTRA_TITLE, EManualUtils
						.getResouceTitle(data.get(position).getRname()));
				intent.putExtra(Detail.EXTRA_SHARE_PATH,
						EManualUtils.genSharePath(data.get(position).getPath()));
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
}
