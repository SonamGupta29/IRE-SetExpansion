package api;

import http.HttpQueries;
import http.HttpException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import util.LogUtil;

import api.BingSearchAPI.results;

import com.google.gson.Gson;

public class WebHoseSearchAPI extends SearchAPI{

	String url = "https://webhose.io/search?token=35699326-6aec-4b1e-8aa4-a0794ba56819&format=json&language=english&q=";
	HttpClient client;
	HashSet<String> URLs = new HashSet<>();
	
	public WebHoseSearchAPI(){
		HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
		client = new DefaultHttpClient();
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}
	@Override
	public List<results> getTopURLs(String query, int n) {
		// TODO Auto-generated method stub
		
		try {
			String q = URLEncoder.encode(query, "UTF-8");
			url = url + q;
			String resp = HttpQueries.sendGetQuery(url, client);
			Result result = new Gson().fromJson(resp,
					Result.class);
			int total = result.posts.size();
			for(int i=0; i<total; i++){
				WebHoseSearchAPI.getHtml(result.posts.get(i).url);
				System.out.println(result.posts.get(i).url);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public static String getHtml(String url) {
		System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
		System.setProperty("http.proxyPort", "8080");
		LogUtil.log.fine("Going to web for : " + url);
		BufferedReader in;
		String inputLine;
		StringBuilder sb = new StringBuilder();
		try {
			in = new BufferedReader(new InputStreamReader(
					new URL(url).openStream()));
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine).append("\n");
			}
		} catch (MalformedURLException e) {
			LogUtil.log.fine(e.toString());
		} catch (IOException e) {
			LogUtil.log.fine(e.toString());
		}
		return sb.toString();
	}

	
	
	static class Result{
		List<posts> posts;
	}
	
	static class posts{
		
		String url;
	}
	
	

	
}
