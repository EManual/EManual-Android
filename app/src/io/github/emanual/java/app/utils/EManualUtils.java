package io.github.emanual.java.app.utils;

import android.util.Log;

/**
 * EManual 自身常用工具类
 * @author jayin
 *
 */
public class EManualUtils {
	
	/**
	 * 获得文件的扩展名
	 * @param filename
	 * @return 
	 */
	public static String getFileExt(String filename) {
		if (!filename.contains(".")) {
			return "";
		} else {
			return filename.substring(filename.lastIndexOf(".") + 1,
					filename.length());
		}
	}
	/**
	 * 获得不带后缀名(扩展名)的文件名
	 * @return
	 */
	public static String getFileNameWithoutExt(String filename){
		return filename.substring(0, filename.lastIndexOf("."));
	}
	/**
	 * 从ResourceCenter里的文件名提取文章的标题
	 * @param filename
	 * @return
	 */
	public static String getResouceTitle(String filename){
		if (filename.contains("-")){
			return getFileNameWithoutExt(filename).split("-")[1];
		}
		return getFileNameWithoutExt(filename);
	}
	
	/**
	 * 从NewsFeeds的文件名中提取标题
	 * @param filename
	 * @return
	 */
	public static String getNewsFeedsTitle(String filename) {
		filename = filename.split("\\.")[0]; // rm ext anme
		String[] s = filename.split("-");
		return s[3];
	}
	/**
	 * 从NewsFeeds的文件名中提取时间(年-月-日)
	 * @param filename
	 * @return
	 */
	public static String getNewsFeedsTime(String filename) {
		filename = filename.split("\\.")[0];
		String[] s = filename.split("-");
		return s[0] + "-" + s[1] + "-" + s[2];
	}


}
