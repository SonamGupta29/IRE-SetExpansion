package api;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;

import api.StackOverflowSearchAPI.Result;
import api.StackOverflowSearchAPI.items;

public class BingSearchAPI extends SearchAPI {

	HttpClient httpclient;

	public BingSearchAPI() {
		httpclient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
	}

	@Override
	public void getTopURLs(String query, int n) {
		// TODO Auto-generated method stub

		try {
			String q = "%27" + URLEncoder.encode(query, "UTF-8") + "%27";
			String accountKey = "rLSasvRW9cvlU5fG9hoSGjJG2M1eIjR+Ld27nFC9Pj8=";
			byte[] accountKeyBytes = Base64
					.encodeBase64((accountKey + ":" + accountKey).getBytes());
			HttpGet httpget = new HttpGet(
					"https://api.datamarket.azure.com/Bing/SearchWeb/v1/Web?Query="
							+ q + "&$format=Json");
			String accKey = new String(accountKeyBytes);
			httpget.setHeader("Authorization", "Basic " + accKey);
			System.out.println("executing request " + httpget.getURI());
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String responseBody = httpclient.execute(httpget, responseHandler);
			
			Result result = new Gson().fromJson(responseBody,
					Result.class);
			int len = result.d.results.length;
			for(int i = 0; i<len; i++){
			
				System.out.println(result.d.results[i].Title + " " + result.d.results[i].Url);
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static class Result {

		d d;
		
	}
	
	static class d{
		results results[];
	}

	static class results {
		String Title;
		String Url;
	}
}
