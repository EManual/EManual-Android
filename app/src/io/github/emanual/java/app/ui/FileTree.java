package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.adapter.FileTreeAdapter;
import io.github.emanual.java.app.entity.FileTreeObject;
import io.github.emanual.java.app.utils._;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class FileTree extends BaseActivity {
	private String root = "";
	private String cur_path  = "";
	private FileTreeObject mFileTreeObject;
	
	@InjectView(R.id.lv_filetree) ListView lv;
	private List<FileTreeObject> data;
	private FileTreeAdapter adapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_filetree);
		ButterKnife.inject(this);
		initData();
		initLayout();
	}

	@Override
	protected void initData() {
		if( getIntent().getStringExtra("LANG_PATH") != null){
			cur_path = root = getIntent().getStringExtra("LANG_PATH");//-> MD_PATH/lang
		}else{
			toast("目录不存在");
			finish();
		}
		getFileTreeInfo();
		data = new ArrayList<FileTreeObject>();
		data.addAll(mFileTreeObject.getFiles());
		adapter = new FileTreeAdapter(this, data);
	}

	@Override
	protected void initLayout() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		lv.setAdapter(adapter);
	}
	//read cur_path/info.json
	private void getFileTreeInfo(){
		String info_json = null;
		try {
			info_json = _.readFile(cur_path+File.separator+"info.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(info_json == null){
			//不存在info.json，后端生成错误，理论上是闪退，但是为了更友好还是关闭
			toast("目录出错:不存在该文件");
			finish();
		}else{
			mFileTreeObject = FileTreeObject.create(info_json);
		}
	}
	
	private void updateTree(){
		data.clear();
		if(!cur_path.equals(root)){
			data.add(FileTreeObject.getParentDirectory());
		}
		getFileTreeInfo();
		data.addAll(mFileTreeObject.getFiles());
		adapter.notifyDataSetChanged();
	}
	
	@OnItemClick(R.id.lv_filetree)
	public void click(int position){
		if("..".equals(data.get(position).getName())){
			cur_path = new File(cur_path).getParent();
			updateTree();
		}else {
			File f = new File(cur_path+File.separator, data.get(position).getName());
			if (f.isDirectory()){
				cur_path = f.getAbsolutePath();
				updateTree();
			}else{
				//处理:显示这个文件
				toast("open-> "+data.get(position));
			}
		}
		debug("cur--> "+cur_path);
		debug("root--> "+root);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
