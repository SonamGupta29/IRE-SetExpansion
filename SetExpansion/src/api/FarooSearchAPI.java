package api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class FarooSearchAPI extends SearchAPI {

	String url = "https://faroo-faroo-web-search.p.mashape.com/api";
	HttpClient client;
	HashSet<String> URLs = new HashSet<>();

	public FarooSearchAPI() {
		HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
		client = new DefaultHttpClient();
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	@Override
	public void getTopURLs(String query, int n) {
		// TODO Auto-generated method stub

		try {
			query = URLEncoder.encode(query,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpGet queryRequest = new HttpGet(url + "?q="+query);
		queryRequest.setHeader("X-Mashape-Key",
				"VjjJRNFatSmshfgdoZFniCH0uzXup1quHLfjsnw8N6aVS0xv4C");
		queryRequest.setHeader("Accept", "application/json");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody;

		try {
			responseBody = client.execute(queryRequest, responseHandler);
			System.out.println(responseBody);
			Result result = new Gson().fromJson(responseBody,
					Result.class);
			System.out.println(responseBody);
			int total = result.results.length;
			System.out.println("total: " + total);

			// Show title and URL of each results
			for (int i = 0; i <= total - 1; i++) {
				System.out.println("Title: " + result.results[i].title + " url: " + result.results[i].url);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	static class Result {
		
		results[] results;
		String query;
	}
	
	static class results{
		
		String title;
		String url;
	}

}


