package io.github.emanual.app.api;

import android.annotation.SuppressLint;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class EmanualAPI {
    /**
     * 下载对应的语言
     *
     * @param lang
     * @param responseHandler
     */
    @SuppressLint("DefaultLocale") public static void downloadLang(String lang, AsyncHttpResponseHandler responseHandler) {
        RestClient.get(String.format("/md-%s/dist/%s.zip", lang.toLowerCase(), lang.toLowerCase()), null, responseHandler);
    }

    /**
     * 获得最新版本的信息
     *
     * @param responseHandler
     */
    public static void getVersionInfo(AsyncHttpResponseHandler responseHandler) {
        RestClient.get("/md-release/dist/info.json", null, responseHandler);
    }

    /**
     * 获取对应语言的信息
     * @param lang
     * @param responseHandler
     */
    public static void getLangInfo(String lang, AsyncHttpResponseHandler responseHandler ){
        RestClient.get(String.format("/md-%s/dist/%s/info.json", lang.toLowerCase(), lang.toLowerCase()), null, responseHandler);
    }


}
