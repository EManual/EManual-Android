package io.github.emanual.app.model;

import java.io.Serializable;

public class FavArticle implements Serializable {

	private static final long serialVersionUID = -4449685455252816854L;
	/** 标题 */
	private String title;
	/** 文章url */
	private String url;
	/** 收藏时间 */
	private long saveTime;

	public FavArticle(String title, String url) {
		this.title = title;
		this.url = url;
		this.saveTime = System.currentTimeMillis();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
