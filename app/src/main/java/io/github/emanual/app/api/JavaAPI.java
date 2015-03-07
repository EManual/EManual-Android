package io.github.emanual.app.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class JavaAPI {
	public static void getKindInfo(String kind,AsyncHttpResponseHandler responseHandler){
		RestClient.get("/java/"+kind+"/info.json", null, responseHandler);
	}
	
	//include topic list
	public static void getTopicInfo(String kind,String topic,AsyncHttpResponseHandler responseHandler){
		RestClient.get("/java/"+kind+"/"+topic+"/info.json", null, responseHandler);
	}
	
	public static void getArticleList(int page,String kind,String topic,AsyncHttpResponseHandler responseHandler){
		RestClient.get("/java/"+kind+"/"+topic+"/"+page+".json", null, responseHandler);
	}
	
	public static String getArticleUrl (String kind,String topic,String article_name){
		return RestClient.BASE_URL+"/java/"+kind+"/"+topic+"/"+article_name;
	}
	
	public static String getArticleParam(String kind,String topic,String article_name){
		return "path=java/"+kind+"/"+topic+"/"+article_name;
	}
	
	public static void getVersionInfo(AsyncHttpResponseHandler responseHandler){
		RestClient.get("/released/java/update.json", null, responseHandler);
	}
	
}
