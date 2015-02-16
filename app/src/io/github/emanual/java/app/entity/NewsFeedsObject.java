package io.github.emanual.java.app.entity;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NewsFeedsObject {
	private String path;
	private String name;
	private String rname;
	private String description;
	
	public static NewsFeedsObject create(String json){
		return new Gson().fromJson(json, NewsFeedsObject.class);
	}
	
	public static List<NewsFeedsObject> createNewsFeedsObjects(String json){
		Type collectionType = new TypeToken<List<NewsFeedsObject>>(){}.getType();
		return new Gson().fromJson(json, collectionType);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
