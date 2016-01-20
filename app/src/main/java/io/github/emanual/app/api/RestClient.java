package io.github.emanual.app.api;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class RestClient {
    //http://localhost:63342/examples/EManual
//	public static final String BASE_URL = "http://192.168.0.105:8080"; // main 
//	public static final String BASE_URL ="http://192.168.199.221:8081";
    //http://jayin.github.io/EManual
    public static final String BASE_URL = "http://emanual.github.io";
    public static final String URL_Preview = BASE_URL + "/assets/preview.html";
    public static final String URL_NewsFeeds = BASE_URL + "/md-newsfeeds/dist/%s";
    public static final String URL_Java = BASE_URL + "/java";
    public static final String URL_FEEDS = " http://emanualresource.github.io";
    private static int HTTP_Timeout = 12 * 1000;
    public static Context context;

    private static AsyncHttpClient client;

    public static AsyncHttpClient getHttpClient() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        return client;
    }

    /**
     * 初始化:如果需要调用登录验证记录session的函数前，必须调用这个方法，否则请求失败
     *
     * @param context Activity or Application context
     */
    public static void init(Context context) {
        RestClient.context = context;
        client = getHttpClient();
    }

    /**
     * get method
     *
     * @param url             相对的url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        get(BASE_URL, url, params, responseHandler);
    }

    public static void get(String baseUrl, String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        initClient();
        client.get(baseUrl + url, params, responseHandler);
    }

    /**
     * post method
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void post(String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        initClient();
        client.post(getAbsoluteUrl(url), params, responseHandler);

    }

    /**
     * 请求前初始化<br>
     * 必须在请求之前初始化，不然cookie失效<br>
     * context不为空时带着cookie去访问<br>
     */
    private static void initClient() {
        if (context != null)
            client.setCookieStore(new PersistentCookieStore(context));
        client.setTimeout(HTTP_Timeout);
        client.setEnableRedirects(false);
    }

    /**
     * 获得绝对url
     *
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
