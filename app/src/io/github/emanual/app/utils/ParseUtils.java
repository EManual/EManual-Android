package io.github.emanual.app.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.github.emanual.app.api.RestClient;

public class ParseUtils {

	public static int getArticleId(String filename) {
		return Integer.parseInt(filename.split("-")[0]);
	}

	public static String getArticleName(String filename) {
		return filename.replaceAll("\\.[Mm]{1}[Dd]{1}", "").split("-")[1];
	}

	// http:xxxxx/assets/preview.html?path=/a/b/[title].md
	public static String getArticleNameByUrl(String url) {
		return getArticleName(url.split("=")[1].split("/")[3]);
	}

	public static String encodeArticleURL(String url) {
		try {
			StringBuilder sb =new StringBuilder(RestClient.URL_Preview + "?path=");
			for(String s : url.split("=")[1].split("/")){
				sb.append(URLEncoder.encode(s, "UTF-8")).append("/");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
