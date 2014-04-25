package io.github.emanual.app.utils;

public class ParseUtils {
	
	public static int getArticleId(String filename){
	    return Integer.parseInt(filename.split("-")[0]);
	}
	
	public static String getArticleName(String filename){
		return filename.replaceAll("\\.[Mm]{1}[Dd]{1}", "").split("-")[1];
	}
}
