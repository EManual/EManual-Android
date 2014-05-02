package io.github.emanual.java.app.entity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

@SuppressWarnings("serial")
@Table(name="FavArticle")
public class FavArticle extends BaseEntity implements Serializable  {

	/** 标题 */
	@Column
	private String title;
	/** 文章url */
	@Column
	@Unique
	private String url;
	/** 收藏时间 */
	@Column
	private long saveTime;
	/**内容 */
	@Column
	private String content;
	
	public FavArticle(){
		this("", "", "");
	}

	public FavArticle(String title, String url) {
		this(title, url, "");
	}
	
	public FavArticle(String title, String url,String content) {
		this.title = title;
		this.url = url;
		this.content = content;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
 
}
