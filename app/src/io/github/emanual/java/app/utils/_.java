package io.github.emanual.java.app.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class _ {
	public static boolean isNumber(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
    
	public static String urlJoin(String baseUrl, String... strs) {
		StringBuilder sb = new StringBuilder(baseUrl);
		for (int i = 0; i < strs.length; i++) {
			try {
				sb.append("/").append(URLEncoder.encode(strs[i], "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String encodeURL(String url) {
		String[] splits = url.split("//");
		StringBuilder sb = new StringBuilder(splits[0] + "//");
		try {
			String s = splits[1];
			for (String _s : s.split("/")) {
				sb.append(URLEncoder.encode(_s, "utf-8")).append("/");
			}
			sb.deleteCharAt(sb.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}
}
