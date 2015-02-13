package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.adapter.TopicListAdapter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	@InjectView(R.id.lv_filetree) ListView lv;
	private List<String> data;
	private TopicListAdapter adapter ;
	
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
		File f= new File(cur_path);
		String[] names = f.list();
		data = new ArrayList<String>();
		data.addAll(Arrays.asList(names));
		adapter = new TopicListAdapter(this, data);
	}

	@Override
	protected void initLayout() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		lv.setAdapter(adapter);
	}
	
	public void updateTree(){
		data.clear();
		if(!cur_path.equals(root)){
			data.add("..");
		}
		String[] _names = new File(cur_path).list();
		data.addAll(Arrays.asList(_names));
		adapter.notifyDataSetChanged();
	}
	
	@OnItemClick(R.id.lv_filetree)
	public void click(int position){
		File f = new File(cur_path+File.separator, data.get(position));
		if(f.isDirectory() || "..".equals(data.get(position))){
			if("..".equals(data.get(position))){
				cur_path = new File(cur_path).getParent();
			}else{
				cur_path = f.getAbsolutePath();
			}
			
			updateTree();
		}else{
			//处理:显示这个文件
			toast("open-> "+data.get(position));
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
