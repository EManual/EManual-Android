package io.github.emanual.java.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.util.Log;

public class _ {
	public static boolean isNumber(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String getContent(InputStream inputStream){
		return readFile(inputStream);
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

	public static String encodeURL(String url){
		try {
			url = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
    public static String readFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
        }
        return outputStream.toString();
    }
}
