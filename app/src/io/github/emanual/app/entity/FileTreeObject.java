package io.github.emanual.app.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class FileTreeObject {
	
	public static final String MODE_TREE = "tree";
	public static final String MODE_FILE = "file";
	
	private String path;// 文件路径
	private String rname;//原来名称
	private String name;//拼音后的名称
	private String mtime;//最后修改时间
	private String mode;//类型
	private List<FileTreeObject> files;
	/**
	 * 生成一个上级目录("..")
	 * @return
	 */
	public static FileTreeObject getParentDirectory(){
		FileTreeObject f = new FileTreeObject();
		f.setPath(null);
		f.setRname("..");
		f.setName("..");
		f.setMtime(System.currentTimeMillis()/1000+"");
		f.setMode(MODE_TREE);
		return f;
	}	

	public static FileTreeObject create(String json){
		return new Gson().fromJson(json, FileTreeObject.class);
	}
	
	public FileTreeObject findFileByRName(String rname){
		if(this.files == null || this.files.size() == 0){
			return null;
		}
		for(FileTreeObject f : this.files){
			if (f.getRname().equals(rname)){
				return f;
			}
		}
		return null;
	}
	
	public List<String> getFileRnames() {
		if (this.files == null)
			return null;
		List<String> names = new ArrayList<String>();
		for(FileTreeObject f : this.files){
			names.add(f.getRname());
		}
		return names;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMtime() {
		return mtime;
	}

	public void setMtime(String mtime) {
		this.mtime = mtime;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<FileTreeObject> getFiles() {
		return files;
	}

	public void setFiles(List<FileTreeObject> files) {
		this.files = files;
	}
	
	
}
