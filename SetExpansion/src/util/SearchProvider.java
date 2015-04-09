package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebResult;
import util.GoogleResults.Result;
import webdb.Web;
import Parser.ListFinderHTML;
import Parser.WebList;
import Parser.WebPage;
import api.BingSearchAPI.results;
import api.SearchAPI;
import api.SearchAPIFactory;

import com.google.gson.Gson;

import constants.SearchAPIConstants.APIType;

public class SearchProvider {
	// private static final String APP_ID =
	// "sWfZW7SsZBVnnuf9UJl7DYhaJBqVUTKB/k9/b7f/zCQ="; // Dharmesh
	// private static final String APP_ID =
	// "4YlNS8WSm+N9DNfJoGwsZ0G9CQrtCot7t0x+dw64PL0=";
	// private static final String APP_ID =
	// "oz+hcEJE0S+ZDjBNO+viPx+2t85Hayx7rJiCxdfuVlw="; // Dharmesh SetExpansion
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

	public static ArrayList<WebPage> bingSearch(ArrayList<String> seedList,
			String concept, int noOfResults, double overlapTolerance) {
		ArrayList<WebPage> listPages = new ArrayList<WebPage>();
		// AzureSearchWebQuery query = new AzureSearchWebQuery();
		ListFinderHTML myfinder = new ListFinderHTML();

		// query.setAppid(APP_ID);
		// query.setQuery(constructQuery(seedList, concept));
		// query.setMarket("en-us");
		// aq2.setPage(2);
		// query.setPerPage(noOfResults);
		// System.out.println(query.getBingApi());
		// System.out.println(query.getQueryPath());
		// query.doQuery();
		/*
		 * AzureSearchResultSet<AzureSearchWebResult> results = query
		 * .getQueryResult();
		 */

		AzureSearchResultSet<AzureSearchWebResult> results = null;
		if (results == null) {

			// check only with google results
			SearchAPI googleSearchAPI = SearchAPIFactory
					.getSearchAPI(APIType.GOOGLE);
			java.util.List<api.BingSearchAPI.results> results1 = googleSearchAPI
					.getTopURLs(constructQuery(seedList, concept), 10);
			System.out.println("got results from google");
			java.util.List<api.BingSearchAPI.results> results2;
			if(concept==null){
				SearchAPI bingSearchAPI = SearchAPIFactory
						.getSearchAPI(APIType.BING);
				results2 = bingSearchAPI
						.getTopURLs(constructQuery(seedList, concept), 10);
			} else {
				SearchAPI bingSearchAPI = SearchAPIFactory
						.getSearchAPI(APIType.WIKI);
				results2 = bingSearchAPI
						.getTopURLs(constructWikiQuery(seedList, concept), 10);
			}
			/*SearchAPI wikiSearchAPI = SearchAPIFactory
					.getSearchAPI(APIType.WIKI);
			java.util.List<api.BingSearchAPI.results> results3 = wikiSearchAPI
					.getTopURLs(constructQuery(seedList, concept), 10);*/
			/*
			 * SearchAPI wikiSearchAPI =
			 * SearchAPIFactory.getSearchAPI(APIType.WIKI);
			 * java.util.List<api.BingSearchAPI.results> results2 =
			 * wikiSearchAPI.getTopURLs(constructQuery(seedList, concept), 10);
			 */
			results1.addAll(results2);
		//	results1.addAll(results3);
			System.out.println("got results from bing " + results1.size());
			for (api.BingSearchAPI.results r : results1) {
				myfinder.SetHTML(r.Url);
				WebPage page = new WebPage(r.Title, r.Url, r.Description);
				ArrayList<String> webList = new ArrayList<String>();
				while ((webList = myfinder.getNextList()) != null) {
					if (webList.size() > 0) {
						if (ListUtil.getOverLap(webList, seedList) >= 4) {
							page.addList(new WebList(webList, myfinder
									.getHeader(), myfinder.getDescription()));
						}
					}
				}
				listPages.add(page);
			}

		}

		return listPages;
	}

