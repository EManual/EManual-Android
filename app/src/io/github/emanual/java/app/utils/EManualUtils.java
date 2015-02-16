package io.github.emanual.java.app.utils;
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

}
