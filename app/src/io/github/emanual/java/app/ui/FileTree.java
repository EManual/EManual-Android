package io.github.emanual.java.app.ui;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.adapter.TopicListAdapter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
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
		if( getIntent().getStringExtra("root") != null){
			root = getIntent().getStringExtra("root");
		}
		debug("locals:");
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//			debug(Environment.getRootDirectory().getAbsolutePath());
//			debug(Environment.getExternalStorageDirectory().getAbsolutePath());
//			debug(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
//			debug(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
			
			cur_path = root = new File(getExternalFilesDir(null),"md").getAbsolutePath();
			
			File f= new File(cur_path);
			String[] names = f.list();
//			for (String n : names){
//				debug(n);
//				try {
//					n = new String(n.getBytes(),"UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//			}
			data = new ArrayList();
			data.addAll(Arrays.asList(names));
			adapter = new TopicListAdapter(this, data);
		}
	}

	@Override
	protected void initLayout() {
		lv.setAdapter(adapter);
	
	}
	
	public void updateTree(){
		data.clear();
		if(!cur_path.equals(root)){
			data.add("..");
		}
		String[] _names = new File(cur_path).list();
//		for(int i=0;i<_names.length;i++){
//			try {
//				debug("befor-->"+_names[i]);
//				_names[i] = new String(_names[i].getBytes("gbk"),"UTF-8");
//				debug("after-->"+_names[i]);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}
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

}