	public static ArrayList<String> bingUrls(ArrayList<String> seedList,
			String concept, int noOfResults, double overlapTolerance) {
		ArrayList<String> listURL = new ArrayList<String>();
		/*ArrayList<WebPage> results = Web.getSearchResults(seedList, concept,
				noOfResults, overlapTolerance);*/

		/*for (WebPage r : results) {
			// System.out.println(r.getUrl());
			listURL.add(r.getUrl());
			// LogUtil.log.fine(r.getDescription());
			// System.out.println(r.getTitle() + " :: " + r.getDescription());
		}*/

		return listURL;
	}
	
	public static ArrayList<String> getURLs(ArrayList<String> seedList,
			String concept, int noOfResults, double overlapTolerance) {
		ArrayList<String> listURL = new ArrayList<String>();
		List<results> urls = SearchAPIFactory.getSearchAPI(APIType.BING).getTopURLs(constructQuery(seedList, concept), 10);

		for (results r : urls) {
			// System.out.println(r.getUrl());
			listURL.add(r.Url);
			// LogUtil.log.fine(r.getDescription());
			// System.out.println(r.getTitle() + " :: " + r.getDescription());
		}

		return listURL;
	}

	public static ArrayList<String> googleSearch(String inputQuery,
			int noOfResults) throws IOException {
		ArrayList<String> urls = new ArrayList<String>();
		String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
		String search = inputQuery;
		String charset = "UTF-8";

		// for(int i=0; i<10;i++){
		// URL url = new URL(google + URLEncoder.encode(search,
		// charset)+"&start="+i);
		URL url = new URL(google + URLEncoder.encode(search, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson()
				.fromJson(reader, GoogleResults.class);

		for (Result r : results.getResponseData().getResults()) {
			// System.out.println(r);
			urls.add(r.getUrl());
		}
		// }

		// Show title and URL of 1st result.
		// System.out.println(results);
		return urls;
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

	// For testing
	public static void main(String[] args) {

		ArrayList<String> seedList = new ArrayList<>();
		seedList.add("january");
		seedList.add("february");
		seedList.add("march");
		seedList.add("april");
		SearchProvider.getNextSeedModified(seedList, "month", 10, 1);

		/*
		 * ArrayList<String> seedList = new ArrayList<>();
		 * seedList.add("facebook"); seedList.add("twitter");
		 * seedList.add("myspace"); seedList.add("google+");
		 * SearchProvider.getNextSeedModified(seedList, "social", 10, 1); String
		 * html =
		 * getHtml("http://en.wikipedia.org/wiki/Social_networking_service");
		 * System.out.println(html);
		 */
	}

	public static ArrayList<WebPage> wikiSearch(ArrayList<String> seedList,
			String concept, int noOfResults, double overlapTolerance) {

		ArrayList<WebPage> listPages = new ArrayList<WebPage>();
		// AzureSearchWebQuery query = new AzureSearchWebQuery();
		ListFinderHTML myfinder = new ListFinderHTML();

		AzureSearchResultSet<AzureSearchWebResult> results = null;
		if (results == null) {

			SearchAPI wikiSearchAPI = SearchAPIFactory
					.getSearchAPI(APIType.BING);
			java.util.List<api.BingSearchAPI.results> results3 = wikiSearchAPI
					.getTopURLs(constructQuery(seedList, concept), 20);

			for (api.BingSearchAPI.results r : results3) {

				myfinder.SetHTML(r.Url);
				WebPage page = new WebPage(r.Title, r.Url, r.Description);
				ArrayList<String> webList = new ArrayList<String>();
				while ((webList = myfinder.getNextList()) != null) {
					if (webList.size() > 0) {
						if (ListUtil.getOverLap(webList, seedList) >= (4)) {
							page.addList(new WebList(webList, myfinder
									.getHeader(), myfinder.getDescription()));
						}
					}
				}
				listPages.add(page);
			}

		}

		return listPages;

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

	public static void getNextSeedModified(ArrayList<String> seedList,
			String concept, int noOfResults, double overlapTolerance) {

		ArrayList<WebPage> listPages = wikiSearch(seedList, concept,
				noOfResults, overlapTolerance);

		for (WebPage wp : listPages) {
			for (WebList wl : wp.getAllList()) {

				int count = 0;
				for (String item : wl.getList()) {

					for (String seed : seedList) {

						if (item.contains(seed)) {
							count++;
						}
					}
				}

				if (count >= seedList.size()) {
					System.out.println(wl.getList());
					break;
				}

			}
		}
	}
}
