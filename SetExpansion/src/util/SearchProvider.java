package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import Parser.WebPage;
import api.SearchAPI;
import api.SearchAPIFactory;
import constants.SearchAPIConstants.APIType;

public class SearchProvider {

	static {
		System.setProperty("https.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("https.proxyPort", "8080");

		System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("http.proxyPort", "8080");

	}

	public static String constructQuery(ArrayList<String> seedList,
			String concept) {
		String ret = new String();
		for (String s : seedList) {
			ret = ret + " " + s;
		}
		return concept == null ? ret : " \"List of " + concept + "\"" + ret;
	}
	
	//Double quotes removed
	public static String constructWikiQuery(ArrayList<String> seedList,
			String concept){
		
		String ret = new String();
		for (String s : seedList) {
			ret = ret + " " + s;
		}
		return concept == null ? ret : "List of " + concept  + " "+ ret;
		
	}



	public static String getHtml(String url) {
		System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("http.proxyPort", "8080");
		LogUtil.log.fine("Going to web for : " + url);
		BufferedReader in;
		String inputLine;
		StringBuilder sb = new StringBuilder();
		try {
			URL pageURL = new URL(url);
			URLConnection con = pageURL.openConnection();
			con.setConnectTimeout(5000);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			//char[] cbuf = new char[4096];
			if(!in.ready()){
				return "";
			}
			while ((inputLine = in.readLine()) != null){
				
				if(inputLine.contains("\0")){
					inputLine = inputLine.replaceAll("\0", ".");
				} 
				sb.append(inputLine).append("\n");
			}
		} catch (MalformedURLException e) {
			LogUtil.log.fine(e.toString());
		} catch (IOException e) {
			LogUtil.log.fine(e.toString());
		}
		return sb.toString();
	}

	
	public static ArrayList<WebPage> getURLs(ArrayList<String> seedList){
		
		ArrayList<WebPage> result = new ArrayList<>();
		SearchAPI bingSearchAPI = SearchAPIFactory
				.getSearchAPI(APIType.BING);
		java.util.List<api.BingSearchAPI.results> resultBing= bingSearchAPI
				.getTopURLs(constructQuery(seedList, null), 20);
		/*SearchAPI wikiSearchAPI = SearchAPIFactory
				.getSearchAPI(APIType.WIKI);
		java.util.List<api.BingSearchAPI.results> resultWiki= wikiSearchAPI
				.getTopURLs(constructQuery(seedList, null), 20);
		
		resultBing.addAll(resultWiki);*/
		for (api.BingSearchAPI.results r : resultBing) {
			
			WebPage page = new WebPage(r.Title, r.Url, r.Description);
			result.add(page);
		}
		return result;
		
	}

}
