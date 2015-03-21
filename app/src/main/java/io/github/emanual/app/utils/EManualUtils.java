package io.github.emanual.app.utils;

import android.content.Context;

import java.io.File;


/**
 * EManual 自身常用工具类
 * @author jayin
 *
 */
public class EManualUtils {
	public static final boolean DEBUG = false; 
	public static final String URL_HOME_PAGE = "http://www.iemanual.com";
    /** 使用手册*/
	public static final String URL_USAGE = "http://iemanual.com/blog/?usage/index.md";
    /** 赞助作者*/
    public static final String URL_SPONSOR = "http://iemanual.com/blog/?about/sponsor.md";
    /** */
    public static final String URL_OPENSOURCE = "http://www.iemanual.com/blog/?about/projects-that-power-emanual.md";
	/**
	 * 获得应用的根目录
	 * <br>
	 * <li>如果DEBUG==true,返回的是ExternalStorage路径方便调试 /storage/emulated/0/Android/data/<App Name>/files
	 * <li>如果DEBUG==false,返回的时InternalStorage /data/data/<App Name>/files
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused") public static String getRootPath(Context context){
		if(StorageUtils.isExternalStorageWritable() && DEBUG){
			return  context.getExternalFilesDir(null).getAbsolutePath();
		}
		return context.getFilesDir().getAbsolutePath();
	}
	/**
	 * 获得markdown文件根目录 
	 * <br>
	 * ROOT_PATH/md
	 * @param context
	 * @return
	 */
	public static String getMdPath(Context context){
		String MD_PATH = getRootPath(context) + File.separator + "md"; 
		File f = new File(MD_PATH);
		if(!f.exists()){
			f.mkdir();
		}
		return f.getAbsolutePath();
	}
	/**
	 * 获得下载路径
	 * <br>
	 * ROOT_PATH/Download
	 * @param context
	 * @return
	 */
	public static String getDownloadPath(Context context){
		String DOWNLOAD_PATH = getRootPath(context) + File.separator + "Download";
		File f = new File(DOWNLOAD_PATH);
		if(!f.exists()){
			f.mkdir();
		}
		return f.getAbsolutePath();
	}
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
		if(filename.lastIndexOf(".") != -1 && filename.lastIndexOf(".") != filename.length()-1){
			return filename.substring(0, filename.lastIndexOf("."));
		}
		return filename;
		
	}
	/**
	 * 获得不带序号,后缀名的文件名
	 * @param filename
	 * @return
	 */
	public static String getFileNameWithouExtAndNumber(String filename){
		filename = getFileNameWithoutExt(filename);
		if(filename.contains("-")){
			return filename.substring(filename.indexOf("-")+1, filename.length());
		}
		return filename;
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
		
		String title = s[3];
		if(s.length > 4){
			//文件名也包含符号`-`
			for(int i = 4; i < s.length; i++){
				title += "-" + s[i];
			}
		}
		return title;
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
	/**
	 * 根据文件的path生成分享url
	 * @param path
	 * @return
	 */
	public static String genSharePath(String path){
		String module = path.split("/")[0];
		return String.format("/md-%s/dist/%s",module,path);
	}
}
