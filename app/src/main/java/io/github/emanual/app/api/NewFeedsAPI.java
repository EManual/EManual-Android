package io.github.emanual.app.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class NewFeedsAPI {
	
	public NewFeedsAPI(){
		
	}
	
	public void getInfo(AsyncHttpResponseHandler responseHandler){
		RestClient.get("/java-newfeeds/article/info.json", null, responseHandler);
	}
	
	public void getNewFeeds(int page,AsyncHttpResponseHandler responseHandler){
		RestClient.get("/md-newsfeeds/dist/"+page+".json", null, responseHandler);
	}
	
	public static String getNewFeedsParam(String filename){
		return "?path=java-newfeeds/article/"+filename;
	}
	

}
