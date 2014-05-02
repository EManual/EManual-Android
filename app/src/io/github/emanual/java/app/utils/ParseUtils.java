package io.github.emanual.java.app.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;

import io.github.emanual.java.app.api.RestClient;

public class ParseUtils {

	public static int getArticleId(String filename) {
		return Integer.parseInt(filename.split("-")[0]);
	}

	// [(2013-1-1,0010)]title].md
	public static String getArticleName(String filename) {
		String[] s = filename.replaceAll("\\.[Mm]{1}[Dd]{1}", "").split("-");
		//这是日期开头
		if(_.isNumber(s[0]) && _.isNumber(s[1]) && _.isNumber(s[2]) ){
			return s[3];
		}else{ //这是文章编号
			return s[1];
		}
	}

	// http:xxxxx/assets/preview.html?path=/a/b/[(2013-1-1,0010)]title].md
//	public static String getArticleNameByUrl(String url) {
//		String[] s = url.split("=")[1].split("/");
//		return getArticleName(s[s.length - 1]);
//	}
	
	// http:xxxxx/a/b/[(2013-1-1,0010)]title].md
	public static String getArticleNameByUrl(String url){
		String[] s = url.split("/");
		return getArticleName(s[s.length - 1]);
	}

	public static String encodeArticleURL(String url) {
		try {
			StringBuilder sb = new StringBuilder(RestClient.URL_Preview
					+ "?path=");
			for (String s : url.split("=")[1].split("/")) {
				sb.append(URLEncoder.encode(s, "UTF-8")).append("/");
			}
			sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
