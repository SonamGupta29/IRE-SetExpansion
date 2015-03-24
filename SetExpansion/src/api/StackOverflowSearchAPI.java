package api;

import http.HttpException;
import http.HttpQueries;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import api.BingSearchAPI.results;

import com.google.gson.Gson;

public class StackOverflowSearchAPI extends SearchAPI {

	String url = "https://api.stackexchange.com/2.2/search/advanced?order=desc&sort=activity&site=stackoverflow&q=";
	@Override
	public List<results> getTopURLs(String query, int n) {
		// TODO Auto-generated method stub

		try {
			String q = URLEncoder.encode(query, "UTF-8");
			url = url + q;
			String resp = HttpQueries.getQuery(url);
			Result result = new Gson().fromJson(resp,
					Result.class);
			int len = result.items.length;
			for(int i=0; i<len; i++){
				System.out.println(result.items[i].link);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;

	}
	static class Result {
		
		items items[];
	}
	static class items{
		String link;
	}

}
