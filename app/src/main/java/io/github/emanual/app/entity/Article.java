package io.github.emanual.app.entity;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Article implements Serializable  {

	/** 标题 */
	private String title;
	/** 文章url */
	private String url;
	/** 收藏时间 */
	private long saveTime;
	/**内容 */
	private String content;
	/** 非0记为已收藏*/
	private int isFavourite;
	
	public Article(){
		this("", "", "");
	}

	public Article(String title, String url) {
		this(title, url, "");
	}
	
	public Article(String title, String url,String content) {
		this.title = title;
		this.url = url;
		this.content = content;
		this.saveTime = System.currentTimeMillis();
		this.isFavourite = 0;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(int isFavourite) {
		this.isFavourite = isFavourite;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", url=" + url + ", saveTime="
				+ saveTime + ", content=" + content + ", isFavourite="
				+ isFavourite + "]";
	}
 
}
