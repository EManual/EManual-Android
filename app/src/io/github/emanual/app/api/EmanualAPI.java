package io.github.emanual.app.api;

import android.annotation.SuppressLint;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class EmanualAPI {
	/**
	 * 下载对应的语言
	 * @param lang
	 * @param responseHandler
	 */
	@SuppressLint("DefaultLocale") public static void downloadLang(String lang,AsyncHttpResponseHandler responseHandler){
		RestClient.get(String.format("/md-%s/dist/%s.zip", lang.toLowerCase(),lang.toLowerCase()), null, responseHandler);
	}
}
