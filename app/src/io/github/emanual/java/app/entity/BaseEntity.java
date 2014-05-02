package io.github.emanual.java.app.entity;

import com.lidroid.xutils.db.annotation.Id;

public class BaseEntity {

	@Id
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
